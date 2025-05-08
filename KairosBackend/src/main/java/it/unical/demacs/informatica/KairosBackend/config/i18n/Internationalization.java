package it.unical.demacs.informatica.KairosBackend.config.i18n;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.i18n.AcceptHeaderLocaleResolver;

import java.util.Arrays;
import java.util.Locale;
import java.util.stream.Collectors;

/*
   NOTE: how do we access bundle files properly?
   LocaleResolver.resolveLocale(HttpServletRequest) (implemented in AcceptHeaderLocaleResolver class)
   is a method that, given an HTTP request (and its Accept-Language header if AcceptHeaderLocaleResolver is used),
   makes us return the specific Locale, that can give us access to the proper
   messages.properties resource bundle file.
*/

@Configuration
public class Internationalization {
    //TODO maybe a custom AcceptHeaderLocaleResolver class where these parameters are specified into it
    //supported (and default) locales are defined in application.properties file!
    //with @Value, we can access them
    @Value("${app.locales.supported}")
    private String[] supportedLocales;

    @Value("${app.locales.default}")
    private String defaultLocale;

    //creates a bean containing our personal locale resolver.
    @Bean
    public AcceptHeaderLocaleResolver localeResolver() {
        //language is determined by HTTP request "accept-language" header (AcceptHeaderLocaleResolver)
        AcceptHeaderLocaleResolver resolver = new AcceptHeaderLocaleResolver();

        //setSupportedLocales requires a List<Locale>. Using streams we can obtain it.
        //Locale.forLanguageTag transforms a string in the corresponding Locale object.
        resolver.setSupportedLocales(Arrays.stream(supportedLocales).map(Locale::forLanguageTag).toList());
        resolver.setDefaultLocale(Locale.forLanguageTag(this.defaultLocale));

        return resolver;
    }
}
