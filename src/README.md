
# TCP Client and Server

The ServerTCP & ClientTCP are two Java programs that allow communication 
between server and client using the TCP protocol. 
The TCP Server listens to client connection on a specific port, 
while the TCP Client connects to the server IP address and port.


### ServerTCP

To use the ServerTCP:

1. Compile the ServerTCP.java file using the Java compiler:

   ```
   javac ServerTCP.java
   ```

2. Run the compiled ServerTCP class, providing the port number as a command-line argument:

   ```
   java ServerTCP.java
   ```

3. Add the `<port>` number on which you want the server to listen for incoming connections.

4. The server will start and display a message indicating the IP address and port it is listening. It will then wait for client connections.


## ClientTCP

The ClientTCP program represents the client-side of the TCP communication.

To use the ClientTCP, follow these steps:

1. Compile the ClientTCP.java file using the Java compiler:

   ```
   javac ClientTCP.java
   ```

2. Run the compiled ClientTCP class, providing the server's IP address and port number as command-line arguments:

   ```
   java ClientTCP <server-ip> <server-port>
   ```

   Replace `<server-ip>` with the IP address (`localhost`) of the server you want to connect to,
   and `<server-port>` with the corresponding port number.

3. The client will attempt to establish a connection with the server. On successful connection, it will display a message indicating the connection status.

4. Once connected, you can enter messages to send to the server. 
The client will display the responses received from the server.


-------

# UDP Client and Server

The ClientUDP & ServerUDP  are two Java programs that enable communication between a server and a client using the 
UDP protocol.

## ServerUDP

The ServerUDP program represents the server-side of the UDP communication. 

To use the ServerUDP, follow these steps:

1. Compile the ServerUDP.java file using the Java compiler:

   ```
   javac ServerUDP.java
   ```

2. Run the compiled ServerUDP class, providing the desired port number as a command-line argument:

   ```
   java ServerUDP.java  
   ```

3. Add the `<port>` number on which you want the server to listen for incoming connections.

4. The server will start and display a message indicating the IP address and port it is listening on. 
5. It will then wait for client connection.


## ClientUDP

The ClientUDP program represents the client-side of the UDP communication. 
It connects to a UDP server using the server's IP address and port number. 


To use the ClientUDP, follow these steps:

1. Compile the ClientUDP.java file using the Java compiler:

   ```
   javac ClientUDP.java
   ```

2. Run the compiled ClientUDP class, providing the server's IP address and port number as command-line arguments:

   ```
   java ClientUDP <server-ip> <server-port>
   ```

   Replace `<server-ip>` with the IP address (`localhost`) 
 of the server you want to connect to, and `<server-port>` with the corresponding port number.

3. The client will attempt to establish a connection with the server. On successful connection, 
it will display a message indicating the connection status.

4. Once connected, you can enter messages to send to the server. 
The client will display the responses received from the server.



