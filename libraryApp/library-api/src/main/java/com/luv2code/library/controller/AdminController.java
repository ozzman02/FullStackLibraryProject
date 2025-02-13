package com.luv2code.library.controller;

import com.luv2code.library.requestmodels.AddBookRequest;
import com.luv2code.library.service.AdminService;
import org.springframework.web.bind.annotation.*;

import static com.luv2code.library.constants.ApplicationConstants.HTTPS_ALLOWED_ORIGINS;
import static com.luv2code.library.constants.ApplicationConstants.USER_TYPE;
import static com.luv2code.library.utils.AppUtil.payloadJwtExtraction;

@CrossOrigin(HTTPS_ALLOWED_ORIGINS)
@RestController
@RequestMapping("/api/admin")
public class AdminController {

    private final AdminService adminService;

    public AdminController(AdminService adminService) {
        this.adminService = adminService;
    }

    @PostMapping("/secure/add/book")
    public void postBook(@RequestHeader(value = "Authorization") String token,
                         @RequestBody AddBookRequest addBookRequest) throws Exception {
        adminService.postBook(addBookRequest, payloadJwtExtraction(token, USER_TYPE));
    }

    @PutMapping("/secure/increase/book/quantity")
    public void increaseBookQuantity(@RequestHeader(value = "Authorization") String token,
                                     @RequestParam Long bookId) throws Exception {
        adminService.increaseBookQuantity(bookId, payloadJwtExtraction(token, USER_TYPE));
    }

    @PutMapping("/secure/decrease/book/quantity")
    public void decreaseBookQuantity(@RequestHeader(value = "Authorization") String token,
                                     @RequestParam Long bookId) throws Exception {
        adminService.decreaseBookQuantity(bookId, payloadJwtExtraction(token, USER_TYPE));
    }

    @DeleteMapping("/secure/delete/book")
    public void deleteBook(@RequestHeader(value = "Authorization") String token,
                           @RequestParam Long bookId) throws Exception {
        adminService.deleteBook(bookId, payloadJwtExtraction(token, USER_TYPE));
    }

}
