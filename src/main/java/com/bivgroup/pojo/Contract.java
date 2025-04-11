package com.bivgroup.pojo;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;
import java.util.List;

@Getter
@Setter
public class Contract {

    private String contractNumber;
    private Long contractId;
    private Date startDate;
    private Date endDate;
    private Date signDate;
    private List<Payment> payments;
}
