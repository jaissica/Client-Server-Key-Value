package main.project4.RMI.Server;
import java.util.concurrent.ConcurrentHashMap;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * Server class implements the Proposer, Acceptor, Learner, and KeyValueStore interfaces
 * and represents a server in the Paxos consensus algorithm.
 */
public class Server extends UnicastRemoteObject implements IProposer, IAcceptor, ILearner, IKeyValueStore {

   final ConcurrentHashMap<String, String> keyValueStore = new ConcurrentHashMap<>();
   final Map<String, Pair<String, Operation>> containsKey;
   IAcceptor[] acceptors;
   ILearner[] learners;

   boolean serverStat = false;
   long serverDownTime = 0;
   final int serverId;
  boolean isSuccess = false;
  int serverDownTimer = 100;
  double div = 2.0;
   final Map<String, Pair<Integer, Boolean>> pairMap;

   static final SimpleDateFormat dateFormat = new SimpleDateFormat("MM-dd-yyyy HH:mm:ss.SSS");

  /**
   * This method constructs a Server object with the given server ID
   *
   * @param serverId The ID of the server
   * @throws RemoteException remote communication error occurs
   */
  public Server(int serverId) throws RemoteException {
    this.serverId = serverId;
    this.containsKey = new HashMap<>();
    this.pairMap = new HashMap<>();
  }


  /**
   * This method sets the learners for this server
   *
   * @param learners array of learners to set
   * @throws RemoteException remote communication error occurs
   */
  public void setLearners(ILearner[] learners) throws RemoteException {
    this.learners = learners;
  }

  /**
   * This method sets the acceptors for this server
   *
   * @param acceptors array of acceptors to set
   * @throws RemoteException remote communication error occurs
   */
  public void setAcceptors(IAcceptor[] acceptors) throws RemoteException {
    this.acceptors = acceptors;
  }



  /**
   * This method retrieves the value associated with the given key
   *
   * @param key whose associated value is to be retrieved
   * @return value associated with the given key if it exists
   * @throws RemoteException remote communication error occurs
   */
  @Override
  public synchronized String get(String key) throws RemoteException {
    if (keyValueStore.containsKey(key))
      return keyValueStore.get(key);
    return "No entry is available for the key: " + key;
  }

  /**
   * This method deletes the value associated with the given key
   *
   * @param key The key whose associated value is to be deleted.
   * @return A status message indicating the result of the DELETE operation.
   * @throws RemoteException if a remote communication error occurs.
   * @throws InterruptedException if the thread is interrupted while waiting for the operation to complete.
   */
  @Override
  public synchronized String delete(String key) throws RemoteException, InterruptedException {
    isSuccess = false;
    proposeOperation(new Operation("DELETE", key, null));
    if (isSuccess)
      return "DELETE operation successfully completed for key: " + key;
    else
      return "Error occurred DELETE operation for key: " + key;
  }

  /**
   * This method checks if key is present
   *
   * @param key to check if key exists
   * @return {true} if the key is present else {false}
   * @throws RemoteException if a remote communication error occurs
   */
  @Override
  public Boolean containsKey(String key) throws RemoteException {
    return keyValueStore.containsKey(key);
  }

  /**
   * This method checks the status of the acceptor server to determine if it is currently down
   *
   * @return {true} if the server is considered down else {false}
   * @throws RemoteException if remote communication error occurs
   */
  public boolean checkAcceptorStatus() throws RemoteException {
    if (serverStat) {
      long currentTime = System.currentTimeMillis() / 1000L;
      if (this.serverDownTime + serverDownTimer <= currentTime) {
        serverStat = false;
        return false;
      }
      return true;
    }
    return false;
  }

  /**
   * This method initiates the proposal of an operation by generating a proposal ID
   *
   * @param operation to be proposed for acceptance
   * @throws RemoteException remote communication error occurs
   * @throws InterruptedException if the current thread is interrupted while waiting
   */
  public void proposeOperation(Operation operation) throws RemoteException, InterruptedException {
    String proposalId = createProposalId();
    propose(proposalId, operation);
  }

  /**
   * This method prepares to accept a proposed operation value by checking necessary condition
   *
   * @param proposalId     unique ID of the proposal
   * @param operation      proposed operation to be prepared for acceptance
   * @return {true} if prepare conditions are met else {false}
   * @throws RemoteException remote communication error occurs.
   */
  @Override
  public synchronized Boolean prepare(String proposalId, Operation operation) throws RemoteException {
    if (checkAcceptorStatus()) {
      return null;
    }
    if (this.containsKey.containsKey(operation.key)) {
      if (Long.parseLong(this.containsKey.get(operation.key).getKey().split(":")[1]) >
          Long.parseLong(proposalId.split(":")[1])) {
        return false;
      }
    }
    this.containsKey.put(operation.key, new Pair<>(proposalId, operation));
    return true;
  }

