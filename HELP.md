# Getting Started

Before starting the application run ```docker compose up -d --build``` 

This command starts MongoDb inside a docker container

Swagger URL: http://localhost:8080/bikeworld/swagger-ui/index.html

### MongoDb
To log into the running container as admin:

```docker exec -it bikeworld_db mongosh -u root -p supersecret --authenticationDatabase admin```
