package bd.bd;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Arrays;

public class MainApplication extends Application {
    private static Stage primaryStage;
    @Override
    public void start(Stage primaryStage) {
        MainApplication.primaryStage = primaryStage;
        MainApplication.primaryStage.setTitle("Login");
        primaryStage.getIcons().add(new Image("/Icon.png"));
        showLoginWindow(); // Показываем окно логина при запуске
    }
    public interface MainApplicationAware {
        void setApp(MainApplication app);
    }

    public void switchWindow(String fxmlFile, String title) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlFile));
            Parent root = loader.load();
            // Проверяем, является ли контроллер DatabaseUserController и устанавливаем ссылку на app
            Object controller = loader.getController();
            if (controller instanceof DatabaseUserController) {
                ((DatabaseUserController)controller).setApp(this);
            }
            if (controller instanceof MainApplicationAware) {
                ((MainApplicationAware) controller).setApp(this);
            }
            if (controller instanceof AddRecordDialogController) {
                ((AddRecordDialogController)controller).setApp(this);
            }
            if (controller instanceof AddNewRecordDialogController) {
                ((AddNewRecordDialogController)controller).setApp(this);
            }
            if (controller instanceof AddNewUserController) {
                ((AddNewUserController)controller).setApp(this);
            }
            if (controller instanceof RegistrationController) {
                ((RegistrationController)controller).setApp(this);
/*
                RegistrationController.setCurrentUserRole(userRole);
*/

            }
            if (controller instanceof DatabaseAdminController) {
                ((DatabaseAdminController)controller).setApp(this);
            }
            Scene scene = new Scene(root);
            primaryStage.setTitle(title);
            primaryStage.getIcons().add(new Image("/Icon.png"));
            primaryStage.setScene(scene);
            primaryStage.show();
        } catch (IOException e) {
            Alerts.showErr(e.getMessage());
        }
    }
    public void newWindow(String title,String file)
    {
        try
        {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(file));
            Parent root = loader.load();
            Object controller = loader.getController();
            if (controller instanceof AddNewUserController) {
                ((AddNewUserController)controller).setApp(this);
            }
            //controller.loadDetails();
            Scene scene = new Scene(root);
            Stage stage = new Stage();
            stage.setTitle(title);
            stage.setScene(scene);
            stage.getIcons().add(new Image("/Icon.png"));
            stage.show();

        }  catch (IOException e) {
            System.out.println(e.getMessage());
            Alerts.showErr(e.getMessage());
        }
    }
    protected void showLoginWindow() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("authentication.fxml"));
            Parent root = loader.load();
            AuthenticationController authController = loader.getController();
            authController.setApp(this);
            primaryStage.setScene(new Scene(root));
            primaryStage.show();
        } catch (IOException e) {
            Alerts.showErr(e.getMessage());
        }
    }
    public static void main(String[] args) {
        launch(args); // Запуск JavaFX приложения
    }
}
