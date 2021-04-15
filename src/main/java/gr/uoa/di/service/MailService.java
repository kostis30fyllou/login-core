package gr.uoa.di.service;

import gr.uoa.di.dto.EmailMessage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.mail.javamail.MimeMessagePreparator;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import javax.mail.MessagingException;
import java.util.ArrayList;

@Service
public class MailService {

    private static final Logger logger = LogManager.getLogger(MailService.class);

    private JavaMailSender mailSender;
    private TemplateEngine templateEngine;

    public enum MessageType {
        DEFAULT,
        VERIFICATION,
        FORGOT_PASSWORD
    }

    @Autowired
    public MailService(JavaMailSender mailSender, TemplateEngine templateEngine) {
        this.mailSender = mailSender;
        this.templateEngine = templateEngine;
    }

    private String build(String message, MessageType type) {
        Context context = new Context();
        context.setVariable("message", message);
        switch (type) {
            case VERIFICATION:
                return templateEngine.process("verificationMailTemplate", context);
            case FORGOT_PASSWORD:
                return templateEngine.process("forgotPasswordMailTemplate", context);
            default:
                return templateEngine.process("mailTemplate", context);
        }
    }

    @Async
    public void sendEmailMessage(EmailMessage emailMessage) {
        SimpleMailMessage message = new SimpleMailMessage();

        if (emailMessage.getTo() == null || emailMessage.getTo().isEmpty()) {
            logger.error("Email message cannot be sent because it does not have an address to..\n{}", emailMessage);
            return;
        }
        emailMessage.getTo().forEach(message::setTo);
        if (emailMessage.getCc() != null) {
            emailMessage.getCc().forEach(message::setCc);
        }
        if (emailMessage.getBcc() != null) {
            emailMessage.getBcc().forEach(message::setBcc);
        }
        message.setSubject(emailMessage.getSubject());
        message.setText(build(emailMessage.getBody(), MessageType.DEFAULT));
        mailSender.send(message);
    }

    @Async
    public void sendHtmlEmailMessage(EmailMessage emailMessage, MessageType type) {
        if (emailMessage.getTo() == null || emailMessage.getTo().isEmpty()) {
            logger.error("Email message cannot be sent because it does not have an address to..\n{}", emailMessage);
            return;
        }
        if (emailMessage.getCc() == null) {
            emailMessage.setCc(new ArrayList<>());
        }
        if (emailMessage.getBcc() == null) {
            emailMessage.setBcc(new ArrayList<>());
        }

        MimeMessagePreparator messagePreparator = mimeMessage -> {
            MimeMessageHelper messageHelper = new MimeMessageHelper(mimeMessage);
            try {
                for (String address : emailMessage.getTo()) {
                    messageHelper.addTo(address);
                }
                for (String address : emailMessage.getCc()) {
                    messageHelper.addCc(address);
                }
                for (String address : emailMessage.getBcc()) {
                    messageHelper.addBcc(address);
                }

                messageHelper.setSubject(emailMessage.getSubject());
                messageHelper.setText(build(emailMessage.getBody(), type), true);
            } catch (MessagingException e) {
                logger.error(e);
            }
        };

        try {
            mailSender.send(messagePreparator);
        } catch (MailException e) {
            logger.error("Mail could not be sent", e);
        }
    }

}
