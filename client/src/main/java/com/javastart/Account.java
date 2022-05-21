package com.javastart;

import java.io.Serializable;

public class Account implements Serializable {

    public static final long serialVersionUID = 8383L;

    private Long id;

    private String name;

    private Bill bill;

    public Account(long id, String name, Bill bill) {
        this.id = id;
        this.name = name;
        this.bill = bill;
    }

    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Bill getBill() {
        return bill;
    }

    public void setBill(Bill bill) {
        this.bill = bill;
    }

    @Override
    public String toString() {
        return "Account{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", bill=" + bill +
                '}';
    }
}
