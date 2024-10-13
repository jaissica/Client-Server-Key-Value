package main.project4.RMI.Server;
import java.rmi.RemoteException;
import java.rmi.Remote;


/**
 * IAcceptor interface defines the methods that Paxos acceptor should implement
 * to involve in the Paxos consensus algorithm
 */
public interface IAcceptor extends Remote {

  /**
   * This method helps in preparing the acceptor with proposal ID and operation
   *
   * @param proposalId unique proposal ID
   * @param operation  proposed operation
   * @return {true} if acceptor is prepared else {false}
   * @throws RemoteException remote communication error occurs
   */
  Boolean prepare(String proposalId, Operation operation) throws RemoteException;

  /**
   * This method accepts proposal with the specified proposal ID and value
   *
   * @param proposalId     unique proposal ID
   * @param proposalValue  value proposed in the proposal
   * @throws RemoteException remote communication error occurs
   */
  void accept(String proposalId, Operation proposalValue) throws RemoteException;
}
