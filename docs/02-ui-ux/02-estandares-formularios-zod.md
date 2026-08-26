# Estándares de Formularios y Validaciones con Zod

En todo el proyecto, los formularios en React se gestionan con **React Hook Form** y se validan con **Zod** mediante el resolver `@hookform/resolvers/zod`.

---

## 📋 Catálogo de Esquemas Zod Principales

### 1. Búsqueda de Vuelos (`flightSearchSchema`)
```typescript
import { z } from 'zod';

export const flightSearchSchema = z.object({
  tripType: z.enum(['ONE_WAY', 'ROUND_TRIP']).default('ONE_WAY'),
  originAirportId: z.string().min(3, 'Seleccione un aeropuerto de origen'),
  destinationAirportId: z.string().min(3, 'Seleccione un aeropuerto de destino'),
  departureDate: z.string().min(1, 'La fecha de salida es obligatoria'),
  returnDate: z.string().optional(),
  passengers: z.number().int().min(1, 'Mínimo 1 pasajero').max(9, 'Máximo 9 pasajeros'),
  travelClass: z.enum(['ECONOMY', 'PREMIUM_ECONOMY', 'BUSINESS', 'FIRST']).default('ECONOMY'),
}).refine(
  (data) => {
    if (data.tripType === 'ROUND_TRIP') {
      if (!data.returnDate) return false;
      return new Date(data.returnDate) >= new Date(data.departureDate);
    }
    return true;
  },
  {
    message: 'La fecha de regreso debe ser igual o posterior a la fecha de salida',
    path: ['returnDate'],
  }
).refine(
  (data) => data.originAirportId !== data.destinationAirportId,
  {
    message: 'El origen y destino no pueden ser el mismo aeropuerto',
    path: ['destinationAirportId'],
  }
);

export type FlightSearchFormValues = z.infer<typeof flightSearchSchema>;
```

---

### 2. Autenticación y Registro (`authSchema`)
```typescript
import { z } from 'zod';

export const registerSchema = z.object({
  firstName: z.string().min(2, 'El nombre debe tener al menos 2 caracteres'),
  lastName: z.string().min(2, 'El apellido debe tener al menos 2 caracteres'),
  email: z.string().email('Ingrese un correo electrónico válido'),
  password: z
    .string()
    .min(8, 'La contraseña debe tener mínimo 8 caracteres')
    .regex(/[A-Z]/, 'Debe incluir al menos una letra mayúscula')
    .regex(/[0-9]/, 'Debe incluir al menos un número'),
  confirmPassword: z.string(),
  phone: z.string().optional(),
}).refine((data) => data.password === data.confirmPassword, {
  message: 'Las contraseñas no coinciden',
  path: ['confirmPassword'],
});

export const loginSchema = z.object({
  email: z.string().email('Correo electrónico no válido'),
  password: z.string().min(1, 'Ingrese su contraseña'),
});

export type RegisterFormValues = z.infer<typeof registerSchema>;
export type LoginFormValues = z.infer<typeof loginSchema>;
```

---

### 3. Configuración de Alertas (`alertConfigSchema`)
```typescript
import { z } from 'zod';

export const alertConfigSchema = z.object({
  tripId: z.string().uuid(),
  notify24HoursBefore: z.boolean().default(true),
  notify3HoursBefore: z.boolean().default(true),
  notifyStatusChanges: z.boolean().default(true),
  targetEmail: z.string().email('Correo no válido para alertas'),
});

export type AlertConfigFormValues = z.infer<typeof alertConfigSchema>;
```
