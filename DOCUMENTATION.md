# Documentation for Student Record Tracker

## Introduction
The **Student Record Tracker** is a JavaFX-based desktop application designed to manage student records and grades in an academic environment. It supports three user roles—administrator, teacher, and student—with functionalities for authentication, student management, grade assignment, and grade viewing. This documentation explains the algorithms, data structures, functions, and modules used in the project, as well as challenges faced during development and their resolutions.

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
- **Complexity**: O(n) for searching the user list, where n is the number of users.

### 2. GPA Calculation
- **Description**: Calculates a student’s GPA on a 4.0 scale based on their grades in Calculus, Programming, and English.
- **Algorithm**:
  1. Sum the grades for Calculus, Programming, and English.
  2. Compute the 100-point GPA: `(sum of grades) / 3`.
  3. Convert to 4.0 scale: `100-point GPA / 25`, capping at 4.0 using `Math.min`.
  4. Update the student’s GPA field whenever a grade is modified.
- **Implementation**: Handled in `Student.calculateHundredPointGPA` and `Student.calculateFourPointGPA`.
- **Complexity**: O(1), as it involves simple arithmetic operations.

### 3. Password Strength Validation
- **Description**: Ensures passwords meet security requirements during registration and password updates.
- **Algorithm**:
  1. Use the zxcvbn library to evaluate the password’s strength.
  2. Check if the password length is ≥ 6 characters.
  3. Verify that the zxcvbn score is ≥ 2 (indicating moderate strength).
  4. If either check fails, throw an exception with a user-friendly error message.
- **Implementation**: Handled in `UserService.registerStudent` and `UserService.updateStudentPassword`.
- **Complexity**: O(1) for zxcvbn evaluation (library handles complexity internally).

## Data Structures

### 1. ArrayList for User and Student Lists
- **Description**: Stores lists of `User` and `Student` objects in memory, loaded from `users.json` and `students.json`.
- **Usage**:
  - `List<User>` in `UserService` for managing user credentials.
  - `List<Student>` in `UserService` for managing student records.
  - `ObservableList<Student>` in `AdminController` and `TeacherController` for dynamic table updates in JavaFX.
- **Reason**: `ArrayList` provides fast access (O(1)) and dynamic resizing, suitable for small datasets. `ObservableList` enables real-time UI updates in JavaFX tables.
- **Implementation**: Used in `UserService.loadUsers`, `UserService.loadStudents`, and JavaFX table bindings.

### 2. JSON Files for Persistent Storage
- **Description**: Two JSON files (`users.json` and `students.json`) store user credentials and student records, respectively.
- **Structure**:
  - `users.json`: Array of objects with fields `username`, `password`, `role`, and `email`.
  - `students.json`: Array of objects with fields `name`, `group`, `email`, `calculusGrade`, `programmingGrade`, `englishGrade`, and `gpa`.
- **Usage**: Loaded into `ArrayList` at runtime, modified in memory, and saved back to JSON using Gson.
- **Reason**: JSON is lightweight, human-readable, and sufficient for small-scale data storage. Gson simplifies serialization/deserialization.
- **Implementation**: Handled in `UserService.saveUsers`, `UserService.saveStudents`, `UserService.loadUsers`, and `UserService.loadStudents`.

### 3. Class-Based Models
- **Description**: `User` and `Student` classes encapsulate data for users and students.
- **Structure**:
  - `User`: Fields for `username`, `password`, `role`, `email`.
  - `Student`: Fields for `name`, `group`, `email`, `calculusGrade`, `programmingGrade`, `englishGrade`, `gpa`.
- **Usage**: Used to represent data in memory, with getters and setters for access and modification.
- **Reason**: Encapsulation ensures data integrity and simplifies data manipulation.
- **Implementation**: Defined in `User.java` and `Student.java`.

## Functions and Modules

### 1. UserService
- **Description**: Core service module for managing user and student data.
- **Key Functions**:
  - `loadUsers()`: Loads users from `users.json`, initializing with default users (admin, teachers) if the file doesn’t exist.
  - `saveUsers(List<User>)`: Saves users to `users.json` using Gson.
  - `loadStudents()`: Loads students from `students.json`, returning an empty list if the file doesn’t exist.
  - `saveStudents(List<Student>)`: Saves students to `students.json` and synchronizes `users.json` to remove orphaned student users.
  - `registerStudent(String name, String email, String password, String group)`: Registers a new student, validating email uniqueness and password strength.
  - `authenticate(String login, String password)`: Verifies user credentials, returning the matching `User` or `null`.
  - `deleteUserByEmail(String email)`: Removes a student user by email from `users.json`.
  - `updateStudentPassword(String email, String newPassword)`: Updates a student’s password with validation.
- **Role**: Centralizes data operations, ensuring consistency and persistence.

