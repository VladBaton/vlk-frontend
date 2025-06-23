package com.bivgroup.pojo;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;

@Getter
@Setter
public class PaymentStrings {

    private static final SimpleDateFormat simpleDateFormat = new SimpleDateFormat("hh:mm dd.MM.yyyy");

    public PaymentStrings(Payment payment) {
        this.paymentId = payment.getPaymentId();
        this.amount = payment.getAmount();
        this.payDate = simpleDateFormat.format(payment.getPayDate());
        this.startDate = simpleDateFormat.format(payment.getStartDate());
        this.finishDate = simpleDateFormat.format(payment.getFinishDate());
        this.orderNum = payment.getOrderNum();
        this.status = payment.getStatus();
    }

    private Long paymentId;
    private BigDecimal amount;
    private String payDate;
    private String startDate;
    private String finishDate;
    private Long orderNum;
    private Long status;
}
