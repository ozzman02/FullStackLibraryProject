package com.luv2code.library.controller;

import com.luv2code.library.entity.Message;
import com.luv2code.library.requestmodels.AdminQuestionRequest;
import com.luv2code.library.service.MessagesService;
import org.springframework.web.bind.annotation.*;

import static com.luv2code.library.constants.ApplicationConstants.USER_EMAIL;
import static com.luv2code.library.constants.ApplicationConstants.USER_TYPE;
import static com.luv2code.library.utils.ExtractJWT.payloadJwtExtraction;

@CrossOrigin("http://localhost:3000")
@RestController
@RequestMapping("/api/messages")
public class MessagesController {

    private final MessagesService messagesService;

    public MessagesController(MessagesService messagesService) {
        this.messagesService = messagesService;
    }

    @PostMapping("/secure/add/message")
    public void postMessage(@RequestHeader(value = "Authorization") String token,
                            @RequestBody Message messageRequest) {
        messagesService.postMessage(messageRequest, payloadJwtExtraction(token, USER_EMAIL));
    }

    @PutMapping("/secure/admin/message")
    public void putMessage(@RequestHeader(value = "Authorization") String token,
                           @RequestBody AdminQuestionRequest adminQuestionRequest) throws Exception {
        messagesService.putMessage(adminQuestionRequest,
                payloadJwtExtraction(token, USER_EMAIL), payloadJwtExtraction(token, USER_TYPE));
    }

    /*@PutMapping("/admin/message")
    public void putMessage(@RequestBody AdminQuestionRequest adminQuestionRequest,
                           @RequestParam("userEmail") String userEmail,
                           @RequestParam("userType") String userType) throws Exception {
        messagesService.putMessage(adminQuestionRequest, userEmail, userType);
    }*/

}
