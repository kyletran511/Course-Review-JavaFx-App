package edu.virginia.cs.hw7.courseReview;

import javax.persistence.*;

@Entity
@Table(name = "COURSES")
public class Course {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "ID", nullable = false)
    private Long id;

    @Column(name = "DEPARTMENT", length = 4, nullable = false)
    private String department;


    @Column(name = "CATALOG_NUMBER", nullable = false)
    private int catalogNumber;

    public Course() {}

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public int getCatalogNumber() {
        return catalogNumber;
    }

    public void setCatalogNumber(int number) {
        this.catalogNumber = number;
    }

    public Course(String department, int catalogNumber) {
        if(!department.isBlank() && department.length() <= 4 && checkIfCapitalized(department)) {
            this.department = department.toUpperCase();
        } else {
            throw new InvalidCourseNameException("Department should be 4 or less CAPITALIZED chars and not blank");
        }
        if(isValidCourseNum(catalogNumber)) {
            this.catalogNumber = catalogNumber;
        } else {
            throw new InvalidCourseNameException("Need a 4 digit number, no leading 0s");
        }
    }

    public static boolean isValidCourseNum(int number) {
        return number >= 1000 && number <= 9999;
    }

    @Override
    public String toString() {
        return "Course{" +
                "id=" + id +
                ", department='" + department + '\'' +
                ", number=" + catalogNumber +
                '}';
    }

    private boolean checkIfCapitalized(String department){
        for (int i = 0; i < department.length(); i++){
            if (!Character.isUpperCase(department.charAt(i))){
                return false;
            }
        }
        return true;
    }
}
