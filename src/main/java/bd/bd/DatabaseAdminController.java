package bd.bd;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;

public class DatabaseAdminController {
    public TableColumn<User, String> columnId;
    public TableColumn<User, String> columnLogin;
    public TableColumn<User, String> columnName;
    public TableColumn<User, String> columnRole;
    @FXML
    public TableView<User> usersTable;
    public Button registerButton;
    public TextField nameField;
    public TextField loginField;
    public PasswordField passwordField;
    public PasswordField confirmPasswordField;
    public TextField roleField;
    public Button backButton;
    public Button addNewUserButton;
    public Button editPassButton;

    private MainApplication app;
    @FXML
    private void initialize() {
        columnId.setCellValueFactory(new PropertyValueFactory<>("userId"));
        columnLogin.setCellValueFactory(new PropertyValueFactory<>("username"));
        columnName.setCellValueFactory(new PropertyValueFactory<>("name"));
        columnRole.setCellValueFactory(new PropertyValueFactory<>("role"));
    }
    public void loadUsers()
    {
        ObservableList <User> users = FXCollections.observableArrayList();
        String SQL_SELECT = "SELECT id, login, name, role FROM users;\n";
        try (Connection conn = DatabaseHandler.connect();
             PreparedStatement pstmtSelect = conn.prepareStatement(SQL_SELECT))
        {
            ResultSet rs = pstmtSelect.executeQuery();
            while(rs.next())
            {
                users.add(new User(rs.getInt("id"),
                        rs.getString("login"),
                        rs.getString("name"),
                        rs.getString("role")));
            }
            usersTable.setItems(users);

        } catch (SQLException e) {
            System.out.println(e.getMessage());
            Alerts.showErrSQL(e.getMessage());
        }

    }
    public void setApp(MainApplication app) {
        this.app = app;
    }

    public void handleExit(ActionEvent actionEvent) {
        app.switchWindow("authentication.fxml","Вход");
    }

    public void handleDeleteRecord(ActionEvent actionEvent) {
        User selectedUser = usersTable.getSelectionModel().getSelectedItem();
        if(selectedUser!=null)
        {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Подтверждение удаления записи");
            alert.setHeaderText(null);
            alert.setContentText("Вы точно хотите удалить выбранную запись?");
            Optional<ButtonType> action = alert.showAndWait();
            if ((action.isPresent()) && (action.get() == ButtonType.OK)) {
                deleteUserFromDatabase(selectedUser.getUserId());
                usersTable.getItems().remove(selectedUser);
            }

        }
        else
        {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Нет выбранной записи");
            alert.setContentText("Выберите запись");
            alert.showAndWait();
        }
    }
    public void deleteUserFromDatabase(Integer id)
    {
        String SQL_DELETE="DELETE FROM users WHERE id = ?;\n";
        try (Connection conn = DatabaseHandler.connect();
        PreparedStatement pstmtDelete = conn.prepareStatement(SQL_DELETE)){
            pstmtDelete.setInt(1,id);
            int affected = pstmtDelete.executeUpdate();
            if(affected>0)
            {
                Alerts.showSuc("Всё удалено");
            }
            else
            {
                Alerts.showErrSQL("Не удалено");
            }

        } catch (SQLException e) {
            Alerts.showErrSQL(e.getMessage());
        }
    }

    public void handleShowUsers(ActionEvent actionEvent) {
        loadUsers();
    }

    public void handleAddNewUser(ActionEvent actionEvent) {
            app.newWindow("Добавить пользователя","AddNewUser.fxml");
    }

    public void handleEditPass(ActionEvent actionEvent) {
        app.newWindow("Смена пароля","ChangePassword.fxml");
    }
}
