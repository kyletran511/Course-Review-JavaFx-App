package edu.virginia.cs.hw7.gui;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

public class ReviewGUI{
    private SimpleStringProperty review;
    private SimpleIntegerProperty rating;

    public ReviewGUI(String review, int rating){
        this.review = new SimpleStringProperty(review);
        this.rating = new SimpleIntegerProperty(rating);
    }

    public String getReview() {
        return review.get();
    }

    public SimpleStringProperty reviewProperty() {
        return review;
    }

    public void setReview(String review) {
        this.review.set(review);
    }

    public int getRating() {
        return rating.get();
    }

    public SimpleIntegerProperty ratingProperty() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating.set(rating);
    }
}
