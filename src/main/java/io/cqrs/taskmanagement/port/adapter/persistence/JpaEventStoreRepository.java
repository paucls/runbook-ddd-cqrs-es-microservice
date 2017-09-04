package io.cqrs.taskmanagement.port.adapter.persistence;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface JpaEventStoreRepository extends JpaRepository<JpaStoredEvent, String> {
}
