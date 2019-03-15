# Netty WebSockets
WebSockets implementation of dummy sport app using Netty just to show how to build server application with using this 
asynchronous and event-driven framework. 

It's a demo for presentation. You can go throught branches step-n where n is a step.

## Step 1
Simple HTTP Server you can test it with simple GET request in your browser
`localhost:9000/hello `

## Step 2
Handling WebSocket handshake, you can test it with your terminal 
`- curl -i -N -H "Connection: Upgrade" -H "Upgrade: websocket" -H "Host: localhost" -H "Origin: localhost" localhost:9000`
or go use ws_client.html from resources.

## Step 3
Handling WS frames (mainly text). Exception handling in handler. 
You can test it with ws_client.html

## Step 4
Server application subscription and match simulation added you can test it with ws_client.html
and simulat match on URL with POST request
`localhost:9000/simulate` or 
`watch -n1 curl -X POST localhost:9000/simulate` which will schedule for every sec.

## Step 5
Logging reference counting and showing that it's released for us in Netty.