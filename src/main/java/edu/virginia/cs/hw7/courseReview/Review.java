package edu.virginia.cs.hw7.courseReview;

import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;

@Entity
@Table(name = "REVIEWS")
public class Review {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "ID")
    private Long id;

    @OneToOne(cascade = CascadeType.ALL)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "STUDENT_ID", referencedColumnName = "ID")
    private Student reviewer;

    @OneToOne(cascade = CascadeType.ALL)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "COURSE_ID", referencedColumnName = "ID")
    private Course course;

    @Column(name = "REVIEW", nullable = false)
    private String review;

    @Column(name = "RATING", nullable = false)
    private Rating rating;
    
    public Review() {}

    public Review(Student reviewer, Course course, String review, Rating rating) {
        this.reviewer = reviewer;
        this.course = course;
        this.review = review;
        if(review == null || review.isBlank()) {
            throw new EmptyReviewException("You must input a review");
        }
        if (rating == null){
            throw new InvalidRatingException("Please enter a rating between 1 and 5");
        }
        this.rating = rating;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Student getReviewer() {
        return reviewer;
    }

    public void setReviewer(Student reviewer) {
        this.reviewer = reviewer;
    }

    public Course getCourse() {
        return course;
    }

    public void setCourse(Course course) {
        this.course = course;
    }

    public String getReview() {
        return review;
    }

    public void setReview(String review) {
        this.review = review;
    }

    public Rating getRating() {
        return rating;
    }

    public void setRating(Rating rating) {
        this.rating = rating;
    }

    @Override
    public String toString() {
        return "Review{" +
                "id=" + id +
                ", reviewer=" + reviewer +
                ", course=" + course +
                ", review='" + review + '\'' +
                ", rating=" + rating +
                '}';
    }

    public enum Rating {
        ONE("1"), TWO("2"), THREE("3"), FOUR("4"), FIVE("5");
        public final String label;
        private Rating(String label){
            this.label = label;
        }

        public static Rating getEnumByString(String input){
            for (Rating r : Rating.values()){
                if (r.label.equals(input)) return r;
            }
            return null;
        }

        public static int enumToInteger(Rating input){
            for (Rating r : Rating.values()){
                if (input.label == null){
                    throw new InvalidRatingException("Please enter a rating between 1 and 5");
                }
                if (r.label.equals(input.label)) return Integer.parseInt(input.label);
            }
            return 0;
        }
    }

}


