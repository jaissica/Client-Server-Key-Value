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


public class ServerTCP {

  static private InputStream read;
  static private OutputStream write;
  static Properties properties;


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


  private static void requestTrack(String str, String ip, String port) {
    System.out.println(getCurrentTime() + " Request from: " + ip + ":" + port + " -> " + str);
  }


  private static void responseTrack(String str) {
    System.out.println(getCurrentTime() + " Response:  "
        + str + "\n");
  }



  private static String getOperation(String[] input) throws IllegalArgumentException {
    try {
      if (input.length < 2) {
        throw new IllegalArgumentException("Invalid input: Insufficient arguments.");
      }
      String operation = input[0].toUpperCase();
      String key = input[1];

      switch (operation) {
        case "PUT": {
          if (input.length < 3) {
            throw new IllegalArgumentException(
                "Invalid input: Value is missing for PUT operation!");
          }
          String value = input[2];
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



  static String addToKeyValue(String key, String value) throws Exception {
    properties.setProperty(key, value);
    properties.store(write, null);
    String result = "Inserted key \"" + key + "\" with value \"" + value + "\"";
    return result;
  }


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


  private static String getCurrentTime() {
    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MM-dd-yyyy HH:mm:ss.SSS");
    return "<Time: " + simpleDateFormat.format(new Date()) + ">";
  }

}