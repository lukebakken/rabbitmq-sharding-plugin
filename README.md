## Application with sharding-plugin and actuator endpoint to clear connections at the cache.

## Problem with version 3.9.5 RabbitMQ

### How to reproduce the error:

1) Create the docker imagens and the cluster:

`docker run -d --hostname rabbit1 --name rabbit1 -e RABBITMQ_ERLANG_COOKIE='rabbitcluster' -p 30000:5672 -p 30001:15672 rabbitmq:3.9.5-management` 

`docker run -d --hostname rabbit2 --name rabbit2 --link rabbit1:rabbit1 -e RABBITMQ_ERLANG_COOKIE='rabbitcluster' -p 30002:5672 -p 30003:15672 rabbitmq:3.9.5-management`

`docker run -d --hostname rabbit3 --name rabbit3 --link rabbit1:rabbit1 --link rabbit2:rabbit2 -e RABBITMQ_ERLANG_COOKIE='rabbitcluster' -p 30004:5672 -p 30005:15672 rabbitmq:3.9.5-management`

2) Join the images to the cluster and enable the plugins

```
docker exec -i -t rabbit2 \bash
root@rabbit2:/# rabbitmqctl stop_app
root@rabbit2:/# rabbitmqctl join_cluster rabbit@rabbit1
root@rabbit2:/# rabbitmqctl start_app
root@rabbit2:/# rabbitmq-plugins enable rabbitmq_event_exchange
root@rabbit2:/# rabbitmq-plugins enable rabbitmq_sharding
root@rabbit2:/# exit

docker exec -i -t rabbit3 \bash
root@rabbit3:/# rabbitmqctl stop_app
root@rabbit3:/# rabbitmqctl join_cluster rabbit@rabbit1
root@rabbit3:/# rabbitmqctl start_app
root@rabbit3:/# rabbitmq-plugins enable rabbitmq_event_exchange
root@rabbit3:/# rabbitmq-plugins enable rabbitmq_sharding
root@rabbit3:/# exit

docker exec -i -t rabbit1 \bash
root@rabbit1:/# rabbitmq-plugins enable rabbitmq_event_exchange
root@rabbit1:/# rabbitmq-plugins enable rabbitmq_sharding
root@rabbit1:/# exit
```

3) Create the configurations to use the sharding-plugin with success
- Endpoint: http://localhost:30001/#/policies
- Add / update a policy
  ```
  Name: testing-shards (It doesn't matter)
  Pattern: ^testing$
  Apply to: Exchanges and queues
  Definition:
  routing-key = 1234 (String)
  shards-per-node = 1 (Number)
  ```

- Endpoint: http://localhost:30001/#/exchanges
- Add a new exchange:
  ```
  Name: testing
  Type: x-modulus-hash
  ```

4) Start the application
5) Stop the node 2 or 3: `docker stop rabbit2`
6) Wait for the conections be distributed to the other nodes
7) Start the node 2: `docker start rabbit2` and see the errors

PS: with versions 3.8.18 and 3.18.21 this doesn't occurs.
