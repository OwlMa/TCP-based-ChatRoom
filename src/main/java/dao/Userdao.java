package dao;

import db.DButil;
import model.User;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class Userdao {
    /**
     * get the friends of a user
     */
    public static List<String> getFriend(String username) throws SQLException {
        Connection con = DButil.getConn();
        String sql = "SELECT * FROM user WHERE username = ?";
        PreparedStatement ps = con.prepareStatement(sql);
        ps.setString(1,username);
        ResultSet rs =  ps.executeQuery();
        List<String> friendsList = new ArrayList<String>();
        if (rs.next()){
            String friends[] = rs.getString("friends").split(" ");
            for (String friend: friends) {
                friendsList.add(friend);
            }
        }
        return friendsList;
    }

    /**
     * get the groups of a user
     */
    public static List<String> getGroup(String username) throws SQLException {
        Connection conn = DButil.getConn();
        String sql = "SELECT * FROM user WHERE username = ? ";
        PreparedStatement ps = conn.prepareStatement(sql);
        ps.setString(1, username);
        ResultSet rs = ps.executeQuery();
        List<String> groupsList = new ArrayList<String>();
        if (rs.next()){
            String[] groups = rs.getString("groups").split(" ");
            for (String group: groups) {
                groupsList.add(group);
            }
        }
        return groupsList;
    }

    public static String getPassword(String username) throws SQLException {
        Connection conn = DButil.getConn();
        String sql = "SELECT * FROM user WHERE username =?";
        PreparedStatement ps = conn.prepareStatement(sql);
        ps.setString(1,username);
        ResultSet rs = ps.executeQuery();
        if (rs.next()){
            return rs.getString("password");
        }else{
            return "error";
        }
    }


    /**
     * get account by the username
     */
    public static Integer getAccountByUserName(String username) throws SQLException {
        Connection conn = DButil.getConn();
        String sql = "SELECT account FROM user WHERE username = ?";
        PreparedStatement ps = conn.prepareStatement(sql);
        ps.setString(1,username);
        ResultSet rs = ps.executeQuery();
        if (rs.next()) {
            return rs.getInt("account");
        }else{
            return -1;
        }
    }


    /**
     *
     * @param username
     * @return status -1 for no such user and 1 for online and 0 for offline
     * @throws SQLException
     */
    public static Integer getStatus(String username) throws SQLException {
        Connection conn = DButil.getConn();
        PreparedStatement ps1 = conn.prepareStatement("SELECT * FROM user WHERE username=?");
        ps1.setString(1, username);
        ResultSet rs = ps1.executeQuery();
        if (rs.next()) {
            Integer status = rs.getInt("status");
            return status;
        }
        else {
            Integer status = -1;
            return  status;
        }
    }
    public static void setStatusOn(String username) throws SQLException {
        Connection conn = DButil.getConn();
        PreparedStatement ps1 = conn.prepareStatement("UPDATE  user SET status=1 WHERE username=?");
        ps1.setString(1, username);
        ps1.execute();
    }
    public static void setStatusOff(String username) throws SQLException {
        Connection conn = DButil.getConn();
        PreparedStatement ps1 = conn.prepareStatement("UPDATE  user SET status=0 WHERE username=?");
        ps1.setString(1, username);
        ps1.execute();
    }
    /**
     * register
     * @param username
     * @param password
     * @return
     * @throws SQLException
     */
    public static void register(String username, String password,String email) throws SQLException {
        Connection conn = DButil.getConn();
        //(account,username,password,email,friend,groups,islogin)
        String sql = "insert into user(username, password, email, friends, status) VALUES (?,?,?,?,0)";
        PreparedStatement ps = conn.prepareStatement(sql);
        ps.setString(1, username);
        ps.setString(2, password);
        ps.setString(3,email);
        ps.setString(4,"1 ");
//        ps.setString(5,"1 ");
        ps.executeUpdate();
    }

    public static void addFriend(String username, List<String> friendsList) throws SQLException {
        Connection conn = DButil.getConn();
        String friends = "";
        for (String str: friendsList) {
            friends += str + " ";
        }
        PreparedStatement ps1 = conn.prepareStatement("UPDATE  user SET friends=? WHERE username=?");
        ps1.setString(1, friends);
        ps1.setString(2, username);
        ps1.execute();
    }
}