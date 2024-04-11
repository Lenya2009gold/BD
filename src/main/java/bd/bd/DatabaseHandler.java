package bd.bd;
import java.sql.*;

public class DatabaseHandler {
    private static final String URL = "jdbc:postgresql://localhost/Avtoto";
    private static final String USER = "postgres";
    private static final String PASSWORD = "1";
    private Connection conn = null;
    private static DatabaseHandler instance;

    private PreparedStatement updateStatusStmt = null;
    private PreparedStatement deleteOrderStmt = null;
    private PreparedStatement selectStmt = null;

    // Закрытый конструктор предотвращает инстанцирование утилитного класса
    protected DatabaseHandler() {
        try {
            this.conn = DriverManager.getConnection(URL, USER, PASSWORD);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public static DatabaseHandler getInstance() {
        if (instance == null) {
            instance = new DatabaseHandler();
        }
        return instance;
    }
    public Connection getConnection() {
        return this.conn;
    }
    // Статический метод для получения подключения к БД
    public static Connection connect() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }
    private void ensureConnection() throws SQLException {
        if (conn == null || conn.isClosed()) {
            conn = DatabaseHandler.getInstance().getConnection();
        }
    }


    public User authenticateUser(String login, String password) {
        String SQL_SELECT = "SELECT * FROM users WHERE login = ? AND password = crypt(?, password)";

        try (Connection conn = connect();
             PreparedStatement pstmt = conn.prepareStatement(SQL_SELECT)) {
            pstmt.setString(1, login);
            pstmt.setString(2, password);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                System.out.println("Успешная аутентификация пользователя: " + login);
                return new User(
                        rs.getInt("id"),
                        rs.getString("login"),
                        rs.getString("name"),
                        rs.getString("role")

                );
            } else {
                System.out.println("Неудачная попытка аутентификации для пользователя: " + login);
            }
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }
        return null;
    }

}
