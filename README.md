# Project Title and Description

## Title: Student Record Tracker

## Path to codes: src/main/java/org/example/student_record_tracker
## Path to fxml(front-end) files: src/main/resources/org/example/student_record_tracker
## Description:

The **Student Record Tracker** is a JavaFX-based desktop application designed to manage student academic records efficiently. It supports three user roles—**Admin**, **Teacher**, and **Student**—with distinct functionalities to streamline academic processes. Admins can manage student data and grades, Teachers assign subject-specific grades, and Students view their grades and GPA. The application ensures data persistence using JSON files and restricts inputs to English characters for consistency.

**Student Name, Surname**: [Ermek Asangulov]

---

## Project Overview

### Purpose:

The purpose of the **Student Record Tracker** is to provide an intuitive platform for academic institutions to manage student records, facilitate grade assignments, and enable students to monitor their academic performance. It simplifies administrative tasks, enhances teacher-student interaction, and ensures reliable data storage without requiring a complex database.

---

### Functionality:

- **Admin Role**: Add, update, or remove student records, including personal details and grades for *Calculus*, *Programming*, and *English*.
- **Teacher Role**: Assign and update grades for a specific subject (*Calculus*, *Programming*, or *English*) for all students.
- **Student Role**: View personal grades and calculated GPA (*100-point and 4.0 scales*).
- **General Features**:
  - User authentication
  - Student registration
  - Logout functionality via an **"Exit"** button
  - Input validation to enforce **English-only** characters for login and registration

- **Data Storage**:
  - Persistent storage of user credentials and student records in `users.json` and `students.json`.

---

## Objectives

The **Student Record Tracker** aims to achieve the following goals:

- **Efficient Record Management**: Enable administrators to centrally manage student data, including personal information and academic grades, in a user-friendly interface.
- **Role-Based Functionality**: Provide distinct, secure interfaces for Admins, Teachers, and Students to perform their specific tasks effectively.
- **Accurate Grade Tracking**: Allow Teachers to assign valid grades (0–100) with input validation, ensuring reliable academic records.
- **Student Access**: Empower Students to view their grades and GPA, supporting academic transparency and planning.
- **Data Reliability**: Use JSON files for persistent, lightweight data storage, ensuring data is preserved between sessions.
- **Input Consistency**: Restrict login and registration inputs to English characters to prevent encoding issues and maintain system integrity.
- **Usability**: Incorporate intuitive navigation, including a logout feature ("Exit" button), to enhance the user experience across all roles.
