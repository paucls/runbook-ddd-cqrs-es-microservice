package io.cqrs.taskmanagement.port.adapter.restapi;

import io.cqrs.taskmanagement.domain.model.runbook.Runbook;
import io.cqrs.taskmanagement.port.adapter.persistence.JpaEventStoreRepository;
import io.cqrs.taskmanagement.read.model.runbook.TaskRepository;
import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class RunbooksApiControllerWebIntegrationTest {

    private static final String RUNBOOKS_URL = "/runbooks";
    private static final String PROJECT_ID = "project-id";
    private static final String RUNBOOK_NAME = "runbook-name";
    private static final String OWNER_ID = "owner-id";
    private static final String TASK_NAME = "task-name";

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private JpaEventStoreRepository eventStoreRepository;

    @Autowired
    private TaskRepository taskRepository;

    @After
    public void tearDown() {
        eventStoreRepository.deleteAll();
        taskRepository.deleteAll();
    }

    @Test
    public void createRunbook_when_success_then_persists_runbookCreated_event() {
        HttpEntity<RunbookDto> request = new HttpEntity<>(new RunbookDto(PROJECT_ID, RUNBOOK_NAME, OWNER_ID));

        restTemplate.postForObject(RUNBOOKS_URL, request, Runbook.class);

        assertThat(eventStoreRepository.count()).isOne();
        assertThat(eventStoreRepository.findAll().get(0).getTypeName()).contains("RunbookCreated");
    }

    @Test
    public void createTask_when_success_then_persists_taskAdded_event() {
        HttpEntity<RunbookDto> request = new HttpEntity<>(new RunbookDto(PROJECT_ID, RUNBOOK_NAME, OWNER_ID));
        restTemplate.postForObject(RUNBOOKS_URL, request, Runbook.class);

        String runbookId = eventStoreRepository.findAll().get(0).getAggregateId();
        String url = RUNBOOKS_URL + "/" + runbookId + "/tasks";
        TaskDto taskDto = new TaskDto(runbookId, OWNER_ID, TASK_NAME);
        HttpEntity<TaskDto> createTaskRequest = new HttpEntity<>(taskDto);

        ResponseEntity<TaskDto> response = restTemplate.exchange(url, HttpMethod.POST, createTaskRequest, TaskDto.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(eventStoreRepository.count()).isEqualTo(2);
        assertThat(eventStoreRepository.findAll().get(1).getAggregateId()).isEqualTo(runbookId);
        assertThat(eventStoreRepository.findAll().get(1).getTypeName()).contains("TaskAdded");
    }

    @Test
    public void createTask_when_success_then_read_model_persists_a_task_entity() {
        HttpEntity<RunbookDto> request = new HttpEntity<>(new RunbookDto(PROJECT_ID, RUNBOOK_NAME, OWNER_ID));
        restTemplate.postForObject(RUNBOOKS_URL, request, Runbook.class);

        String runbookId = eventStoreRepository.findAll().get(0).getAggregateId();
        String url = RUNBOOKS_URL + "/" + runbookId + "/tasks";
        TaskDto taskDto = new TaskDto(runbookId, OWNER_ID, TASK_NAME);
        HttpEntity<TaskDto> createTaskRequest = new HttpEntity<>(taskDto);

        ResponseEntity<TaskDto> response = restTemplate.exchange(url, HttpMethod.POST, createTaskRequest, TaskDto.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(taskRepository.count()).isEqualTo(1);
        assertThat(taskRepository.findTasksByRunbookId(runbookId).get(0).getRunbookId()).isEqualTo(runbookId);
        assertThat(taskRepository.findTasksByRunbookId(runbookId).get(0).getName()).isEqualTo(TASK_NAME);
        assertThat(taskRepository.findTasksByRunbookId(runbookId).get(0).getAssigneeId()).isEqualTo(OWNER_ID);
    }

//    @Test
//    public void createTask_when_nonexisting_runbook_then_returns_404_not_found() {
//        String runbookId = "non-existing-runbook-id";
//        String url = RUNBOOKS_URL + "/" + runbookId + "/tasks";
//        TaskDto taskDto = new TaskDto(runbookId, OWNER_ID, TASK_NAME);
//        HttpEntity<TaskDto> createTaskRequest = new HttpEntity<>(taskDto);
//
//        ResponseEntity<TaskDto> response = restTemplate.exchange(url, HttpMethod.POST, createTaskRequest, TaskDto.class);
//
//        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
//
//        assertThat(eventStoreRepository.count()).isEqualTo(0);
//    }

}
