package com.luv2code.library.controller;

import com.luv2code.library.service.AddBookRequest;
import com.luv2code.library.service.AdminService;
import com.luv2code.library.utils.ExtractJWT;
import org.springframework.web.bind.annotation.*;

import static com.luv2code.library.constants.ApplicationConstants.USER_TYPE;

@CrossOrigin("http://localhost:3000")
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
        String admin = ExtractJWT.payloadJwtExtraction(token, USER_TYPE);
        if (admin == null || !admin.equals("admin")) {
            throw new Exception("Administration page only");
        }
        adminService.postBook(addBookRequest);
    }
}
