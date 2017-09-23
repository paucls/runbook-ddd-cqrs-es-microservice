package io.cqrs.taskmanagement.port.adapter.persistence;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.cqrs.taskmanagement.event.sourcing.EventStore;
import io.cqrs.taskmanagement.domain.model.DomainEvent;
import io.cqrs.taskmanagement.event.sourcing.EventStream;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.persistence.EntityNotFoundException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Observable;

@Component
public class JpaEventStore extends Observable implements EventStore {

    private JpaEventStoreRepository eventStoreRepository;

    private static final ObjectMapper mapper = new ObjectMapper();

    @Autowired
    public JpaEventStore(JpaEventStoreRepository eventStoreRepository) {
        this.eventStoreRepository = eventStoreRepository;
    }

    @Override
    public void appendToStream(String aggregateId, List<DomainEvent> domainEvents, int originalVersion) {
        for (DomainEvent event : domainEvents) {
            this.appendToStream(aggregateId, event, originalVersion);
        }
    }

    @Override
    public void appendToStream(String aggregateId, DomainEvent event, int originalVersion) {
        try {
            String eventSerialization = mapper.writeValueAsString(event);

            JpaStoredEvent storedEvent = new JpaStoredEvent(aggregateId, eventSerialization, new Date(), event.getClass().getName());

            eventStoreRepository.save(storedEvent);

            // Notify subscribers
            this.setChanged();
            this.notifyObservers(event);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }

    @Override
    public EventStream loadEventStream(String aggregateId) throws EntityNotFoundException {
        List<JpaStoredEvent> storedEvents = eventStoreRepository.findJpaStoredEventByAggregateId(aggregateId);

        if (storedEvents.isEmpty()) {
            throw new EntityNotFoundException();
        }

        List<DomainEvent> events = new ArrayList<>();
        for (JpaStoredEvent storedEvent : storedEvents) {
            events.add(storedEvent.toDomainEvent());
        }

        return new EventStream(events, 0); //TODO populate last version number
    }

}
