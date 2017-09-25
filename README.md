# runbook-ddd-cqrs-es-microservice

A sample microservice with CQRS and Event Sourcing architecture. Implemented in Java and Spring Boot.

## The Domain
For this sample application, we will work in a operations team domain. Our focus will be on the concept of a **Runbook** which tracks **Tasks**. 

A Runbook is used to track all the tasks to be completed in order for a new system deployment or maintenance operation to be done. 
Operations planner can: create a runbook for a project, add a task to the runbook, assign a task to an operator, reassign a task. 
A task can be marked in progress only by the task assignee. An in-progress task can be completed or rejected. 
When all tasks in the runbook are completed and/or rejected, a runbook can be marked as completed.
To keep it simple, there are no dependencies between tasks thus can be completed in any order.

![Event Storming](docs/event_storming.png)

## Architecture
CQRS and Event Sourcing architecture
![CQRS+ES Architecture](docs/ddd-cqrs-es-architecture.png)

## Overview  
Start your server as an simple java application  

You can view the api documentation in swagger-ui by pointing to  
http://localhost:8080/  

## Demo

## Documentation
Links to some of the articles and documentation used to implement this project:

- Edument CQRS Tutorial http://cqrs.nu/Tutorial
- Implementing Domain-Driven Design, Vaughn Vernon.
- IDDD Samples https://github.com/VaughnVernon/IDDD_Samples
- Why do commands include the entity tabId when creating entities? https://github.com/gregoryyoung/m-r/issues/17
- Should Aggregates be Event Handlers https://stackoverflow.com/questions/26876757/should-aggregates-be-event-handlers
- Should getters from domain classes not be prefixed with “get” in DDD, and why? https://stackoverflow.com/questions/46139501/should-getters-from-domain-classes-not-be-prefixed-with-get-in-ddd-and-why/46141986#46141986
- CQRS + Event Sourcing – A Step by Step Overview, Daniel Whittaker http://danielwhittaker.me/2014/10/02/cqrs-step-step-guide-flow-typical-application/
- Aggregate Root – How to Build One for CQRS and Event Sourcing, Daniel Whittaker http://danielwhittaker.me/2014/11/15/aggregate-root-cqrs-event-sourcing/
- How To Validate Commands in a CQRS Application, Daniel Whittaker http://danielwhittaker.me/2016/04/20/how-to-validate-commands-in-a-cqrs-application/
- CQRS Commands and Queries - Do they belong in the domain? https://stackoverflow.com/questions/32216408/cqrs-commands-and-queries-do-they-belong-in-the-domain
- Implementing an Event Sourced Aggregate, Nick Chamberlain https://buildplease.com/pages/fpc-9/
