# Documentation for Student Record Tracker

## Introduction
The **Student Record Tracker** is a JavaFX-based desktop application designed to manage student records and grades in an academic environment. It supports three user roles—administrator, teacher, and student—with functionalities for authentication, student management, grade assignment, and grade viewing. This documentation explains the algorithms, data structures, functions, and modules used in the project, along with challenges faced during development and their resolutions. Code snippets are provided to illustrate key implementations.

## Algorithms

### 1. User Authentication
- **Description**: Verifies user credentials (username/email and password) against stored data to grant access to role-specific dashboards.
- **Algorithm**:
  1. Read user input (login and password) from the login interface.
  2. Load the list of users from `users.json` using Gson deserialization.
  3. Iterate through the user list to find a match where the login (username or email) and password are identical.
  4. If a match is found, return the user object; otherwise, return `null` and display an error.
  5. Redirect to the appropriate dashboard based on the user’s role (`admin`, `teacher`, or `student`).
- **Implementation**: Handled in `UserService.authenticate` and `LoginController.handleSignIn`.
- **Code Example**:
  ```java
  public User authenticate(String login, String password) {
      List<User> users = loadUsers();
      for (User user : users) {
          if ((user.getUsername().equals(login) || user.getEmail().equals(login)) 
              && user.getPassword().equals(password)) {
              return user;
          }
      }
      return null;
  }
  ```
- **Complexity**: O(n) for searching the user list, where n is the number of users.

### 2. GPA Calculation
- **Description**: Calculates a student’s GPA on a 4.0 scale based on their grades in Calculus, Programming, and English.
- **Algorithm**:
  1. Sum the grades for Calculus, Programming, and English.
  2. Compute the 100-point GPA: `(sum of grades) / 3`.
  3. Convert to 4.0 scale: `100-point GPA / 25`, capping at 4.0 using `Math.min`.
  4. Update the student’s GPA field whenever a grade is modified.
- **Implementation**: Handled in `Student.calculateHundredPointGPA` and `Student.calculateFourPointGPA`.
- **Code Example**:
  ```java
  public double calculateHundredPointGPA() {
      return (calculusGrade + programmingGrade + englishGrade) / 3.0;
  }
  public double calculateFourPointGPA() {
      return Math.min(calculateHundredPointGPA() / 25.0, 4.0);
  }
  ```
- **Complexity**: O(1), as it involves simple arithmetic operations.

### 3. Password Strength Validation
- **Description**: Ensures passwords meet security requirements during registration and password updates.
- **Algorithm**:
  1. Use the zxcvbn library to evaluate the password’s strength.
  2. Check if the password length is ≥ 6 characters.
  3. Verify that the zxcvbn score is ≥ 2 (indicating moderate strength).
  4. If either check fails, throw an exception with a user-friendly error message.
- **Implementation**: Handled in `UserService.registerStudent` and `UserService.updateStudentPassword`.
- **Code Example**:
  ```java
  Zxcvbn zxcvbn = new Zxcvbn();
  Strength strength = zxcvbn.measure(password);
  if (password.length() < 6) {
      throw new IllegalArgumentException("Password must be at least 6 characters long");
  }
  if (strength.getScore() < 2) {
      throw new IllegalArgumentException("Password is too weak: " + strength.getFeedback().getWarning());
  }
  ```
- **Complexity**: O(1) for zxcvbn evaluation (library handles complexity internally).

## Data Structures

### 1. ArrayList for User and Student Lists
- **Description**: Stores lists of `User` and `Student` objects in memory, loaded from `users.json` and `students.json`.
- **Usage**:
  - `List<User>` in `UserService` for managing user credentials.
  - `List<Student>` in `UserService` for managing student records.
  - `ObservableList<Student>` in `AdminController` and `TeacherController` for dynamic table updates in JavaFX.
- **Reason**: `ArrayList` provides fast access (O(1)) and dynamic resizing, suitable for small datasets. `ObservableList` enables real-time UI updates in JavaFX tables.
- **Code Example**:
  ```java
  private ObservableList<Student> students;
  students = FXCollections.observableArrayList(userService.loadStudents());
  studentTable.setItems(students);
  ```
- **Implementation**: Used in `UserService.loadUsers`, `UserService.loadStudents`, and JavaFX table bindings.

### 2. JSON Files for Persistent Storage
- **Description**: Two JSON files (`users.json` and `students.json`) store user credentials and student records, respectively.
- **Structure**:
  - `users.json`: Array of objects with fields `username`, `password`, `role`, and `email`.
  - `students.json`: Array of objects with fields `name`, `group`, `email`, `calculusGrade`, `programmingGrade`, `englishGrade`, and `gpa`.
- **Usage**: Loaded into `ArrayList` at runtime, modified in memory, and saved back to JSON using Gson.
- **Code Example**:
  ```java
  public void saveStudents(List<Student> students) {
      try (FileWriter writer = new FileWriter("students.json")) {
          new Gson().toJson(students, writer);
      } catch (IOException e) {
          e.printStackTrace();
      }
      synchronizeUsersWithStudents(students);
  }
  ```
