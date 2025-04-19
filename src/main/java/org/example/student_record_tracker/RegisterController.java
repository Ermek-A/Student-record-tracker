package org.example.student_record_tracker;


import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;

import com.nulabinc.zxcvbn.Zxcvbn;
import com.nulabinc.zxcvbn.Strength;

public class RegisterController {

    @FXML
    private TextField nameField;

    @FXML
    private TextField emailField;

    @FXML
    private ComboBox<String> groupComboBox;

    @FXML
    private PasswordField passwordField;


    private final UserService userService = new UserService();

    @FXML
    public void initialize() {
        groupComboBox.getItems().addAll("ComCEH", "ComSE", "MatDais");
    }

    @FXML
    protected void handleRegistration(ActionEvent event) {
        String name = nameField.getText().trim();
        String email = emailField.getText().trim();
        String group = groupComboBox.getValue();
        String password = passwordField.getText().trim();

        Zxcvbn passwordCheck = new Zxcvbn();
        Strength strength = passwordCheck.measure(password);

        if (name.isEmpty() || email.isEmpty() || group == null || password.isEmpty()) {
            showAlert("Error", "Please fill in all fields.");
            return;
        }

        if (!email.contains("@")) {
            showAlert("Error", "Please enter a valid email address.");
            return;
        }

        if (password.length() < 6) {
            showAlert("Error", "Your password must be 6 or more characters long.");
            return;
        }
        if (strength.getScore() < 2) {
            showAlert("Error", "Weak password! Please choose a stronger password. Hint: " + strength.getFeedback().getWarning());
            return;
        }

        if (!isLatinName(name)) {
            showAlert("Error", "Please write name only in English.");
            return;
        }
        if (!isLatinEmail(email)) {
            showAlert("Error", "Please write email only in English.");
            return;
        }
        if (!isLatinPassword(password)) {
            showAlert("Error", "Please write password only in English.");
            return;
        }

        try {
            userService.registerStudent(name, email, password, group);
            showAlert("Success", "Registration completed successfully.");
            Stage stage = (Stage) nameField.getScene().getWindow();
            stage.close();
        } catch (IllegalArgumentException e) {
            showAlert("Error", e.getMessage());
        }
    }
    private boolean isLatinName(String input) {
        return input.matches("^[a-zA-Z0-9\\s]+$");
    }

    private boolean isLatinEmail(String input) {
        return input.matches("^[a-zA-Z0-9@.\\-_]+$");
    }

    private boolean isLatinPassword(String input) {
        return input.matches("^[a-zA-Z0-9_]+$");
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}