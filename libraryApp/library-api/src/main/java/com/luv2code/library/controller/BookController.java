package com.luv2code.library.controller;

import com.luv2code.library.entity.Book;
import com.luv2code.library.service.BookService;
import org.springframework.web.bind.annotation.*;

@CrossOrigin("http://localhost:3000")
@RestController
@RequestMapping("/api/books")
public class BookController {

    private final BookService bookService;

    public BookController(BookService bookService) {
        this.bookService = bookService;
    }

    @PutMapping("/secure/checkout")
    public Book checkoutBook(@RequestParam Long bookId) {
        String userEmail = "testuser@email.com";
        return bookService.checkoutBook(userEmail, bookId);
    }

    @GetMapping("/secure/ischeckedout/byuser")
    public Boolean isBookCheckedOutByUser(@RequestParam Long bookId) {
        String userEmail = "testuser@email.com";
        return bookService.isBookCheckedOutByUser(userEmail, bookId);
    }

    @GetMapping("/secure/currentloans/count")
    public int currentLoansCount() {
        String userEmail = "testuser@email.com";
        return bookService.currentLoansCount(userEmail);
    }

}
