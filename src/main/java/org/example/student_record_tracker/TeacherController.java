package org.example.student_record_tracker;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.io.IOException;

public class TeacherController {

    @FXML private TableView<Student> studentTable;
    @FXML private TableColumn<Student, String> nameColumn;
    @FXML private TableColumn<Student, String> emailColumn;
    @FXML private TableColumn<Student, Number> gradeColumn;
    @FXML private TextField gradeField;

    @FXML private Label titleLabel;

    private String currentUser;
    private final UserService userService = new UserService();
    private ObservableList<Student> students;

    @FXML
    public void initialize() {
        students = FXCollections.observableArrayList(userService.loadStudents());

        // Настройка колонок
        nameColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getName()));
        emailColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getEmail()));
        gradeColumn.setCellValueFactory(cellData -> {
            switch (currentUser) {
                case "calculus": return new SimpleIntegerProperty(cellData.getValue().getCalculusGrade());
                case "proglang": return new SimpleIntegerProperty(cellData.getValue().getProgrammingGrade());
                case "english": return new SimpleIntegerProperty(cellData.getValue().getEnglishGrade());
                default: return new SimpleIntegerProperty(0);
            }
        });

        studentTable.setItems(students);
    }

    public void setCurrentUser(String id) {
        this.currentUser = id;
        switch (id) {
            case "calculus":
                titleLabel.setText("Teacher Panel (Calculus)");
                gradeColumn.setText("Calculus Grade");
                break;
            case "proglang":
                titleLabel.setText("Teacher Panel (Programming)");
                gradeColumn.setText("Programming Grade");
                break;
            case "english":
                titleLabel.setText("Teacher Panel (English)");
                gradeColumn.setText("English Grade");
                break;
        }
        studentTable.refresh();
    }

    @FXML
    private void handleUpdate() {
        Student selected = studentTable.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showAlert("Error", "Please select a student.");
            return;
        }

        String gradeText = gradeField.getText().trim();
        if (gradeText.isEmpty()) {
            showAlert("Error", "Please enter a grade.");
            return;
        }

        try {
            int grade = Integer.parseInt(gradeText);
            if (grade < 0 || grade > 100) {
                showAlert("Error", "Grade must be between 0 and 100.");
                return;
            }

            switch (currentUser) {
                case "calculus":
                    selected.setCalculusGrade(grade);
                    break;
                case "proglang":
                    selected.setProgrammingGrade(grade);
                    break;
                case "english":
                    selected.setEnglishGrade(grade);
                    break;
            }

            userService.saveStudents(students);
            studentTable.refresh();
            gradeField.clear();
        } catch (NumberFormatException e) {
            showAlert("Error", "Grade must be a valid number.");
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
            Stage currentStage = (Stage) titleLabel.getScene().getWindow();
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