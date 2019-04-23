package Server;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;

public class DocumentLoader {
	Document alunosDoc;
	public DocumentLoader() {
		try {
			DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
			alunosDoc = dBuilder.parse("correctAlunos.xml");
			alunosDoc.getDocumentElement().normalize();

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	public Document getAlunosDoc() {
		return alunosDoc;
	}
}