- **Reason**: JSON is lightweight, human-readable, and sufficient for small-scale data storage. Gson simplifies serialization/deserialization.

### 3. Class-Based Models
- **Description**: `User` and `Student` classes encapsulate data for users and students.
- **Structure**:
  - `User`: Fields for `username`, `password`, `role`, `email`.
  - `Student`: Fields for `name`, `group`, `email`, `calculusGrade`, `programmingGrade`, `englishGrade`, `gpa`.
- **Code Example**:
  ```java
  public class Student {
      private String name, group, email;
      private int calculusGrade, programmingGrade, englishGrade;
      private double gpa;
      // Getters and setters
  }
  ```
- **Usage**: Used to represent data in memory, with getters and setters for access and modification.
- **Reason**: Encapsulation ensures data integrity and simplifies data manipulation.

## Functions and Modules

### 1. UserService
- **Description**: Core service module for managing user and student data.
- **Key Functions**:
  - `loadUsers()`: Loads users from `users.json`, initializing with default users (admin, teachers) if the file doesn’t exist.
  - `saveUsers(List<User>)`: Saves users to `users.json` using Gson.
  - `loadStudents()`: Loads students from `students.json`, returning an empty list if the file doesn’t exist.
  - `saveStudents(List<Student>)`: Saves students to `students.json` and synchronizes `users.json`.
  - `registerStudent(String name, String email, String password, String group)`: Registers a new student.
  - `authenticate(String login, String password)`: Verifies user credentials.
  - `deleteUserByEmail(String email)`: Removes a student user by email.
  - `updateStudentPassword(String email, String newPassword)`: Updates a student’s password.
- **Code Example**:
  ```java
  public void registerStudent(String name, String email, String password, String group) {
      List<User> users = loadUsers();
      if (users.stream().anyMatch(u -> u.getEmail().equals(email))) {
          throw new IllegalArgumentException("Email already exists");
      }
      users.add(new User(email, password, "student", email));
      saveUsers(users);
      List<Student> students = loadStudents();
      students.add(new Student(name, group, email, 0, 0, 0));
      saveStudents(students);
  }
  ```
- **Role**: Centralizes data operations, ensuring consistency and persistence.

### 2. LoginController
- **Description**: Manages the login interface (`login.fxml`) and user authentication.
- **Key Functions**:
  - `handleSignIn(ActionEvent)`: Validates input, authenticates via `UserService`, and redirects to dashboards.
  - `openRegistration(ActionEvent)`: Opens the registration window.
  - `isLatin(String)`: Validates input for Latin characters.
  - `loadWindow(String fxml, String title, String role, String id)`: Loads a dashboard window.
- **Code Example**:
  ```java
  @FXML
  private void handleSignIn(ActionEvent event) {
      String login = loginField.getText();
      String password = passwordField.getText();
      if (!isLatin(login) || !isLatin(password)) {
          showAlert("Error", "Use only Latin characters, digits, or underscores");
          return;
      }
      User user = userService.authenticate(login, password);
      if (user != null) {
          loadWindow(user.getRole() + ".fxml", user.getRole(), user.getRole(), user.getEmail());
      } else {
          showAlert("Error", "Invalid username or password");
      }
  }
  ```
- **Role**: Handles user entry point and navigation.

### 3. RegisterController
- **Description**: Manages the registration interface (`register.fxml`) for students.
- **Key Functions**:
  - `initialize()`: Populates the group dropdown.
  - `handleRegistration(ActionEvent)`: Validates input and registers a student.
  - `isLatinName(String)`, `isLatinEmail(String)`, `isLatinPassword(String)`: Validate input.
- **Code Example**:
  ```java
  @FXML
  private void handleRegistration(ActionEvent event) {
      String name = nameField.getText();
      String email = emailField.getText();
      String password = passwordField.getText();
      String group = groupComboBox.getValue();
      try {
          userService.registerStudent(name, email, password, group);
          showAlert("Success", "Registration successful");
          Stage stage = (Stage) nameField.getScene().getWindow();
          stage.close();
      } catch (IllegalArgumentException e) {
          showAlert("Error", e.getMessage());
      }
  }
  ```
- **Role**: Facilitates secure student registration.

### 4. AdminController
- **Description**: Manages the administrator dashboard (`admin.fxml`) for student and grade management.
- **Key Functions**:
  - `initialize()`: Sets up the student table and group dropdown.
  - `handleAdd()`: Adds a new student.
  - `handleRemove()`: Deletes a student.
  - `handleClear()`: Clears all students.
  - `handleUpdate()`: Updates a student’s details.
  - `exit(ActionEvent)`: Returns to login.
- **Code Example**:
  ```java
  @FXML
  private void handleAdd() {
      try {
          String name = nameField.getText();
          String email = emailField.getText();
          String password = passwordField.getText();
          String group = groupComboBox.getValue();
          userService.registerStudent(name, email, password, group);
          students.setAll(userService.loadStudents());
          clearFields();
      } catch (IllegalArgumentException e) {
          showAlert("Error", e.getMessage());
      }
  }
  ```
