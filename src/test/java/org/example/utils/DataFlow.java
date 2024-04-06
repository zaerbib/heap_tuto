package org.example.utils;

import lombok.*;

import java.time.LocalDateTime;

@AllArgsConstructor
@Builder
@Getter
@Setter
@EqualsAndHashCode
public class DataFlow {
    private String id;
    private Double open,
            close, volume,
            splitFactor,
            dividend;
    private String symbol, exchange;
    private LocalDateTime date;
}
