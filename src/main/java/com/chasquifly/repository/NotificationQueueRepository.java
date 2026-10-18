package com.chasquifly.repository;

import com.chasquifly.model.NotificationQueue;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;

@Repository
public interface NotificationQueueRepository extends JpaRepository<NotificationQueue, UUID> {

    List<NotificationQueue> findByStatusAndNotifyTimeBefore(String status, OffsetDateTime time);

    List<NotificationQueue> findByUserId(UUID userId);
}
