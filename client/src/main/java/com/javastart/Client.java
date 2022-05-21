package com.javastart;

import com.javastart.exceptions.ClientSocketException;
import com.javastart.exceptions.ObjectClassException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.net.Socket;
import java.util.Scanner;

public class Client {

    private DataOutputStream toServer;
    private ObjectInputStream ois;
    private ObjectOutputStream oos;
    private Logger log = LoggerFactory.getLogger(Client.class);
    Scanner scanner = new Scanner(System.in);

    public Client(int port, Account account, Payment payment, Adjustment adjustment) {
        try {
            Socket socket = new Socket("localhost", port);
            toServer = new DataOutputStream(socket.getOutputStream());
            oos = new ObjectOutputStream(socket.getOutputStream());
            ois = new ObjectInputStream(socket.getInputStream());
            System.out.println("Client is connected");
            log.info("Created the client-socket " + socket);

            while (socket.isConnected()) {
                System.out.println("Insert 1 - to make payment or adjustment; 2 - to close the app");
                int command = scanner.nextInt();
                if (command == 1) {
                    oos.writeObject("start");
                    sendNotification(1L, "CHECK-1", "params1");
                    chooseOperation(account, payment, adjustment);
                }
                if (command == 2) {
                    oos.writeObject("close");
                    oos.close();
                    ois.close();
                    socket.close();
                    log.info("Connection with server is over");
                    break;
                }
            }
        } catch (IOException e) {
            throw new ClientSocketException("Client socket connection error");
        }
    }

    public void sendNotification(Long externalId, String message, String extraParams) {
        try {
            toServer.writeBytes(externalId + "\n" + message + "\n" + extraParams + "\n");

            String response = (String) ois.readObject();
            System.out.println("Sending a message to server and receiving a response");
            System.out.println("From server: " + response);
        } catch (IOException e) {
            throw new ClientSocketException("Error sending message");
        } catch (ClassNotFoundException e) {
            throw new ObjectClassException("Invalid class of incoming object");
        }
    }

    public void chooseOperation(Account account, Payment payment, Adjustment adjustment) {
        System.out.println("Choose operation: 1 - payment; 2 - adjustment");
        int operation = scanner.nextInt();

        try {
            if (operation == 1) {
                oos.writeObject("payment");
                sendAccount(account);
                sendPayment(payment);
            }
            if (operation == 2) {
                oos.writeObject("adjustment");
                sendAccount(account);
                sendAdjustment(adjustment);
            }
        } catch (IOException e) {
            throw new ClientSocketException("Error sending object (number of operation)");
        }
    }

    public void sendAccount(Account account) {
        try {
            oos.reset();
            oos.writeObject(account);
        } catch (IOException e) {
            throw new ClientSocketException("Error sending object (Account)");
        }
    }

    public void sendPayment(Payment payment) {
        try {
            oos.reset();
            oos.writeObject(payment);
            String operationStatus = (String) ois.readObject();
            System.out.println(operationStatus);
            System.out.println("----------------------");
        } catch (IOException e) {
            throw new ClientSocketException("Error sending object (Payment)");
        } catch (ClassNotFoundException e) {
            throw new ObjectClassException("Invalid class of incoming object");
        }
    }

    public void sendAdjustment(Adjustment adjustment) {
        try {
            oos.reset();
            oos.writeObject(adjustment);
            String operationStatus = (String) ois.readObject();
            System.out.println(operationStatus);
            System.out.println("----------------------");
        } catch (IOException e) {
            throw new ClientSocketException("Error sending object (Adjustment)");
        } catch (ClassNotFoundException e) {
            throw new ObjectClassException("Invalid class of incoming object");
        }
    }
}