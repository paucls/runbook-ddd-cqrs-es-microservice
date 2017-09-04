package io.cqrs.taskmanagement.port.adapter.persistence;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.cqrs.taskmanagement.application.EventStore;
import io.cqrs.taskmanagement.domain.model.DomainEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;

@Component
public class JpaEventStore implements EventStore {

    private JpaEventStoreRepository eventStoreRepository;

    private static final ObjectMapper mapper = new ObjectMapper();

    @Autowired
    public JpaEventStore(JpaEventStoreRepository eventStoreRepository) {
        this.eventStoreRepository = eventStoreRepository;
    }

    @Override
    public void append(DomainEvent aDomainEvent) {
        try {
            String eventSerialization = mapper.writeValueAsString(aDomainEvent);

            JpaStoredEvent storedEvent = new JpaStoredEvent(eventSerialization, new Date(), aDomainEvent.getClass().getName());

            eventStoreRepository.save(storedEvent);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void append(List<DomainEvent> domainEvents) {
        for (DomainEvent event : domainEvents) {
            this.append(event);
        }
    }
}
