import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Objects;
import java.util.Properties;
import java.util.Scanner;


public class ServerUDP {
  static private InputStream read;
  static private OutputStream write;
  static Properties properties;


  public static void main(String[] args) throws Exception {

    System.out.print("Enter port Number: ");
    Scanner port = new Scanner(System.in);
    int PORT = port.nextInt();
    if ( PORT > 65535) {
      throw new IllegalArgumentException("Invalid IP and port number provided. " +
              "Please provide valid IP and port number and restart.");
    }

    try (DatagramSocket datagramSocket = new DatagramSocket(PORT)){

      String currentTimeDisplay = getCurrentTime();
      System.out.println(currentTimeDisplay + " Server started on port: " + PORT);
      byte[] reqBuffer = new byte[512];
      byte[] resBuffer;

      read = new FileInputStream("key-value.properties");
      properties = new Properties();
      properties.load(read);
      write = new FileOutputStream("key-value.properties");
      properties.store(write, null);


      while (true) {
        DatagramPacket receivePacket = new DatagramPacket(reqBuffer, reqBuffer.length);
        datagramSocket.receive(receivePacket);
        InetAddress client = receivePacket.getAddress();
        int clientPort = receivePacket.getPort();
        String req = new String(receivePacket.getData(), receivePacket.getOffset(), receivePacket.getLength());
        requestTrack(req, client.toString(), String.valueOf(clientPort));

        // Validate packet size
        if (receivePacket.getLength() > 512) {
          errorTrack("Received packet exceeds maximum allowed size.");
          continue;
        }

        try {
          String[] input = req.split(" ");
          if (input.length < 2) {
            throw new IllegalArgumentException("Malformed request!");
          }
          String result = getOperation(input);
          responseTrack(result);
          resBuffer = result.getBytes();

        } catch (IllegalArgumentException e) {
          String errorMessage = e.getMessage();
          errorTrack(errorMessage);
          resBuffer = errorMessage.getBytes();
        }

        DatagramPacket responsePacket = new DatagramPacket(resBuffer, resBuffer.length,
            client, clientPort);
        datagramSocket.send(responsePacket);
        reqBuffer = new byte[512];

      }
    } catch (Exception e) {
      errorTrack("Error! Please make sure IP and Port are valid and try again.");
    }
  }


  private static void requestTrack(String str, String ip, String port) {
    System.out.println(getCurrentTime() + " Request from: " + ip + ":" + port  + " -> "+ str);
  }


  private static void responseTrack(String str) { System.out.println(getCurrentTime() +
      " Response -> " + str + "\n");}


  private static void errorTrack(String err) { System.out.println(getCurrentTime() +
      " Error -> " + err);}


  private static String getCurrentTime() {
    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MM-dd-yyyy HH:mm:ss.SSS");
    return "<Time: " + simpleDateFormat.format(new Date()) + ">";
  }


  private static String getOperation(String[] input) throws IllegalArgumentException {
    try {
      String operation = input[0].toUpperCase();
      String key = "";
      int j = 0;
      for(int i = 1; i < input.length; i++) {
        if(Objects.equals(input[i], ",")) {
          j = i;
          break;
        }
        else key = key + input[i] + " ";
      }
      key = key.trim();

      switch (operation) {
        case "PUT": {
          String value = "";
          for(int i = j+1; i < input.length; i++) value = value + " " + input[i].trim();
          value = value.trim();
          return addToKeyValues(key, value);
        }
        case "DELETE": {
          return deleteFromKeyValues(key);
        }
        case "GET": {
          return getFromKeyValues(key);
        }
        default:
          throw new IllegalArgumentException();
      }
    } catch (Exception e) {
      return "BAD REQUEST!! Please provide only from the available operations" + e;
    }

  }


  static String addToKeyValues(String key, String value) throws Exception {
    properties.setProperty(key, value);
    properties.store(write, null);
    String result = "Inserted key \"" + key + "\" with value \"" + value + "\"";
    return result;
  }


  private static String deleteFromKeyValues(String key) throws IOException {
    String result = "";
    if(properties.containsKey(key)) {
      properties.remove(key);
      properties.store(write, null);
      result = "Deleted key \"" + key + "\"" + " successfully!";
    }
    else {
      result = "Key not found.";
    }
    return result;
  }


  private static String getFromKeyValues(String key) throws IOException {
    String value = properties.getProperty(key);
    String result = value == null ?
        "No value found for key \"" + key + "\"" : "Key: \"" + key + "\" ,Value: \"" + value + "\"";
    return result;
  }

}