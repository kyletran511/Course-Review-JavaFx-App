package edu.virginia.cs.hw7.courseReview;

import java.util.List;

public interface CourseReview {
    void loginExistingUser(String loginName, String password);
    void createNewUser(String loginName, String password, String confirmPassword);
    void submitCourseForReview(String department, int catalogNumber);
    void submitReview(String review, Review.Rating rating);
    List<Review> viewReview(String department, int catalogNumber);
    void loginOut();
    boolean isLoggedIn();
}

