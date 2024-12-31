package com.luv2code.library.service;

import com.luv2code.library.dao.BookRepository;
import com.luv2code.library.dao.CheckoutRepository;
import com.luv2code.library.entity.Book;
import com.luv2code.library.entity.Checkout;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

@Service
@Transactional
public class BookService {

    private final BookRepository bookRepository;

    private final CheckoutRepository checkoutRepository;

    public BookService(BookRepository bookRepository, CheckoutRepository checkoutRepository) {
        this.bookRepository = bookRepository;
        this.checkoutRepository = checkoutRepository;
    }

    public Book checkoutBook(String userEmail, Long bookId) {
        return bookRepository.findById(bookId).map(existingBook -> {
            if (checkoutRepository.findByUserEmailAndBookId(userEmail, bookId) != null
                    || existingBook.getCopiesAvailable() <= 0) {
                throw new RuntimeException("Book already checked out by user");
            }
            existingBook.setCopiesAvailable(existingBook.getCopiesAvailable() - 1);
            bookRepository.save(existingBook);
            Checkout checkout = new Checkout(
                    userEmail,
                    LocalDate.now().toString(),
                    LocalDate.now().plusDays(7).toString(),
                    existingBook.getId()
            );
            checkoutRepository.save(checkout);
            return existingBook;
        }).orElseThrow(() -> new RuntimeException("Book does not exist"));
    }

    public Boolean isBookCheckedOutByUser(String userEmail, Long bookId) {
        return checkoutRepository.findByUserEmailAndBookId(userEmail, bookId) != null;
    }

    public int currentLoansCount(String userEmail) {
        return checkoutRepository.findBooksByUserEmail(userEmail).size();
    }

}
