package org.example.student_record_tracker;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.io.IOException;

public class StudentController {

    @FXML
    private Label welcomeLabel;

    @FXML
    private Label groupLabel;

    @FXML
    private Label calculusLabel;

    @FXML
    private Label programmingLabel;

    @FXML
    private Label englishLabel;

    @FXML
    private Label gpaLabel;

    private String currentUser;

    private final UserService userService = new UserService();

    public void setCurrentUser(String email) {
        this.currentUser = email;
        refreshStudentInfo();
    }

    private void refreshStudentInfo() {
        Student student = userService.loadStudents().stream()
                .filter(s -> s.getEmail().equals(currentUser))
                .findFirst()
                .orElse(null);

        if (student != null) {
            welcomeLabel.setText("Welcome, " + student.getName());
            groupLabel.setText("Group: " + student.getGroup());
            calculusLabel.setText("Calculus: " + student.getCalculusGrade());
            programmingLabel.setText("Programming: " + student.getProgrammingGrade());
            englishLabel.setText("English: " + student.getEnglishGrade());
            gpaLabel.setText(String.format("GPA: %.2f (4.0 scale: %.2f)",
                    student.calculateHundredPointGPA(), student.calculateFourPointGPA()));
        }
    }
    @FXML
    protected void exit(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/example/student_record_tracker/login.fxml"));
            Scene scene = new Scene(loader.load());
            Stage stage = new Stage();
            stage.setTitle("Student Record Tracker");
            stage.setScene(scene);
            stage.show();
            Stage currentStage = (Stage) welcomeLabel.getScene().getWindow();
            currentStage.close();
        } catch (IOException e) {
            showAlert("Error", "Failed to open login window: " + e.getMessage());
        }
    }
    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}