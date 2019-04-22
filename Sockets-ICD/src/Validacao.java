import java.io.File;
import java.util.Scanner;

import javax.xml.XMLConstants;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Source;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;

import org.w3c.dom.Document;
import org.xml.sax.SAXException;

public class Validacao {

	public static void main(String[] args) throws SAXException {
		
		Validacao v = new Validacao();
		
		Document doc = v.xmlDocument();
		
		System.out.println(v.validXML(doc, "alunos.xsd", XMLConstants.W3C_XML_SCHEMA_NS_URI));
		
		// validarXSD(doc, "poem.xsd", XMLConstants.W3C_XML_SCHEMA_NS_URI));

	}
	
	public boolean validXML(Document xml, String xsd, String type) throws SAXException {
		
		if (xml==null) {
			return false;
		}
		//xml = xmlDocument();

		// create a SchemaFactory capable of understanding WXS schemas
		SchemaFactory factory = SchemaFactory.newInstance(type);

		// load a WXS schema, represented by a Schema instance
		Source schemaFile = new StreamSource(new File(xsd));
		Schema schema = factory.newSchema(schemaFile);

		// create a Validator instance, which can be used to validate an instance document
		Validator validator = schema.newValidator();

		// validate the DOM tree
		try {
		    validator.validate(new DOMSource(xml));
		    return true;
		} catch (Exception e) {
		    // instance document is invalid!
			System.out.println("XML inválido!");
			return false;
		}		
		
		//return true;
	}
	
	public Document xmlDocument() {
		
		Scanner sc = new Scanner(System.in);
		
		System.out.print("Documento XML? ");
		String nameXML = sc.nextLine();
		
		Document doc = null;
		try {
			DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
			doc = dBuilder.parse(nameXML);			
			doc.getDocumentElement().normalize();			
		}
		catch (Exception e) {
			System.out.println("Documento não encontrado");
		}
		
		sc.close();		
		return doc;
		
	}

}
