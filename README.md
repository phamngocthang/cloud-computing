# Topic: Building a Cloud system to simulate a course registration page that can scale up and down by itself

## GROUP 29
* Lê Anh Nhân - 20110689
* Phạm Ngọc Thắng - 20110728
* Đỗ Quốc Việt - 19110498

- - -
This is the final topic of building a Cloud system to simulate a course registration page that can scale up and down by itself. In this topic, applying the features of Docker Swarm, helping the system to load blancing, high availability. This topic uses prometheus paired with cadvisor metrics to determine cpu usage. It then uses the manager node to determine if a service wants to be auto scaled and uses the node manager to scale the service.
Currently, the project only uses cpu for auto-scale. If cpu usage reaches 30%, the service will scale up, if it reaches 10%, the service will scale down.

>     CPU_PERCENTAGE_UPPER_LIMIT=30
>     CPU_PERCENTAGE_LOWER_LIMIT=10

Some technologies used in the project:
- Spring Boot Framework
- MariaDB Galera Cluster
- Redis Database
- Docker Swarm
