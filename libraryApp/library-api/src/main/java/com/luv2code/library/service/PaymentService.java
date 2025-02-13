package com.luv2code.library.service;

import com.luv2code.library.dao.PaymentRepository;
import com.luv2code.library.entity.Payment;
import com.luv2code.library.requestmodels.PaymentInfoRequest;
import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.PaymentIntent;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.luv2code.library.constants.ApplicationConstants.*;


@Service
@Transactional
public class PaymentService {

    private final PaymentRepository paymentRepository;

    public PaymentService(PaymentRepository paymentRepository, @Value("${stripe.key.secret}") String secretKey) {
        this.paymentRepository = paymentRepository;
        Stripe.apiKey = secretKey;
    }

    public PaymentIntent createPaymentIntent(PaymentInfoRequest paymentInfoRequest) throws StripeException {
        List<String> paymentMethodTypes = new ArrayList<>();
        paymentMethodTypes.add("card");
        Map<String, Object> params = new HashMap<>();
        params.put(STRIPE_AMOUNT_PARAM, paymentInfoRequest.getAmount());
        params.put(STRIPE_CURRENCY_PARAM, paymentInfoRequest.getCurrency());
        params.put(STRIPE_PAYMENT_METHOD_TYPES, paymentMethodTypes);
        return PaymentIntent.create(params);
    }

    public ResponseEntity<String> stripePayment(String userEmail) throws Exception {
        if (userEmail == null) throw new Exception("User email is missing");
        Payment payment = paymentRepository.findByUserEmail(userEmail)
                .orElseThrow(() -> new Exception("Payment information is missing"));
        payment.setAmount(00.00);
        paymentRepository.save(payment);
        return new ResponseEntity<>(HttpStatus.OK);
    }

}
