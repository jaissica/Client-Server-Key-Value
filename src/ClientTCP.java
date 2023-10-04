import java.net.*;
import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Objects;


/**
 * ClientTCP helps in sending requests to a TCP server, receiving responses and dealing with exceptions.
 */

class ClientTCP {
  static private String key;
  static private String val;
  static private String req;
  static private BufferedReader bufferedReader;

  /**
   * The main functions is the starting of the ClientTCP. Whenever a client uses
   * TCP Client they are navigated through this main function
   *
   * @param args command-line arguments: Host Name & Port Number
   * @throws Exception error during execution.
   */

  public static void main(String[] args) throws Exception {
    if (args.length != 2 || Integer.parseInt(args[1]) > 65535) {
      throw new IllegalArgumentException("Invalid IP and port number provided." +
              "Please provide valid IP and port number and restart.");
    }
    InetAddress serIP = InetAddress.getByName(args[0]);
    int serPort = Integer.parseInt(args[1]);

    try (Socket s = new Socket()) {
      int timeOut = 10000;
      s.connect(new InetSocketAddress(serIP, serPort), timeOut);
      DataInputStream dataInput = new DataInputStream(s.getInputStream());
      DataOutputStream dataOutput = new DataOutputStream(s.getOutputStream());
      bufferedReader = new BufferedReader(new InputStreamReader(System.in));
      String currentTimeDisplay = getCurrentTime();
      System.out.println(currentTimeDisplay + " Client has started on port: " + s.getPort());
      prePopulatedData(dataInput, dataOutput);
      while (true) {
        printOptions();
        String operation = bufferedReader.readLine().trim();
        if (!(Objects.equals(operation, "1") || Objects.equals(operation, "2")
                || Objects.equals(operation, "3")))
        {
          System.out.println("Invalid operation input, please reenter");
          continue;
        }
        req = validateOptions(operation);
        sendPacket(dataOutput, req);
        String response = receivePacket(dataInput);
        if (response.startsWith("ERROR")) {
          System.out.println("Received error from the server: " + response);
        } else {
          responseTrack(response);
        }
      }
    } catch (UnknownHostException | SocketException e) {
      System.out.println("Host/Port number is not known. Kindly provide a valid host name and port number!");
    } catch (SocketTimeoutException e) {
      System.out.println("Connection has timed out.");
    } catch (Exception e) {
      System.out.println("Exception encountered" + e);
    }
  }

  /**
   * Method which helps to pre-populate few values.
   *
   * @param dataInput Data input stream
   * @param dataOutput Data output stream
   * @throws Exception if error occurs while pre-populating the data
   */
  private static void prePopulatedData(DataInputStream dataInput, DataOutputStream dataOutput)
          throws Exception{
    System.out.println("Pre-populating with the static key-value store...");
    System.out.println(" ");
    // Case1
    req = "PUT " + 1 + " " + 2;
    sendPacket(dataOutput, req);
    String response1 = receivePacket(dataInput);
    if (response1.startsWith("ERROR")) {
      System.out.println("Received error response from the server: " + response1);
    } else {
      responseTrack(response1);
    }
    //case2
    req = "PUT " + 3 + " " + 4;
    sendPacket(dataOutput, req);
    response1 = receivePacket(dataInput);
    if (response1.startsWith("ERROR")) {
      System.out.println("Received error response from the server: " + response1);
    } else {
      responseTrack(response1);
    }
    //case3
    req = "PUT " + 6 + " " + 5;
    sendPacket(dataOutput, req);
    response1 = receivePacket(dataInput);
    if (response1.startsWith("ERROR")) {
      System.out.println("Received error response from the server: " + response1);
    } else {
      responseTrack(response1);
    }
    //case4
    req = "PUT " + 7 + " " + 8;
    sendPacket(dataOutput, req);
    response1 = receivePacket(dataInput);
    if (response1.startsWith("ERROR")) {
      System.out.println("Received error response from the server: " + response1);
    } else {
      responseTrack(response1);
    }

    //case5
    req = "PUT " + 9 + " " + 10;
    sendPacket(dataOutput, req);
    response1 = receivePacket(dataInput);
    if (response1.startsWith("ERROR")) {
      System.out.println("Received error response from the server: " + response1);
    } else {
      responseTrack(response1);
    }
  }

  /**
   * Method which helps to validate the input of operations given by the Client.
   * @param operation : Operation provided by the user.
   * @return req: Request Message
   * @throws Exception if error occurs by Client while giving input.
   */
  private static String validateOptions(String operation) throws Exception
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
  private static String putOperation() throws Exception {
    getKey();
    getValue();
    req = "PUT " + key + " " + val;
    return req;
  }

  /**
   * Method creates request for a GET Operation for further processing.
   * @return req: Request Message
   * @throws Exception if error occurs
   */
  private static String getOperation() throws Exception
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
  private static String deleteOperation() throws Exception
  {
    getKey();
    req = "DELETE " + key;
    return req;
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
    key = bufferedReader.readLine();
  }

  /**
   * Method to get the value for a particular key which is input from the user.
   *
   * @throws IOException if an error occurs while taking input from the user.
   */

  private static void getValue() throws IOException {
    System.out.print("Enter Value: ");
    val = bufferedReader.readLine();
  }

  /**
   * Method to send a packet to the server.
   *
   * @param outputStream output stream to write the packet
   * @param packet       packet to send
   * @throws IOException if an error occurs
   */
  private static void sendPacket(DataOutputStream outputStream, String packet) throws IOException {
    outputStream.writeUTF(packet);
    outputStream.flush();
    requestTrack(packet);
  }

  /**
   * Method to receive a packet from the server.
   *
   * @param inputStream  input stream to read the packet
   * @return  received packet
   * @throws IOException if an error occurs
   */

  private static String receivePacket(DataInputStream inputStream) throws IOException {
    return inputStream.readUTF();
  }

  /**
   * Method to print Request messages.
   *
   * @param s message string
   */
  private static void requestTrack(String s) {
    System.out.println(getCurrentTime() + " Request: " + s);
  }

  /**
   * Method to print Response messages.
   *
   * @param s message string
   */
  private static void responseTrack(String s) {
    System.out.println(getCurrentTime() + " Response: " + s + "\n");
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
