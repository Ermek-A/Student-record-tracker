package org.example.studen_record_tracker;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.io.IOException;

public class LoginController {

    @FXML
    private TextField loginField;

    @FXML
    private PasswordField passwordField;

    @FXML
    private Label messageLabel;

    @FXML
    protected void handleSignIn(ActionEvent event) {
        String login = loginField.getText().trim();
        String password = passwordField.getText().trim();

        if (login.isEmpty() || password.isEmpty()) {
            showAlert("Error", "Please fill in all fields.");
            return;
        }

        User user = UserService.authenticate(login, password);
        if (user == null) {
            showAlert("Error", "Invalid username or password.");
            return;
        }

        String role = user.getRole();
        String email = user.getEmail();
        switch (role) {
            case "admin":
                showAlert("Success", "Signed in as Administrator.");
                loadWindow("admin.fxml", "Admin Panel", role, email);
                break;
            case "student":
                showAlert("Success", "Signed in as Student.");
                loadWindow("student.fxml", "Student Panel", role, email);
                break;
            case "teacher":
                showAlert("Success", "Signed in as Teacher.");
                loadWindow("teacher.fxml", "Teacher Panel", role, login);
                break;
            default:
                showAlert("Error", "Unknown role detected.");
        }
    }

    @FXML
    protected void openRegistration(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/example/studen_record_tracker/register.fxml"));
            Scene scene = new Scene(loader.load());
            Stage stage = new Stage();
            stage.setTitle("Register");
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            showAlert("Error", "Failed to open registration window: " + e.getMessage());
        }
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void loadWindow(String fxml, String title, String role, String id) {
        try {
            // Явно указываем путь к FXML
            String fxmlPath = "/org/example/studen_record_tracker/" + fxml;
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));
            if (loader.getLocation() == null) {
                throw new IOException("FXML file not found: " + fxmlPath);
            }
            Scene scene = new Scene(loader.load());
            Stage stage = new Stage();
            stage.setTitle(title);
            stage.setScene(scene);

            // Инициализация контроллера
            if ("student".equals(role)) {
                StudentController controller = loader.getController();
                controller.setCurrentUser(id);
            } else if ("teacher".equals(role)) {
                TeacherController controller = loader.getController();
                controller.setCurrentUser(id);
            } else if ("admin".equals(role)) {
                AdminController controller = loader.getController();
                controller.setRole(role);
                controller.setCurrentUser(id);
            }

            stage.show();
            Stage currentStage = (Stage) loginField.getScene().getWindow();
            currentStage.close();
        } catch (IOException e) {
            e.printStackTrace(); // Для дебаггинга
            showAlert("Error", "Failed to open window: " + e.getMessage());
        }
    }
}