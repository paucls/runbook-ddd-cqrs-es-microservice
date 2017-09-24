package io.cqrs.taskmanagement.port.adapter.persistence;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.cqrs.taskmanagement.domain.model.DomainEvent;
import io.cqrs.taskmanagement.event.StoredEvent;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.io.IOException;
import java.util.Date;

@Entity
public class JpaStoredEvent implements StoredEvent {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long eventId;
    private String aggregateId;
    @Column(length = 65000, nullable = false)
    private String eventBody;
    @Column(nullable = false)
    private Date occurredOn;
    @Column(nullable = false)
    private String typeName;

    private static final ObjectMapper mapper = new ObjectMapper();

    // empty constructor needed by JPA
    public JpaStoredEvent() {
    }

    public JpaStoredEvent(String aggregateId, String eventBody, Date occurredOn, String typeName) {
        this.aggregateId = aggregateId;
        this.eventBody = eventBody;
        this.occurredOn = occurredOn;
        this.typeName = typeName;
    }

    public long getEventId() {
        return eventId;
    }

    public String getAggregateId() {
        return aggregateId;
    }

    public String getEventBody() {
        return eventBody;
    }

    public Date getOccurredOn() {
        return occurredOn;
    }

    public String getTypeName() {
        return typeName;
    }

    public <T extends DomainEvent> T toDomainEvent() {
        Class<T> domainEventClass = null;

        try {
            domainEventClass = (Class<T>) Class.forName(this.typeName);
        } catch (Exception e) {
            throw new IllegalStateException("Class load error, because: " + e.getMessage());
        }

        T domainEvent = null;
        try {
            domainEvent = mapper.readValue(this.eventBody, domainEventClass);
        } catch (IOException e) {
            throw new IllegalStateException("Event deserialization error, because: " + e.getMessage());
        }

        return domainEvent;
    }

}
