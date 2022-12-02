package org.data.model.event;

import lombok.*;
import org.springframework.data.relational.core.mapping.Table;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.time.LocalDateTime;
import java.util.UUID;

@Setter
@Getter
@AllArgsConstructor
@Builder
@ToString
@Table(value = "url_event")
public class URLEvent {
    private Long id;
    private UUID uuid;
    private Long urlId;
    private UUID urlUUID;
    private LocalDateTime createdAt;
    private LocalDateTime processStartTime;
    private LocalDateTime processEndTime;
    private double totalProcessTime;
    private String shortCode;
    private Long distributedUID;
    private EventType eventType;
    private String message;
    private String exception;

    public static URLEvent create() {
        return URLEvent.builder().processStartTime(LocalDateTime.now()).build();
    }

    public static URLEvent create(EventType eventType) {
        return URLEvent.builder()
                .processStartTime(LocalDateTime.now())
                .eventType(eventType)
                .build();
    }
}
