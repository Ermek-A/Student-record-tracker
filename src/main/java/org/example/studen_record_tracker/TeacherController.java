package org.example.studen_record_tracker;

import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.util.List;

public class TeacherController {

    @FXML
    private ListView<String> studentList;

    @FXML
    private TextField gradeField;

    @FXML
    private Button updateButton;

    @FXML
    private Label titleLabel;

    private String currentUser;

    private final UserService userService = new UserService();

    private List<Student> students;

    @FXML
    public void initialize() {
        students = userService.loadStudents();
        refreshStudentList();
    }

    public void setCurrentUser(String id) {
        this.currentUser = id;
        switch (id) {
            case "calculus":
                titleLabel.setText("Teacher Panel (Calculus)");
                break;
            case "proglang":
                titleLabel.setText("Teacher Panel (Programming)");
                break;
            case "english":
                titleLabel.setText("Teacher Panel (English)");
                break;
        }
        refreshStudentList();
    }

    @FXML
    private void handleUpdate() {
        int index = studentList.getSelectionModel().getSelectedIndex();
        if (index < 0) {
            showAlert("Error", "Please select a student.");
            return;
        }

        Student selected = students.get(index);
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
            refreshStudentList();
            gradeField.clear();
        } catch (NumberFormatException e) {
            showAlert("Error", "Grade must be a valid number.");
        }
    }

    private void refreshStudentList() {
        studentList.getItems().clear();
        for (Student student : students) {
            int grade = switch (currentUser) {
                case "calculus" -> student.getCalculusGrade();
                case "proglang" -> student.getProgrammingGrade();
                case "english" -> student.getEnglishGrade();
                default -> 0;
            };
            studentList.getItems().add(String.format("%s - %s (Grade: %d)",
                    student.getName(), student.getEmail(), grade));
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