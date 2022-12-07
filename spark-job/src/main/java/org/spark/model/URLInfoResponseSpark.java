package org.spark.model;

import lombok.*;
import org.data.model.response.URLInfoResponse;

import java.io.Serializable;
import java.time.LocalDateTime;

@AllArgsConstructor
//@Builder
@Setter
@Getter
@ToString
public class URLInfoResponseSpark extends URLInfoResponse  implements Serializable {
    private LocalDateTime processStartTime;
    private LocalDateTime processEndTime;
    private LocalDateTime createdAt;
    private double totalProcessTime;

    public URLInfoResponseSpark(){}
}
