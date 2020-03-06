//package entities.demo_hibernate_code_first;
//
//import javax.persistence.*;
//import java.util.Set;
//
//
//@Entity
//@Table(name = "teachers")
//public class Teacher extends Person {
//
//    private String speciality;
//    private Set<Course> courses;
//
//    public Teacher() {
//    }
//
//    @Column (name = "speciality")
//    public String getSpeciality() {
//        return speciality;
//    }
//
//    public void setSpeciality(String speciality) {
//        this.speciality = speciality;
//    }
//
//
//    @OneToMany
//    @JoinColumn(name = "course_id", referencedColumnName = "id")
//    public Set<Course> getCourses() {
//        return courses;
//    }
//
//    public void setCourses(Set<Course> courses) {
//        this.courses = courses;
//    }
//}
