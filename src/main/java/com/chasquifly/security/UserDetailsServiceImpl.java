package com.chasquifly.security;

import com.chasquifly.model.User;
import com.chasquifly.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Implementación personalizada de {@link UserDetailsService} para Spring Security.
 * Se encarga de cargar los detalles del usuario desde la base de datos durante el proceso de autenticación.
 */

@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UserRepository userRepository;

     /**
     * Busca un usuario registrado utilizando su correo electrónico como identificador único.
     *
     * @param email el correo electrónico del usuario que intenta autenticarse.
     * @return una instancia de {@link UserPrincipal} con los datos y roles del usuario.
     * @throws UsernameNotFoundException si no existe ningún usuario con el correo proporcionado.
     */
    @Override
    @Transactional(readOnly = true)
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado con email: " + email));

        return UserPrincipal.create(user);
    }
}
