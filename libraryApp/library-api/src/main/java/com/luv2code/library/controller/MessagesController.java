package com.luv2code.library.controller;

import com.luv2code.library.entity.Message;
import com.luv2code.library.requestmodels.AdminQuestionRequest;
import com.luv2code.library.service.MessagesService;
import org.springframework.web.bind.annotation.*;

import static com.luv2code.library.constants.ApplicationConstants.*;
import static com.luv2code.library.utils.AppUtil.payloadJwtExtraction;

@CrossOrigin(HTTPS_ALLOWED_ORIGINS)
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

}
