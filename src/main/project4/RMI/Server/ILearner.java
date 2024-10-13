package main.project4.RMI.Server;
import java.rmi.RemoteException;
import java.rmi.Remote;


/**
 * ILearner interface defines the methods for learners in the Paxos consensus algorithm,
 * allowing to learn accepted values for proposals.
 */
public interface ILearner extends Remote {

  /**
   * This method helps in notifying the learner
   *
   * @param proposalId     unique identifier of the proposal
   * @param acceptedValue  accepted value for the proposal
   * @throws RemoteException remote communication error occurs
   */
  void learn(String proposalId, Operation acceptedValue) throws RemoteException;
}
