package edu.virginia.cs.hw7.courseReview;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class CourseReviewTests {

    private CourseReview courseReviewTest;
    private Repository repository = Repository.getInstance();

    @BeforeEach
    public void setUp(){
        courseReviewTest = new CourseReviewImpl();
    }

    @Test
    public void testCreateNewUser(){
        Student addStudent = new Student("Bob", "test");
        courseReviewTest.createNewUser("Bob", "test", "test");
        assertEquals(addStudent, repository.getStudentByName("Bob"));
    }
}