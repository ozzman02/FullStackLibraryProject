package com.luv2code.library.entity;

import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;

import java.util.Date;
import java.util.Objects;

@Entity
@Table(name = "review")
public class Review {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "user_email")
    private String userEmail;

    @CreationTimestamp
    @Column(name = "date")
    private Date date;

    @Column(name = "rating")
    private double rating;

    @Column(name = "book_id")
    private Long bookId;

    @Column(name = "review_description")
    private String reviewDescription;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public double getRating() {
        return rating;
    }

    public void setRating(double rating) {
        this.rating = rating;
    }

    public Long getBookId() {
        return bookId;
    }

    public void setBookId(Long bookId) {
        this.bookId = bookId;
    }

    public String getReviewDescription() {
        return reviewDescription;
    }

    public void setReviewDescription(String reviewDescription) {
        this.reviewDescription = reviewDescription;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Review review = (Review) o;
        return Double.compare(rating, review.rating) == 0 &&
                Objects.equals(id, review.id) &&
                Objects.equals(userEmail, review.userEmail) &&
                Objects.equals(date, review.date) &&
                Objects.equals(bookId, review.bookId) &&
                Objects.equals(reviewDescription, review.reviewDescription);
    }

    @Override
    public int hashCode() {
        int result = Objects.hashCode(id);
        result = 31 * result + Objects.hashCode(userEmail);
        result = 31 * result + Objects.hashCode(date);
        result = 31 * result + Double.hashCode(rating);
        result = 31 * result + Objects.hashCode(bookId);
        result = 31 * result + Objects.hashCode(reviewDescription);
        return result;
    }
}
