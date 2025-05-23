package it.unical.demacs.informatica.KairosBackend.aspect;

import it.unical.demacs.informatica.KairosBackend.core.service.EmailService;
import it.unical.demacs.informatica.KairosBackend.core.service.JwtService;
import it.unical.demacs.informatica.KairosBackend.dto.user.UserDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

@Aspect
@Component
@RequiredArgsConstructor
@Slf4j
public class EmailSendingAspect {
    private final EmailService emailService;
    private final JwtService jwtService;

    @Pointcut("execution(* it.unical.demacs.informatica.KairosBackend.data.services.UserServiceImpl.createUser(..))")
    public void userCreationOperation() {
    }

    @AfterReturning(pointcut = "userCreationOperation()", returning = "result")
    public void afterUserCreation(JoinPoint joinPoint, Object result) {
        log.info("After user creation operation with join point: {}", joinPoint);
        if (result instanceof UserDTO user) {
            String to = user.getEmail();
            String username = user.getUsername();

            String verificationToken = jwtService.generateEmailVerificationToken(username);

            String confirmationLink = ServletUriComponentsBuilder.fromCurrentContextPath()
                    .path("/v1/auth/confirm")
                    .queryParam("token", verificationToken)
                    .toUriString();

            log.info("Sending confirmation email to '{}' for user '{}' with link: {}", to, username, confirmationLink);
            emailService.sendConfirmationEmail(to, username, confirmationLink);
        }
    }
}
