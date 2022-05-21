package com.javastart;

import java.io.Serializable;

public class Bill implements Serializable {

    private static final long serialVersionUID = 8383L;

    private Long id;

    private Integer amount;

    public Bill(Long id, Integer amount) {
        this.id = id;
        this.amount = amount;
    }

    public Long getId() {
        return id;
    }

    public Integer getAmount() {
        return amount;
    }

    public void setAmount(Integer amount) {
        this.amount = amount;
    }

    @Override
    public String toString() {
        return "Bill{" +
                "id=" + id +
                ", amount=" + amount +
                '}';
    }
}
