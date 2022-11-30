package org.data.model.request;

import lombok.*;

import java.time.LocalDateTime;

@Setter
@Getter
@AllArgsConstructor
@Builder
@ToString
public class URLInfoRequest {
    private String url;
    private Long userId;
    private String userUUID;
    private LocalDateTime expireAt;

    public URLInfoRequest() {}


}
