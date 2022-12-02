package org.data.model.entity;

import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.UUID;

@Setter
@Getter
@AllArgsConstructor
@Builder
@ToString
@Entity
@Table(name = "url_info")
public class URLInfo {

    public static final String KEY = "url_info:";
    public static final String URL_HASH = "url_hash:";
    public static final String SHORT_CODE = "short_code:";

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private UUID uuid;

    @Column(name = "long_url")
    private String longURL;

    @Column(name = "url_hash")
    private String urlHash;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "short_code")
    private String shortCode;

    @Column(name = "distributed_uid")
    private Long distributedUID;

    @Column(name = "expire_at")
    private LocalDateTime expireAt;
    private String domain;

    public URLInfo(){}
}
