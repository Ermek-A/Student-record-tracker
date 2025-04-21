# Project Title and Description

## Title: Student Record Tracker

## Path to codes: src/main/java/org/example/student_record_tracker
## Path to fxml(front-end) files: src/main/resources/org/example/student_record_tracker
## Presentation -> https://drive.google.com/drive/folders/1OYDLP72Do9oOSnq4G5EUeylHeKYQKZ2l?usp=sharing
## Description:

The **Student Record Tracker** is a JavaFX-based desktop application designed to manage student academic records efficiently. It supports three user roles—**Admin**, **Teacher**, and **Student**—with distinct functionalities to streamline academic processes. Admins can manage student data and grades, Teachers assign subject-specific grades, and Students view their grades and GPA. The application ensures data persistence using JSON files and restricts inputs to English characters for consistency.

**Student Name, Surname**: [Ermek Asangulov]

---
## Overview

The purpose of the Student Record Tracker is to provide a centralized system for managing student information and grades. The application supports three user roles:

- **Administrator**: Manages student records (add, update, delete, clear) and can assign grades.
- **Teacher**: Assigns grades for their specific subject (Calculus, Programming, or English).
- **Student**: Views their grades and calculated Grade Point Average (GPA).

Key features include:

- **User Authentication**: Secure login and registration for students, with password strength validation using the zxcvbn library.
- **Student Management**: Administrators can add, update, or remove student records, including personal details and grades.
- **Grade Management**: Teachers can assign grades for their subject, and administrators can manage grades for all subjects.
- **GPA Calculation**: Automatically calculates a student’s GPA on a 4.0 scale based on their grades.
- **Persistent Storage**: Uses JSON files (`users.json` for user credentials and `students.json` for student records) to store data.
- **User Interface**: Built with JavaFX and FXML for a responsive and intuitive experience.

The application starts with a login screen, allowing users to sign in or register (for students). Based on the user’s role, it redirects to the appropriate dashboard (admin, teacher, or student), where role-specific functionalities are available.

## Objectives

The primary goals of the Student Record Tracker are:

1. **Efficient Data Management**: To provide a system that simplifies the creation, modification, and deletion of student records, reducing manual effort for administrators.
2. **Role-Based Access**: To implement distinct functionalities for administrators, teachers, and students, ensuring that each user can only access features relevant to their role.
3. **Accurate Grade Tracking**: To enable teachers to assign grades and students to view their performance, with automatic GPA calculation for academic evaluation.
4. **Data Persistence**: To ensure that user and student data is saved reliably in JSON files, maintaining consistency across application sessions.
5. **Secure Authentication**: To incorporate user registration and login with password strength checks, enhancing the security of user accounts.
6. **User-Friendly Interface**: To design an intuitive and visually appealing interface using JavaFX, making the application accessible to users with varying technical expertise.

By achieving these objectives, the project aims to deliver a robust and practical tool for academic institutions to manage student records and grades efficiently.
