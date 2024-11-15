# Fault-Tolerant Distributed Key-Value Store
A Java-based distributed key-value store that uses Remote Procedure Calls (RPC) for communication and the Paxos consensus algorithm for maintaining consistency across nodes. This project is designed for fault tolerance, scalability, and efficient handling of concurrent client requests.

## Features
1. Distributed Key-Value Storage: Provides reliable and consistent storage of key-value pairs across multiple nodes, ensuring data availability and integrity.
2. Consensus with Paxos: Implements Paxos to maintain a single consistent view of operations, even during network or node failures.
3. Highly Available Architecture: Ensures uninterrupted service by replicating data across multiple nodes and recovering seamlessly from failures.
4. Concurrent Request Processing: Leverages **multithreading** to handle multiple client requests simultaneously, optimizing system throughput and response times.
5. Dynamic Client-Server Communication: Uses **RPC** for efficient and seamless client-server interaction, enabling real-time distributed operations.
6. Fault Tolerant: Designed to handle partial network failures or server crashes gracefully without data loss.
7. Modular Codebase: Organized for easy scalability, allowing the addition of new features or support for larger distributed setups.

## Technologies Used
1. Java: The backbone of the application, chosen for its multithreading capabilities, robust networking support, and performance in building distributed systems.
2. Remote Procedure Call (RPC): Facilitates efficient communication between the client and server, simulating real-world distributed environments.
3. Paxos Algorithm: Ensures consensus among nodes, providing reliability and consistency in the system.
4. Multithreading: Enhances the server's ability to manage concurrent requests efficiently.
5. Highly Available Design: Incorporates redundancy to minimize downtime and maintain service continuity.
6. JAR Files: Packages the application into portable Java archives for straightforward execution.
7. Exception Handling: Implements robust error handling mechanisms to ensure system stability during failures.

## System Components
1. Client: Handles client-side interactions with the server.
2. ProcessRequest: Manages server-side request processing.
3. Server: Implements Paxos and manages the key-value store.
4. jar: Contains compiled JAR files for easy execution.
5. out: Compiled outputs generated during the build process.
6. META-INF: Metadata required for JAR execution.

## How to Run
1. Navigate to the JAR Directory: Open a terminal and navigate to the /jar folder.
2. **Start the Server**: Run the server using:
 `java -jar Server.jar <port> <remoteObject>` <br/>
3. **Start the Client**: In a new terminal, execute:
   `java -jar Client.jar <serverIP> <port>` <br/>
4. Perform Operations: Use the client to perform operations like inserting, retrieving, updating, or deleting key-value pairs.Test the system's behavior in both normal and failure scenarios.