### 2. LoginController
- **Description**: Manages the login interface (`login.fxml`) and user authentication.
- **Key Functions**:
  - `handleSignIn(ActionEvent)`: Validates input, authenticates via `UserService`, and redirects to role-specific dashboards.
  - `openRegistration(ActionEvent)`: Opens the registration window.
  - `isLatin(String)`: Validates that input contains only Latin characters, digits, or underscores.
  - `loadWindow(String fxml, String title, String role, String id)`: Loads a new dashboard window and passes user data.
- **Role**: Handles user entry point and navigation to appropriate dashboards.

### 3. RegisterController
- **Description**: Manages the registration interface (`register.fxml`) for students.
- **Key Functions**:
  - `initialize()`: Populates the group dropdown with options (ComCEH, ComSE, MatDais).
  - `handleRegistration(ActionEvent)`: Validates input (name, email, password, group) and registers a new student via `UserService`.
  - `isLatinName(String)`, `isLatinEmail(String)`, `isLatinPassword(String)`: Validate input for Latin characters.
- **Role**: Facilitates secure student registration with robust validation.

### 4. AdminController
- **Description**: Manages the administrator dashboard (`admin.fxml`) for student and grade management.
- **Key Functions**:
  - `initialize()`: Sets up the student table with data from `UserService` and populates the group dropdown.
  - `handleAdd()`: Adds a new student with validated input.
  - `handleRemove()`: Deletes a selected student and updates JSON files.
  - `handleClear()`: Clears all student records.
  - `handleUpdate()`: Updates a selected student’s details and grades.
  - `exit(ActionEvent)`: Returns to the login screen.
- **Role**: Provides full control over student data for administrators.

### 5. TeacherController
- **Description**: Manages the teacher dashboard (`teacher.fxml`) for grade assignment.
- **Key Functions**:
  - `initialize()`: Loads students and configures the table based on the teacher’s subject (Calculus, Programming, or English).
  - `handleUpdate()`: Updates a selected student’s grade for the teacher’s subject.
  - `exit(ActionEvent)`: Returns to the login screen.
- **Role**: Enables subject-specific grade management.

### 6. StudentController
- **Description**: Manages the student dashboard (`student.fxml`) for grade viewing.
- **Key Functions**:
  - `initialize()`: Loads and displays the student’s grades and GPA.
  - `exit(ActionEvent)`: Returns to the login screen.
- **Role**: Provides read-only access to a student’s academic performance.

## Challenges and Solutions

### 1. JSON File Synchronization
- **Challenge**: Ensuring consistency between `users.json` (storing user credentials) and `students.json` (storing student records) when adding or deleting students.
- **Solution**: Implemented synchronization in `UserService.saveStudents`, which updates `users.json` to remove student users whose emails are not in `students.json`. This prevents orphaned user records.
- **Impact**: Maintains data integrity across JSON files.

### 2. JavaFX Table Binding
- **Challenge**: Dynamically updating the student table in `AdminController` and `TeacherController` when data changes.
- **Solution**: Used `ObservableList<Student>` with JavaFX’s `TableView`, binding table columns to `Student` properties via `setCellValueFactory`. Called `table.refresh()` after updates to reflect changes.
- **Impact**: Ensures real-time UI updates without manual reloading.

### 3. Password Security
- **Challenge**: Ensuring strong passwords without overly complex validation logic.
- **Solution**: Integrated the zxcvbn library to evaluate password strength, requiring a minimum length of 6 characters and a score ≥ 2. Provided user-friendly feedback from zxcvbn’s analysis.
- **Impact**: Balances security and usability in registration and password updates.

### 4. Role-Based Navigation
- **Challenge**: Redirecting users to the correct dashboard based on their role after login.
- **Solution**: Implemented `LoginController.loadWindow`, which uses a switch statement to load the appropriate FXML file (`admin.fxml`, `teacher.fxml`, or `student.fxml`) and passes user data to the corresponding controller.
- **Impact**: Simplifies navigation and ensures role-specific access.

### 5. Input Validation
- **Challenge**: Preventing invalid inputs (e.g., non-numeric grades, non-Latin characters) from causing errors.
- **Solution**: Added comprehensive validation in controllers (`LoginController`, `RegisterController`, `AdminController`, `TeacherController`) using regular expressions and try-catch blocks for numeric inputs. Displayed user-friendly alerts for errors.
- **Impact**: Enhances robustness and user experience by catching errors early.

## Conclusion
The Student Record Tracker leverages simple yet effective algorithms (authentication, GPA calculation, password validation), data structures (`ArrayList`, JSON, class models), and modular components (`UserService`, controllers) to deliver a functional and user-friendly application. The use of JavaFX and Gson ensures a responsive interface and reliable data storage, while challenges like JSON synchronization and input validation were addressed to maintain data integrity and usability. This documentation provides a comprehensive overview of the project’s technical implementation, serving as a guide for understanding its design and functionality.
