package client;

import View.Login;
import dao.Userdao;

import java.io.IOException;
import java.net.Socket;
import java.sql.SQLException;

public class ClientCollection {
    public static void main(String[] args) {
        final int port = 2333;
        Thread t2 = new Thread() {
            @Override
            public void run() {
                String username = "admin01";
                String password = "admin";
                try {
                    new Login(username, password);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        };
        System.out.println("start run login process");
//        t1.start();
        t2.start();
    }
}
