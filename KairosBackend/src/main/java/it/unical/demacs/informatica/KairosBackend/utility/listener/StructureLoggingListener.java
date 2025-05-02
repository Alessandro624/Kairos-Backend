package it.unical.demacs.informatica.KairosBackend.utility.listener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class StructureLoggingListener
{
    private static final Logger logger = LoggerFactory.getLogger(StructureLoggingListener.class);

    @EventListener
    public void getStructureById(UUID id)
    {
        logger.info("get structure by id {}", id);
    }
}
