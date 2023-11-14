package com.example.tallyapp.dao;

import com.example.tallyapp.entity.User;
import com.example.tallyapp.helper.DBHelper;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UserDao {
    public static User getUser(String username) throws SQLException {
        String sql = "SELECT* FROM user where username = ?";
        Connection connection = null;
        PreparedStatement ps = null;
        ResultSet res = null;
        try{
            connection = DBHelper.getConnection();
            if(connection != null){
                ps = connection.prepareStatement(sql);
                ps.setString(1, username);
                res = ps.executeQuery();
                while(res.next()){
                    User user = new User();
                    user.setName(res.getString("name"));
                    user.setUsername(res.getString("username"));
                    user.setPassword(res.getString("password"));
                    user.setRemember(res.getInt("remenber"));
                    return user;
                }

            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }finally {
            DBHelper.close(connection, ps, res);
        }
        return null;
    }

    public static int insertUser(User user) throws SQLException {
        String sql = "insert into user(name,username,password) values(?,?,?)";
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        try {
            connection = DBHelper.getConnection();
            if (connection == null) {
                throw new SQLException("数据库连接失败");
            }
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, user.getName());
            preparedStatement.setString(2, user.getUsername());
            preparedStatement.setString(3, user.getPassword());
            int result = preparedStatement.executeUpdate();
            return result;
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            DBHelper.close(connection, preparedStatement, null);
        }
    }

    public static int updateUser(User user) throws SQLException {
        String sql = "update user set remember = 1 where username = ?";
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        try {
            connection = DBHelper.getConnection();
            if (connection == null) {
                throw new SQLException("数据库连接失败");
            }
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, user.getUsername());
            int result = preparedStatement.executeUpdate();
            return result;
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            DBHelper.close(connection, preparedStatement, null);
        }
    }
}
