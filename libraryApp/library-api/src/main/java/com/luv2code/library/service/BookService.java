package com.luv2code.library.service;

import com.luv2code.library.dao.BookRepository;
import com.luv2code.library.dao.CheckoutRepository;
import com.luv2code.library.entity.Book;
import com.luv2code.library.entity.Checkout;
import com.luv2code.library.responsemodels.ShelfCurrentLoansResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.*;
import java.util.concurrent.TimeUnit;

import static com.luv2code.library.constants.ApplicationConstants.YEAR_MONTH_DAY_FORMAT;

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

    public List<ShelfCurrentLoansResponse> currentLoans(String userEmail) throws Exception {
        List<ShelfCurrentLoansResponse> shelfCurrentLoansResponseList = new ArrayList<>();
        List<Checkout> checkoutList = checkoutRepository.findBooksByUserEmail(userEmail);
        List<Long> bookIdList = checkoutList.stream()
                .map(Checkout::getBookId)
                .toList();
        List<Book> books = bookRepository.findBooksByBookIds(bookIdList);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(YEAR_MONTH_DAY_FORMAT);
        for (Book book : books) {
            Optional<Checkout> checkoutOptional = checkoutList.stream()
                    .filter(checkout -> Objects.equals(checkout.getBookId(), book.getId())).findFirst();
            if (checkoutOptional.isPresent()) {
                Date returnDate =  simpleDateFormat.parse(checkoutOptional.get().getReturnDate());
                Date currentDate = simpleDateFormat.parse(LocalDate.now().toString());
                TimeUnit time = TimeUnit.DAYS;
                long differenceInTime = time.convert(returnDate.getTime() - currentDate.getTime(),
                        TimeUnit.MILLISECONDS);
                shelfCurrentLoansResponseList.add(new ShelfCurrentLoansResponse(book, (int) differenceInTime));
            }
        }
        return shelfCurrentLoansResponseList;
    }

}