- **Role**: Provides full control over student data.

### 5. TeacherController
- **Description**: Manages the teacher dashboard (`teacher.fxml`) for grade assignment.
- **Key Functions**:
  - `initialize()`: Configures the table based on the teacher’s subject.
  - `handleUpdate()`: Updates a student’s grade.
  - `exit(ActionEvent)`: Returns to login.
- **Code Example**:
  ```java
  @FXML
  private void handleUpdate() {
      Student selected = studentTable.getSelectionModel().getSelectedItem();
      if (selected != null) {
          try {
              int grade = Integer.parseInt(gradeField.getText());
              if (grade < 0 || grade > 100) {
                  showAlert("Error", "Grade must be between 0 and 100");
                  return;
              }
              if (subject.equals("Calculus")) selected.setCalculusGrade(grade);
              userService.saveStudents(students);
              studentTable.refresh();
          } catch (NumberFormatException e) {
              showAlert("Error", "Grade must be a number");
          }
      }
  }
  ```
- **Role**: Enables subject-specific grade management.

### 6. StudentController
- **Description**: Manages the student dashboard (`student.fxml`) for grade viewing.
- **Key Functions**:
  - `initialize()`: Displays the student’s grades and GPA.
  - `exit(ActionEvent)`: Returns to login.
- **Code Example**:
  ```java
  @FXML
  public void initialize() {
      Student student = userService.loadStudents().stream()
          .filter(s -> s.getEmail().equals(currentUser))
          .findFirst().orElse(null);
      if (student != null) {
          calculusLabel.setText(String.valueOf(student.getCalculusGrade()));
          programmingLabel.setText(String.valueOf(student.getProgrammingGrade()));
          englishLabel.setText(String.valueOf(student.getEnglishGrade()));
          gpaLabel.setText(String.format("%.2f", student.getGpa()));
      }
  }
  ```
- **Role**: Provides read-only access to academic performance.

## Challenges and Solutions

### 1. JSON File Synchronization
- **Challenge**: Ensuring consistency between `users.json` and `students.json` when adding or deleting students.
- **Solution**: Implemented `synchronizeUsersWithStudents` in `UserService.saveStudents` to remove student users from `users.json` whose emails are not in `students.json`.
- **Code Example**:
  ```java
  private void synchronizeUsersWithStudents(List<Student> students) {
      List<User> users = loadUsers();
      users.removeIf(user -> user.getRole().equals("student") && 
          students.stream().noneMatch(student -> student.getEmail().equals(user.getEmail())));
      saveUsers(users);
  }
  ```
- **Impact**: Maintains data integrity across JSON files.

### 2. JavaFX Table Binding
- **Challenge**: Dynamically updating the student table when data changes.
- **Solution**: Used `ObservableList<Student>` with `TableView`, binding columns via `setCellValueFactory` and calling `table.refresh()` after updates.
- **Code Example**:
  ```java
  nameColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getName()));
  studentTable.setItems(students);
  studentTable.refresh();
  ```
- **Impact**: Ensures real-time UI updates.

### 3. Password Security
- **Challenge**: Ensuring strong passwords without complex logic.
- **Solution**: Integrated zxcvbn, requiring a minimum length of 6 characters and score ≥ 2, with user-friendly feedback.
- **Impact**: Balances security and usability.

### 4. Role-Based Navigation
- **Challenge**: Redirecting users to the correct dashboard based on their role.
- **Solution**: Implemented `loadWindow` in `LoginController` with a switch statement to load the appropriate FXML file.
- **Code Example**:
  ```java
  private void loadWindow(String fxml, String title, String role, String id) throws IOException {
      FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/example/student_record_tracker/" + fxml));
      Scene scene = new Scene(loader.load());
      Stage stage = new Stage();
      stage.setTitle(title);
      stage.setScene(scene);
      stage.show();
  }
  ```
- **Impact**: Simplifies navigation and ensures role-specific access.

### 5. Input Validation
- **Challenge**: Preventing invalid inputs (e.g., non-numeric grades, non-Latin characters).
- **Solution**: Added validation using regular expressions and try-catch blocks, with user-friendly alerts.
- **Code Example**:
  ```java
  private boolean isLatin(String str) {
      return str.matches("^[a-zA-Z0-9_]+$");
  }
  ```
- **Impact**: Enhances robustness and user experience.

## Conclusion
The Student Record Tracker leverages efficient algorithms (authentication, GPA calculation, password validation), data structures (`ArrayList`, JSON, class models), and modular components (`UserService`, controllers) to deliver a functional application. JavaFX and Gson ensure a responsive interface and reliable storage, while challenges like JSON synchronization and table binding were resolved to maintain integrity and usability. Code snippets illustrate key implementations, providing clarity on the project’s design and functionality.
