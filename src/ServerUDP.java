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

/**
 * ServerUDP listens to incoming UDP packets, process client requests and send back responses.
 */
public class ServerUDP {
  static private InputStream read;
  static private OutputStream write;
  static Properties properties;


  /**
   * The main function is the starting point of the ServerUDP program, whenever a
   * UDP Server is executed this ,method is the first method to be called.
   *
   * @param args command-line argument
   * @throws Exception handles error during execution
   */
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


  /**
   * Method to print Request Message.
   *
   * @param s  message string
   * @param ip   Client IP Address
   * @param port Client Port Number
   */
  private static void requestTrack(String s, String ip, String port) {
    System.out.println(getCurrentTime() + " Request from: " + ip + ":" + port  + " -> "+ s);
  }


  /**
   * Method to print Response Message.
   *
   * @param s message string
   */
  private static void responseTrack(String s) { System.out.println(getCurrentTime() +
      " Response -> " + s + "\n");}


  /**
   * Method to print Response Message.
   *
   * @param error message string
   */
  private static void errorTrack(String error) { System.out.println(getCurrentTime() +
      " Error -> " + error);}


  /**
   * Method to return current date and timestamp.
   *
   * @return current date and timestamp
   */
  private static String getCurrentTime() {
    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MM-dd-yyyy HH:mm:ss.SSS");
    return "<Time: " + simpleDateFormat.format(new Date()) + ">";
  }


  /**
   * Method to validate and perform user requests.
   *
   * @param inputStr user request
   * @return the PUT/GET/DELETE operation
   * @throws IllegalArgumentException if invalid input is given
   */
  private static String getOperation(String[] inputStr) throws IllegalArgumentException {
    try {
      String operation = inputStr[0].toUpperCase();
      String key = "";
      int j = 0;
      for(int i = 1; i < inputStr.length; i++) {
        if(Objects.equals(inputStr[i], ",")) {
          j = i;
          break;
        }
        else key = key + inputStr[i] + " ";
      }
      key = key.trim();

      switch (operation) {
        case "PUT": {
          String value = "";
          for(int i = j+1; i < inputStr.length; i++) value = value + " " + inputStr[i].trim();
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

  /**
   * Method to add key-value pair in the properties file.
   *
   * @param key   the key to be added.
   * @param value the corresponding value associated to that key.
   * @return Success Message
   * @throws Exception error during execution
   */
  static String addToKeyValues(String key, String value) throws Exception {
    properties.setProperty(key, value);
    properties.store(write, null);
    String result = "Inserted key \"" + key + "\" with value \"" + value + "\"";
    return result;
  }


  /**
   * Method to delete a key-value pair from properties file.
   *
   * @param key the key to be deleted
   * @return success or key not found message
   * @throws IOException if an error appears
   */
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

  /**
   * Method to get the value associated with the provided key from the keyValues.
   *
   * @param key the key for which corresponding value need to be fetched.
   * @return the value corresponding to the key or message if the key is not found
   * @throws IOException error occur during the operation
   */

  private static String getFromKeyValues(String key) throws IOException {
    String value = properties.getProperty(key);
    String result = value == null ?
        "No value found for key \"" + key + "\"" : "Key: \"" + key + "\" ,Value: \"" + value + "\"";
    return result;
  }

}