/**
 * @(#)Calculator.java 09/08/18
 *
 * @author Jialiang Li
 * @since 09/08/18
 * This is the interface for the reverse Polish caculator.
 * This interface is used to instantiate the stub object
 * on both client and server side.
*/

//package calculator;

// Used API
import java.util.*;
import java.rmi.Remote;
import java.rmi.RemoteException;

public interface Calculator extends Remote
{
    // Push the value of operand and push it on to the top of the calculator stack.
    void pushValue(String val, Stack<String> chooseStack) throws RemoteException;
    // Push operator +-*/ to the stack.
    void pushOperator(String operatorInUse, Stack<String> chooseStack) throws RemoteException;
    // Pop the top of the calculator stack and return it to the client.
    int pop(Stack<String> chooseStack) throws RemoteException;
    // Return true if the stack is empty. Else, false.
    boolean isEmpty(Stack<String> chooseStack) throws RemoteException;
    // Wait millis milliseconds b4 carrying out the pop operation as above.
    int delayPop(Stack<String> chooseStack) throws RemoteException;
    // Encapsulate the whole calculating process into one method
    int runCalculation(String firstOperand, String secondOperand,
        String operatorInUse, Stack<String> chooseStack) throws RemoteException;
    // Multiple clients share the common stack in the case of RMI
    int sharedStack(String firstOperand, String secondOperand,
        String operatorInUse) throws RemoteException;
    // Multiple clients have their own private stack in the case of RMI
    int privateStack(String firstOperand, String secondOperand,
        String operatorInUse) throws RemoteException;
}