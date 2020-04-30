package client;

import View.Login;
import View.Main;
import dao.Userdao;
import model.Message;
import model.User;

import javax.swing.*;
import java.io.IOException;
import java.io.ObjectOutputStream;
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
                Socket socket = null;
                try {
                    socket = new Socket("localhost",port);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                try {
                    Userdao.setStatusOn(username);
                } catch (SQLException e) {
                    e.printStackTrace();
                }
                try {
                    ClientTest.main(username, socket);
                } catch (SQLException e) {
                    e.printStackTrace();
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
