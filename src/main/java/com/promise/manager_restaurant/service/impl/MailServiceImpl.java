package com.promise.manager_restaurant.service.impl;

import com.promise.manager_restaurant.dto.response.email.DataMailDTO;
import com.promise.manager_restaurant.service.MailService;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import jakarta.transaction.Transactional;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;

@Service

@Transactional
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class MailServiceImpl implements MailService {

    JavaMailSender mailSender;
    SpringTemplateEngine templateEngine;


    @Override
    public void send(DataMailDTO dataMailDTO, String templateName) throws MessagingException {
        MimeMessage mimeMessage = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage);

        Context context = new Context();
        context.setVariables(dataMailDTO.getProps());

        String html = templateEngine.process(templateName, context);

        helper.setTo(dataMailDTO.getTo());
        helper.setSubject(dataMailDTO.getSubject());
        helper.setText(html, true);
        mailSender.send(mimeMessage);


    }
}
