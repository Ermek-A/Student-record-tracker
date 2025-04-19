package org.example.student_record_tracker;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import com.nulabinc.zxcvbn.Zxcvbn;
import com.nulabinc.zxcvbn.Strength;

import java.io.*;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class UserService {

    private static final String USERS_FILE = "users.json";
    private static final String STUDENTS_FILE = "students.json";
    private static final Gson gson = new GsonBuilder().setPrettyPrinting().create();

    public List<User> loadUsers() {
        File file = new File(USERS_FILE);
        if (!file.exists()) {
            List<User> users = new ArrayList<>();
            users.add(new User("admin", "admin", "admin", "admin@xai.com"));
            users.add(new User("calculus", "calculus", "teacher", "calculus@xai.com"));
            users.add(new User("proglang", "proglang", "teacher", "proglang@xai.com"));
            users.add(new User("english", "english", "teacher", "english@xai.com"));
            saveUsers(users);
            return users;
        }

        try (Reader reader = new FileReader(file)) {
            Type listType = new TypeToken<List<User>>() {}.getType();
            List<User> users = gson.fromJson(reader, listType);
            return users != null ? users : new ArrayList<>();
        } catch (IOException e) {
            return new ArrayList<>();
        }
    }

    public void saveUsers(List<User> users) {
        try (Writer writer = new FileWriter(USERS_FILE)) {
            gson.toJson(users, writer);
        } catch (IOException e) {
            throw new RuntimeException("Failed to save users.");
        }
    }

    public void registerStudent(String name, String email, String password, String group) {
        List<User> users = loadUsers();
        List<Student> students = loadStudents();

        if (users.stream().anyMatch(user -> user.getEmail().equals(email) || user.getUsername().equals(email))) {
            throw new IllegalArgumentException("This email or username is already taken.");
        }


        Zxcvbn passwordCheck = new Zxcvbn();
        Strength strength = passwordCheck.measure(password);
        if (password.length() < 6) {
            throw new IllegalArgumentException("Your password must be 6 or more characters long.");
        }
        if (strength.getScore() < 2) {
            throw new IllegalArgumentException("Weak password! Please choose a stronger password. Hint: " + strength.getFeedback().getWarning());
        }

        String username = email.split("@")[0];
        users.add(new User(username, password, "student", email));
        saveUsers(users);

        if (students.stream().noneMatch(student -> student.getEmail().equals(email))) {
            students.add(new Student(name, group, email));
            saveStudents(students);
        }
    }

    public static User authenticate(String login, String password) {
        List<User> users = new UserService().loadUsers();
        return users.stream()
                .filter(user -> (user.getEmail().equals(login) || user.getUsername().equals(login)) && user.getPassword().equals(password))
                .findFirst()
                .orElse(null);
    }

    public List<Student> loadStudents() {
        File file = new File(STUDENTS_FILE);
        if (!file.exists()) {
            return new ArrayList<>();
        }

        try (Reader reader = new FileReader(file)) {
            Type listType = new TypeToken<List<Student>>() {}.getType();
            List<Student> students = gson.fromJson(reader, listType);
            return students != null ? students : new ArrayList<>();
        } catch (IOException e) {
            return new ArrayList<>();
        }
    }

    public void saveStudents(List<Student> students) {
        try (Writer writer = new FileWriter(STUDENTS_FILE)) {
            gson.toJson(students, writer);
        } catch (IOException e) {
            throw new RuntimeException("Failed to save students.");
        }

        List<User> users = loadUsers();
        List<String> studentEmails = students.stream()
                .map(Student::getEmail)
                .collect(Collectors.toList());
        users = users.stream()
                .filter(user -> !user.getRole().equals("student") || studentEmails.contains(user.getEmail()))
                .collect(Collectors.toList());
        saveUsers(users);
    }

    public void deleteUserByEmail(String email){
        List<User> users = loadUsers();
        users = users.stream()
                .filter(user -> !user.getEmail().equals(email) || !user.getRole().equals("student"))
                .collect(Collectors.toList());
        saveUsers(users);
    }

    public void updateStudentPassword(String email, String newPassword) {
        List<User> users = loadUsers();
        User user = users.stream()
                .filter(u -> u.getEmail().equals(email))
                .findFirst()
                .orElse(null);

        if (user != null) {
            Zxcvbn passwordCheck = new Zxcvbn();
            Strength strength = passwordCheck.measure(newPassword);
            if (newPassword.length() < 6) {
                throw new IllegalArgumentException("Your password must be 6 or more characters long.");
            }
            if (strength.getScore() < 2) {
                throw new IllegalArgumentException("Weak password! Please choose a stronger password. Hint: " + strength.getFeedback().getWarning());
            }
            user.setPassword(newPassword);
            saveUsers(users);
        }
    }
}