package com.luv2code.library.service;

import com.luv2code.library.dao.BookRepository;
import com.luv2code.library.dao.CheckoutRepository;
import com.luv2code.library.dao.ReviewRepository;
import com.luv2code.library.entity.Book;
import com.luv2code.library.requestmodels.AddBookRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.luv2code.library.utils.AppUtil.validateIfUserIsAdmin;

@Service
@Transactional
public class AdminService {

    private final BookRepository bookRepository;

    private final CheckoutRepository checkoutRepository;

    private final ReviewRepository reviewRepository;

    public AdminService(BookRepository bookRepository,
                        CheckoutRepository checkoutRepository,
                        ReviewRepository reviewRepository) {
        this.bookRepository = bookRepository;
        this.reviewRepository = reviewRepository;
        this.checkoutRepository = checkoutRepository;
    }

    public void postBook(AddBookRequest addBookRequest, String userType) throws Exception {
        validateIfUserIsAdmin(userType);
        bookRepository.save(new Book(addBookRequest.getTitle(), addBookRequest.getAuthor(),
                addBookRequest.getDescription(), addBookRequest.getCopies(), addBookRequest.getCopies(),
                addBookRequest.getCategory(), addBookRequest.getImg()));
    }

    public void increaseBookQuantity(Long bookId, String userType) throws Exception {
        validateIfUserIsAdmin(userType);
        Book book = bookRepository.findById(bookId).orElseThrow(() -> new Exception("Book not found"));
        book.setCopiesAvailable(book.getCopiesAvailable() + 1);
        book.setCopies(book.getCopies() + 1);
        bookRepository.save(book);
    }

    public void decreaseBookQuantity(Long bookId, String userType) throws Exception {
        validateIfUserIsAdmin(userType);
        Book book = bookRepository.findById(bookId).orElseThrow(() -> new Exception("Book not found"));
        if (book.getCopiesAvailable() <= 0 || book.getCopies() <= 0) throw new Exception("Book not found or quantity locked");
        book.setCopiesAvailable(book.getCopiesAvailable() - 1);
        book.setCopies(book.getCopies() - 1);
        bookRepository.save(book);
    }

    public void deleteBook(Long bookId, String userType) throws Exception {
        validateIfUserIsAdmin(userType);
        Book book = bookRepository.findById(bookId).orElseThrow(() -> new Exception("Book not found"));
        bookRepository.deleteById(bookId);
        checkoutRepository.deleteAllByBookId(bookId);
        reviewRepository.deleteAllByBookId(bookId);
    }

}
