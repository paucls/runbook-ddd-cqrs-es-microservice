package io.cqrs.taskmanagement.domain.model;

import java.lang.reflect.Method;

public abstract class EventSourcedAggregate implements Aggregate {

    protected void applyDomainEvent(Class<? extends EventSourcedAggregate> aggregateType,
                                    DomainEvent event) {
        Class<? extends DomainEvent> eventType = event.getClass();
        try {
            getApplyMethod(aggregateType, eventType).invoke(this, event);
        } catch (ReflectiveOperationException e) {
            throw new RuntimeException("Aggregate do not know how to apply " + eventType);
        }
    }

    private Method getApplyMethod(
            Class<? extends EventSourcedAggregate> aggregateType,
            Class<? extends DomainEvent> domainEventType) throws NoSuchMethodException {
        Method applyMethod = aggregateType.getDeclaredMethod("apply", domainEventType);
        applyMethod.setAccessible(true);
        return  applyMethod;
    }

}
