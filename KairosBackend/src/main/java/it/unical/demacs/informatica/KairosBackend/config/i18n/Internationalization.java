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
 * HOW IT WORKS:
 * Given an HTTP request, Spring DispatcherServlet calls this LocaleResolver object in order to
 * obtain the proper Locale object using resolveLocale(HTTPServletRequest) method, that returns the Locale used for the current request.
 * The LocaleResolver object is injected in another object, called LocaleContext, that is saved in
 * a ThreadLocal using static method LocaleContextHolder.setLocale(...)
 *
 * From this point, if you call LocaleContextHolder.getLocale() wherever your code is, you get the correct Locale object!
 *
 * QUESTION: WHY NOT USING The LocaleResolver directly?
 *
 * ACCESS THE PROPER .properties file saved in resource bundle
 * Done automatically by Spring Boot, given a file messages_X.properties, where X is it,en,...
 *
 * ACCESS THE (CORRECT!) MESSAGE
 * Inject a MessageSource object (see WishlistController, for example), and in particular a ResourceBundleMessageSource object.
 * Specify the resource bundle location and other stuff
 * With the messageSource.getMessage(String propertyName, Object... args, Locale locale), you can get the proper message!
 * ... And what to specify as Locale?
 * LocaleContextHolder.getLocale()!!!! (see above.)
 *
 * (see MessageReader class: why we used it?)
 * */

@Configuration
public class Internationalization {
    //TODO maybe a custom AcceptHeaderLocaleResolver class where these parameters are specified into it
    //supported (and default) locales are defined in application.properties file!
    //with @Value, we can access them
    @Value("${app.locales.supported}")
    private String[] supportedLocales;

    @Value("${app.locales.default}")
    private String defaultLocale;

    @Bean
    public AcceptHeaderLocaleResolver localeResolver() {
        //Locale is determined by HTTP request "accept-language" header (AcceptHeaderLocaleResolver) using resolveLocale method (see above)
        AcceptHeaderLocaleResolver resolver = new AcceptHeaderLocaleResolver();

        //setSupportedLocales requires a List<Locale>. Using streams we can obtain it.
        //Locale.forLanguageTag transforms a string in the corresponding Locale object.
        resolver.setSupportedLocales(Arrays.stream(supportedLocales).map(Locale::forLanguageTag).toList());
        resolver.setDefaultLocale(Locale.forLanguageTag(this.defaultLocale));

        return resolver;
    }

    @Bean
    public ResourceBundleMessageSource messageSource() {
        ResourceBundleMessageSource source = new ResourceBundleMessageSource();
        //here we specify all the messages bundles location of our .property files (starting from resources folder!)
        source.setBasenames("language/messages");
        source.setDefaultEncoding("UTF-8");

        return source;
    }
}
