package server;

// java file i/o import
import java.io.File;

// java xml imports
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

 // w3c xml imports
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
 
/**
 * The CreateXML class creates a XML document used for storing the email
 * sent by the user in the ServerConnectionHandler class
 * The code I used was adapted from https://www.journaldev.com/1112/how-to-write-xml-file-in-java-dom-parser
 * 
 * @author 100385188 (originally by pankaj)
 * @version 1.0
 * @since 2017-11-30
 *
 */

public class CreateXML {
	//initialise local variables for populating the xml file
	private String from;
	private String to;
	private String fromName;
	private String toName;
	private String date;
	private String subject;
	private String text;
	
	// ===== CONSTRUCTOR =====
	/**
	 * CreateXML: Initialises the CreateXML class with the data to be written in the XML document being passed in as paramaters
	 *
	 * @author 100385188
	 * @version 1.0
	 * @since 2017-12-08
	 */
	public CreateXML() {
		from = "";
		to = "";
		fromName = "";
		toName = "";
		date = "";
		subject = "";
		text = "";
	}
	/**
	 * addMailFromAddress: Adds the mail from address to the xml class to be added to
	 * the xml file when the create method is called
	 * 
	 * @param fromIn The Mail From address
	 */
	public void addMailFromAddress(String fromIn) {
		from = fromIn;
	}
	/**
	 * addRcptToAddress: Adds the recipient to address to the xml class to be added to
	 * the xml file when the create method is called
	 * 
	 * @param toIn The Rcpt To address
	 */
	public void addRcptToAddress(String toIn) {
		to = toIn;
	}
	/**
	 * addNames: Adds the from name and to name to the mail body.
	 * 
	 * @param fromNameIn The name of the sender that is sending the email
	 * @param toNameIn The name of the recipient of the email
	 * 
	 * */
	public void addNames(String fromNameIn, String toNameIn) {
		fromName = fromNameIn;
		toName = toNameIn;
	}
	/**
	 * addData: populates the data variables with data passed into the constructor.
	 * The data will be added when the create method is calld upon
	 * 

	 * @param dateIn The Date of the email being sent
	 * @param subjectIn The subject of the email message
	 * @param textIn The main message of the email message
	 */
	public void addData(String dateIn, String subjectIn, String textIn) {
		
		date = dateIn;
		subject = subjectIn;
		text = textIn;
	}
	/**
	 * clear: clears the data in the XML document and returns the data to emptey strings.
	 * Used for the rset command.
	 */
	public void clear() {
		from = "";
		to = "";
		fromName = "";
		toName = "";
		date = "";
		subject = "";
		text = "";
	}
	/**
	 * create: Create's the XML document using the paramateres passed into the constructor when the class is initialised
	 * 
	 * @param fileName the name of the xml file
	 * @author 100385188
	 * @version 1.0
	 * @since 2017-12-08
	 */
	public void create(String fileName) {
		// initialise the DocumentBuilderFactory and the DocumentBuilder for writing the xml file
		DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder dBuilder;
        try {
        	// create the document builder and initialise a new document
            dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.newDocument();
            //add the root element to Document
            Element rootElement = doc.createElementNS(null, "email");
            //append root element to document
            doc.appendChild(rootElement);
            
            //create the message envelope with the from adress and the to adress
            rootElement.appendChild(createEnvnelope(doc, from, to));
 
            // create the message element to store the message header and body
            Element message = doc.createElement("message");
            // create the message header
            message.appendChild(createHeader(doc, fromName, toName, date, subject));
            // create the message body
            message.appendChild(createBody(doc, text));
            rootElement.appendChild(message);
            
            // for output the xml to a .xml file
            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            
            // setting the transformer up to correctly indent the xml file
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "2");
            DOMSource source = new DOMSource(doc);
 
            // write to a newly created file
            StreamResult file = new StreamResult(new File(fileName + ".xml"));
 
            // write data
            transformer.transform(source, file);
            System.out.println("Written XML");
 
        } catch (Exception e) {
        	// exception thrown (e). Write out the error to the "err" console.
            System.err.println("Error during creating XML file: " + e.getMessage());
        }
	}
	
	// createEnvalope creates the Enveleope element to store the
	//mail from address and the rcpt to address
	/**
	 * createEnvalope creates the Enveleope element to store the
	 * mail from address and the rcpt to address
	 * @param doc The document to create the envelope element in
	 * @param from The mail from address
	 * @param to The rcpt to address
	 * @return header: The newly created header
	 * @author 100385188
	 * @version 1.0
	 * @since 2017-12-08
	 */
	private static Node createEnvnelope(Document doc, String from, String to) {
        Element header = doc.createElement("envnelope");
        
        //create MAIL from element
        header.appendChild(createElementWithID(doc, header, "MAIL From", "from", from));
        
        //create RCPT to element
        header.appendChild(createElementWithID(doc, header, "RCPT to", "to", to));
 
        return header;
    }
	
	// createHeader creates the header element to store the
	//from user's name, the to user's name, the date the email was sent
	//and the subject of the message
	/**
	 *  createHeader creates the header element to store the
	 * from user's name, the to user's name, the date the email was sent
	 * and the subject of the message
	 * 
	 * @param doc The document to create the elements in
	 * @param from The from users email address
	 * @param to The to user's email address
	 * @param date The date the email is to be sent
	 * @param subject The subject of the email
	 * @return header: The newly created header
	 * @author 100385188
	 * @version 1.0
	 * @since 2017-12-08
	 */
    private static Node createHeader(Document doc, String from, String to, String date, String subject) {
        Element header = doc.createElement("header");
        
        //create from element
        header.appendChild(createElementWithoutID(doc, header, "from", from));
        
        //create to element
        header.appendChild(createElementWithoutID(doc, header, "to", to));
        
        //create date element
        header.appendChild(createElementWithoutID(doc, header, "date", date));
        
        //create subject element
        header.appendChild(createElementWithoutID(doc, header, "subject", subject));
 
        return header;
    }

    // createBody creates the body element to store the
 	//text of the email
    /**
     * createBody creates the body node to store the
     * text of the email
     * 
     * @param doc The document to create the elements in
     * @param text The text to write from the email app
     * @return body: The newly created body node
     * @author 100385188
     * @version 1.0
     * @since 2017-12-08
     */
    private static Node createBody(Document doc, String text) {
        Element body = doc.createElement("body");
        
        //create text element and write the text inputted by the user in the xml file
        body.appendChild(createElementWithoutID(doc, body, "text", text));
        
        return body;
    }
	
    // createElementWithID:- utility method to create a text node element
    //in the xml file complete with an ID
    /**
     * createElementWithID:- utility method to create a text node element
     * in the xml file complete with an ID
     * 
     * @param doc The document to create the element in
     * @param element The parent element to apply the element to
     * @param idIn The ID to set the element with
     * @param name The name of the element
     * @param value The value of the element
     * @return node: The newly created node
     * @author 100385188
     * @version 1.0
     * @since 2017-12-08
     */
    private static Node createElementWithID(Document doc, Element element, String idIn, String name, String value) {
        Element node = doc.createElement(name);
        node.setAttribute("id", idIn);
        node.appendChild(doc.createTextNode(value));
        return node;
    }
    
 // createElementWithoutID:- utility method to create a text node element
    //in the xml file without an ID
    /**
     *  createElementWithoutID:- utility method to create a text node element
     * in the xml file without an ID
     * 
     * @param doc The document to create the element in
     * @param element The parent element to add the new element to
     * @param name The name of the element
     * @param value The value of the element
     * @return node: The newly created node
     * @author 100385188
     * @version 1.0
     * @since 2017-12-08
     */
    private static Node createElementWithoutID(Document doc, Element element, String name, String value) {
        Element node = doc.createElement(name);
        node.appendChild(doc.createTextNode(value));
        return node;
    }
}
