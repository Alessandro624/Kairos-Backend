package it.unical.demacs.informatica.KairosBackend.core.service.payment;

import com.stripe.model.Charge;
import it.unical.demacs.informatica.KairosBackend.data.entities.Ticket;
import it.unical.demacs.informatica.KairosBackend.data.services.EventService;
import it.unical.demacs.informatica.KairosBackend.data.services.TicketService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;


import java.util.Set;

@Slf4j
@Service
@RequiredArgsConstructor
public class StripeServiceImpl implements StripeService {

    private final EventService eventService;
    private final TicketService ticketService;


    // https://docs.stripe.com/connect/collect-then-transfer-guide?lang=java&connect-account-creation-pattern=typeless
    public Charge createCharge(Set<Ticket> tickets) {
        // TODO: Implement!
        return null;
    }

}
