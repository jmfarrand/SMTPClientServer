package client;

import java.io.*;
import java.net.*;

/**
 * The ClientWriter class handles the user input for sending commands to the server SMTP.
 * It runs in a loop checking for user input and then sending the command straight to the server
 * which does all of the command handiling and then sends the results back to the client
 * which reads it using the ClientReader class<br>
 * This class implements runnable to be able to run this in a Thread
 * 
 * @author 100385188
 * @version 1.0
 * @since 2017-11-29
 */

//This thread is responsible for writing messages
public class ClientWriter implements Runnable {
    Socket cwSocket = null;
    static Boolean running = null;
    Boolean message = true;
    
    /**
     * ClientWriter: Constructs a new instance of the ClientWriter class
     * Passing in the socket to output user input to and a Boolean that
     * Determines when the thread should stop in synch with the ClientReader thread
     * 
     * @param outputSoc The socket to output user input to
     * @param runningIn A running Boolean that allows it so both threads can quit at the same time
     * @author 100385188
	 * @version 1.0
	 * @since 2017-12-08
     */
    
    public ClientWriter (Socket outputSoc, Boolean runningIn) {
        cwSocket = outputSoc;
        running = runningIn;
    }
    
    /**
     * run: this code is called upon when the thread run method is called
     * 
     * @author 100385188
	 * @version 1.0
	 * @since 2017-12-08
     */
    
    public void run() {
        try {
            //Create the outputstream to send data through
            DataOutputStream dataOut = new DataOutputStream(cwSocket.getOutputStream());
            
            // I/O code
            String fromUser;
            BufferedReader stdIn = new BufferedReader(new InputStreamReader(System.in));
            
            while(running) {
            	fromUser = stdIn.readLine();
                if ((fromUser = fromUser.toUpperCase()).equals("QUIT")) {
                	dataOut.writeUTF(fromUser);
                    dataOut.flush();
                    dataOut.close();
                    running = false;
                } else {
                    //Write message to output stream and send through socket
                    dataOut.writeUTF(fromUser);
                    dataOut.flush();
                    }
                }
            }
        catch (Exception e) {
            //Exception thrown (e) when something went wrong, pushing message to the console
        	System.err.println("Error in Writer--> " + e.getMessage());
        }
    }
}
