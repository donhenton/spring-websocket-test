docker image

https://hub.docker.com/r/webcenter/activemq/

to get the admin http://<docker-ip>:8161/admin


where <docker-ip> if found by docker-machine ip on the mac

61616 is the broker
61614 is websocket
61613 is stomp via tcp and is not active it appears it EITHER 61614 OR 61613

Run command 
docker run --name='activemq' -it --rm     -e 'ACTIVEMQ_MIN_MEMORY=512'     -e 'ACTIVEMQ_MAX_MEMORY=2048'   -p 61614:61614     -p 8161:8161     -p 61616:61616    webcenter/activemq:latest