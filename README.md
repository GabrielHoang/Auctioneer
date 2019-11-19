# Auctioneer
Academic project about RMI communication between multiple clients and server, simulating live auction.

## Prerequisites
* Lombok plugin (make sure to have annotation processing turned on)
* disabled firewall
* Java 8

## Running

To run both client and server the VM options have to be set:

    -Djava.rmi.server.useCodebaseOnly=false -Djava.rmi.server.hostname=127.0.0.1

Entrypoints are in the ``ClientOperation`` and ``ServerOperation`` classes.
##Conclusions

#### Starting RMI registry:

Not needed. It's created programatically by server side.

#### Passing remote interface from client to server

   When passing a remote interface of client to server it has to extend UnicastRemoteObject and contain constant of serialVersionUID, otherwise it will throw "not serializable" error. It's kinda confusing since it points on implementing Serializable interface which in the end does not help to solve it.