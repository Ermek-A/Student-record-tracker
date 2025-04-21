# Project Requirement List for Student Record Tracker

## Overview
The **Student Record Tracker** is a JavaFX-based desktop application designed to manage student records and grades in an academic environment. The following list outlines the specific tasks, features, and deliverables required to achieve the project’s objectives, ensuring a functional, user-friendly, and reliable system for administrators, teachers, and students.

## Key Requirements

1. **User Authentication System**  
   - Implement a login screen that allows users to sign in using a username or email and password.  
   - Support authentication for three roles: administrator, teacher, and student.  
   - Verify credentials against data stored in a JSON file (`users.json`).  
   - Deliverable: A functional login interface with validation and error handling for incorrect credentials.

2. **Student Registration**  
   - Provide a registration interface for students to create accounts with their name, email, password, and academic group.  
   - Validate password strength using the zxcvbn library (minimum length of 6 characters, score ≥ 2).  
   - Ensure email and username uniqueness, storing new user data in `users.json` and student data in `students.json`.  
   - Deliverable: A registration form with input validation and success/error messages.

3. **Role-Based Dashboards**  
   - Redirect users to role-specific dashboards after successful login:  
     - Administrator: Full access to manage students and grades.  
     - Teacher: Access to assign grades for their subject.  
     - Student: View-only access to their grades and GPA.  
   - Deliverable: Three distinct FXML-based dashboards tailored to each role’s functionality.

4. **Administrator Student Management**  
   - Enable administrators to add new students with details (name, email, group, password).  
   - Allow updating existing student records (name, email, group, password, grades).  
   - Support deletion of individual students or clearing all student records.  
   - Deliverable: An admin dashboard with a table view and controls for adding, updating, and removing students.

5. **Grade Assignment by Teachers**  
   - Allow teachers to assign grades (0–100) for their specific subject:  
     - Calculus teacher: Calculus grades.  
     - Programming teacher: Programming grades.  
     - English teacher: English grades.  
   - Update student records in `students.json` with new grades.  
   - Deliverable: A teacher dashboard with a table view and input fields for grade assignment.

6. **Grade Viewing for Students**  
   - Enable students to view their grades for Calculus, Programming, and English, along with their calculated GPA.  
   - Display grades in a clear, read-only format.  
   - Deliverable: A student dashboard showing grades and GPA in a user-friendly layout.

7. **GPA Calculation**  
   - Automatically calculate a student’s GPA on a 4.0 scale based on their grades:  
     - Average grades (Calculus, Programming, English) to get a 100-point GPA, then divide by 25, capping at 4.0.  
   - Update GPA whenever grades are modified.  
   - Deliverable: Accurate GPA displayed in student and admin dashboards, stored in `students.json`.

8. **Persistent Data Storage**  
   - Store user credentials (username, password, role, email) in `users.json`.  
   - Store student records (name, group, email, grades, GPA) in `students.json`.  
   - Use the Gson library for JSON serialization and deserialization to ensure data persistence across sessions.  
   - Deliverable: Functional JSON-based storage with data integrity.

9. **Input Validation and Security**  
   - Validate all user inputs (e.g., email format, Latin characters for username/password, numeric grades between 0–100).  
   - Implement password strength checks during registration and password updates.  
   - Handle errors gracefully with user-friendly alerts.  
   - Deliverable: Robust input validation and error messaging throughout the application.

10. **User-Friendly JavaFX Interface**  
    - Design intuitive interfaces using JavaFX and FXML for all screens (login, registration, dashboards).  
    - Use consistent layouts, clear labels, and interactive elements (buttons, tables, text fields, combo boxes).  
    - Ensure responsive navigation between screens (e.g., login to dashboard, registration to login).  
    - Deliverable: A polished, visually appealing UI that enhances user experience.

## Conclusion
These requirements ensure that the Student Record Tracker meets its objectives of providing efficient student management, secure authentication, accurate grade tracking, and a user-friendly experience. Each requirement is implemented to support the core functionality, making the application a practical tool for academic institutions.
