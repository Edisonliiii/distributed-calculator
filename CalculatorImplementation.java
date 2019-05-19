/**
 * @(#)CalculatorImplementation.java 09/08/18
 * @author Jialiang Li
 * @since 09/08/18
 * {@inheritDoc} Calculator.java
 * This is the implementation of the calculatior interface.
 * It is locates on the server side.
 */

//package calculator;

// Used API
import java.util.*;
import java.rmi.server.UnicastRemoteObject;
import java.rmi.RemoteException;
import java.rmi.dgc.Lease;
import java.rmi.dgc.VMID;

public class CalculatorImplementation extends UnicastRemoteObject implements Calculator
{
    // Shared data members if all clients using the common stack
    private String firstOperand;
    private String secondOperand;
    private String operatorInUse;
    private static Stack<String> calStack = new Stack<String>();
    private int result;

    // Constructor
    public CalculatorImplementation() throws RemoteException
    {
        super();
    }

    // Methods
    /**
     * This method is used to push the value from client into the stack on server side.
     * @param val It represents the value, in String type. 
     * @param chooseStack It represents the stack we choose to use.
     * @return void No return value, just data opeartion.
     */
    @Override
    public void pushValue(String val, Stack<String> chooseStack) throws RemoteException
    {
        chooseStack.push(val);
    }

    /**
     * This method is used to push the operator from client into the stack on the server side.
     * After pushing in the operator, it will process the calculation behavior and push the
     * result back to the stack.
     * @param operatorInuse It represents the operator in String type.
     * @param chooseStack It represents the stack we choose to use.
     * @return void No return value, just data operation.
     */
    @Override
    public void pushOperator(String operatorInUse, Stack<String> chooseStack) throws RemoteException
    {
    	chooseStack.push(operatorInUse);

        String getOperator = chooseStack.pop();
        int getSecondOperand = Integer.parseInt(chooseStack.pop());
        int getFirstOperand = Integer.parseInt(chooseStack.pop());

        // Calculate the result based on the operator
        switch (getOperator)
        {
            case "+":
                result = getFirstOperand+getSecondOperand;
                break;
            case "-":
                result = getFirstOperand-getSecondOperand;
                break;
            case "*":
                result = getFirstOperand*getSecondOperand;
                break;
            case "/":
                result = getFirstOperand/getSecondOperand;
                break;
        }

        chooseStack.push(Integer.toString(result));
    }

    /**
     * This method tends to check if the stack used is empty or not
     * @param chooseStack represents the what kind of stack is going to be use.(shared or private)
     * @return boolean if it is, return true; else false.
     */
    @Override
    public boolean isEmpty(Stack<String> chooseStack) throws RemoteException
    {
        boolean stackStatus = chooseStack.empty();
        return stackStatus;
    }

    /**
     * This method tends to delay a while befour the result would be pop and send back to the client.
     * @param chooseStack represents the what kind of stack is going to be use.(shared or private)
     * @return int represents the return value.
     */
    @Override
    public int delayPop(Stack<String> chooseStack) throws RemoteException
    {
        try{
            Thread.sleep(0);
        }catch(InterruptedException e){
            e.printStackTrace();
        }

        return Integer.parseInt(chooseStack.pop());
    }

    /**
     * This method tends to pop off the result and send it back to the client.
     * @param chooseStack represents the what kind of stack is going to be use.(shared or private)
     * @return int represents the return value.
     */
    @Override
    public int pop(Stack<String> chooseStack) throws RemoteException
    {
        int result = this.delayPop(chooseStack);
        return result;
    }

    /**
     * All of the calculator processes have been encapsulated into this method.
     * It push values and operator into the stack and pop off the result after waiting and check
     * if the stack is empty or not.
     * The reason I did this encapsulation is to make the sychronization easier to be handled.
     * @param firstOperand --- the first operand
     * @param secondOperand --- the second operand
     * @param operatorInUse --- the operator
     * @param chooseStack --- represents the what kind of stack is going to be use.(shared or private).
     * @return int --- get the final result.
     */
    @Override
    public int runCalculation(String firstOperand, String secondOperand,
        String operatorInUse, Stack<String> chooseStack) throws RemoteException
    {
        this.pushValue(firstOperand, chooseStack);
        this.pushValue(secondOperand, chooseStack);
        this.pushOperator(operatorInUse, chooseStack);
        int getResult = this.pop(chooseStack);
        return getResult;
    }

    /**
     * This method is used to handle one-server-one-stack -- multiple clients case.
     * The calculation process would be invoked after the case matching.
     * The calculation process would use the common shared stack so the lock has to
     * limit that there is only one object could hold this method.
     * @param firstOperand --- the first operand
     * @param secondOperand --- the second operand
     * @param operatorInUse --- the operator
     * @return int --- return the final result back to the client.
     */
    @Override
    public synchronized int sharedStack(String firstOperand, String secondOperand,
        String operatorInUse) throws RemoteException
    {  
        this.firstOperand  = firstOperand;
        this.secondOperand = secondOperand;
        this.operatorInUse = operatorInUse;

        int getResult = this.runCalculation(firstOperand, secondOperand, operatorInUse, this.calStack);
        return getResult;
    }

    /**
     * This method is used to handle one-server-multiple-stack -- multiple clients case.
     * The calculation process would be invoked after the case matching.
     * The calculation process would assign each new-coming client a new stack and let them
     * process their calculations individually. So no lock is needed here.
     * @param firstOperand --- the first operand
     * @param secondOperand --- the second operand
     * @param operatorInUse --- the operator
     * @return int --- return the final result back to the client.
     */
    @Override
    public int privateStack(String firstOperand, String secondOperand,
        String operatorInUse) throws RemoteException
    {
        String fir = firstOperand;
        String sec = secondOperand;
        String ope = operatorInUse;
        Stack<String> makeStack = new Stack<String>();
        int getResult = this.runCalculation(fir, sec, ope, makeStack);
        return getResult;
    }
}