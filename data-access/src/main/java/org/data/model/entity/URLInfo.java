package org.data.model.entity;

import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Setter
@Getter
@AllArgsConstructor
@Builder
@ToString
public class URLInfo {

    private Long id;
    private UUID uuid;
    private String longURL;
    private String urlHash;
    private LocalDateTime createdAt;
    private String shortCode;
    private Long distributedUID;
    private LocalDateTime expireAt;
    private String domain;

    public URLInfo(){}
}
