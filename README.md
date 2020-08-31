## Deployment Guide
[中文文档](./README_CN.md)

- This is a distributed application, there are two parts, they  use Dubbo RPC communicate with each other.
    - Main module: Be responsible for processing HTTP requests (including static resources), updating product, store, and shopping cart information.
    - Order module: Be responsible for the order module.
- Application dependency：
    - Redis: Maintain distributed login status.
    - Mysql: Store all data.
    - Zookeeper: Be as the registration center of Dubbo.
    - NGINX: implementation of multi-instance deployment and load balancing

- Deployment steps
    - `docker run -d -v /path/to/wxshop-data:/var/lib/mysql -p 3306:3306 -e MYSQL_ROOT_PASSWORD=root -e MYSQL_DATABASE=wxshop --name=wxshop-mysql mysql`
    - `docker run -p 6379:6379 -d redis`
    - `docker run -p 2181:2181 -d zookeeper`
    - Wait for half a minute, wait for the container to start.
    -Create `order` database
    ```
    docker exec -it wxshop-mysql mysql -uroot -proot -e 'create database if not exists `order`
    ```
  - `./mvnw install -DskipTests`
  - `./mvnw flyway:migrate -pl wxshop-main` 
  - `./mvnw flyway:migrate -pl wxshop-order`
  
- Launch the app itself
  - Run in the first window `java -jar wxshop-order/target/wxshop-order-0.0.1-SNAPSHOT.jar`
  - Run in the second window `java -jar wxshop-main/target/wxshop-main-0.0.1-SNAPSHOT.jar`
- open http://localhost:8080

### Database/Redis/ZooKeeper configuration

If your mysql/redis/zookeeper server is not started on localhost, but on another ip, then you need to replace localhost globally with the corresponding ip
    
**Note, only replace `localhost`, don't replace `127.0.0.1`! ! ! **

    
    
