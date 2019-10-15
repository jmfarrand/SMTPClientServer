package server;

import java.io.*;
import java.net.*;

/**
 * THe User class defines a user object which is created when a
 * new user connects to the server. The ServerConnectionHandler creates a
 * new user and inputs the ip address, domain name and current smtp
 * state of the user
 * 
 * @author 100385188
 * @version 1.0
 * @since 2017-11-30
 *
 */

public class User {
	// create local class variables to store the ip address
	//and domain name of the user as well as the current state of the user
	String domainName;
	UserState state;
	/**
	 * User: The constructor for the User class that takes the user's
	 * domain name and the current state as a paramater
	 * @param domainNameIn The domain name of the user
	 * @param stateIn The current state of the user
	 * @author 100385188
	 * @version 1.0
	 * @since 2017-11-30
	 */
	public User(String domainNameIn, UserState stateIn) {
		domainName = domainNameIn;
		state = stateIn;
	}
	/**
	 * setDomainName: Set's the domain name of the user to a
	 * newly provided value(domainNameIn)
	 * @param domainNameIn The new domain name of the user.
	 * @author 100385188
	 * @version 1.0
	 * @since 2017-11-30
	 */
	public void setDomainName(String domainNameIn) {
		domainName = domainNameIn;
	}
	/**
	 * getDomainName: return's the user's domain name
	 * @return domainName: The current user's domain name.
	 * @author 100385188
	 * @version 1.0
	 * @since 2017-11-30
	 */
	public String getDomainName() {
		return domainName;
	}
	/**
	 * setState: Set's the current user's state to a new value specified as a paramater
	 * @param stateIn The new state to set the user to
	 * @author 100385188
	 * @version 1.0
	 * @since 2017-11-30
	 */
	public void setState(UserState stateIn) {
		state = stateIn;
	}
	/**
	 * getState: Get's the current user's UserState state
	 * @return state: The current state(UserState) of the user
	 * @author 100385188
	 * @version 1.0
	 * @since 2017-11-30
	 */
	public UserState getState() {
		return state;
	}
}
