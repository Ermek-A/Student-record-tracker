package org.example.student_record_tracker;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.List;

public class AdminController {

    @FXML
    private ListView<String> studentList;

    @FXML
    private TextField nameField;

    @FXML
    private ComboBox<String> groupComboBox;

    @FXML
    private TextField emailField;

    @FXML
    private TextField passwordField;

    @FXML
    private TextField calculusField;

    @FXML
    private TextField programmingField;

    @FXML
    private TextField englishField;

    @FXML
    private Button addButton;

    @FXML
    private Button removeButton;

    @FXML
    private Button clearButton;

    @FXML
    private Button updateButton;

    private String role;

    private String currentUser;

    private final UserService userService = new UserService();

    private List<Student> students;

    @FXML
    public void initialize() {
        students = userService.loadStudents();
        groupComboBox.getItems().addAll("ComCEH", "ComSE", "MatDais");
        refreshStudentList();
    }

    public void setRole(String role) {
        this.role = role;
        refreshStudentList();
    }

    public void setCurrentUser(String id) {
        this.currentUser = id;
        refreshStudentList();
    }

    @FXML
    private void handleAdd() {
        String name = nameField.getText().trim();
        String group = groupComboBox.getValue();
        String email = emailField.getText().trim();
        String password = passwordField.getText().trim();

        if (name.isEmpty() || group == null || email.isEmpty() || password.isEmpty()) {
            showAlert("Error", "Please fill in all fields.");
            return;
        }

        if (!email.contains("@")) {
            showAlert("Error", "Please enter a valid email address.");
            return;
        }

        if (password.length() > 8) {
            showAlert("Error", "Password must not exceed 8 characters.");
            return;
        }

        if (students.stream().anyMatch(student -> student.getEmail().equals(email))) {
            showAlert("Error", "A student with this email already exists.");
            return;
        }

        userService.registerStudent(name, email, password, group);
        students = userService.loadStudents();
        refreshStudentList();
        clearFields();
    }

    @FXML
    private void handleRemove() {
        int index = studentList.getSelectionModel().getSelectedIndex();
        if (index >= 0) {
            students.remove(index);
            userService.saveStudents(students);
            refreshStudentList();
        }
    }

    @FXML
    private void handleClear() {
        students.clear();
        userService.saveStudents(students);
        refreshStudentList();
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
            Stage currentStage = (Stage) nameField.getScene().getWindow();
            currentStage.close();
        } catch (IOException e) {
            showAlert("Error", "Failed to open login window: " + e.getMessage());
        }
    }

    @FXML
    private void handleUpdate() {
        int index = studentList.getSelectionModel().getSelectedIndex();
        if (index < 0) {
            showAlert("Error", "Please select a student.");
            return;
        }

        Student selected = students.get(index);
        String name = nameField.getText().trim();
        String group = groupComboBox.getValue();
        String email = emailField.getText().trim();
        String password = passwordField.getText().trim();
        String calculusText = calculusField.getText().trim();
        String programmingText = programmingField.getText().trim();
        String englishText = englishField.getText().trim();

        if (!name.isEmpty()) {
            selected.setName(name);
        }
        if (group != null) {
            selected.setGroup(group);
        }
        if (!email.isEmpty()) {
            if (!email.contains("@")) {
                showAlert("Error", "Please enter a valid email address.");
                return;
            }
            selected.setEmail(email);
        }
        if (!password.isEmpty()) {
            try {
                userService.updateStudentPassword(selected.getEmail(), password);
            } catch (IllegalArgumentException e) {
                showAlert("Error", e.getMessage());
                return;
            }
        }
        if (!calculusText.isEmpty()) {
            try {
                int grade = Integer.parseInt(calculusText);
                if (grade < 0 || grade > 100) {
                    showAlert("Error", "Calculus grade must be between 0 and 100.");
                    return;
                }
                selected.setCalculusGrade(grade);
            } catch (NumberFormatException e) {
                showAlert("Error", "Calculus grade must be a valid number.");
                return;
            }
        }
        if (!programmingText.isEmpty()) {
            try {
                int grade = Integer.parseInt(programmingText);
                if (grade < 0 || grade > 100) {
                    showAlert("Error", "Programming grade must be between 0 and 100.");
                    return;
                }
                selected.setProgrammingGrade(grade);
            } catch (NumberFormatException e) {
                showAlert("Error", "Programming grade must be a valid number.");
                return;
            }
        }
        if (!englishText.isEmpty()) {
            try {
                int grade = Integer.parseInt(englishText);
                if (grade < 0 || grade > 100) {
                    showAlert("Error", "English grade must be between 0 and 100.");
                    return;
                }
                selected.setEnglishGrade(grade);
            } catch (NumberFormatException e) {
                showAlert("Error", "English grade must be a valid number.");
                return;
            }
        }

        students.set(index, selected);
        userService.saveStudents(students);
        refreshStudentList();
        clearFields();
    }

    private void refreshStudentList() {
        studentList.getItems().clear();
        if ("admin".equals(role)) {
            for (Student student : students) {
                studentList.getItems().add(String.format("%s - %s - %s Calc: %d, Prog: %d, Eng: %d, GPA: %.2f ( %.1f)",
                        student.getName(), student.getGroup(), student.getEmail(),
                        student.getCalculusGrade(), student.getProgrammingGrade(), student.getEnglishGrade(),
                        student.calculateHundredPointGPA(), student.calculateFourPointGPA()));
            }
        }
    }

    private void clearFields() {
        nameField.clear();
        emailField.clear();
        passwordField.clear();
        calculusField.clear();
        programmingField.clear();
        englishField.clear();
        groupComboBox.getSelectionModel().clearSelection();
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}