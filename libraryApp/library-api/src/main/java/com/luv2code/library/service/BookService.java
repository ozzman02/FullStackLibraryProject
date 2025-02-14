package com.luv2code.library.service;

import com.luv2code.library.dao.BookRepository;
import com.luv2code.library.dao.CheckoutRepository;
import com.luv2code.library.dao.HistoryRepository;
import com.luv2code.library.dao.PaymentRepository;
import com.luv2code.library.entity.Book;
import com.luv2code.library.entity.Checkout;
import com.luv2code.library.entity.History;
import com.luv2code.library.entity.Payment;
import com.luv2code.library.responsemodels.ShelfCurrentLoansResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.ParseException;
import java.time.LocalDate;
import java.util.*;
import java.util.concurrent.TimeUnit;

import static com.luv2code.library.constants.ApplicationConstants.YEAR_MONTH_DAY_FORMATTER;

@Service
@Transactional
public class BookService {

    private final BookRepository bookRepository;

    private final CheckoutRepository checkoutRepository;

    private final HistoryRepository historyRepository;

    private final PaymentRepository paymentRepository;

    public BookService(BookRepository bookRepository,
                       CheckoutRepository checkoutRepository,
                       HistoryRepository historyRepository,
                       PaymentRepository paymentRepository) {
        this.bookRepository = bookRepository;
        this.checkoutRepository = checkoutRepository;
        this.historyRepository = historyRepository;
        this.paymentRepository = paymentRepository;
    }

    public Book checkoutBook(String userEmail, Long bookId) {
        return bookRepository.findById(bookId).map(existingBook -> {
            if (checkoutRepository.findByUserEmailAndBookId(userEmail, bookId) != null
                    || existingBook.getCopiesAvailable() <= 0) {
                throw new RuntimeException("Book already checked out by user");
            }

            List<Checkout> currentBooksCheckedOut = checkoutRepository.findBooksByUserEmail(userEmail);

            boolean booksNeedToBeReturned = currentBooksCheckedOut.stream()
                    .anyMatch(currentBookCheckedOut -> {
                        try {
                            return getDifferenceInTime(currentBookCheckedOut) < 0;
                        } catch (ParseException e) {
                            throw new RuntimeException(e);
                        }
                    });

            Optional<Payment> userPaymentOptional = paymentRepository.findByUserEmail(userEmail);

            if ((userPaymentOptional.isPresent() && userPaymentOptional.get().getAmount() > 0)
                    || booksNeedToBeReturned) {
                throw new RuntimeException("Outstanding Fees");
            }

            if (userPaymentOptional.isEmpty()) {
                Payment payment = new Payment();
                payment.setAmount(00.00);
                payment.setUserEmail(userEmail);
                paymentRepository.save(payment);
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

        for (Book book : books) {
            Optional<Checkout> checkoutOptional = checkoutList.stream()
                    .filter(checkout -> Objects.equals(checkout.getBookId(), book.getId())).findFirst();
            if (checkoutOptional.isPresent()) {
                shelfCurrentLoansResponseList.add(new ShelfCurrentLoansResponse(book,
                        (int) getDifferenceInTime(checkoutOptional.get())));
            }
        }
        return shelfCurrentLoansResponseList;
    }

    public void returnBook(String userEmail, Long bookId) throws Exception {
        Optional<Book> book = bookRepository.findById(bookId);
        Checkout validateCheckout = checkoutRepository.findByUserEmailAndBookId(userEmail, bookId);
        if (book.isEmpty() || validateCheckout == null) {
            throw new Exception("Book does not exist or not checked out by user");
        }
        book.get().setCopiesAvailable(book.get().getCopiesAvailable() + 1);
        bookRepository.save(book.get());

        double differenceInTime = getDifferenceInTime(validateCheckout);

        if (differenceInTime < 0) {
            Optional<Payment> paymentOptional = paymentRepository.findByUserEmail(userEmail);
            if (paymentOptional.isPresent()) {
                paymentOptional.get().setAmount(paymentOptional.get().getAmount() + (differenceInTime * -1));
                paymentRepository.save(paymentOptional.get());
            }
        }

        checkoutRepository.deleteById(validateCheckout.getId());

        History history = new History(
                userEmail,
                validateCheckout.getCheckoutDate(),
                LocalDate.now().toString(),
                book.get().getTitle(),
                book.get().getAuthor(),
                book.get().getDescription(),
                book.get().getImage()
        );
        historyRepository.save(history);
    }

    public void renewLoan(String userEmail, Long bookId) throws Exception {
        Checkout validateCheckout = checkoutRepository.findByUserEmailAndBookId(userEmail, bookId);
        if (validateCheckout == null) {
            throw new Exception("Book does not exist or not checked out by user");
        }
        //SimpleDateFormat formatter = new SimpleDateFormat(YEAR_MONTH_DAY_FORMAT);
        Date returnDate = YEAR_MONTH_DAY_FORMATTER.parse(validateCheckout.getReturnDate());
        Date currentDate = YEAR_MONTH_DAY_FORMATTER.parse(LocalDate.now().toString());
        if (returnDate.compareTo(currentDate) > 0 || returnDate.compareTo(currentDate) == 0) {
            validateCheckout.setReturnDate(LocalDate.now().plusDays(7).toString());
            checkoutRepository.save(validateCheckout);
        }
    }

    private double getDifferenceInTime(Checkout checkout) throws ParseException {
        Date returnDate = YEAR_MONTH_DAY_FORMATTER.parse(checkout.getReturnDate());
        Date currentDate = YEAR_MONTH_DAY_FORMATTER.parse(LocalDate.now().toString());
        TimeUnit time = TimeUnit.DAYS;
        return time.convert(returnDate.getTime() - currentDate.getTime(), TimeUnit.MILLISECONDS);
    }

}
