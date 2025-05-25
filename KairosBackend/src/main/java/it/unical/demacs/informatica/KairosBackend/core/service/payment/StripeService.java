package it.unical.demacs.informatica.KairosBackend.core.service.payment;

import com.stripe.model.Charge;
import it.unical.demacs.informatica.KairosBackend.data.entities.Ticket;

import java.util.Set;

public interface StripeService {
    Charge createCharge(Set<Ticket> tickets);
}
