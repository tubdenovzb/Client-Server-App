package com.javastart;

public class Main {

    public static void main(String[] args) {

        Bill bill = new Bill(83L, 1000);

        Payment payment = new Payment(3L, 38L, 500);

        Adjustment adjustment = new Adjustment(8L, 38L, 300);

        Account account = new Account(38L, "Lori", bill);

        Client client = new Client(9991, account, payment, adjustment);
    }
}
