package com.luv2code.library.service;

import com.luv2code.library.dao.BookRepository;
import com.luv2code.library.entity.Book;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class AdminService {

    private final BookRepository bookRepository;

    public AdminService(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    public void postBook(AddBookRequest addBookRequest) {
        Book book = new Book(addBookRequest.getTitle(), addBookRequest.getAuthor(),
                addBookRequest.getDescription(), addBookRequest.getCopies(), addBookRequest.getCopies(),
                addBookRequest.getCategory(), addBookRequest.getImg());
        bookRepository.save(book);
    }

}
