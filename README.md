# Data Migration Project

## Goals
* Hands-on experience in Spring Boot multi-module project architecture.
* Familiarity with writing scheduled task, building web APIs, using MySQL as database and Kafka for message queue.

## File Structure
```
DataMigrationProject
│   pom.xml (parent pom for the entire project, empty or with global configurations)
│
├── Scheduler
│   ├── src
│   │   └── main
│   │       └── java
│   ├── pom.xml (module-specific configurations)
│
├── Consumer
│   ├── src
│   │   └── main
│   │       └── java
│   ├── pom.xml (module-specific configurations)
│
├── Web
│   ├── src
│   │   └── main
│   │       └── java
│   ├── pom.xml (module-specific configurations)
│
├── Common
│   ├── src
│   │   └── main
│   │       └── java
│   ├── pom.xml (module-specific configurations)
```

## High Level Workflow
* Pricing data from a CSV file is read on a scheduled basis (1.00 AM every night) and saved to the database table via Http POST request sent to the web API.
* Once the web API receives the request, it transforms the list of pricing data items and then saves them all in a DB and send the success response back to the scheduler.
* If, there occurs any errors while saving data into the DB, appropriate response body is constructed and sent back to the scheduler.
* The scheduler catches the errors (4xx or 5xx) and if the error caught is transient (e.g. internet connection failure), it uses fallback approach to produce data via Kafka topic.
* The Kafka consumer then will do the same work of fetching csv pricing data and save them into DB.
* However, if the error caught in the scheduler is non-transient (4xx or file path invalid or any other client based errors), appropriate error message is logged.
* The scheduler can also be triggered through an API call.

## API Endpoints

* To trigger scheduler: /api/v1/scheduler/trigger (defined in scheduler/../controller/SchedulerApiController.java)
* To process and save csv data: /api/v1/pricing-data (defined in web/../controller/CsvDataMigrationController.java)

## Environment Variables

* Scheduler Module
  * CSV_DATA_MIGRATION_CRON_EXPRESSION
  * CSV_FILE_PATH
  
* Web Module
  * SERVER_PORT
  * DB_URL
  * DB_USERNAME
  * DB_PASSWORD
  * HIBERNATE_AUTO_DDL
  * SHOW_SQL