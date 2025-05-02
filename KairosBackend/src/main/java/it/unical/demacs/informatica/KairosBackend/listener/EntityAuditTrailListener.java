package it.unical.demacs.informatica.KairosBackend.listener;

import jakarta.persistence.*;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.UUID;

@Slf4j
public class EntityAuditTrailListener<T>{
    //FIXME maybe the usage of reflection is too much in this context. The usage of an interface is maybe preferable.

    @PrePersist
    @PreUpdate
    @PreRemove
    private void beforeAnyUpdate(T entity) {
        UUID entityID = getId(entity);
        if (entityID == null) {
            log.info("[{} AUDIT] About to add a student", entity.getClass().getName().toUpperCase());
        } else {
            log.info("[{} AUDIT] About to update/delete student: {}", entity.getClass().getName().toUpperCase(), entityID);
        }
    }

    @PostPersist
    @PostUpdate
    @PostRemove
    private void afterAnyUpdate(T entity) {
        log.info("[{} AUDIT] add/update/delete complete for student: {}", entity.getClass().getName().toUpperCase(), getId(entity));
    }

    @PostLoad
    private void afterLoad(T entity) {
        log.info("[{} AUDIT] user loaded from database: {}", entity.getClass().getName().toUpperCase(), getId(entity));
    }

    private UUID getId(T entity) {
        try {
            Method method = entity.getClass().getDeclaredMethod("getId");
            method.setAccessible(true);
            return (UUID) method.invoke(entity);
        } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
            throw new RuntimeException("Cannot retrieve ID from entity", e);
        }
    }
}
