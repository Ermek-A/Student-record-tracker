package org.example.student_record_tracker;

public class Student {
    private String name;
    private String group;
    private String email;
    private int calculusGrade;
    private int programmingGrade;
    private int englishGrade;
    private double gpa;

    public Student(String name, String group, String email) {
        this.name = name;
        this.group = group;
        this.email = email;
        this.calculusGrade = 0;
        this.programmingGrade = 0;
        this.englishGrade = 0;
        this.gpa = calculateFourPointGPA();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getGroup() {
        return group;
    }

    public void setGroup(String group) {
        this.group = group;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public int getCalculusGrade() {
        return calculusGrade;
    }

    public void setCalculusGrade(int grade) {
        this.calculusGrade = grade;
        this.gpa = calculateFourPointGPA();
    }

    public int getProgrammingGrade() {
        return programmingGrade;
    }

    public void setProgrammingGrade(int grade) {
        this.programmingGrade = grade;
        this.gpa = calculateFourPointGPA();
    }

    public int getEnglishGrade() {
        return englishGrade;
    }

    public void setEnglishGrade(int grade) {
        this.englishGrade = grade;
        this.gpa = calculateFourPointGPA();
    }

    public double getGpa() {
        return gpa;
    }

    public double calculateHundredPointGPA() {
        return (calculusGrade + programmingGrade + englishGrade) / 3.0;
    }

    public double calculateFourPointGPA() {
        double fourPointGPA = calculateHundredPointGPA() / 25.0;
        return Math.min(fourPointGPA, 4.0);
    }
}