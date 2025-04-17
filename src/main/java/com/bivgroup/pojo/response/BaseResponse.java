package com.bivgroup.pojo.response;

public class BaseResponse {

    private String rqId;
    private String rsTm;
    private String rsId;
    private Long statusCode;
    private String statusDescription;

    public String getRqId() {
        return rqId;
    }

    public void setRqId(String rqId) {
        this.rqId = rqId;
    }

    public String getRsTm() {
        return rsTm;
    }

    public void setRsTm(String rsTm) {
        this.rsTm = rsTm;
    }

    public String getRsId() {
        return rsId;
    }

    public void setRsId(String rsId) {
        this.rsId = rsId;
    }

    public Long getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(Long statusCode) {
        this.statusCode = statusCode;
    }

    public String getStatusDescription() {
        return statusDescription;
    }

    public void setStatusDescription(String statusDescription) {
        this.statusDescription = statusDescription;
    }
}
