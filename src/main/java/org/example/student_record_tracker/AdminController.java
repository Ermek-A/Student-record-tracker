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

import com.nulabinc.zxcvbn.Zxcvbn;
import com.nulabinc.zxcvbn.Strength;

import java.io.IOException;

public class AdminController {

    @FXML private TableView<Student> studentTable;
    @FXML private TableColumn<Student, String> nameColumn;
    @FXML private TableColumn<Student, String> emailColumn;
    @FXML private TableColumn<Student, String> groupColumn;
    @FXML private TableColumn<Student, Number> calculusColumn;
    @FXML private TableColumn<Student, Number> programmingColumn;
    @FXML private TableColumn<Student, Number> englishColumn;
    @FXML private TableColumn<Student, String> gpaColumn;
    @FXML private TextField nameField;
    @FXML private ComboBox<String> groupComboBox;
    @FXML private TextField emailField;
    @FXML private TextField passwordField;
    @FXML private TextField calculusField;
    @FXML private TextField programmingField;
    @FXML private TextField englishField;


    private String role;
    private String currentUser;
    private final UserService userService = new UserService();
    private ObservableList<Student> students;

    @FXML
    public void initialize() {
        students = FXCollections.observableArrayList(userService.loadStudents());
        groupComboBox.getItems().addAll("ComCEH", "ComSE", "MatDais");


        nameColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getName()));
        emailColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getEmail()));
        groupColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getGroup()));
        calculusColumn.setCellValueFactory(cellData -> new SimpleIntegerProperty(cellData.getValue().getCalculusGrade()));
        programmingColumn.setCellValueFactory(cellData -> new SimpleIntegerProperty(cellData.getValue().getProgrammingGrade()));
        englishColumn.setCellValueFactory(cellData -> new SimpleIntegerProperty(cellData.getValue().getEnglishGrade()));
        gpaColumn.setCellValueFactory(cellData -> new SimpleStringProperty(String.format("%.2f", cellData.getValue().getGpa())));

        studentTable.setItems(students);
    }

    public void setRole(String role) {
        this.role = role;
    }

    public void setCurrentUser(String id) {
        this.currentUser = id;
    }

    @FXML
    private void handleAdd() {
        String name = nameField.getText().trim();
        String group = groupComboBox.getValue();
        String email = emailField.getText().trim();
        String password = passwordField.getText().trim();

        Zxcvbn passwordCheck = new Zxcvbn();
        Strength strength = passwordCheck.measure(password);

        if (name.isEmpty() || group == null || email.isEmpty() || password.isEmpty()) {
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

        if (students.stream().anyMatch(student -> student.getEmail().equals(email))) {
            showAlert("Error", "A student with this email already exists.");
            return;
        }

        userService.registerStudent(name, email, password, group);
        students.setAll(userService.loadStudents());
        clearFields();
    }

    @FXML
    private void handleRemove() {
        Student selected = studentTable.getSelectionModel().getSelectedItem();
        if (selected != null) {
            String email = selected.getEmail();

            students.remove(selected);
            userService.saveStudents(students);
            userService.deleteUserByEmail(email);
        }
    }

    @FXML
    private void handleClear() {
        students.forEach(student -> userService.deleteUserByEmail(student.getEmail()));

        students.clear();

        userService.saveStudents(students);
    }

    @FXML
    private void handleUpdate() {
        Student selected = studentTable.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showAlert("Error", "Please select a student.");
            return;
        }



        String name = nameField.getText().trim();
        String group = groupComboBox.getValue();
        String email = emailField.getText().trim();
        String password = passwordField.getText().trim();
        String calculusText = calculusField.getText().trim();
        String programmingText = programmingField.getText().trim();
        String englishText = englishField.getText().trim();



        if (!name.isEmpty()) selected.setName(name);
        if (group != null) selected.setGroup(group);
        if (!email.isEmpty()) {
            if (!email.contains("@")) {
                showAlert("Error", "Please enter a valid email address.");
                return;
            }
            selected.setEmail(email);
        }
        if (!password.isEmpty()) {
            Zxcvbn passwordCheck = new Zxcvbn();
            Strength strength = passwordCheck.measure(password);
            if (password.length() < 6) {
                showAlert("Error", "Your password must be 6 or more characters long.");
                return;
            }
            if (strength.getScore() < 2) {
                showAlert("Error", "Weak password! Please choose a stronger password. Hint: " + strength.getFeedback().getWarning());
                return;
            }

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

        userService.saveStudents(students);
        studentTable.refresh();
        clearFields();
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