package org.example.studen_record_tracker;

import javafx.fxml.FXML;
import javafx.scene.control.*;

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
}