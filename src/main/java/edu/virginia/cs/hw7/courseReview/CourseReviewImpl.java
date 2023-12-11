package edu.virginia.cs.hw7.courseReview;

import org.hibernate.Session;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.List;

public class CourseReviewImpl implements CourseReview{
    private Student currentUser;
    private Course currentCourse;
    private boolean loginStatus;
    private Repository repository;
    //private Session session;



    public CourseReviewImpl(){
        //initializeDatabase();
        loginStatus = false;
        currentUser = null;
        currentCourse = null;
        repository = Repository.getInstance();
    }

    /* Purpose: To Login User into Program
     * @throws StudentNotFoundException when inputted credentials aren't in the Student database*/
    public void loginExistingUser(String loginName, String password){
        if (loginName.isEmpty() || password.isEmpty()) { throw new IllegalArgumentException("Please don't leave any of the fields blank"); }
        Student student = repository.getStudentByName(loginName);
        if (student != null && student.getPassword().equals(password)){
            loginStatus = true;
            currentUser = student;
        }
        else {
            throw new StudentNotFoundException("Inputted Credentials Not Found in System");
        }
    }

    /* Purpose: To Create New User to add to Student Database
     * @throws IllegalArgumentException when passwords don't match or when the username is already taken*/
    public void createNewUser(String loginName, String password, String confirmPassword) {
        if (loginName.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) { throw new IllegalArgumentException("Please don't leave any of the fields blank"); }
        if (password.equals(confirmPassword)) {
            Student newUser = new Student(loginName, password);
            checkIfUserAlreadyInDatabase(newUser.getUsername());
            repository.saveStudent(newUser);//Save the new student
            loginStatus = true; //logging in
            currentUser = newUser;
        }
        else{
            throw new PasswordDoesNotMatchException("Please Enter Matching Passwords");
        }
    }

    private void checkIfUserAlreadyInDatabase (String userName){
        if (repository.studentExistsByName(userName)) {
            throw new UsernameTakenException("Sorry, that username was already taken. Please be unique");
        }
    }

    /*
    Purpose: Allow User to input Course They Want to Review
    @throws UserAlreadyReviewedCourseException when they try to review a course more than once
    @throws InvalidCourseNameException when user inputs invalid course name
    */
    public void submitCourseForReview (String department, int catalogNumber){
        Course courseForReview = new Course(department, catalogNumber);
        addCourseToDatabaseIfNeeded(courseForReview);
        hasStudentReviewedCourse(currentCourse);
    }

    private void hasStudentReviewedCourse (Course course){
        if (repository.getReviewByUserAndCourse(currentUser, course) != null){
            throw new UserAlreadyReviewedCourseException("You Have Already Reviewed This Course");
        }
    }

    private void addCourseToDatabaseIfNeeded(Course courseToBeAdd){
        Course is_present = repository.getCourse(courseToBeAdd.getDepartment(), courseToBeAdd.getCatalogNumber());
        if (is_present == null){
            repository.saveCourse(courseToBeAdd);
            currentCourse = courseToBeAdd;//The id will be set by saveCourse
            return;
        }
        currentCourse = is_present;
    }

    /*
    Purpose: Allow User to submit a review for a course
    @throws IllegalArgumentException when insert invalid rating number
    */
    public void submitReview (String review, Review.Rating rating){
            if(currentCourse == null)
                throw new IllegalStateException("User has not selected a course to review");
            Review newReview = new Review(currentUser, currentCourse, review, rating);
            repository.saveReview(newReview);
            currentCourse = null;
    }

    public List<Review> viewReview (String department, int catalogNumber){
        Course getCourse = new Course(department, catalogNumber); //throws @InvalidNameException if invalid input
        currentCourse = repository.getCourse(department, catalogNumber);
        if (currentCourse == null){
            throw new IllegalArgumentException("Sorry, No Reviews For Given Course"); //bring back to main menu
        }
        List<Review> reviews = repository.getReviewByCourse(currentCourse);
        if (reviews != null){
            return reviews;
        }
        else{
            throw new IllegalArgumentException("Sorry, No Reviews For Given Course"); //bring back to main menu
        }
    }

    public void loginOut(){
        loginStatus = false;
        currentUser = null;
        //controller will log user out ??
    }
    public boolean isLoggedIn(){ return loginStatus;}
    private void initializeDatabase(){ //part A
    }

}

