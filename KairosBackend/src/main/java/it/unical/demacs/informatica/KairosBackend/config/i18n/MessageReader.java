package it.unical.demacs.informatica.KairosBackend.config.i18n;

import lombok.AllArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Component;

/*
* NOTE: this class is just an Adapter. It simplifies the getMessage interface.
* */
@Component
@AllArgsConstructor
public class MessageReader {
    private MessageSource messageSource;

    public String getMessage(String source) {
        return messageSource.getMessage(source,null, LocaleContextHolder.getLocale());
    }

    public String getMessage(String source, Object... args) {
        return messageSource.getMessage(source,args, LocaleContextHolder.getLocale());
    }
}
