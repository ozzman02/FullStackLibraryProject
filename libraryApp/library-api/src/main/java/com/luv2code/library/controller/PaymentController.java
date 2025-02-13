package com.luv2code.library.controller;

import com.luv2code.library.requestmodels.PaymentInfoRequest;
import com.luv2code.library.service.PaymentService;
import com.stripe.exception.StripeException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static com.luv2code.library.constants.ApplicationConstants.HTTPS_ALLOWED_ORIGINS;
import static com.luv2code.library.constants.ApplicationConstants.USER_EMAIL_CLAIM;
import static com.luv2code.library.utils.AppUtil.payloadJwtExtraction;

@CrossOrigin(HTTPS_ALLOWED_ORIGINS)
@RestController
@RequestMapping("/api/payment/secure")
public class PaymentController {

    private final PaymentService paymentService;

    public PaymentController(PaymentService paymentService) {
        this.paymentService = paymentService;
    }

    @PostMapping("/payment-intent")
    public ResponseEntity<String> createPaymentIntent(@RequestBody PaymentInfoRequest paymentInfoRequest)
            throws StripeException {
        return new ResponseEntity<>(paymentService.createPaymentIntent(paymentInfoRequest).toJson(), HttpStatus.OK);
    }

    @PutMapping("/payment-complete")
    public ResponseEntity<String> stripePaymentComplete(@RequestHeader(value = "Authorization") String token)
            throws Exception {
        return paymentService.stripePayment(payloadJwtExtraction(token, USER_EMAIL_CLAIM));
    }
}
