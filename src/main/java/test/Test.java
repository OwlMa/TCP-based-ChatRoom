package test;

import View.ServerMain;
import dao.Userdao;
import model.User;

import javax.swing.*;
import java.sql.SQLException;

public class Test {
    public static void main(String[] args) throws ClassNotFoundException, UnsupportedLookAndFeelException, InstantiationException, IllegalAccessException, SQLException {
        Userdao.setStatusOff("admin");
        Userdao.setStatusOff("admin01");
        ServerMain.main(new String[0]);
    }
}
