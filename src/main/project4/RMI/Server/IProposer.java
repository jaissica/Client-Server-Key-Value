package main.project4.RMI.Server;
import java.rmi.RemoteException;
import java.rmi.Remote;


/**
 * IProposer interface represents a proposer in the Paxos consensus algorithm
 * Proposers are responsible for initiating the proposal phase and proposing values
 */
public interface IProposer extends Remote {

  /**
   * This method proposes a value with a given proposal ID to the acceptors
   *
   * @param proposalId     unique ID for the proposal
   * @param proposalValue  value to be proposed
   * @throws RemoteException remote communication error occurs
   * @throws InterruptedException operation is interrupted
   */
  void propose(String proposalId, Operation proposalValue) throws RemoteException, InterruptedException;
}
