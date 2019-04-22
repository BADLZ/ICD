package xml;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.StringReader;

import javax.xml.XMLConstants;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;


public class xmlUtil {
	
	public static boolean Validar(Document document, String xsdFile) {
		Schema schema = null;
		try {
			String language = XMLConstants.W3C_XML_SCHEMA_NS_URI;
			SchemaFactory factory = SchemaFactory.newInstance(language);
			schema = factory.newSchema(new File(xsdFile));
			Validator validator = schema.newValidator();
			validator.validate(new DOMSource(document)); // se falhar existe
															// excepção
			return true;
		} catch (Exception e) {
			return false;
		}
	}

	/* escreve um dom para um stream de output */
	
	public static final void writeDocument(Document input, OutputStream output) {
        try {
        	DOMSource domSource = new DOMSource(input);
        	StreamResult resultStream = new StreamResult(output);
        	TransformerFactory transformFactory = TransformerFactory.newInstance();
        	Transformer transformer = transformFactory.newTransformer();
        	try {
        		transformer.transform(domSource, resultStream);
        	} catch (javax.xml.transform.TransformerException e) {
        	}
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
	
	/* lê um dom do de um stream de input */
	
	public static final Document readDocument(InputStream input) {
		// create a new DocumentBuilderFactory
	      DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
	      Document doc=null;
	      try {
	         // use the factory to create a documentbuilder
	         DocumentBuilder builder = factory.newDocumentBuilder();
	         doc = builder.parse(input);
	      } catch (Exception ex) {
	         ex.printStackTrace();
	      }
	      return doc;
	}
	public static final Document readDocument(String input) {
		// create a new DocumentBuilderFactory
	      DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
	      Document doc=null;
	      try {
	         // use the factory to create a documentbuilder
	         DocumentBuilder builder = factory.newDocumentBuilder();
	         doc = builder.parse(input);
	      } catch (Exception ex) {
	         ex.printStackTrace();
	      }
	      return doc;
	}
	protected static boolean existsAccount(String name) {
		Document document = readDocument("alunos.xml");
		
		NodeList allPlayers = document.getElementsByTagName("nome");
		
		for (int i = 0; i < allPlayers.getLength(); i++) {
			
			Node databaseName = allPlayers.item(i);
			if (databaseName.getNodeType() == Node.ELEMENT_NODE && databaseName.getTextContent().equals(name)) {
				return true;
			}
		}
		
		return false;
	}

	public static boolean verificarResponse(String xml, String protocoloXSD) {
		String Listar = xml;
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		DocumentBuilder builder = null;
		Document document = null;

		try {
			builder = factory.newDocumentBuilder();
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
			return false;
		}
		try {
			document = builder.parse(new InputSource(new StringReader(Listar)));
		} catch (SAXException | IOException e) {
			e.printStackTrace();
			System.out.println("Não foi possivel verificar a resposta do servidor!");
			return false;
		}
		if (!xmlUtil.Validar(document, "Protocolos\\" + protocoloXSD)) {
			return false;
		} else {
			return true;
		}
	}

}
