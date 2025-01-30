package com.luv2code.library.controller;

import com.luv2code.library.requestmodels.ReviewRequest;
import com.luv2code.library.service.ReviewService;
import org.springframework.web.bind.annotation.*;

import static com.luv2code.library.constants.ApplicationConstants.extractionValue;
import static com.luv2code.library.utils.ExtractJWT.payloadJwtExtraction;

@CrossOrigin("http://localhost:3000")
@RestController
@RequestMapping("/api/reviews")
public class ReviewController {

    private final ReviewService reviewService;

    public ReviewController(ReviewService reviewService) {
        this.reviewService = reviewService;
    }

    @PostMapping("/secure")
    public void postReview(@RequestHeader(value = "Authorization") String token,
                           @RequestBody ReviewRequest reviewRequest) throws Exception {

        String userEmail = payloadJwtExtraction(token, extractionValue);
        if (userEmail == null) {
            throw new Exception("User email is missing");
        }
        reviewService.postReview(userEmail, reviewRequest);
    }

    @GetMapping("/secure/user/book")
    public Boolean reviewBookByUser(@RequestHeader(value = "Authorization") String token,
                                    @RequestParam Long bookId) throws Exception {
        String userEmail = payloadJwtExtraction(token, extractionValue);
        if (userEmail == null) {
            throw new Exception("User email is missing");
        }
        return reviewService.userReviewListed(userEmail, bookId);
    }

}
