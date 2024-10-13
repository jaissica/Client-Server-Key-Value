package main.project4.RMI.Client;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.RMIClientSocketFactory;
import java.rmi.server.ServerNotActiveException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;
import java.util.Scanner;
import main.project4.RMI.RequestProcess.requestProcessing;
import main.project4.RMI.Server.IKeyValueStore;

/**
 * Client class handles communication with RMI server
 */
public class Client {

  public static final SimpleDateFormat dateFormat = new SimpleDateFormat("MM-dd-yyyy HH:mm:ss.SSS");

  /**
   * Main method to start client application
   *
   * @param args Command line arguments: <host> <port> <remoteObjectName>
   */

  public static void main(String[] args) {
    try {
      if (args.length != 3) {
        System.out.println("java Client hostname port remoteObjectName");
        System.exit(1);
      }

      String host = args[0];
      int port = Integer.parseInt(args[1]);
      String remoteObjectName = args[2];

      RMIClientSocketFactory clientSocketFactory = new RMIClientSocketFactory() {
        public Socket createSocket(String host, int port) throws IOException {
          Socket socket = new Socket();
          socket.connect(new InetSocketAddress(host, port), 5000);
          return socket;
        }

        public ServerSocket createServerSocket(int port) throws IOException {
          return new ServerSocket(port);
        }
      };

      clientSocketFactory.createSocket(host, port);

      Random rand = new Random();
      int sum = rand.nextInt(4);
      Registry registry = LocateRegistry.getRegistry(host, port + sum, clientSocketFactory);
      IKeyValueStore remoteObject = (IKeyValueStore) registry.lookup(remoteObjectName);

      // pre populating data
      prePopulate(remoteObject);

      while (true) {
        try {
          Scanner sc = new Scanner(System.in);
          System.out.println(getDateCurrentTime() + " - Enter GET(to retrieve value for a key), PUT(insert a key value pair), DELETE(to remove a key) or EXIT(to exit): ");
          String oper = sc.nextLine();
          sum = rand.nextInt(4);
          registry = LocateRegistry.getRegistry(host, port + sum, clientSocketFactory);
          System.out.println("Server port - " + (port + sum));
          remoteObject = (IKeyValueStore) registry.lookup(remoteObjectName);
          if (oper.equalsIgnoreCase("EXIT"))
            break;
          else if (oper.startsWith("PUT ") || oper.startsWith("GET ") || oper.startsWith("DELETE ")) {
            handleRequest(oper, remoteObject);
          }
        } catch (RemoteException e) {
          System.out.println(getDateCurrentTime() + " - RemoteException occurred");
        } catch (ServerNotActiveException se) {
          System.out.println(getDateCurrentTime() + " - ServerNotActiveException occurred");
        } catch (Exception e) {
          System.out.println(getDateCurrentTime() + " - Exception occurred: " + e.getMessage());
        }
      }
    } catch (RemoteException e) {
      System.out.println(getDateCurrentTime() + " - RemoteException occurred during registry");
    } catch (Exception e) {
      System.out.println(getDateCurrentTime() + " - Exception occurred : " + e.getMessage());
    }
  }

  public static void prePopulate(IKeyValueStore remoteObject) throws
          RemoteException {
    try {
      for (int i = 101; i <= 107; i++) {
        System.out.println(getDateCurrentTime() + " Starting pre populating data");
        handleRequest("PUT key" + i + " value" + i+1, remoteObject);
        System.out.println(getDateCurrentTime() + " Pre-Population of key values completed!");
      }


      for (int i = 101; i <= 107; i++) {
        System.out.println(getDateCurrentTime() + " GET Operation");
        handleRequest("GET key" + i, remoteObject);
        System.out.println(getDateCurrentTime() + " GET Operation completed successfully");
      }


      for (int i = 101; i <= 107; i++) {
        System.out.println(getDateCurrentTime() + " DELETE Operation");
        handleRequest("DELETE key" + i, remoteObject);
        System.out.println(getDateCurrentTime() + " DELETE Operation completed successfully");
      }

      for (int i = 101; i <= 107; i++) {
        System.out.println(getDateCurrentTime() + " GET Operation");
        handleRequest("GET key" + i, remoteObject);
        System.out.println(getDateCurrentTime() + " GET Operation completed successfully");
      }

    }
    catch(Exception e)
    {
      System.out.println("Error occurred in pre populate data");
    }
  }

  /**
   * This method helps in handling specified operation on remote object.
   *
   * @param oper   operation to be performed
   * @param remoteObject remoteObject remote KeyValueStore object
   * @throws ServerNotActiveException exception occurs when server is not active
   * @throws RemoteException         exception occurs when remote communication error occurs
   * @throws InterruptedException    exception occurs when operation is interrupted
   */


  public static void handleRequest(String oper, IKeyValueStore remoteObject)
          throws ServerNotActiveException, RemoteException, InterruptedException {
    System.out.println(getDateCurrentTime() + " Received operation -> " + oper);
    requestProcessing response = requestProcessing(oper, remoteObject);
    String responseData;
    if (!response.status) {
      System.out.println(getDateCurrentTime() + " : Received incorrect request length " + oper.length());
      responseData = response.message;
    } else {
      responseData = response.value;
    }
    System.out.println(getDateCurrentTime() + " Response from server: " + responseData);
  }

  /**
   * This method helps in processing the request on the remote KeyValueStore object
   *
   * @param request request requested data
   * @param remoteObject remoteObject remote KeyValueStore object
   * @return The requestProcess response
   * @throws RemoteException      exception occurs when remote communication error occurs
   * @throws InterruptedException exception occurs when operation is interrupted
   */

  public static requestProcessing requestProcessing(String request, IKeyValueStore remoteObject)
          throws RemoteException, InterruptedException {
    if (request.startsWith("PUT")) {
      String[] requestSplit = request.split(" ");
      if (requestSplit.length == 3) {
        String key = requestSplit[1];
        String value = requestSplit[2];
        remoteObject.put(key, value);
        return new requestProcessing(true, "PUT process successful", "Key:" + key + " added with the Value:" + value);
      } else {
        return new requestProcessing(false, "PUT operation failed due to incorrect input", "");
      }
    }

    if (request.startsWith("GET")) {
      String[] requestSplit = request.split(" ");
      if (requestSplit.length == 2) {
        String key = requestSplit[1];
        if (remoteObject.containsKey(key)) {
          String value = remoteObject.get(key);
          return new requestProcessing(true, "GET operation successful!", "Value returned for the given Key is : " + value);
        } else {
          return new requestProcessing(false, "Key not found in key store", "");
        }
      } else {
        return new requestProcessing(false, "GET operation failed due to incorrect input", "");
      }
    }

    if (request.startsWith("DELETE")) {
      String[] requestSplit = request.split(" ");
      if (requestSplit.length == 2) {
        String key = requestSplit[1];
        remoteObject.delete(key);
        return new requestProcessing(true, "DELETE operation successful!", "Value deleted for Key:" + key);
      } else {
        return new requestProcessing(false, "DELETE operation failed due to incorrect input", "");
      }
    }
    return new requestProcessing(false, "Operation failed due to incorrect input", "");
  }

  /**
   * This method helps in getting current timestamp in the specified format
   *
   * @return The timestamp in specific format
   */
  public static String getDateCurrentTime() {
    return "[Time: " + dateFormat.format(new Date()) + "]";
  }
}

