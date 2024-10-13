package main.project4.RMI.Server;
import java.rmi.RemoteException;
import java.rmi.Remote;



/**
 * IKeyValueStore interface defines the methods for interacting with a key value store using RMI
 */
public interface IKeyValueStore extends Remote {

  /**
   * This method puts key value pair into the store
   *
   * @param key   key to be inserted
   * @param value value associated with the key
   * @return response after performing PUT operation
   * @throws RemoteException remote communication error occurs
   * @throws InterruptedException if operation is interrupted
   */
  String put(String key, String value) throws RemoteException, InterruptedException;

  /**
   * This method helps in retrieving the value associated with a given key
   *
   * @param key key
   * @return value associated with the key
   * @throws RemoteException      remote communication error occurs
   * @throws InterruptedException if operation is interrupted
   */
  String get(String key) throws RemoteException, InterruptedException;

  /**
   * This method helps in deleting key value pair
   *
   * @param key to be deleted
   * @return success or failure message
   * @throws RemoteException      remote communication error occurs
   * @throws InterruptedException if operation is interrupted
   */
  String delete(String key) throws RemoteException, InterruptedException;

  /**
   * This method helps in checking if a specific key is available
   *
   * @param key to check
   * @return {true} if the store contains the key else {false}
   * @throws RemoteException      remote communication error occurs
   * @throws InterruptedException if operation is interrupted
   */
  Boolean containsKey(String key) throws RemoteException, InterruptedException;
}
