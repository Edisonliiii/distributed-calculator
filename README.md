# RMI calculator

RMI calculator is a distributed calculator system which can handle multiclient case with one activate server.

## How to use

## Mode Explain

There are three different situations you can run and check.

### One server and one client

```
Run: java runTest
```

It will start a RMI server first and let one server connect to it to process the calculation.

### One server and multiclient(shared stack)

```
Run: java -DclientNum={THE INT NUMBER YOU WANT} runTest
```

It will start a RMI server first and let multiple clients connect to the server.
In this case, there is only one stack on server. So every clients share it.

### One server and multiclient(dynamically build private stack)

```
Run: java -DclientNum={THE INT NUMBER YOU WANT} -DstackOption=true runTest
```

It will start a RMI server first and let multiple clients connect to the server.
In this case, each time a new client connects to the server, it will create a new stack for the coming client. So different clients have their private stack rather than a public shared stack.

## About Testing

In my design, the test is based on the implementation of RMI calculator and separate from the functionality implementation. That means, you are NOT able to run like:

```
java CalculatorClient
```
You can NOT run like above command straightly. Since there is no main function in CalculatorClient.java file. The reason I designed it is to easy the testing process.

I separate testing implementation to runTest.java file. In this way, if you want to check one-server-one-client case, just set the option like:

```
-DclientNum=1
```
Else, if you want to check one-server-shared-stack-multiclient case, set the option like:

```
-DclientNum={ANY INTEGER YOU WANT}
```

or 

```
-DclientNum={ANY INTEGER YOU WANT} -DstackOption=false
```

Because -DstackOption use false as its default value.

Else, if you want to check the case that each client has their own stack, set the option like:

```
-DclientNum={ANY INTEGER YOU WANT} -DstackOption=true
```
