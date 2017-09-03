package io.cqrs.taskmanagement.domain.model.runbook;

import io.cqrs.taskmanagement.application.EventPublisherStub;
import io.cqrs.taskmanagement.domain.model.Aggregate;
import io.cqrs.taskmanagement.domain.model.DomainEventPublisher;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.Transient;
import java.util.HashMap;
import java.util.Map;

@Entity
public class Runbook implements Aggregate {

    @Id
    private String runbookId;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "runbook_id")
    private Map<String, Task> tasks;

    private String projectId;
    private String name;
    private String ownerId;
    private boolean isCompleted;

    @Transient
    private DomainEventPublisher eventPublisher;

    // empty constructor for rest api TODO: probably we need a DTO there
    Runbook() {
        this.eventPublisher = new EventPublisherStub(); // JPA is using this constructor.
        // As a quick hack, instantiating directly the event publisher here.
        // TODO: Redesign AR to not use a event publisher and instead use a collection of Unpublished events.
    }

    // constructor needed for reconstruction
    Runbook(DomainEventPublisher eventPublisher) {
        this.eventPublisher = eventPublisher;
    }

    Map<String, Task> getTasks() {
        return this.tasks;
    }

    // We won't need accessors if we do not use this Entity as a read model
    public String getProjectId() {
        return this.projectId;
    }

    public String getRunbookId() {
        return this.runbookId;
    }

    public String getName() {
        return this.name;
    }

    public boolean isCompleted() {
        return this.isCompleted;
    }

    public String getOwnerId() {
        return ownerId;
    }

    //
    // Handle
    //

    // Note this constructor is also a command handler
    public Runbook(CreateRunbook c, DomainEventPublisher eventPublisher) {
        this.eventPublisher = eventPublisher;

        RunbookCreated runbookCreated = new RunbookCreated(c.getProjectId(), c.getRunbookId(), c.getName(), c.getOwnerId());
        eventPublisher.publish(runbookCreated);
        apply(runbookCreated);
    }

    public void handle(AddTask c) {
        TaskAdded taskAdded = new TaskAdded(c.getTaskId(), c.getName(), "description", "user-id");
        eventPublisher.publish(taskAdded);
        apply(taskAdded);
    }

    public void handle(StartTask c) {
        verifyAssignee(c.getTaskId(), c.getUserId());

        TaskMarkedInProgress taskMarkedInProgress = new TaskMarkedInProgress(c.getTaskId());
        eventPublisher.publish(taskMarkedInProgress);
        apply(taskMarkedInProgress);
    }

    public void handle(CompleteTask c) {
        verifyAssignee(c.getTaskId(), c.getUserId());
        verifyInProgress(c.getTaskId());

        TaskCompleted taskCompleted = new TaskCompleted(c.getTaskId(), c.getUserId());
        eventPublisher.publish(taskCompleted);
        apply(taskCompleted);
    }

    public void handle(CompleteRunbook c) {
        verifyIsOwner(c.getUserId());
        verifyAllTasksCompleted();

        RunbookCompleted runbookCompleted = new RunbookCompleted(c.getRunbookId());
        eventPublisher.publish(runbookCompleted);
        apply(runbookCompleted);
    }

    private void verifyIsOwner(String userId) {
        if (!this.ownerId.equals(userId)) throw new RunbookOwnedByDifferentUserException();
    }

    private void verifyAllTasksCompleted() {
        boolean hasPendingTasks = tasks.entrySet()
                .stream()
                .anyMatch(entry -> !entry.getValue().isClosed());
        if (hasPendingTasks) throw new RunBookWithPendingTasksException();
    }

    private void verifyAssignee(String taskId, String userId) {
        Task task = tasks.get(taskId);
        if (!task.getUserId().equals(userId)) {
            throw new TaskAssignedToDifferentUserException();
        }
    }

    private void verifyInProgress(String taskId) {
        if (!tasks.get(taskId).isInProgress()) { // TODO should not this go down to the Task aggregate?
            throw new CanOnlyCompleteInProgressTaskException();
        }
    }

    //
    // Apply
    //

    void apply(RunbookCreated c) {
        this.projectId = c.getProjectId();
        this.runbookId = c.getRunbookId();
        this.name = c.getName();
        this.ownerId = c.getOwnerId();
        this.isCompleted = false;
        this.tasks = new HashMap<>();
    }

    void apply(TaskAdded e) {
        tasks.put(e.getTaskId(), new Task(e.getTaskId(), e.getUserId()));
    }

    void apply(TaskMarkedInProgress e) {
        // TODO Which aggregate should be responsible to apply the task status change?
        tasks.get(e.getTaskId()).apply(e);
    }

    void apply(RunbookCompleted e) {
        this.isCompleted = true;
    }

    void apply(TaskCompleted e) {
        tasks.get(e.getTaskId()).apply(e);
    }
}
