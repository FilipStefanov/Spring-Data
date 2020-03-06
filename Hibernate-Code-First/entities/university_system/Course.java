//package entities.university_system;
//
//import entities.BaseEntity;
//
//import javax.persistence.*;
//import java.time.LocalDate;
//import java.util.Set;
//
//@Entity
//@Table(name = "courses")
//public class Course extends BaseEntity {
//    private String name;
//    private String description;
//    private LocalDate start_date;
//    private LocalDate end_date;
//    private int credits;
//    private Set<Student> students;
//    private Teacher teacher;
//
//    public Course() {
//    }
//
//    @Column(name = "first_name")
//    public String getName() {
//        return name;
//    }
//
//    public void setName(String name) {
//        this.name = name;
//    }
//
//    @Column(name = "description", length = 100)
//    public String getDescription() {
//        return description;
//    }
//
//    public void setDescription(String description) {
//        this.description = description;
//    }
//
//    @Column(name = "start_date")
//    public LocalDate getStart_date() {
//        return start_date;
//    }
//
//    public void setStart_date(LocalDate start_date) {
//        this.start_date = start_date;
//    }
//
//    @Column(name = "end_date")
//    public LocalDate getEnd_date() {
//        return end_date;
//    }
//
//    public void setEnd_date(LocalDate end_date) {
//        this.end_date = end_date;
//    }
//
//    @Column(name = "credits")
//    public int getCredits() {
//        return credits;
//    }
//
//    public void setCredits(int credits) {
//        this.credits = credits;
//    }
//
//    @ManyToMany
//    @JoinTable(name = "courses_students",
//            joinColumns = @JoinColumn(name = "course_id", referencedColumnName = "id"),
//            inverseJoinColumns = @JoinColumn(name = "student_id", referencedColumnName ="id")
//    )
//    public Set<Student> getStudents() {
//        return students;
//    }
//
//    public void setStudents(Set<Student> students) {
//        this.students = students;
//    }
//
//    @ManyToOne
//    @JoinColumn(name = "teacher_id",
//            referencedColumnName = "id"
//    )
//    public Teacher getTeacher() {
//        return teacher;
//    }
//
//    public void setTeacher(Teacher teacher) {
//        this.teacher = teacher;
//    }
//}
