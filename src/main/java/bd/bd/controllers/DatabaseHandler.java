package bd.bd.controllers;
import bd.bd.User;
import bd.bd.UserDAO;
import bd.bd.UserDAOImpl;
import javafx.scene.chart.PieChart;

import java.sql.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public class DatabaseHandler {
    private static final Logger LOGGER = Logger.getLogger(DatabaseHandler.class.getName());
    private static final String URL = "jdbc:postgresql://localhost/Avtoto";
    private static final String USER = "postgres";
    private static final String PASSWORD = "1";
    private static final UserDAO userDAO = new UserDAOImpl();
    private Connection conn = null;

    // Закрытый конструктор предотвращает инстанцирование утилитного класса
    protected DatabaseHandler() {
        try {
            this.conn = DriverManager.getConnection(URL, USER, PASSWORD);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public Connection getConnection() {
        return this.conn;
    }
    public static Connection connect() throws SQLException {
        try {
            return DriverManager.getConnection(URL, USER, PASSWORD);
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE,"Error connection",e);
            return null;

        }
    }


    public User authenticateUser(String login, String password) {
        return userDAO.authenticateUser(login, password);
    }
}