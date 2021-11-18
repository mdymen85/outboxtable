## Outboxtable

from: https://microservices.io/patterns/data/transactional-outbox.html

### Problem
How to reliably/atomically update the database and send messages/events?

### Forces
-   2PC is not an option
-   If the database transaction commits messages must be sent. Conversely, if the database rolls back, the messages must not be sent
-   Messages must be sent to the message broker in the order they were sent by the service. This ordering must be preserved across multiple service instances that update the same aggregate.

### Solution
A service that uses a relational database inserts messages/events into an _outbox_ table (e.g. `MESSAGE`) as part of the local transaction. An service that uses a NoSQL database appends the messages/events to attribute of the record (e.g. document or item) being updated. A separate _Message Relay_ process publishes the events inserted into database to a message broker.

![](https://microservices.io/i/patterns/data/ReliablePublication.png)

### My solution

I created three separeted modules that runs in differente EC2 instsances, the first one: **base**, has an API rest that receives a post:
```
curl --location --request POST '<<HOST>>:8081/api/v1/spending' \
--header 'Content-Type: application/json' \
--data-raw '{
"identity":"AB1",
"name":"Martin",
"surname":"Robi",
"amount":51.3
}'

```

This message will be recorded in two tables, in a database that only module **base**: **obt_spending**, and other table that will be used for a **job**, called: **obt**.

The second module: **job**, will read from that database in module **base** and the table mentioned before, and post that message in a **SQS queue**. 

The third module: **consumer**, will pick that message posted in the queue and will save that information in their database, in the table **obt_spending**

![](https://github.com/mdymen85/outboxtable/blob/main/outboxtable.drawio.png)

### Docker

Each module can run in a differente instance, and can be pulled in my docker hub, as:

Module: **base**
    
    docker run --net first-app --name outboxtable-base -p 8081:8080 -d mdymen85/outboxtable-base:latest
    
    docker run --net first-app --name outboxtable-base-mysql -p 3306:3306 -e MYSQL_ROOT_PASSWORD=mdymen_pass -d mysql:latest
    docker exec -i outboxtable-base-mysql sh -c 'exec mysql -uroot -p"$MYSQL_ROOT_PASSWORD"' < data.sql
    
Module: **job**
    
    docker run --name outboxtable-job -p 8080:8080 -e MYSQL_HOST=<<IP_DESTINY>> -d mdymen85/outboxtable-job:latest

Module: **consumer**
    
    docker run --net first-app --name outboxtable-consumer -p 8081:8080 -d mdymen85/outboxtable-consumer:latest
        
    docker run --net first-app --name outboxtable-consumer-mysql -p 3306:3306 -e MYSQL_ROOT_PASSWORD=mdymen_pass -d mysql:latest
    docker exec -i outboxtable-consumer-mysql sh -c 'exec mysql -uroot -p"$MYSQL_ROOT_PASSWORD"' < /data_consumer.sql



### Cloudformation and Jenkins

There is a file that can be used to deploy all the stack in aws: **cloudformation.yml** with 3 instances EC2, security group and the policy to communicate between instances. But, also, in the root directory is a **Jenkinsfile** with the name: **Jenkinsfile-cloudformation** that has the code to run the stack in **AWS** environment in a CI/CD way. That script pick the cloudformation file from an S3 bucket -the same file that is published in the project root.- That cloudformation file has all the necessary to pull docker and docker's images in instances, so it's not necessary to do something else, just run the cloudformation file to create the stack and run the pattern.

Its needed to configure the **AWS_ACCESS_KEY** and **AWS_SECRET_ACCESS_KEY** in Jenkins, in order to work the conection to **AWS** from **jenkins**.

