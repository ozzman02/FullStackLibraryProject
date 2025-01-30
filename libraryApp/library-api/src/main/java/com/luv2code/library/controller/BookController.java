package com.luv2code.library.controller;

import com.luv2code.library.entity.Book;
import com.luv2code.library.service.BookService;
import org.springframework.web.bind.annotation.*;

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
        String userEmail = payloadJwtExtraction(token, extractionValue);
        return bookService.checkoutBook(userEmail, bookId);
    }

    @GetMapping("/secure/ischeckedout/byuser")
    public Boolean isBookCheckedOutByUser(@RequestHeader(value = "Authorization") String token,
                                          @RequestParam Long bookId) {
        String userEmail = payloadJwtExtraction(token, extractionValue);
        return bookService.isBookCheckedOutByUser(userEmail, bookId);
    }

    @GetMapping("/secure/currentloans/count")
    public int currentLoansCount(@RequestHeader(value = "Authorization") String token) {
        String userEmail = payloadJwtExtraction(token, extractionValue);
        return bookService.currentLoansCount(userEmail);
    }

}
