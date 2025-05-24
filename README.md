# Getting Started

Run ```docker compose up -d --build``` 

This command starts MongoDb inside a docker container

Swagger URL: http://localhost:8090/bikeworld/swagger-ui/index.html

### Badges
![Build](https://github.com/em-gei/swam-backend/actions/workflows/ci.yml/badge.svg)

[![Coverage Status](https://coveralls.io/repos/github/em-gei/swam-backend/badge.svg?branch=main)](https://coveralls.io/github/em-gei/swam-backend?branch=develop)


-- add coverage badge

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

```docker run --rm --network backend_bikeworld_networks --name=bikeworld_app_container -e MONGO_URI=mongodb://app_user:4pp_p4ssw0rd@mongodb:27017/bikeworld bikeworld_app```
