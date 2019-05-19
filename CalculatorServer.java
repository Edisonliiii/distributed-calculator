/**
 * @(#)CalculatorServer.java 09/08/18
 * @author Jialiang Li
 * @since 09/08/18
 * This is class used to define server side logic
 */
//package calculator;

// Used API
import java.rmi.RemoteException;
import java.rmi.registry.Registry;
import java.rmi.registry.LocateRegistry;
import java.rmi.server.UnicastRemoteObject;

public class CalculatorServer extends CalculatorImplementation
{
    public CalculatorServer() throws RemoteException {}

    public static void main(String[] args)
    {
        try{
            // Create the remote server object that provides the service.
            // Export the remote obj to the java RMI
            Calculator stub =  new CalculatorImplementation();
            // Instantiate a registry object sticking with server at port 8080.
            Registry registry = LocateRegistry.createRegistry(8080);
            // Bind a name to the remote object stub, if there already is, cover it!
            registry.rebind("Calculator", stub);

            System.err.println("Server is ready!");
        }catch (Exception e){
        	System.err.println("Server Exception: " + e.toString());
        	e.printStackTrace();
        }
    }
}