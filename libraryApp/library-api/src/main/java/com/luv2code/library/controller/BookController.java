package com.luv2code.library.controller;

import com.luv2code.library.entity.Book;
import com.luv2code.library.responsemodels.ShelfCurrentLoansResponse;
import com.luv2code.library.service.BookService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.luv2code.library.constants.ApplicationConstants.extractionValue;
import static com.luv2code.library.utils.ExtractJWT.payloadJwtExtraction;

@CrossOrigin("http://localhost:3000")
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
        return bookService.checkoutBook(payloadJwtExtraction(token, extractionValue), bookId);
    }

    @GetMapping("/secure/ischeckedout/byuser")
    public Boolean isBookCheckedOutByUser(@RequestHeader(value = "Authorization") String token,
                                          @RequestParam Long bookId) {
        return bookService.isBookCheckedOutByUser(payloadJwtExtraction(token, extractionValue), bookId);
    }

    @GetMapping("/secure/currentloans/count")
    public int currentLoansCount(@RequestHeader(value = "Authorization") String token) {
        return bookService.currentLoansCount(payloadJwtExtraction(token, extractionValue));
    }

    @GetMapping("/secure/currentloans")
    public List<ShelfCurrentLoansResponse> currentLoans(@RequestHeader(value = "Authorization") String token)
            throws Exception {
        return bookService.currentLoans(payloadJwtExtraction(token, extractionValue));
    }

}
