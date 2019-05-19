/**
 * @(#)CalculatorClient.java 09/08/18
 * @author Jialiang Li
 * @since 09/08/18
 * This is class used to define client side logic
 */

//package calculator;

// Used API
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.concurrent.*;
import java.util.Random;
import java.util.Scanner;

public class CalculatorClient
{
    public CalculatorClient(){}

    /**
     * This method is used to define the main behavior of client side.
     * The reason I used a method to encapsulate all of the client behavior
     * is just because it would be easier to process my test case. So there
     * is no main function in this file.
     * @param Boolean multipleStack --- this is a flag which is used to determine
     *                (false) one-stack-multiple-client case would be tested or
     *                (true) each-client-one-stack case would be tested.
     */
    public boolean fromClient(Boolean mulipleStack)
    {
        // Generate the calculation complete randomly.
        final String operatorArr[] = {"+", "-", "*", "/"};
        String firsOpIn = Integer.toString(new Random().nextInt(1000)+1);
        String secoOpIn  = Integer.toString(new Random().nextInt(1000)+1);  // If division, denominator would never be 0
        String operatorIn = operatorArr[new Random().nextInt(operatorArr.length)];
        
        int rightResult=0;  // record the right result and compare with the result from server later
        boolean signal=false;  // if the result is right, then signal would be true
        
        // Calculate the right result.
        switch(operatorIn)
        {
            case "+":
                rightResult = Integer.parseInt(firsOpIn) + Integer.parseInt(secoOpIn);
                break;
            case "-":
                rightResult = Integer.parseInt(firsOpIn) - Integer.parseInt(secoOpIn);
                break;
            case "*":
                rightResult = Integer.parseInt(firsOpIn) * Integer.parseInt(secoOpIn);
                break;
            case "/":
                rightResult = Integer.parseInt(firsOpIn) / Integer.parseInt(secoOpIn);
                break;
        }
        // Calculate the RMI result
        try{
            System.out.println(Thread.currentThread().getName() +" "+"Processing the calculation: " 
                    + firsOpIn +" "+ secoOpIn+" "+ operatorIn);
            // Connect to registry
            Registry registry = LocateRegistry.getRegistry(8080);
            // Look for the object to reduce resource overhead
            Calculator stub = null;
            synchronized(this)
            {
                stub = (Calculator) 
                        registry.lookup("Calculator");
            }
            // Run the method
            int finalResult;
            if (mulipleStack == false)
            {
                /* one-stack-multiple-client case */
                finalResult = stub.sharedStack(firsOpIn, secoOpIn, operatorIn);
            }else{
                /* each-client-one-stack case */
                finalResult = stub.privateStack(firsOpIn, secoOpIn, operatorIn);
            }
            
            // If the RMI result is right, then signal would be true.
            if(finalResult == rightResult)
            {
                System.out.println(Thread.currentThread().getName() + " Result: " + finalResult 
                    + " [PASS THE TEST SUCCESSFULLY!]");
                signal = true;
            }
        }catch(Exception e){
            System.err.println("Client Exception: " + e.toString());
            e.printStackTrace();
        }
        return signal;
    }
}