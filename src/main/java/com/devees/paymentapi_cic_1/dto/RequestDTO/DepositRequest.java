package com.devees.paymentapi_cic_1.dto.RequestDTO;

import java.math.BigDecimal;

public class DepositRequest {
    private String subscriberCode;
    private BigDecimal amount;

    public String getSubscriberCode() { return subscriberCode; }
    public void setSubscriberCode(String subscriberCode) { this.subscriberCode = subscriberCode; }

    public BigDecimal getAmount() { return amount; }
    public void setAmount(BigDecimal amount) { this.amount = amount; }
}