package dao;

import db.DButil;
import model.User;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class Userdao {
    /**
     * add an user
     * @param u
     * @return
     * @throws SQLException
     */
//    public static int addUser(User u) throws SQLException {
//        Connection conn = DButil.getConn();
//        String sql = "INSERT INTO user(username,password,email) VALUES (?,?,?)";
//        PreparedStatement ps = conn.prepareStatement(sql);
//        ps.setString(1, u.getUsername());
//        ps.setString(2, u.getPassword());
//        ps.setString(3, u.getEmail());
//        return ps.executeUpdate();
//    }
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
    public static String getgroup(String account) throws SQLException {
        Connection conn = DButil.getConn();
        String sql = "SELECT groups FROM user WHERE account = ? ";
        PreparedStatement ps = conn.prepareStatement(sql);
        ps.setString(1,account);
        ResultSet rs = ps.executeQuery();
        if (rs.next()){
            return rs.getString("groups");
        }else{
            return "error";
        }
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
     * 传入用户对象对用户进行更新
     * @param u
     * @throws SQLException
     */
//    public void updateUser(User u) throws SQLException {
//        Connection conn = DButil.getConn();
//        String sql = "UPDATE  user SET password=? WHERE username=?";
//        //准备
//        PreparedStatement ps = conn.prepareStatement(sql);
//        //设置参数(对应于数据库的数据类型)
//        ps.setString(2, u.getUsername());
//        ps.setString(1, u.getPassword());
//        //执行
//        ps.execute();
//    }

    /**
     * 通过username删除用户
     * @param username
     * @throws SQLException
     */
    public void deleteUser(String username) throws SQLException {
        Connection conn = DButil.getConn();
        String sql = "DELETE FROM user WHERE username = ?";
        PreparedStatement ps = conn.prepareStatement(sql);
        ps.setString(1, username);
        ps.execute();
    }
    /**
     * 通过账号获取
     */
    public static String getUserNameByAccount(String account) throws SQLException {
        Connection conn = DButil.getConn();
        String sql = "SELECT username FROM user WHERE account = ?";
        PreparedStatement ps = conn.prepareStatement(sql);
        ps.setString(1,account);
        ResultSet rs = ps.executeQuery();
        if (rs.next()) {
            return rs.getString("username");
        }else{
            return new String("error");
        }
    }
    /**
     * 通过用户名
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
     * 返回用户集合
     * @return
     * @throws SQLException
     */
//    public List<User> query() throws SQLException {
//        Connection conn = DButil.getConn();
//        Statement st = conn.createStatement();
//        ResultSet rs = st.executeQuery("SELECT * FROM user");
//        List<User> getlist = new ArrayList<User>();
//        User u = null;
//        while (rs.next()) {
//            u = new User();
//            u.setUsername(rs.getString("username"));
//            u.setPassword(rs.getString("password"));
//            getlist.add(u);
//        }
//        return getlist;
//    }

    /**
     * 通过username获取用户
     * @param username
     * @return
     * @throws SQLException
     */
//    public User get(String username) throws SQLException {
//        Connection conn = DButil.getConn();
//        String sql = "SELECT * FROM user WHERE username=?";
//        PreparedStatement ps = conn.prepareStatement(sql);
//        ps.setString(1, username);
//
//        ResultSet rs = ps.executeQuery();
//
//        User u = new User();
//        //不要忘记了加上rs.next
//        while (rs.next()) {
//            u.setUsername(rs.getString("username"));
//            u.setPassword(rs.getString("password"));
//        }
//        return u;
//    }

    /**
     * 用户登录
     * @param u
     * @return
     * @throws SQLException
     */
    public static boolean login(User u) throws SQLException {
        Connection conn = DButil.getConn();
        String sql = "SELECT  * FROM user WHERE username = ?";
        PreparedStatement ps = conn.prepareStatement(sql);
        ps.setString(1, u.getUsername());
        ResultSet rs = ps.executeQuery();
        while (rs.next()) {
            String password = rs.getString("password");
            if (password.equals(u.getPassword())) {
                return true;
            } else {
                return false;
            }
        }
        return false;
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