package com.solactive.challenge.solactivetickvalues.model;


import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode
public class TickValue {

    private LocalDateTime epoch_timestamp;

    private Double price;

    private Double closePrice;

    private String currency;

    private String ric;


}
