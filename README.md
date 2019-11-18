###Starting RMI registry:

Not needed. It's created programatically by server side.

###Client & Server VM options:

-Djava.rmi.server.useCodebaseOnly=false -Djava.rmi.server.hostname=127.0.0.1

##Important

	When passing a remote interface of client to server it has to extend UnicastRemoteObject and contain constant of serialVersionUID, otherwise it will throw "not serializable" error. It's kinda confusing since it points on implementing Serializable interface which in the end does not help to solve it.