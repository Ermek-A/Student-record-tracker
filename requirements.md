## Project Requirement List

The following list defines the specific tasks, features, and deliverables required to achieve the objectives of the Student Record Tracker project:

1. **User Authentication**: The system must allow users to log in using a username/email and password, verifying credentials against stored data.
2. **Role-Based Access**: The system must provide distinct interfaces for Admin, Teacher, and Student roles, each with role-specific functionalities.
3. **Student Registration**: The system must enable new students to register with a name, email, password, and group, ensuring unique email addresses.
4. **Admin Student Management**: The system must allow Admins to add new students, including their name, group, email, and initial grades.
5. **Admin Data Editing**: The system must enable Admins to update student details (name, group, email, password) and grades (Calculus, Programming, English).
6. **Admin Data Removal**: The system must allow Admins to delete individual students or clear all student records.
7. **Teacher Grade Assignment**: The system must enable Teachers to assign grades (0–100) to students for their specific subject (Calculus, Programming, or English).
8. **Student Grade Viewing**: The system must display a Student’s grades and calculated GPA (100-point and 4.0 scales) in their interface.
9. **Data Persistence**: The system must store user credentials and student records persistently in JSON files (`users.json`, `students.json`).
10. **Input Validation**: The system must validate inputs, ensuring:
    - Emails contain an '@' symbol.
    - Passwords are no longer than 8 characters.
    - Grades are numbers between 0 and 100.
11. **English-Only Input**: The system must restrict login and registration inputs to Latin characters, displaying an error ("Please write only in English") for non-Latin inputs.
12. **Logout Functionality**: The system must provide an "Exit" button in Admin, Teacher, and Student interfaces to return to the login screen.
13. **Error Handling**: The system must display user-friendly error messages for invalid inputs, failed logins, or missing selections.
