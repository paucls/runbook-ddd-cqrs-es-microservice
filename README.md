# cqrs-microservice

A sample microservice with CQRS and Event Sourcing architecture. Implemented in Java and Spring Boot.

## The Domain
For this sample application, we'll work in the cafe domain. Our focus will be on the concept of a **tab**, which tracks 
the visit of an individual or group to the cafe. When people arrive to the cafe and take a table, a tab is **opened**. 
They may then **order** drinks and food. **Drinks** are **served** immediately by the table staff, however **food** 
must be cooked by a chef. Once the chef has **prepared** the food, it can then be **served**.

During their time at the restaurant, visitors may **order** extra food or drinks. If they realize they ordered the wrong 
thing, they may **amend** the order - but not after the food and drink has been served to and accepted by them.

Finally, the visitors **close** the tab by paying what is owed, possibly with a tip for the serving staff. Upon closing 
a tab, it must be paid for in full. A tab with unserved items cannot be closed unless the items are either marked as 
served or cancelled first.

## Overview  
Start your server as an simple java application  

You can view the api documentation in swagger-ui by pointing to  
http://localhost:8080/  

## Demo

## Documentation
Links to some of the articles and documentation used to implement this project:

- Implementing Domain-Driven Design, Vaughn Vernon.
- IDDD Samples https://github.com/VaughnVernon/IDDD_Samples
- Edument CQRS Tutorial http://cqrs.nu/Tutorial
