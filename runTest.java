/**
 * @(#)runTest.java 09/08/18
 * @author Jialiang Li
 * @since 09/08/18
 * This is class used to make all of the test according to
 * the requirement in assignment sheet. With couple of options.
 *
 * If you choose to run with multiple clients, then these clients
 * would run under the same process but different signle threads.
 */


import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

public class runTest
{
    /**
     * This method fires the server via background execution
     * The sleep invocation is used to avoid the conflict
     */
    private static void startServer()
    {   try{
            Runtime.getRuntime().exec("java -classpath ."+
                                  " -Djava.rmi.server.codebase=./"+
                                  " CalculatorServer &");
            Thread.sleep(200);
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    // Set up the atmoic counter to count the number of clients with right answer
    static AtomicInteger counter = new AtomicInteger(0);
    static CountDownLatch waiter=null;
    static boolean getSignal = false;

    public static void main(String[] args) throws InterruptedException
    {
        // Fire server up
        startServer();
        
        // Setup the option
        /**
         * -DclientNum=int --- setup the number of the client you want
         * -DstackOption=true/false --- choose the case you want to test
         *               true --- one common stack
         *               false --- each client has a stack
         */
        final int clientNum = Integer.parseInt(System.getProperty("clientNum", "1"));
        final Boolean stackOption = Boolean.valueOf(System.getProperty("stackOption", "false"));
        // Set up the counter and the thread pool
        waiter = new CountDownLatch(clientNum);
        ExecutorService exec = Executors.newFixedThreadPool(clientNum);
        
        for(int i=0; i<clientNum; i++)
        {
            CalculatorClient clientFeed = new CalculatorClient();
            exec.execute(new Runnable(){
                @Override
                public void run()
                {
                    getSignal = clientFeed.fromClient(stackOption);
                    // When the result is right, increase the counter
                    if(getSignal == true)
                    {
                        counter.getAndIncrement();
                    }
                    waiter.countDown();

                }
            });
        }
        // Main thread wait the kids
        waiter.await();
        // Stop accepting new tasks first
        exec.shutdown();
        // Completely terminate after 100 millis
        try{
            if(!exec.awaitTermination(100, TimeUnit.MILLISECONDS))
            {
                exec.shutdownNow();
            }
        }catch (InterruptedException e){
            exec.shutdownNow();
        }
        
        if(counter.get() == clientNum)
        {
            System.out.println("<100% PASS THE TEST!>");
        }else{
            System.out.println("Limited Resource!");
        }
    }
}