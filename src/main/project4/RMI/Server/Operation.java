package main.project4.RMI.Server;

/**
 * The Operation class includes an operation in the Paxos consensus algorithm.
 */
class Operation {

  String type;
  String key;
  String value;

  /**
   * This method constructs an Operation object with the specified key, value and type
   *
   * @param type  type of operation
   * @param key   key associated with operation
   * @param value value for the key
   */
  Operation(String type, String key, String value) {
    this.type = type;
    this.key = key;
    this.value = value;
  }
}
