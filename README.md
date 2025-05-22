# Getting Started

Run ```docker compose up -d --build``` 

This command starts MongoDb inside a docker container

Swagger URL: http://localhost:8090/bikeworld/swagger-ui/index.html
Credentials: 

## MongoDb
To log into MongoDb container as admin:

```docker exec -it bikeworld_db mongosh -u root -p supersecret --authenticationDatabase admin```

### MongoDb cheat sheet
* Show all databases   ```show dbs```
* Select database ```use bikeworld```
* Show all collections ```show collections```
* Show user collection data ```db.user.find()```
* List all databases users 
  * ```use admin```
  * ```db.system.users.find()```
* List specific database users 
  * ```use bikeworld```
  * ```db.getUsers()```




## SpringBoot
To create docker image

```docker build -t bikeworld_app -f src/main/docker/Dockerfile .```

To run docker image

```docker run --rm --network backend_bikeworld_networks --name=bikeworld_app_container bikeworld_app```
