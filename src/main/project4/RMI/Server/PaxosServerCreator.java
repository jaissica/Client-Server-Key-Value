package main.project4.RMI.Server;
import java.util.TimerTask;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.text.SimpleDateFormat;
import java.util.Timer;
import java.util.Date;

/**
 * The PaxosServerCreator class is responsible for creating and managing Paxos servers
 */
public class PaxosServerCreator {

  public static final SimpleDateFormat dateFormat = new SimpleDateFormat("MM-dd-yyyy HH:mm:ss.SSS");

  /**
   * This main method of the PaxosServerCreator program
   *
   * @param args Command line arguments.
   */
  public static void main(String[] args) {
    try {
      int serverNum = 5;
      try {
        if (args.length != 2) {
          System.out.println(getCurrentDateTime() + " - Format: java PaxosServer port remoteObjectName");
          System.exit(1);
        }

        int inputPortNumber = Integer.parseInt(args[0]);
        String remoteObjectName = args[1];

        Server[] servers = new Server[serverNum];


        for (int serverId = 0; serverId < serverNum; serverId++) {
          int port = inputPortNumber + serverId;

          LocateRegistry.createRegistry(port);

          servers[serverId] = new Server(serverId);

          Registry registry = LocateRegistry.getRegistry(port);
          registry.rebind(remoteObjectName, servers[serverId]);

          System.out.println(getCurrentDateTime() + " : Server " + serverId + " is running at " + port);
        }

        scheduler(servers);

        for (int serverId = 0; serverId < serverNum; serverId++) {
          IAcceptor[] acceptors = new IAcceptor[serverNum];
          ILearner[] ILearners = new ILearner[serverNum];
          for (int i = 0; i < serverNum; i++) {
            acceptors[i] = servers[i];
            ILearners[i] = servers[i];
          }
          servers[serverId].setAcceptors(acceptors);
          servers[serverId].setLearners(ILearners);
        }

      } catch (Exception e) {
        System.err.println("Server exception: " + e.toString());
        e.printStackTrace();
      }
    } catch (Exception e) {
      System.out.println("Exception occurred: " + e.getMessage());
    }
  }

  /**
   * This method schedules server drops at regular intervals
   *
   * @param servers array of servers
   */
  public static void scheduler(Server[] servers) {
    Timer timer = new Timer();
    timer.schedule(new TimerTask() {
      @Override
      public void run() {
        dropServer(servers);
      }
    }, 10000, 100000);
  }


  /**
   * This method gets the current time in a formatted string
   *
   * @return formatted current time string
   */
  public static String getCurrentDateTime() {
    return "<Time: " + dateFormat.format(new Date()) + ">";
  }

  /**
   * This method simulates a server going down randomly
   *
   * @param servers array of servers
   */
  public static void dropServer(Server[] servers) {
    int id = (int) (Math.random() * servers.length);
    servers[id].setServerDown();
    System.out.println(getCurrentDateTime() + " - Server " + id + " is going down...!!");
  }
}
