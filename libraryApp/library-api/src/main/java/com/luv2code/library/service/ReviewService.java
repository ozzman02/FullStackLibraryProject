package com.luv2code.library.service;

import com.luv2code.library.dao.ReviewRepository;
import com.luv2code.library.entity.Review;
import com.luv2code.library.requestmodels.ReviewRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Date;
import java.time.LocalDate;

@Service
@Transactional
public class ReviewService {

    private final ReviewRepository reviewRepository;

    public ReviewService(ReviewRepository reviewRepository) {
        this.reviewRepository = reviewRepository;
    }

    public void postReview(String userEmail, ReviewRequest reviewRequest) throws Exception {
        if (reviewRepository.findByUserEmailAndBookId(userEmail, reviewRequest.getBookId()) != null) {
            throw new Exception("Review already created");
        }
        Review review = new Review();
        review.setBookId(reviewRequest.getBookId());
        review.setRating(reviewRequest.getRating());
        review.setUserEmail(userEmail);
        if (reviewRequest.getReviewDescription().isPresent()) {
            review.setReviewDescription(reviewRequest.getReviewDescription()
                    .map(Object::toString)
                    .orElse(null)
            );
        }
        review.setDate(Date.valueOf(LocalDate.now()));
        reviewRepository.save(review);
    }

    public boolean userReviewListed(String userEmail, Long bookId) {
        return reviewRepository.findByUserEmailAndBookId(userEmail, bookId) != null;
    }

}
