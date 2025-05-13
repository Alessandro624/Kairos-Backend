package it.unical.demacs.informatica.KairosBackend.config;

import com.stripe.Stripe;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
public class StripeConfig {
    @Value("${stripe.key.secret}")
    private String apiKey;

    private String currency = "eur";

    public StripeConfig() {
        Stripe.apiKey = apiKey;
    }
}
