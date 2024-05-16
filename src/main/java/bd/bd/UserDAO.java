package bd.bd;

import javafx.collections.ObservableList;

public interface UserDAO {
    User authenticateUser(String login, String password);
    ObservableList<User> loadUsers();
    void registerUser(String pass, String user, String name, String role,String second);
    void chacngePassword(String login,String newPas);
    void deleteUserFromDatabase(Integer id);
    void registerUserWithOutRole(String user, String pass, String name, String second);
}
