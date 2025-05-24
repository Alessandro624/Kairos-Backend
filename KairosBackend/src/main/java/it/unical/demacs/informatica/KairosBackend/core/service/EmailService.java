package it.unical.demacs.informatica.KairosBackend.core.service;

import it.unical.demacs.informatica.KairosBackend.config.i18n.MessageReader;
import it.unical.demacs.informatica.KairosBackend.exception.EmailNotSentException;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import org.thymeleaf.exceptions.TemplateInputException;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
public class EmailService {
    private final JavaMailSender mailSender;
    private final TemplateEngine templateEngine;
    private final MessageReader messageReader;


    public void sendEmailWithTemplate(String to, String subjectKey, String templateName, Map<String, Object> variables) {
        Locale locale = LocaleContextHolder.getLocale();
        Context context = new Context(locale);
        context.setVariables(variables);
        String templatePath = "email/" + templateName + "_" + locale.getLanguage();

        log.info("Preparing to send email to '{}' using template '{}', locale '{}'", to, templatePath, locale);

        String body;
        try {
            body = templateEngine.process(templatePath, context);
        } catch (TemplateInputException e) {
            log.warn("Localized template not found: '{}', falling back to default template '{}'", templatePath, templateName);
            String fallbackTemplate = "email/" + templateName;
            body = templateEngine.process(fallbackTemplate, context);
        }

        MimeMessage message = mailSender.createMimeMessage();
        try {
            MimeMessageHelper helper = new MimeMessageHelper(message, true);
            helper.setTo(to);
            helper.setSubject(messageReader.getMessage(subjectKey, locale));
            helper.setText(body, true); // true = HTML
            mailSender.send(message);
            log.info("Email sent successfully to '{}'", to);
        } catch (MessagingException e) {
            log.error("Failed to send email to '{}'", to, e);
            throw new EmailNotSentException(messageReader.getMessage("email.not_sent.failure"));
        }
    }

    public void sendConfirmationEmail(String to, String username, String confirmationLink) {
        log.debug("Sending confirmation email to '{}'", to);
        Map<String, Object> variables = new HashMap<>();
        variables.put("username", username);
        variables.put("confirmationLink", confirmationLink);
        sendEmailWithTemplate(to, "email.confirmation.subject", "confirmation-email", variables);
    }

    public void sendPasswordResetEmail(String to, String username, String resetLink) {
        log.debug("Sending password reset email to '{}'", to);
        Map<String, Object> variables = new HashMap<>();
        variables.put("username", username);
        variables.put("resetLink", resetLink);
        sendEmailWithTemplate(to, "email.reset.subject", "reset-password", variables);
    }
}
