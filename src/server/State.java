package server;

/**
 * The State enum class holds enum values to track the user state
 * in the smtp server
 * 
 * <br>
 * NC: No Connection<br>
 * CE: Conenction Established<br>
 * MAIL: Mail from<br>
 * RCPT: Rcpt to<br>
 * DATA: Data<br>
 * CQ: Connection quitted<br>
 * 
 * @author 100385188
 * @version 1.0
 * @since 2017-12-07
 *
 */

public enum State {
	NC, CE, MAIL, RCPT, DATA, CQ
}
