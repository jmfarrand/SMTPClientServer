package server;
import java.io.*;
import java.net.*;
import java.util.Scanner;

/**
 * The server class is a implementation of RFC821
 * for the SMTP protocol for the networking assingment
 * 
 * @author 100385188
 * @version 1.0
 * @since 2017-11-29
 * 
 */
 
public class Server {
    //Main Method:- called when running the class file.
	/**
	 * Main: The main method that is run when the Server is run
	 * The user enteres in the port number that they want to connect on
	 * 
	 * @param args command line arguements
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
        
        try {
            //Setup the socket for communication 
            ServerSocket serverSoc = new ServerSocket(portNumber);
            Boolean running = true;
            
            while (running) {
                //accept incoming communication
                System.out.println("Waiting for client");
                Socket soc = serverSoc.accept();
                
                //create a new thread for the connection and start it.
                ServerConnectionHandler sch = new ServerConnectionHandler(soc);
                           
                Thread schThread = new Thread(sch);
               
                schThread.start();
            }
            serverSoc.close();
        }
        catch (Exception e) {
            //Exception thrown (e) when something went wrong, pushing message to the console
        	System.err.println("Error --> " + e.getMessage());
        }
        
    }   
}


