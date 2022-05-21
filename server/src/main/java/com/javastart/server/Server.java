package main.java.com.javastart.server;

import com.javastart.Account;
import com.javastart.Adjustment;
import com.javastart.Payment;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import main.java.com.javastart.server.exceptions.BufferReaderException;
import main.java.com.javastart.server.exceptions.DataBaseException;
import main.java.com.javastart.server.exceptions.ObjectClassException;
import main.java.com.javastart.server.exceptions.SocketException;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.SQLException;

public class Server {

    private ServerSocket serverSocket;
    private BufferedReader reader;
    private ObjectInputStream ois;
    private ObjectOutputStream oos;
    private Logger log = LoggerFactory.getLogger(Server.class);
    private final int port;
    DataBase dataBase = new DataBase();

    public Server(int port) {
        this.port = port;
    }

    public void start() {
        try {
            createConnection(port);
            Socket clientSocket = serverSocket.accept();
            reader = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            ois = new ObjectInputStream(clientSocket.getInputStream());
            oos = new ObjectOutputStream(clientSocket.getOutputStream());

            startListener();
            log.info("Connected to client and waiting for a command");
            dataBase.createTable();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            throw new DataBaseException("Table creation error");
        }
    }

    private void createConnection(int port) {
        try {
            serverSocket = new ServerSocket(port, 10000);
            System.out.println("Server starts");
            log.info("Waiting for connection with client to port: " + port);
        } catch (IOException e) {
            System.out.println("Can't create connection");
            e.printStackTrace();
        }
    }

    public void startListener() {
        Thread t1 = new Thread(() -> {
            while (true) {
                try {
                    if (reader.ready()) {
                        readCommand();
                    }
                    if (serverSocket.isClosed()) {
                        break;
                    }
                } catch (IOException | ClassNotFoundException e) {
                    throw new BufferReaderException("Server is not ready to read incoming notification");
                }
            }
        });
        t1.start();
    }

    private void readCommand() throws IOException, ClassNotFoundException {
        String command = (String) ois.readObject();
        String externalId;
        String message;
        String extraParams;

        if (command.equals("start")) {
            try {
                externalId = reader.readLine();
                message = reader.readLine();
                extraParams = reader.readLine();
                System.out.println("Checkup message: " + externalId + " " + message + " " + extraParams);
                oos.writeObject(externalId + " " + message + " " + extraParams);
                log.info("Server send back message to client");

                String operation = (String) ois.readObject();
                if (operation.equals("payment")) {
                    readPayment();
                }
                if (operation.equals("adjustment")) {
                    readAdjustment();
                }
            } catch (IOException e) {
                throw new SocketException("Error sending message");
            } catch (ClassNotFoundException e) {
                throw new ObjectClassException("Invalid class of incoming object");
            }
        }
        if (command.equals("close")) {
            oos.close();
            ois.close();
            serverSocket.close();
            log.info("Connection with client is over");
        }
    }

    public void readPayment() {
        try {
            Account account = (Account) ois.readObject();
            Payment payment = (Payment) ois.readObject();
            dataBase.addToTable(account);
            int prevBill = DataBase.getBillFromTable(payment);
            int currentBill = DataBase.updateTableByPayment(payment);
            account.getBill().setAmount(currentBill);
            System.out.println(account + " added to table");
            System.out.println("----------------------");

            if (prevBill < payment.getPayment()) {
                oos.writeObject("insufficient funds in that bill: " + currentBill);
            }
            if (prevBill >= payment.getPayment()) {
                oos.writeObject("Payment: " + payment.getPayment() + " completed successfully "
                        + "\nCurrent Bill: " + currentBill);
            }
        } catch (IOException e) {
            throw new SocketException("Error sending object");
        } catch (ClassNotFoundException e) {
            throw new ObjectClassException("Invalid class of incoming object");
        } catch (SQLException e) {
            throw new DataBaseException("database read/write error");
        }
    }

    public void readAdjustment() {
        try {
            Account account = (Account) ois.readObject();
            Adjustment adjustment = (Adjustment) ois.readObject();
            dataBase.addToTable(account);
            int currentBill = DataBase.updateTableByAdjustment(adjustment);
            account.getBill().setAmount(currentBill);
            System.out.println(account + " added to table");
            System.out.println("----------------------");

            oos.writeObject("Adjustment: " + adjustment.getAdjustment() + " completed successfully "
                    + "\nCurrent Bill: " + currentBill);
        } catch (IOException e) {
            throw new SocketException("Error sending object");
        } catch (ClassNotFoundException e) {
            throw new ObjectClassException("Invalid class of incoming object");
        } catch (SQLException e) {
            throw new DataBaseException("database read/write error");
        }
    }
}