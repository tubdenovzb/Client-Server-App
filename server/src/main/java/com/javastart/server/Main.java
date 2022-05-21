package main.java.com.javastart.server;

public class Main {

    public static void main(String[] args) {

        Server server = new Server(9991);

        server.start();
    }
}