import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Properties;
import java.util.Scanner;

/**
 * ServerTCP listens to TCP connections, processes client requests and sends back responses.
 */
public class ServerTCP {

  static private InputStream read;
  static private OutputStream write;
  static Properties properties;


  /**
   * The main function is the starting point of the ServerTCP program, whenever a
   * TCP Server is executed this ,method is the first method to be called..
   *
   * @param args command-line argument
   * @throws Exception handles error during execution
   */
  public static void main(String[] args) throws Exception {

    System.out.println("Enter port Number: ");
    Scanner port = new Scanner(System.in);
    int PORT = port.nextInt();
    if (PORT > 65535) {
      throw new IllegalArgumentException("Invalid IP and port number provided. " +
              "Please provide valid IP and port number and restart.");
    }
    try (ServerSocket serverSocket = new ServerSocket(PORT)) {
      String currentTimeDisplay = getCurrentTime();
      System.out.println(currentTimeDisplay + " Server started on port: " + PORT);
      Socket clientSocket = serverSocket.accept();
      DataInputStream dataInput = new DataInputStream(clientSocket.getInputStream());
      DataOutputStream dataOutput = new DataOutputStream(clientSocket.getOutputStream());

      read = new FileInputStream("key-value.properties");
      properties = new Properties();
      properties.load(read);

      write = new FileOutputStream("key-value.properties");
      properties.store(write, null);



      while (true) {
        String input = dataInput.readUTF();
        requestTrack(input, String.valueOf(clientSocket.getInetAddress()),
            String.valueOf(clientSocket.getPort()));
        String result = getOperation(input.split(" "));
        responseTrack(result);
        dataOutput.writeUTF(result);
        dataOutput.flush();
      }
    } catch (Exception e) {
      System.out.println(getCurrentTime() + " Error: " + e);
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
    System.out.println(getCurrentTime() + " Request from: " + ip + ":" + port + " -> " + s);
  }


  /**
   * Method to print Response Message.
   *
   * @param s message string
   */
  private static void responseTrack(String s) {
    System.out.println(getCurrentTime() + " Response:  "
        + s + "\n");
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
      if (inputStr.length < 2) {
        throw new IllegalArgumentException("Invalid input: Insufficient arguments.");
      }
      String operation = inputStr[0].toUpperCase();
      String key = inputStr[1];

      switch (operation) {
        case "PUT": {
          if (inputStr.length < 3) {
            throw new IllegalArgumentException(
                "Invalid input: Value is missing for PUT operation!");
          }
          String value = inputStr[2];
          return addToKeyValue(key, value);
        }
        case "DELETE": {
          return deleteFromKeyValue(key);
        }
        case "GET": {
          return getFromKeyValue(key);
        }
        default:
          throw new IllegalArgumentException("Invalid operation requested: " + operation);
      }
    } catch (IllegalArgumentException e) {
      return "Bad Request: " + e.getMessage();
    } catch (Exception e) {
      return "Error: " + e.getMessage();
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

  static String addToKeyValue(String key, String value) throws Exception {
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
  private static String deleteFromKeyValue(String key) throws IOException {
    String result = "";
    if (properties.containsKey(key)) {
      properties.remove(key);
      properties.store(write, null);
      result = "Deleted key \"" + key + "\"" + " successfully!";
    } else {
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
  private static String getFromKeyValue(String key) throws IOException {
    try {
      String value = properties.getProperty(key);
      String result = value == null ?
          "No value found for key \"" + key + "\""
          : "Key: \"" + key + "\" ,Value: \"" + value + "\"";
      return result;
    } catch (Exception e) {
      throw new IOException("Error: " + e);
    }
  }

  /**
   * Method to return current date and timestamp.
   *
   * @return current date and timestamp
   */
  private static String getCurrentTime() {
    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MM-dd-yyyy HH:mm:ss.SSS");
    return "<Time: " + simpleDateFormat.format(new Date()) + ">";
  }

}