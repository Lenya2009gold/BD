package bd.bd.controllers;

import bd.bd.MainApplication;
import bd.bd.User;
import bd.bd.UserDAO;
import bd.bd.UserDAOImpl;
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

    private static final UserDAO userDAO = new UserDAOImpl();

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
    public void setApp(MainApplication app) {
        this.app = app;
    }

    public void handleExit(ActionEvent actionEvent) {
        app.showLoginWindow();
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
                userDAO.deleteUserFromDatabase(selectedUser.getUserId());
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

    public void handleShowUsers(ActionEvent actionEvent) {
        usersTable.setItems(userDAO.loadUsers());
    }

    public void handleAddNewUser(ActionEvent actionEvent) {
            app.newWindow("Добавить пользователя","AddNewUser.fxml");
    }

    public void handleEditPass(ActionEvent actionEvent) {
        app.newWindow("Смена пароля","ChangePassword.fxml");
    }
}
