import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Objects;
import java.util.Scanner;


/**
 * ClientUPD helps in sending requests to UDP server, receives responses and handle exceptions.
 */
public class ClientUDP {

  static private String key;
  static private String value;
  static private String req;
  static Scanner scanner;

  /**
   * The main functions is the starting of the ClientUDP. Whenever a client uses
   * UDP Client they are navigated through this main function
   *
   * @param args command-line arguments: Host Name & Port Number
   * @throws IOException error during execution.
   */

  public static void main(String[] args) throws IOException {
    if (args.length != 2 || Integer.parseInt(args[1]) > 65535) {
      throw new IllegalArgumentException("Invalid IP and port number provided. " +
              "Please provide valid IP and port number and restart.");
    }
    InetAddress servIP = InetAddress.getByName(args[0]);
    int serPort = Integer.parseInt(args[1]);
    scanner = new Scanner(System.in);

    try (DatagramSocket datagramSocket = new DatagramSocket()) {
      datagramSocket.setSoTimeout(10000);
      String currentTimeDisplay = getCurrentTime();
      System.out.println(currentTimeDisplay + " Client started");
      prePopulatedData(servIP, serPort, datagramSocket);

      while (true) {
        printOptions();
        String operation = scanner.nextLine().trim();
        if (!(Objects.equals(operation, "1") || Objects.equals(operation, "2")
                || Objects.equals(operation, "3")))
        {
          System.out.println("Invalid operation input, please reenter");
          continue;
        }
        req = validateOptions(operation);
        requestTrack(req);
        byte[] reqBuffer = req.getBytes();
        if (reqBuffer.length > 65507) {
          System.out.println("Error: Request size exceeds the permissible limit.");
          continue;
        }
        DatagramPacket requestPacket = new DatagramPacket(reqBuffer, reqBuffer.length,
                servIP, serPort);
        datagramSocket.send(requestPacket);

        byte[] resultBuffer = new byte[512];
        DatagramPacket resultPacket = new DatagramPacket(resultBuffer, resultBuffer.length);

        try {
          datagramSocket.receive(resultPacket);
          String result = new String(resultBuffer);
          responseTrack(result);
        } catch (java.net.SocketTimeoutException e) {
          System.out.println("Timeout encountered! ");
        }
      }
    } catch (UnknownHostException | SocketException e) {
      System.out.println(
              "Host or Port number not known, kindly try again!");
    }
  }



  /**
   * Method which helps to validate the input of operations given by the Client.
   * @param operation : Operation provided by the user.
   * @return req: Request Message
   * @throws Exception if error occurs by Client while giving input.
   */
  private static String validateOptions(String operation) throws IOException
  {
    if (Objects.equals(operation, "1")) {
      return putOperation();
    } else if (Objects.equals(operation, "2")) {
      return getOperation();
    } else if (Objects.equals(operation, "3")) {
      return deleteOperation();
    }
    return "";
  }

  /**
   * Method creates request for a PUT Operation for further processing.
   * @return req: Request Message
   * @throws Exception if error occurs
   */
  private static String putOperation() throws IOException {
    getKey();
    getValue();
    req = "PUT " + key + " , " + value;
    return req;
  }

  /**
   * Method creates request for a GET Operation for further processing.
   * @return req: Request Message
   * @throws Exception if error occurs
   */

  private static String getOperation() throws IOException
  {
    getKey();
    req = "GET " + key;
    return req;
  }

  /**
   * Method creates request for a DELETE Operation for further processing.
   * @return req: Request Message
   * @throws Exception if error occurs
   */
  private static String deleteOperation() throws IOException
  {
    getKey();
    req = "DELETE " + key;
    return req;
  }

