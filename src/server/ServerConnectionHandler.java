package server;

import java.io.*;
import java.net.*;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

/**
 * The ServerConnectionHandler class handles a new incoming client connection
 * prompting the user for a domain name if not entered properly.
 * If the user entered in a correct domain name then it starts a ServerConnection thread.<br>
 * This class implements runnable to be able to run this in a Thread
 * 
 * @author 100385188
 * @version 1.0
 * @since 2017-11-29
 * 
 */

public class ServerConnectionHandler implements Runnable {
	Socket clientSocket = null;
    public static String strInput;
    public static String[] strInputArray;
    State state = State.NC;
    /**
     * ServerConnectionHandler: Initialises the ServerConnectionHandler class taking
     * the socket to communicate over as a paramater
     * 
     * @param inSoc The socket to communicate over using the class
     * @author 100385188
     * @version 1.0
     * @since 2017-12-08
     */
	public ServerConnectionHandler (Socket inSoc) {
		clientSocket = inSoc;
	}
	/**
	 * run: the code that is called when the instance of the ServerConnectionHandler class calls the method run(). 
	 */
	public void run() {
		try {
			// print out message that confirms that client is conencted
			System.out.println("Client connected");
			// create data input and output streams
			DataInputStream dataIn = new DataInputStream(clientSocket.getInputStream());
			DataOutputStream dataOut = new DataOutputStream(clientSocket.getOutputStream());
			// send message to client that service is ready
			dataOut.writeUTF("220: Service Ready");
			//Setting the state of the user to conenction established (CE)
			state = State.CE;
			Boolean running = true;
			// arrays to store the parsed email adress the user enteres in the mail from and rcpt to states
			//to populate the from and to fields
			String[] mailFromParts = null;
			String[] rcptToParts = null;
			// the command the user enteres
			String command = "";
			// the address (the second paramater) the user enteres
			String clientAddress = "";
			// Create a new instance of the CreateXML class in order to populate the xml document as the program is worked through
			CreateXML document = new CreateXML();
			// variable to hold the subject the user entered
			String subjectInput;
			// variable to store the input the user enteres during the DATA state
			String dataInput;
			// local variable to store the subject to bw written to the xml fike
			String subject = "";
			// local variable to store the text the user eneters into the xml file
			String text = "";
    	
			while(running) {
				if (dataIn.available() > 0) {
					//read the user input from the socket connection and
					//assing it to the strInput variable
					strInput = dataIn.readUTF();
					// determine if the input string contains a space.
					// if so then split the string to get the command the user requested
					// if not send a prompt for the user to enter in a domain name
					if (strInput.contains(" ")) {
						strInputArray = strInput.split(" ");
						if (strInputArray.length == 2) {
							// move what is stored in the splitted input array into local variables
							command = strInputArray[0];
							clientAddress = strInputArray[1];
						} else {
							dataOut.writeUTF("500: Syntax error command unrecognized. ");
							dataOut.flush();
						}
					} else if (strInput.toUpperCase().equals("DATA") && state == State.DATA) {
						// special if statement to handle the data command. calling the
						//data command doesnt require extra paramateres so the user
						//just enteres "DATA" and presses enter and the input (assumed
						//to be DATA) is saved to the local command variable and then passed onto the main DATA code
						command = strInput;
					} else if (strInput.toUpperCase().equals("QUIT")) {
						//QUIT
						command = strInput; // write over command so server conection code isn't called upon
						dataOut.writeUTF("221: Service Closing");
						dataOut.flush();
					} else {
						dataOut.writeUTF("501: Syntax error in pararameteres or agrugements");
						dataOut.flush();
					}
					if (!command.toUpperCase().equals("QUIT") && strInputArray.length == 2) {
						// **************************************************************
						// *															*
						// *					SERVER CONNECTION CODE					*
						// *															*
						// **************************************************************
						
						if (command.toUpperCase().equals("HELO") && state.equals(State.CE)) {
							// HELO
							// split clientAddress by "." to check the user entered in a dot so that it is a valid domain name
							String[] splittedDomainByDot = clientAddress.split("\\.");
							if (splittedDomainByDot.length >= 2) {
								// it is a valid domain name
								// set the user state to the MAIL state and send over 250 OK
								state = State.MAIL;
								//User set to MAIL state
								dataOut.writeUTF("250: OK");
								dataOut.flush();
							} else {
								dataOut.writeUTF("500: Syntax error, command unrecognised");
							}
						} else if (command.toUpperCase().equals("MAIL") && state.equals(State.MAIL)) {
							// MAIL FROM
							// split clientAddress by "@" and then "." to check that the user entered a valid email address
							String[] splittedDomainByAtSymbol = clientAddress.split("@");
							// check that their is a '@' before there is a '.'
								if (splittedDomainByAtSymbol.length == 2) {
									String[] splittedDomainByDot = splittedDomainByAtSymbol[1].split("\\.");
									if (splittedDomainByDot.length >= 2) {
										// it is a valid domain name
										// now split the second entrey (clientAddress) by a colon so see if the user has correctly formatted the MAIL FROM: command
										mailFromParts = clientAddress.split(":");
										if (mailFromParts.length == 2 && mailFromParts[0].toUpperCase().equals("FROM")) {
											String mailFromAddress = mailFromParts[1];
											document.addMailFromAddress(mailFromAddress.toLowerCase());
											dataOut.writeUTF("250: OK");
											state = State.RCPT;
											//User set to RCPT state
											dataOut.flush();
										} else {
											dataOut.writeUTF("500: Syntax error, command unrecognised");
										}
									} else {
										dataOut.writeUTF("500: Syntax error, command unrecognised");
										}
									} else {
									dataOut.writeUTF("500: Syntax error, command unrecognised");
									}
						} else if (command.toUpperCase().equals("RCPT") && state.equals(State.RCPT)) {
							//RCPT TO
							// split clientAddress by "@" and then "." to check that the user entered a valid email address
							String[] splittedDomainByAtSymbol = clientAddress.split("@");
							// check that their is a '@' before there is a '.'
								if (splittedDomainByAtSymbol.length == 2) {
									String[] splittedDomainByDot = splittedDomainByAtSymbol[1].split("\\.");
									if (splittedDomainByDot.length >= 2) {
										// it is a valid domain name
										// split second user entrey by the colon to check RCPT TO: command is formatted correctly
										rcptToParts = clientAddress.split(":");
										if (rcptToParts.length == 2 && rcptToParts[0].toUpperCase().equals("TO")) {
											String rcptToAddress = rcptToParts[1];
											document.addRcptToAddress(rcptToAddress.toLowerCase());
											dataOut.writeUTF("250: OK");
											state = State.DATA;
											//User set to DATA state
											dataOut.flush();
										} else {
											dataOut.writeUTF("500: Syntax error, command unrecognised");
										}
									} else {
										dataOut.writeUTF("500: Syntax error, command unrecognised");
										}
									} else {
									dataOut.writeUTF("500: Syntax error, command unrecognised");
									}

						} else if (command.toUpperCase().equals("DATA") && state.equals(State.DATA)) {
							//DATA

							// ===== CODE TO POPULATE FROM AND TO FIELDS =====
							// create array to store the first part of the mail from email address and then store it in the local fromName variable.
							String[] fromNameArray = mailFromParts[1].split("@");
							String fromName = fromNameArray[0];
							// create array to store a splitted email address of the rcpt to address and then assing the first part to the toName variable.
							String[] toNameArray = rcptToParts[1].split("@");
							String toName = toNameArray[0];
							// write these local variables to the xml file, converting them to lower case.
							document.addNames(fromName.toLowerCase(), toName.toLowerCase());
							
							// ===== CODE TO POPULATE DATE FIELD =====
							// code to print out current date, time and time zone to the xml file.
							DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss (OOOO/zzzz)");
							ZonedDateTime now = ZonedDateTime.now();
							String dateTime = dtf.format(now);
							
							// prompt the user to enter start entering mail input
							dataOut.writeUTF("354: Start mail input; end with <CRLF>.<CRLF>");
							
							// ===== GET THE SUBJECT FROM THE USER =====
							if (!(subjectInput = dataIn.readUTF()).equals("\n")) {
								subject = subjectInput;
							}
							
							// ===== GET THE MAIN USER TEXT INPUT =====
							Boolean enteringText = true;
							String previousText1 = "";
							String previousText2 = "";
							while (enteringText) {
								// catch what the user enteres in into a local dataInput variable
								dataInput = dataIn.readUTF();
								
								// if the user enteres <CRLF>.<CRLF> then they aren't entering text anymore
								if (previousText2.equals("") && previousText1.equals(".") && dataInput.equals("")) {
									enteringText = false;
								} else {
									// store the text the user enteres into the text variable and split with a \r when the user hits enter
									text = text + "\r" + dataInput;
								}
								// copy the previous text into local variables to track whether the user has entered <CRLF>.<CRLF> in order to quit the DATA state
								previousText2 = previousText1;
								previousText1 = dataInput;
							}
							
							// ===== ADDING THE USER DATE/TIME AND USER INPUTTED SUBJECT AND TEXT TO XML FILE =====
							document.addData(dateTime, subject.toLowerCase(), text.toLowerCase());
							
							// ===== CODE TO POPULATE DATE/TIME FIELD FOR XML FILE NAME =====
							// code to print out current date, time and time zone to the xml file.
							DateTimeFormatter dtfFileName = DateTimeFormatter.ofPattern("yyyy.MM.dd.HH.mm.ss");
							ZonedDateTime nowFileName = ZonedDateTime.now();
							String dateTimeFileName = dtfFileName.format(nowFileName);
							
							// ===== CREATING XML DOCUMENT =====
							// create the actual xml document. the document is stored in the root eclipse direcry
							//i.e. ..\path\to\project\SMTPClientServer\yyyy.MM.dd.HH.mm.ss.xml
							document.create(dateTimeFileName);
							dataOut.writeUTF("250: OK");
							state = State.CE;
							// user set back to the CE state
							
							dataOut.writeUTF("You are now back into the CE state. You may start another SMTP message or enter QUIT to exit the program.");
							dataOut.flush();
						} else if (command.toUpperCase().equals("RSET")) {
							//RSET
							document.clear();
							state = State.CE;
							dataOut.writeUTF("250: OK");
							dataOut.flush();
						} else if (command.toUpperCase().equals("NOOP")) {
							//NOOP
							dataOut.writeUTF("250: OK");
							dataOut.flush();
						}
						// I haven't implemented the following commands so all of these comamnds return a error code 502 not implemeneted
						else if (command.toUpperCase().equals("VRFY")) {
							//VRFY
							dataOut.writeUTF("502: Command not implemeneted");
							dataOut.flush();
						} else if (command.toUpperCase().equals("EXPN")) {
							//EXPN
							dataOut.writeUTF("502: Command not implemeneted");
							dataOut.flush();
						} else if (command.toUpperCase().equals("HELP")) {
							//HELP
							dataOut.writeUTF("502: Command not implemeneted");
							dataOut.flush();
						} else {
							dataOut.writeUTF("501: Syntax error in parameteres or argumentes");
							dataOut.flush();
						}	
					} else if (strInput.toUpperCase().equals("QUIT")) {
						// QUIT is handled above where message is sent to client
					} else {
						// do nothing
					}
				}   
			}           
		}
		catch (Exception e) {
			//Exception thrown (except) when something went wrong, pushing message to the console
			System.err.println("Error in ServerHandler--> " + e.getMessage());
		}
	}
}