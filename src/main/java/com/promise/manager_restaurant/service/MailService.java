package com.promise.manager_restaurant.service;

import com.promise.manager_restaurant.dto.response.email.DataMailDTO;
import jakarta.mail.MessagingException;
import org.springframework.stereotype.Service;

@Service
public interface MailService {
    void send(DataMailDTO dataMailDTO, String templateName) throws MessagingException;

}
