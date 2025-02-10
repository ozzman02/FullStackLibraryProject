package com.luv2code.library.service;

import com.luv2code.library.dao.MessageRepository;
import com.luv2code.library.entity.Message;
import com.luv2code.library.requestmodels.AdminQuestionRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class MessagesService {

    private final MessageRepository messageRepository;

    public MessagesService(MessageRepository messageRepository) {
        this.messageRepository = messageRepository;
    }

    public void postMessage(Message messageRequest, String userEmail) {
        Message message = new Message(messageRequest.getTitle(), messageRequest.getQuestion());
        message.setUserEmail(userEmail);
        messageRepository.save(message);
    }

    public void putMessage(AdminQuestionRequest adminQuestionRequest, String userEmail, String userType)
            throws Exception {

        if (userType == null || !userType.equals("admin"))
            throw new Exception("Administration page only");

        Message message = messageRepository.findById(adminQuestionRequest.getId())
                .orElseThrow(() -> new Exception("Message not found"));

        message.setAdminEmail(userEmail);
        message.setResponse(adminQuestionRequest.getResponse());
        message.setClosed(true);

        messageRepository.save(message);
    }

}
