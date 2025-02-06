package com.luv2code.library.controller;

import com.luv2code.library.entity.Message;
import com.luv2code.library.service.MessagesService;
import org.springframework.web.bind.annotation.*;

import static com.luv2code.library.constants.ApplicationConstants.extractionValue;
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
        messagesService.postMessage(messageRequest, payloadJwtExtraction(token, extractionValue));
    }

}
