//package entities.demo_hibernate_code_first;
//
//import javax.persistence.*;
//import java.util.Set;
//
//@Entity
//@Table(name = "students")
//public class Student extends Person {
//
//    private double grade;
//    private Set<Course> courses;
//
//    public Student() {
//    }
//    @Column(name = "grade")
//    public double getGrade() {
//        return this.grade;
//    }
//
//    public void setGrade(double grade) {
//        this.grade = grade;
//    }
//
//
//    @ManyToMany(mappedBy = "students", targetEntity = Course.class)
//    public Set<Course> getCourses() {
//        return courses;
//    }
//
//    public void setCourses(Set<Course> courses) {
//        this.courses = courses;
//    }
//}
