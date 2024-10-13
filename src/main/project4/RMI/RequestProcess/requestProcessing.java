package main.project4.RMI.RequestProcess;

/**
 * The requestProcessing class represents a response to a request made to the RMI server
 */
public class requestProcessing {

  /**
   * This method helps in creating a requestProcess object
   *
   * @param status  status of the request
   * @param message message providing additional information
   * @param value   value associated with the request
   */
  public requestProcessing(Boolean status, String message, String value) {
    this.status = status;
    this.message = message;
    this.value = value;
  }

  public Boolean status;
  public String message;
  public String value;
}
