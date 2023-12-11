package edu.virginia.cs.hw7.courseReview;

public class PasswordDoesNotMatchException extends IllegalArgumentException {
    public PasswordDoesNotMatchException(String message) { super(message); }
}
