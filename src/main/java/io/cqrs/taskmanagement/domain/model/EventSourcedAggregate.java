package io.cqrs.taskmanagement.domain.model;

import java.lang.reflect.Method;
import java.util.List;

public abstract class EventSourcedAggregate implements Aggregate {

    private static final String APPLY_METHOD_NAME = "apply";

    /**
     * A convenient Handle method used by the Aggregate reinstate constructor and also by unit tests.
     */
    public void apply(List<DomainEvent> events) {
        events.forEach(this::apply);
    }

    public void apply(DomainEvent event) {
        Class<? extends DomainEvent> eventType = event.getClass();
        try {
            getApplyMethod(this.getClass(), eventType).invoke(this, event);
        } catch (ReflectiveOperationException e) {
            throw new RuntimeException("Aggregate do not know how to apply " + eventType);
        }
    }

    private Method getApplyMethod(
            Class<? extends EventSourcedAggregate> aggregateType,
            Class<? extends DomainEvent> domainEventType) throws NoSuchMethodException {
        Method applyMethod = aggregateType.getDeclaredMethod(APPLY_METHOD_NAME, domainEventType);
        applyMethod.setAccessible(true);
        return applyMethod;
    }

}
