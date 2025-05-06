package it.unical.demacs.informatica.KairosBackend.listener;

import jakarta.persistence.*;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.Method;
import java.util.UUID;

@Slf4j
public class StructureAuditTrailListener<T>
{
    @PrePersist
    @PreUpdate
    @PreRemove
    private void beforeAnyUpdate(T entity)
    {
        String name = entity.getClass().getSimpleName();
        UUID id = getId(entity);

        if (id == null)
        {
            log.info("[{} AUDIT] About to add a new {}", name.toUpperCase(), name.toLowerCase());
        }
        else
        {
            log.info("[{} AUDIT] About to update/delete {}: {}", name.toUpperCase(), id, name.toLowerCase());
        }
    }

    @PostPersist
    @PostUpdate
    @PostRemove
    private void afterAnyUpdate(T entity)
    {
        String name = entity.getClass().getSimpleName();
        log.info("[{} AUDIT] add/update/delete complete for {}: {}", name.toUpperCase(), getId(entity), name.toLowerCase());
    }

    @PostLoad
    private void afterLoad(T entity)
    {
        String name = entity.getClass().getSimpleName();
        log.info("[{} AUDIT] entity loaded from database: {}", name.toUpperCase(), getId(entity));
    }

    private UUID getId(T entity)
    {
        try
        {
            Method getIdMethod = entity.getClass().getMethod("getId");
            Object result = getIdMethod.invoke(entity);
            return (UUID) result;
        }
        catch (Exception e)
        {
            log.warn("Failed to retrieve ID from entity of type {}: {}", entity.getClass().getName(), e.getMessage());
            return null;
        }
    }
}
