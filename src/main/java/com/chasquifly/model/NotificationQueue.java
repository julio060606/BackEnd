package com.chasquifly.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.OffsetDateTime;
import java.util.UUID;

@Entity
@Table(name = "notifications_queue")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class NotificationQueue {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "trip_id", nullable = false)
    private UserTrip trip;

    @Column(name = "notify_time", nullable = false)
    private OffsetDateTime notifyTime;

    @Column(nullable = false, length = 50)
    private String type; // e.g. PROXIMITY_24H, PROXIMITY_3H, STATUS_CHANGE

    @Column(nullable = false, length = 30)
    @Builder.Default
    private String status = "PENDING"; // PENDING, SENT, FAILED

    @Column(name = "sent_at")
    private OffsetDateTime sentAt;

    @Column(name = "message_payload", nullable = false, columnDefinition = "TEXT")
    private String messagePayload;
}
