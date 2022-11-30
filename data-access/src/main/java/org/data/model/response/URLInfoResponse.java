package org.data.model.response;

import lombok.*;

import java.time.LocalDateTime;

@AllArgsConstructor
@Builder
@Setter
@Getter
@ToString
public class URLInfoResponse {
    private String shortUrl;
    private String url;
    private Long userId;
    private String userUUID;
    private LocalDateTime expireAt;
    private String message;

    public URLInfoResponse() {}
}
