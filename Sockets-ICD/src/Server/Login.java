package Server;

import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.w3c.dom.Document;

public class Login {

	// //aluno[@numero="45170"]/nome
	public static boolean alunoExiste(Document doc, String numero) {

		XPath xpath = XPathFactory.newInstance().newXPath();
		String expressao = "//@numero='" + numero + "'";
		String ret = null;
		try {
			ret = (String) xpath.evaluate(expressao, doc, XPathConstants.STRING);

		} catch (XPathExpressionException e) {
			e.printStackTrace();
		}

		if (ret.equals("true")) {
			return true;
		} else {
			System.out.println("Aluno não encontrado");
			return false;
		}
	}

	// já se assume que o login foi aceite
	public static void welcome(Document doc, int numero) {
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
