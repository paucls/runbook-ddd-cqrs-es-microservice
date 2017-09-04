package io.cqrs.taskmanagement.port.adapter.persistence;

import io.cqrs.taskmanagement.application.StoredEvent;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.util.Date;

@Entity
public class JpaStoredEvent implements StoredEvent {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long eventId;
    @Column(length = 65000, nullable = false)
    private String eventBody;
    @Column(nullable = false)
    private Date occurredOn;
    @Column(nullable = false)
    private String typeName;

    public JpaStoredEvent(String eventBody, Date occurredOn, String typeName) {
        this.eventBody = eventBody;
        this.occurredOn = occurredOn;
        this.typeName = typeName;
    }

    public long getEventId() {
        return eventId;
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
}
