# Jaissica Hora

# Readme

This project is an extension of Project #3 and introduces fault tolerance and consensus mechanisms 
to a replicated Key-Value Store setup. Our goal in this project was to improve system resilience against replica failures 
and achieve synchronized updates using the Paxos algorithm. To implement this project I have utilized Paxos roles including Proposers, Acceptors, and Learners. 
The project enabled me to understand the algorithm better and steps needed for consensus.


# Steps to run the code:

1. Go to the jar(/jar) folder in the submitted project.

2. To run the Server:

   ```
      java -jar Server.jar 1000 AB
   ```
where 1000 is the port number and AB is the remoteObject which is remote registry object 
name using which client accepts the server methods.

3. To run the Client:   
    ```
      java -jar Client.jar localhost 1000 AB
    ```
where localhost is the hostname, 1000 which is the port on which server is running and ‘AB’ is the registry object.

4. When both Client and Server are up you’ll see the prepopulated data which perform the operations in 
following order: PUT, GET, DELETE & GET on 7 key value pairs.

5. After that you can input following operations:
   1. To PUT value for specific key : PUT <KEY> <VALUE>
        ```
          PUT key10 jj
        ```
   2. To GET value for specific key: GET <KEY>
        ```
          GET key10
        ```
   3. To DELETE value for specific key: DELETE <KEY>
         ```
          DELETE key10
         ```
   4. To EXIT:
         ```
           EXIT
         ```
I have attached screenshots for the same.

