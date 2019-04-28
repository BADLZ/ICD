package legacyCode;

import java.util.HashMap;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class testes {

	public static void main(String args[]) {
//		String msg = "<?xml version='1.0' encoding='ISO-8859-1' standalone='yes'?>" + "<Listar>" + "<Alunos/>"
//				+ "</Listar>";
//		System.out.println(msg);

//		System.out.println(Register.diaMesValido(150, "55", 1999));

		Document doc = null;
		try {
			DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
			doc = dBuilder.parse("perguntas.xml");
			doc.getDocumentElement().normalize();
		} catch (Exception e) {
			e.printStackTrace();
		}

		HashMap<String, String> perguntas = new HashMap<String, String>();

		NodeList p = doc.getElementsByTagName("pergunta");
		int subb = 0;

		for (int i = 0; i < p.getLength(); i++) {
			Node n = p.item(i);
			XPath xpath = XPathFactory.newInstance().newXPath();
			String expressao = "//pergunta[last()-" + subb + "]/@categoria";
			String perguntaTitleexp = "/perguntas/pergunta["+ (i+1) +"]/texto";
			String ret = null;
			String ret1 = null;
			try {
				ret1 = (String) xpath.evaluate(perguntaTitleexp, doc, XPathConstants.STRING);
				ret = (String) xpath.evaluate(expressao, doc, XPathConstants.STRING);
			} catch (XPathExpressionException e) {
				e.printStackTrace();
			}
			subb++;
			perguntas.put(ret, ret1);
		}

//		try {
//			Thread.sleep(100);
//		} catch (InterruptedException e) {
//
//			e.printStackTrace();
//		}

	}
}
