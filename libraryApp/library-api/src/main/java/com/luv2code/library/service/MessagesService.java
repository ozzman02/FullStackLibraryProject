package com.luv2code.library.service;

import com.luv2code.library.dao.MessageRepository;
import com.luv2code.library.entity.Message;
import com.luv2code.library.requestmodels.AdminQuestionRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.luv2code.library.utils.AppUtil.validateIfUserIsAdmin;

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

        validateIfUserIsAdmin(userType);

        Message message = messageRepository.findById(adminQuestionRequest.getId())
                .orElseThrow(() -> new Exception("Message not found"));

        message.setAdminEmail(userEmail);
        message.setResponse(adminQuestionRequest.getResponse());
        message.setClosed(true);

        messageRepository.save(message);
    }

}