  /**
   * This method puts a new key-value pair or updates the value of an existing key
   *
   * @param key key to be inserted or updated
   * @param value value to be associated with the key
   * @return success or failure response message
   * @throws RemoteException if a remote communication error occurs
   * @throws InterruptedException if the operation is interrupted
   */
  @Override
  public synchronized String put(String key, String value) throws RemoteException, InterruptedException {
    isSuccess = false;
    proposeOperation(new Operation("PUT", key, value));
    if (isSuccess)
      return "PUT operation successfully completed for key: " + key + " with value: " + value;
    else
      return "Error occurred during PUT operation for key: " + key;
  }

  /**
   * This method accepts a proposed operation value after ensuring proper conditions are met.
   *
   * @param proposalId     unique ID of the proposal
   * @param proposalValue  proposed operation value
   * @throws RemoteException if a remote communication error occurs
   */
  @Override
  public synchronized void accept(String proposalId, Operation proposalValue) throws RemoteException {
    if (checkAcceptorStatus()) {
      return;
    }

    if (this.containsKey.containsKey(proposalValue.key)) {
      if (Long.parseLong(this.containsKey.get(proposalValue.key).getKey().split(":")[1]) <=
          Long.parseLong(proposalId.split(":")[1])) {
        for (ILearner ILearner : this.learners) {
          ILearner.learn(proposalId, proposalValue);
        }
      }
    }
  }

  /**
   * This method handles the learning phase of the Paxos protocol upon receiving an accepted value.
   *
   * @param proposalId     unique ID of the proposal
   * @param acceptedValue  accepted operation value
   * @throws RemoteException remote communication error occurs
   */
  @Override
  public synchronized void learn(String proposalId, Operation acceptedValue) throws RemoteException {
    if (!this.pairMap.containsKey(proposalId)) {
      this.pairMap.put(proposalId, new Pair<>(1, false));
    } else {
      Pair<Integer, Boolean> learnerPair = this.pairMap.get(proposalId);
      learnerPair.setKey(learnerPair.getKey() + 1);
      if (learnerPair.getKey() >= Math.ceil(acceptors.length / div) && !learnerPair.getValue()) {
        this.isSuccess = executeOperation(acceptedValue);
        learnerPair.setValue(true);
      }
      this.pairMap.put(proposalId, learnerPair);
    }
  }

  /**
   * This method proposes an operation to be accepted through the Paxos consensus protocol
   *
   * @param proposalId     unique ID of the proposal.
   * @param proposalValue  proposed operation value.
   * @throws RemoteException remote communication error occurs.
   */
  @Override
  public synchronized void propose(String proposalId, Operation proposalValue) throws RemoteException {
    List<Boolean> prepareResponse = new ArrayList<>();
    for (IAcceptor acceptor : this.acceptors) {
      Boolean res = acceptor.prepare(proposalId, proposalValue);
      prepareResponse.add(res);
    }
    int majority = 0;

    for (int i = 0; i < 5; i++) {
      if (prepareResponse.get(i) != null) {
        if (prepareResponse.get(i))
          majority += 1;
      }
    }

    if (majority >= Math.ceil(acceptors.length / div)) {
      for (int i = 0; i < 5; i++) {
        if (prepareResponse.get(i) != null)
          this.acceptors[i].accept(proposalId, proposalValue);
      }
    }
  }



  /**
   * This method sets the server status to "down" and records the timestamp
   */
  public void setServerDown() {
    this.serverStat = true;
    this.serverDownTime = System.currentTimeMillis() / 1000L;
  }

  /**
   * This method creates a unique proposal ID joining the server's ID and the current timestamp
   *
   * @return unique proposal ID in the format: serverId:timestamp
   * @throws RemoteException remote communication error occurs
   */
  public String createProposalId() throws RemoteException {
    return serverId + ":" + System.currentTimeMillis();
  }

  /**
   * This method helps in getting the current time in a formatted string
   *
   * @return current time string
   */
  public String getCurrentDateTime() {
    return "<Time: " + dateFormat.format(new Date()) + ">";
  }

  /**
   * This method executes the specified operation
   *
   * @param operation operation to be executed
   * @return {True} if operation was successfully executed else {false}
   * @throws RemoteException remote communication error occurs
   */

  public boolean executeOperation(Operation operation) throws RemoteException {
    if (operation == null) return false;
    switch (operation.type) {
      case "PUT":
        keyValueStore.put(operation.key, operation.value);
        System.out.println(getCurrentDateTime()+ " PUT operation successfully completed for key: " + operation.key + " and value: " + operation.value);
        return true;
      case "DELETE":
        if (keyValueStore.containsKey(operation.key)) {
          keyValueStore.remove(operation.key);
          System.out.println(getCurrentDateTime() + " - DELETE operation successfully completed for key: " + operation.key);
          return true;
        } else {
          System.out.println(getCurrentDateTime() + " - DELETE operation is not possible as key does not exist for key: " + operation.key);
          return false;
        }
      default:
        throw new IllegalArgumentException("Invalid operation type: " + operation.type);
    }
  }




}
