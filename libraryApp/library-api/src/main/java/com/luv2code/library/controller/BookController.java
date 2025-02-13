package com.luv2code.library.controller;

import com.luv2code.library.entity.Book;
import com.luv2code.library.responsemodels.ShelfCurrentLoansResponse;
import com.luv2code.library.service.BookService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.luv2code.library.constants.ApplicationConstants.HTTPS_ALLOWED_ORIGINS;
import static com.luv2code.library.constants.ApplicationConstants.USER_EMAIL_CLAIM;
import static com.luv2code.library.utils.AppUtil.payloadJwtExtraction;

@CrossOrigin(HTTPS_ALLOWED_ORIGINS)
@RestController
@RequestMapping("/api/books")
public class BookController {

    private final BookService bookService;

    public BookController(BookService bookService) {
        this.bookService = bookService;
    }

    @PutMapping("/secure/checkout")
    public Book checkoutBook(@RequestHeader(value = "Authorization") String token,
                             @RequestParam Long bookId) {
        return bookService.checkoutBook(payloadJwtExtraction(token, USER_EMAIL_CLAIM), bookId);
    }

    @GetMapping("/secure/ischeckedout/byuser")
    public Boolean isBookCheckedOutByUser(@RequestHeader(value = "Authorization") String token,
                                          @RequestParam Long bookId) {
        return bookService.isBookCheckedOutByUser(payloadJwtExtraction(token, USER_EMAIL_CLAIM), bookId);
    }

    @GetMapping("/secure/currentloans/count")
    public int currentLoansCount(@RequestHeader(value = "Authorization") String token) {
        return bookService.currentLoansCount(payloadJwtExtraction(token, USER_EMAIL_CLAIM));
    }

    @GetMapping("/secure/currentloans")
    public List<ShelfCurrentLoansResponse> currentLoans(@RequestHeader(value = "Authorization") String token)
            throws Exception {
        return bookService.currentLoans(payloadJwtExtraction(token, USER_EMAIL_CLAIM));
    }

    @PutMapping("/secure/return")
    public void returnBook(@RequestHeader(value = "Authorization") String token,
                           @RequestParam Long bookId) throws Exception {
        bookService.returnBook(payloadJwtExtraction(token, USER_EMAIL_CLAIM), bookId);
    }

    @PutMapping("/secure/renew/loan")
    public void renewLoan(@RequestHeader(value = "Authorization") String token,
                           @RequestParam Long bookId) throws Exception {
        bookService.renewLoan(payloadJwtExtraction(token, USER_EMAIL_CLAIM), bookId);
    }
}
