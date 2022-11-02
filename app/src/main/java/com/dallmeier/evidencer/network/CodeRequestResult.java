package com.dallmeier.evidencer.network;

public class CodeRequestResult {
    private ApiTaskType apiTaskType;
    private String successCode;
    private String errCode;
    private String description;

    public String getSuccessCode() {
        return successCode + "";
    }

    public ApiTaskType getApiTaskType() {
        return apiTaskType;
    }

    public void setApiTaskType(ApiTaskType apiTaskType) {
        this.apiTaskType = apiTaskType;
    }

    public void setSuccessCode(String successCode) {
        this.successCode = successCode;
    }

    public String getErrCode() {
        return errCode;
    }

    public void setErrCode(String errCode) {
        this.errCode = errCode;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
