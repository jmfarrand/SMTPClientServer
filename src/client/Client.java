package client;
import java.io.*;
import java.net.*;
import java.util.Scanner;

/**
 * The Client class is an implementation of the client side
 * of the RFC 821 for the networking assingment.
 * It manages the client side communication, creating and starting threads for
 * reading and writing data that is to be sent to the server
 * 
 * @author 100385188
 * @version 1.0
 * @since 2017-11-29
 */
public class Client {
	//Main Method:- called when running the class file.
	
	/**
	 * Main: The main method that first runs when you run the Client java file.
	 * 
	 * @param args Command-line argumentes
	 * @author 100385188
	 * @version 1.0
	 * @since 2017-12-08
	 */
	
    public static void main(String[] args) {
    	System.out.println("Please enter in the port number to connect on in the range 50000 - 50010");
        //Portnumber:- number of the port we wish to connect on.
        
        Scanner scan = new Scanner(System.in);
        int port = scan.nextInt();
        
        int portNumber = 0;
        if (port > 50000 && port <= 50010) {
        	portNumber = port;
        } else {
        	portNumber = 50000;
        	System.err.println("Port number out of range. Defauling to 50000");
        }
        
        //ServerIP:- IP address of the server.
        String serverIP = "localhost";
        Boolean running = true;
        
        try {
            //Create a new socket for communication
            Socket soc = new Socket(serverIP,portNumber);
            // create new instance of the client writer and read thread, intialise it and start it running
            ClientWriter clientWrite = new ClientWriter(soc, running);
            ClientReader clientRead = new ClientReader(soc, running);
            Thread clientWriteThread = new Thread(clientWrite);
            Thread clientReadThread = new Thread(clientRead);
            clientWriteThread.start();
            clientReadThread.start();
        } catch (Exception e) {
            //Exception thrown (e) when something went wrong, pushing message to the console
        	System.err.println("Error --> " + e.getMessage());
        }
    }
}
