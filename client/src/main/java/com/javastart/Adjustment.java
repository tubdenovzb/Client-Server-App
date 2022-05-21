package com.javastart;

import java.io.Serializable;

public class Adjustment implements Serializable {

    private static final long serialVersionUID = 8383L;

    private Long id;

    private Long accountId;

    private Integer adjustment;

    public Adjustment(Long id, Long accountId, Integer adjustment) {
        this.id = id;
        this.accountId = accountId;
        this.adjustment = adjustment;
    }

    public Long getId() {
        return id;
    }

    public Long getAccountId() {
        return accountId;
    }

    public Integer getAdjustment() {
        return adjustment;
    }

    public void setAdjustment(Integer adjustment) {
        this.adjustment = adjustment;
    }

    @Override
    public String toString() {
        return "Adjustment{" +
                "id=" + id +
                ", accountId=" + accountId +
                ", adjustment=" + adjustment +
                '}';
    }
}
