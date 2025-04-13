## Documentation

This section provides a detailed explanation of the algorithms, data structures, functions/modules, and challenges faced during the development of the Student Record Tracker, a JavaFX-based application for managing student academic records.

### Algorithms

1. **User Authentication**:
   - **Description**: Verifies login credentials by comparing input username/email and password against stored user data.
   - **Process**: Iterate through a list of users, checking for a match of username or email and password. Return the user object if found, otherwise return null.
   - **Use Case**: Applied during login to determine user role (Admin, Teacher, Student) and open the corresponding interface.

2. **GPA Calculation**:
   - **Description**: Computes a student’s GPA on 100-point and 4.0 scales based on grades.
   - **Process**:
     - 100-point GPA: Calculate the average of Calculus, Programming, and English grades (sum / 3).
     - 4.0-scale GPA: Divide 100-point GPA by 25, capping at 4.0.
   - **Use Case**: Used in the Student interface to display academic performance.

3. **Student List Rendering**:
   - **Description**: Populates UI lists with student data, customized by user role.
   - **Process**: Iterate through the student list, format each entry as a string (e.g., name, email, grades), and add to a ListView.
   - **Use Case**: Admin sees all student details and grades, Teachers see subject-specific grades, Students see their own data.

4. **Input Validation**:
   - **Description**: Ensures user inputs meet requirements (e.g., valid email, English-only text).
   - **Process**: Use regular expressions to check:
     - Email: Must contain '@'.
     - Grades: Must be numbers between 0 and 100.
     - English-only: Allow only Latin letters, digits, and specific symbols (e.g., '@', '.', '-').
   - **Use Case**: Applied during login, registration, and grade updates to prevent errors.

### Data Structures

1. **User List**:
   - **Type**: `List<User>` (Java ArrayList).
   - **Purpose**: Stores user credentials and metadata.
   - **Fields**: Each `User` has `username`, `password`, `role` (admin, teacher, student), and `email`.
   - **Storage**: Serialized to `users.json` for persistence.

2. **Student List**:
   - **Type**: `List<Student>` (Java ArrayList).
   - **Purpose**: Stores student academic records.
   - **Fields**: Each `Student` has `name`, `group`, `email`, `calculusGrade`, `programmingGrade`, `englishGrade`.
   - **Storage**: Serialized to `students.json` for persistence.

3. **JSON Files**:
   - **Type**: Plain text files (`users.json`, `students.json`).
   - **Purpose**: Persistent storage for users and students.
   - **Structure**: JSON arrays of objects, where each object represents a `User` or `Student`.
   - **Access**: Read/written using Gson library for serialization/deserialization.

### Functions/Modules

1. **App Module** (`App.java`):
   - **Purpose**: Entry point for the JavaFX application.
   - **Functionality**: Initializes the application and loads the login interface (`login.fxml`).

2. **User Service Module** (`UserService.java`):
   - **Purpose**: Manages data operations for users and students.
   - **Key Functions**:
     - `loadUsers()`: Reads `users.json`, creates default users if file is missing.
     - `saveUsers(List<User>)`: Writes user list to `users.json`.
     - `loadStudents()`: Reads `students.json`, returns empty list if file is missing.
     - `saveStudents(List<Student>)`: Writes student list to `students.json`.
     - `authenticate(login, password)`: Verifies credentials, returns `User` or null.
     - `registerStudent(name, email, password, group)`: Adds a new student to both JSON files.
     - `updateStudentPassword(email, newPassword)`: Updates a student’s password with validation.

3. **Login Module** (`LoginController.java`):
   - **Purpose**: Handles user login and navigation to role-specific interfaces.
   - **Key Functions**:
     - `handleSignIn`: Authenticates user and opens Admin, Teacher, or Student panel.
     - `openRegistration`: Opens the registration form.
     - `loadWindow`: Loads role-specific FXML and initializes its controller.

4. **Registration Module** (`RegisterController.java`):
   - **Purpose**: Manages student registration.
   - **Key Functions**:
     - `initialize`: Populates group selection with options (ComCEH, ComSE, MatDais).
     - `handleRegistration`: Validates inputs (English-only, email format, password length) and registers a new student.

5. **Admin Module** (`AdminController.java`):
   - **Purpose**: Manages student records and grades for Admins.
   - **Key Functions**:
     - `initialize`: Loads students and group options.
     - `handleAdd`: Adds a new student.
     - `handleUpdate`: Updates student details and grades.
     - `handleRemove`: Deletes a selected student.
     - `handleClear`: Clears all student records.
     - `exit`: Returns to the login screen.
     - `refreshStudentList`: Updates the student list UI with details and GPA.

6. **Teacher Module** (`TeacherController.java`):
   - **Purpose**: Allows Teachers to assign subject-specific grades.
   - **Key Functions**:
     - `initialize`: Loads student data.
     - `setCurrentUser`: Sets the teacher’s subject (Calculus, Programming, English) and updates UI.
     - `handleUpdate`: Assigns a grade to a selected student.
     - `exit`: Returns to the login screen.
     - `refreshStudentList`: Updates the student list with subject grades.

7. **Student Module** (`StudentController.java`):
   - **Purpose**: Displays grades and GPA for Students.
   - **Key Functions**:
     - `setCurrentUser`: Sets the student’s email and loads their data.
     - `refreshStudentInfo`: Updates UI with student’s name, group, grades, and GPA.
     - `exit`: Returns to the login screen.

### Challenges Faced

1. **NullPointerException in Teacher Interface**:
   - **Problem**: Attempting to refresh the student list before setting the teacher’s subject caused crashes.
   - **Solution**: Deferred list refresh until after setting the subject in `setCurrentUser`, with a null check to prevent premature calls.

2. **JSON File Initialization**:
   - **Problem**: Absence of `users.json` or `students.json` on first run led to file access errors.
   - **Solution**: Implemented fallback in `UserService` to create default users (admin, teachers) if `users.json` is missing, and return an empty list for `students.json`.

3. **English-Only Input Validation**:
   - **Problem**: Users could enter non-Latin characters (e.g., Cyrillic), risking JSON encoding issues.
   - **Solution**: Added regular expression checks in `LoginController` and `RegisterController` to restrict inputs to Latin characters, displaying "Please write only in English" for invalid entries.

4. **Exit Button Consistency**:
   - **Problem**: Adding a logout feature required uniform behavior across all interfaces (Admin, Teacher, Student).
   - **Solution**: Implemented an `exit` method in each controller to load `login.fxml` and close the current window, ensuring seamless navigation.

5. **Input Validation Errors**:
   - **Problem**: Invalid inputs (e.g., non-numeric grades, missing '@' in emails) could crash the application or save incorrect data.
   - **Solution**: Added comprehensive checks (e.g., regex for emails, range checks for grades) with user-friendly error alerts to guide users.
