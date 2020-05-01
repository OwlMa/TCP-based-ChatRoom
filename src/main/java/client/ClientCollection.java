package client;

import View.Login;
import dao.Userdao;

import java.io.IOException;
import java.net.Socket;
import java.sql.SQLException;

public class ClientCollection {
    public static void main(String[] args) {
        final int port = 2333;
//        Thread t1 = new Thread() {
//            @Override
//            public void run() {
//                String username = "admin";
//                String password = "123456";
//                Socket socket = null;
//                try {
//                    socket = new Socket("localhost",port);
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//                try {
//                    Userdao.setStatusOn(username);
//                } catch (SQLException e) {
//                    e.printStackTrace();
//                }
//                try {
//                    Main.RunMain(username, socket);
//                } catch (SQLException e) {
//                    e.printStackTrace();
//                }
//            }
//        };
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
