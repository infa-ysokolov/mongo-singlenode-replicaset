 1. Create the following docker-compose file (e.g. mongodb-single-replica-set.yml):
````
 version: '2.2'

 services:
   mdm-mongo:
     image: mongo:4.2
     ports:
       - "27017"
     volumes:
       - "/data/db/data"
       - "/data/db/log"
     command: mongod --dbpath /data/db/data --replSet "rs0" --port 27017 --wiredTigerCacheSizeGB 0.256
     hostname: "mdm-mongo"
   mongo-setup:
     image: mongo:4.2
     command: [
     "bash",
     "-c",
     "mongo --host mdm-mongo:27017 --eval 'rs.initiate();' && exit"
     ]
````     
 2. Start Mongo
````
docker-compose -f mongodb-single-replica-set.yml up -d mdm-mongo 
````
 3. Initialize replica set
````
  docker-compose -f mongodb-single-replica-set.yml run --rm mongo-setup
````  
 4. Get the external port
````
  $ docker ps
  CONTAINER ID        IMAGE               COMMAND                  CREATED             STATUS              PORTS                      NAMES
  6eb9e78cc19b        mongo:4.2           "docker-entrypoint.s…"   36 seconds ago      Up 41 seconds       0.0.0.0:32770->27017/tcp   docker-compose_mdm-mongo_1
````  

 5. Run this class and pass mongo port as a parameter:
````
 mvn clean install -Dport=32770
````
