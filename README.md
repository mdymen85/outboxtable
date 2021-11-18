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
This will be recorded in two tables: **obt_spending**, and other table that will be used for a **job**, called: **obt**.

The second module: **job**, will read from that database in module **base** and the table mentioned before, and post that message in a **SQS queue**. 

The third module: **consumer**, will pick that message posted in the queue and will save that information in their database, in the table **obt_spending**

[](https://viewer.diagrams.net/?tags=%7B%7D&highlight=0000ff&edit=_blank&layers=1&nav=1&title=outboxtable#R5VrLlto4EP0alj0HP4FloDvJyemZPFhMssoRWG0rI1seWW6afH1KWH7KBjfgGQgbsAo9696qUpUZWYvw5R1HcfAn8zAdmWPvZWTdj0zTsJwxfEnJNpNMTSXwOfFUp1KwJD%2BxEubdUuLhpNZRMEYFievCNYsivBY1GeKcberdnhitrxojH2uC5RpRXfo38USQn2JSyt9j4gf5yoY7y34JUd5ZnSQJkMc2FZH1MLIWnDGRPYUvC0yl8nK9ZOPedvxabIzjSPQZ8DX98GHjPU4WwXgxDr%2F9iD7%2B9XSnZnlGNFUHhqNTzNWWxTbXA%2Bw%2Blo9pSB%2FJE6YkgtY8xpyEWEB%2F654q8adSNt8EROBljNZy6AYYArJAhBRaBjwCaALBEF60KUVxQla7Vccg4Xid8oQ84y84ybghpSwVcqVFgbkUqpNgLvBLp4qMQvHAWMxgn3wLXdQAV0GluDpTzU0JvDFWsqACupULkSKbX8xc4gEPCpJXwGNp8KxQgm8THGN6aejkPqoCjwZNRaExI5HY7cGZj5z7BkKMi4D5LEK0ilEvvXVTp1OZtlPT5bRNl7oqzeFUqTuiK1WlYfXTpT2YKk1NlQl6xnJYNJIxwqWwhfkKfLzri51iXBRK46d6i63E9yTGkUciv%2FilPipvNWYVSDqKJobYg%2BiqmiVMD6W06oEQJX4knRd%2BkjMm4KpgG4%2B71j0gMod9vZExHporytb%2FSJ%2FE0sjDnnI7gCvfflXT7Rrfag5Jbqd%2Bt0Dcx%2Fu4YJxIGo4pEuAy6xeVFg6ooZ8k00uyOXUfaBgNFiUs5WusBpVEAi2hbaWbsp%2FOZabty5S0zCYsSVqc8ATe6sHuWl3A5P92AfZ5XcAtWP6FGLTj%2FCcGrZZpNWh9j016N%2FaYOU1tj%2BdyDKbuGDgWKY%2F2uQdp5ARymTeKSSsmBAvrzGEQ2EDioSTY8SajSp6ETjVOncScPrFlGIbV0dPucB0M0%2Ba5Mw9MNDANdJ%2F2g600CtxEKlSkNJeTCrktNoq8LNgcH1quw77tQezWntbNzTKPtFt3sn%2Biod23no9czr2uA7ocAruuOaPfvW46lJU5miaXn5d3n1Oc3mhNyDaPdITWUBCZepi6HLY7%2B5XZiO89CxmDsV0PKUC%2FJA1vtTrt2EeSfTZY1J%2B1kD1LBz3ynOeDCjXITsbwmcgXLqoTLFrpdy3hXvHy4HW%2Bw9rOmzCaTXD7X%2BcPTDT0teCSi%2Bfuaxyl1fNaMFi5x9SL541yzxnqu79bOacD4lOt02hQw%2Bplna8u57Qv07Oc49YHW409Dm34%2Bi32Sg2%2FeF98wPCNwdLuiaZKj4B1iXVwajBtN8Mextb3RUpHObWiyvxieaJJFu9I8vJl88LaN2IaswMTDWw4ho52AohJZTP4%2BLc1Dbw02M2OjPv8uFuNm5Jl9yut64UYd%2FaHs3%2Bqo5GHZvkPoKx7%2BT8q6%2BEX)

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

