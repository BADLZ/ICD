package Server;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;

public class DocumentLoader {
	Document alunosDoc;
	public DocumentLoader(String doc) {
		try {
			DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
			alunosDoc = dBuilder.parse(doc);
			alunosDoc.getDocumentElement().normalize();

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	public Document getInfo() {
		return alunosDoc;
	}
}
