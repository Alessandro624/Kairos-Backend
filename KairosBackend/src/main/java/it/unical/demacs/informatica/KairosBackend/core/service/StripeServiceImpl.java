package it.unical.demacs.informatica.KairosBackend.core.service;

import com.stripe.model.Charge;
import com.stripe.model.Customer;
import com.stripe.model.Product;
import com.stripe.param.ChargeCreateParams;
import com.stripe.param.checkout.SessionCreateParams;
import it.unical.demacs.informatica.KairosBackend.data.entities.Ticket;
import it.unical.demacs.informatica.KairosBackend.data.services.EventService;
import it.unical.demacs.informatica.KairosBackend.data.services.TicketService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import

import java.util.Set;

@Slf4j
@Service
@RequiredArgsConstructor
public class StripeServiceImpl implements StripeService {

    private EventService eventService;
    private TicketService ticketService;

    public Charge createCharge(Set<Ticket> tickets) {
        // TODO: Implement!
        return null;
    }

}
