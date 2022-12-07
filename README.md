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

> **Note:**  Above is the entire source code. The content of the application has been packaged into docker images to be on docker hub. Available in the docker compose file, just run deploy stack to be able to work.

## 1. System section
In this article, the system uses applications to monitor the system:
- **cAdvisor (Container Advisor)** provides container users with an understanding of the resource usage and performance characteristics of their running containers. This is a running daemon that collects, aggregates, processes and exports information about running containers. Specifically, for each container, it keeps resource isolation parameters, historical resource usage, complete historical resource usage graph, and network statistics. This data is exported by container and machine-wide.
- **Prometheus** is a service and system monitoring system. It collects metrics from configured targets at certain intervals, evaluates rule expressions, displays results and can trigger an alert if certain conditions are observed. correct.
  Here, Prometheus will monitor the updated parameters from the containers through cAdvisor, from that data will check the performance of the cpu, then set up the rules to scale up and scale down.
  The configurations are packaged into the docker-swarm-autoscaler image. The configuration is located in the docker-swarm-autoscaler directory.
- **The packaging of the sentence uses the syntax:**
> docker build . -t lequocbao29072001/autoscaler:1.0
- **Create a file prometheus.yml containing the configuration information of prometheus (the file is contained in this repository)**
- **Create file docker-swarm-autoscaler.yml containing configuration applications to deploy system monitoring (files are contained in this repository)**
- **Execute deploy docker stack, use following command**:

> `docker stack deploy -c swarm-autoscaler-stack.yml autoscaler`

## 2. Application section

Below are the application packaging details.
- **Install maven to build into jar package**

> sudo apt-get install maven

- **Use maven build into .jar package**

> mvn install

- **Use the docker file contained in the repo (`Dockerfile` ) to package it into an image**

> docker build . -t hungfq/dkmh:1.5

- **Create file docker compose dkmh.yml . This file is located in the repo. In this file define 3 services: app, db, redis.**

>In which:
>- _`app` : is a service that contains web application containers_
> - _`db` : is the service containing the database container_
>- _`redis` : is a service database that helps to store caches and sessions to help containers keep stateless_
>- _`image`: define the image name to create the container_
>- _`deploy`: configuration options for deployment_
>- _`replicas`: Number of container copies_
>- _`constraints`: [node.role == worker/manager] → Determines which host the container will be deployed to in the docker swarm. Here, we locate docker host according to manager/worker role in docker swarm._
>- _`volumes`: to mount between docker host and container._
>- _`swarm.autoscaler`: Required. This allows for automatic scaling for a service. Anything other than `true` will not trigger it_
>- _`swarm.autoscaler.minimum`: This is the desired minimum number of replicas for a service. The auto scaler will not scale below this number_
   >-`swarm.autoscaler.maximum`: This is the maximum desired number of replicas for a service. The Automated Calculator will not scale beyond this number.
>
>In the service “app”, declare some ENV variables to create database information such as: MYSQL_HOST, REDIS_HOST… After creating and running the stack, we can check the web and db connection.

>For services you want to auto scale yourself, you will need an implementation label swarm.autoscaler = true
 ```
deploy:
  labels:
    - "swarm.autoscaler=true"
```
> This is best combined with resource constraints. This is also the implementation key.
```
deploy:
  resources:
    reservations:
      cpus: '0.25'
      memory: 512M
    limits:
      cpus: '0.50'
```

- **Execute deploy docker stack, use following command**:

> docker stack deploy -c dkmh.yml dkmh
## 3. Usage
1. From the root directory deploy prometheus, cadvisor and docker-swarm-autoscaler with the command:
    > docker stack deploy -c swarm-autoscaler-stack.yml autoscaler
2. Then run the app with the command:
    > docker stack deploy -c dkmh.yml dkmh
3. Use visualizer to view container control more easily.
    > docker run -it -d -p 8888:8080 -v /var/run/docker.sock:/var/run/docker.sock dockeramples/visualizer
   
## 4. References
1. [Autoscale Docker Swarm services based on cpu utilization. (github.com)](https://github.com/jcwimer/docker-swarm-autoscaler)
2. [Getting Started | Spring Boot with Docker](https://spring.io/guides/gs/spring-boot-docker/)
3. [toughiq/mariadb-cluster - Docker Image | Docker Hub](https://hub.docker.com/r/toughiq/mariadb-cluster)
4. [prom/prometheus - Docker Image | Docker Hub](https://hub.docker.com/r/prom/prometheus)
5. [google/cadvisor - Docker Image | Docker Hub](https://hub.docker.com/r/google/cadvisor/)
6. [Redis - Official Image | Docker Hub](https://hub.docker.com/_/redis)
7. [Docker visualizer](https://github.com/dockersamples/docker-swarm-visualizer)
