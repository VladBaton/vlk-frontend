package com.bivgroup.pojo;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.Date;

@Getter
@Setter
public class Payment {

    private Long paymentId;
    private BigDecimal amount;
    private Date payDate;
    private Date startDate;
    private Date finishDate;
    private Long orderNum;
    private Long status;
}
