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



public class ClientUDP {

  static private String key;
  static private String value;
  static private String req;
  static Scanner scanner;


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
      prePopulatedValues(servIP, serPort, datagramSocket);

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

  private static void prePopulatedValues(InetAddress serverIP, int serverPort, DatagramSocket datagramSocket )
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

  private static String putOperation() throws IOException {
    getKey();
    getValue();
    req = "PUT " + key + " , " + value;
    return req;
  }

  private static String getOperation() throws IOException
  {
    getKey();
    req = "GET " + key;
    return req;
  }

  private static String deleteOperation() throws IOException
  {
    getKey();
    req = "DELETE " + key;
    return req;
  }

  private static void printOptions()
  {
    System.out.println("Available Operations:");
    System.out.println("1. PUT-");
    System.out.println("2. GET-");
    System.out.println("3. DELETE-");
  }


  private static void getKey() throws IOException {
    System.out.print("Enter key: ");
    key = scanner.nextLine();
  }


  private static void getValue() throws IOException {
    System.out.print("Enter Value: ");
    value = scanner.nextLine();
  }


  private static void requestTrack(String str) {
    System.out.println(getCurrentTime() +
            " Request -> " + str);
  }


  private static void responseTrack(String str) {
    System.out.println(getCurrentTime() +
            " Response -> " + str + "\n");
  }


  private static String getCurrentTime() {
    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MM-dd-yyyy HH:mm:ss.SSS");
    return "[Time: " + simpleDateFormat.format(new Date()) + "]";
  }
}


