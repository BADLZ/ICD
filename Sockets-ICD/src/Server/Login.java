package Server;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.w3c.dom.Document;

public class Login {
	Document doc = null;
	private int numero;

	public Login(int numero) {
		this.numero = numero;
		try {
			DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
			doc = dBuilder.parse("correctAlunos.xml");
			doc.getDocumentElement().normalize();

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public boolean verifica() {
		if (alunoExiste(doc, numero))
			return true;
		return false;

	}

	// //aluno[@numero="45170"]/nome
	public boolean alunoExiste(Document doc, int numero) {

		XPath xpath = XPathFactory.newInstance().newXPath();
		String expressao = "//@numero='" + numero + "'";
		String ret = null;
		try {
			ret = (String) xpath.evaluate(expressao, doc, XPathConstants.STRING);

		} catch (XPathExpressionException e) {
			e.printStackTrace();
		}

		if (ret.equals("true")) {
			welcome(doc, numero);
			return true;
		} else {
			System.out.println("Aluno não encontrado");
			return false;
		}

	}

	// já se assume que o login foi aceite
	public void welcome(Document doc, int numero) {
		XPath xpath = XPathFactory.newInstance().newXPath();
		String expressao = "//aluno[@numero='" + numero + "']/nome";
		String ret = null;
		try {
			ret = (String) xpath.evaluate(expressao, doc, XPathConstants.STRING);

		} catch (XPathExpressionException e) {
			e.printStackTrace();
		}

		System.out.println("Olá " + ret);
		System.out.println("Boa sorte com o questionário!");

	}

}
