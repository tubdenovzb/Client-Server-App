package com.javastart;

import java.io.Serializable;

public class Payment implements Serializable {

    private static final long serialVersionUID = 8383L;

    private Long id;

    private Long accountId;

    private Integer payment;

    public Payment(Long id, Long accountId, Integer payment) {
        this.id = id;
        this.accountId = accountId;
        this.payment = payment;
    }

    public Long getId() {
        return id;
    }

    public Long getAccountId() {
        return accountId;
    }

    public Integer getPayment() {
        return payment;
    }

    public void setPayment(Integer payment) {
        this.payment = payment;
    }

    @Override
    public String toString() {
        return "Payment{" +
                "id=" + id +
                ", accountId=" + accountId +
                ", payment=" + payment +
                '}';
    }
}