  /**
   * Method which helps to pre-populate few values.
   *
   * @param serverIP Ip address
   * @param serverPort Port number
   * @param datagramSocket Datagram Socket
   * @throws IOException if error occurs while pre-populating the data
   */
  private static void prePopulatedData(InetAddress serverIP, int serverPort, DatagramSocket datagramSocket )
          throws IOException
  {
    System.out.println("Pre-populating with the static key-value store");
    System.out.println(" ");
    //Case 1
    String req = "PUT " + "1" + " , " + "11";
    requestTrack(req);
    byte[] reqBuffer = req.getBytes();
    DatagramPacket reqPacket = new DatagramPacket(reqBuffer, reqBuffer.length,
            serverIP, serverPort);
    datagramSocket.send(reqPacket);
    byte[] resBuffer = new byte[512];
    DatagramPacket resultPacket = new DatagramPacket(resBuffer, resBuffer.length);

    try {
      datagramSocket.receive(resultPacket);
      String result = new String(resBuffer);
      responseTrack(result);
    } catch (java.net.SocketTimeoutException e) {
      System.out.println("Timeout occurred. " +
              "The server did not respond within the specified time.");
    }


    //case2
    req = "PUT " + "2" + " , " + "22";
    requestTrack(req);
    reqBuffer = req.getBytes();
    reqPacket = new DatagramPacket(reqBuffer, reqBuffer.length,
            serverIP, serverPort);
    datagramSocket.send(reqPacket);

    resBuffer = new byte[512];
    resultPacket = new DatagramPacket(resBuffer, resBuffer.length);

    try {
      datagramSocket.receive(resultPacket);
      String result = new String(resBuffer);
      responseTrack(result);
    } catch (java.net.SocketTimeoutException e) {
      System.out.println("Timeout encountered! ");
    }

    //case3
    req = "PUT " + "3" + " , " + "33";
    requestTrack(req);
    reqBuffer = req.getBytes();
    reqPacket = new DatagramPacket(reqBuffer, reqBuffer.length,
            serverIP, serverPort);
    datagramSocket.send(reqPacket);

    resBuffer = new byte[512];
    resultPacket = new DatagramPacket(resBuffer, resBuffer.length);

    try {
      datagramSocket.receive(resultPacket);
      String result = new String(resBuffer);
      responseTrack(result);
    } catch (java.net.SocketTimeoutException e) {
      System.out.println("Timeout encountered! ");
    }

    //case4
    req = "PUT " + "4" + " , " + "44";
    requestTrack(req);
    reqBuffer = req.getBytes();
    reqPacket = new DatagramPacket(reqBuffer, reqBuffer.length,
            serverIP, serverPort);
    datagramSocket.send(reqPacket);

    resBuffer = new byte[512];
    resultPacket = new DatagramPacket(resBuffer, resBuffer.length);

    try {
      datagramSocket.receive(resultPacket);
      String result = new String(resBuffer);
      responseTrack(result);
    } catch (java.net.SocketTimeoutException e) {
      System.out.println("Timeout encountered! ");
    }

    //case5
    req = "PUT " + "5" + " , " + "55";
    requestTrack(req);
    reqBuffer = req.getBytes();
    reqPacket = new DatagramPacket(reqBuffer, reqBuffer.length,
            serverIP, serverPort);
    datagramSocket.send(reqPacket);

    resBuffer = new byte[512];
    resultPacket = new DatagramPacket(resBuffer, resBuffer.length);

    try {
      datagramSocket.receive(resultPacket);
      String result = new String(resBuffer);
      responseTrack(result);
    } catch (java.net.SocketTimeoutException e) {
      System.out.println("Timeout encountered! ");
    }
  }

  /**
   * Method to print the available operations for the Client.
   */
  private static void printOptions()
  {
    System.out.println("Available Operations:");
    System.out.println("1. PUT-");
    System.out.println("2. GET-");
    System.out.println("3. DELETE-");
  }


  /**
   * Method to get the key from the user via input.
   *
   * @throws IOException if an error occurs while taking input from the user.
   */
  private static void getKey() throws IOException {
    System.out.print("Enter key: ");
    key = scanner.nextLine();
  }

  /**
   * Method to get the value for a particular key which is input from the user.
   *
   * @throws IOException if an error occurs while taking input from the user.
   */
  private static void getValue() throws IOException {
    System.out.print("Enter Value: ");
    value = scanner.nextLine();
  }

  /**
   * Method to print Request messages.
   *
   * @param s message string
   */
  private static void requestTrack(String s) {
    System.out.println(getCurrentTime() +
            " Request -> " + s);
  }

  /**
   * Method to print Response messages.
   *
   * @param s message string
   */
  private static void responseTrack(String s) {
    System.out.println(getCurrentTime() +
            " Response -> " + s + "\n");
  }


  /**
   * Method to track current date and timestamp.
   *
   */
  private static String getCurrentTime() {
    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MM-dd-yyyy HH:mm:ss.SSS");
    return "[Time: " + simpleDateFormat.format(new Date()) + "]";
  }
}


