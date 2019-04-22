package Client.Professor;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

public class ClienteProfessor extends Thread {

	
	final int port = 5025;
	final String host = "localhost";
	private Socket s;
	private BufferedReader is;
	private PrintWriter os;

	public ClienteProfessor() {
		this.start();
		try {
			s = new Socket(host, port);				
			is = new BufferedReader(new InputStreamReader(s.getInputStream()));
			os = new PrintWriter(s.getOutputStream(), true);
		} catch (Exception e) {
			System.err.println("Connection failed. " + e.getMessage());
			return;
		}
		Executa();
	}
	
	private void Executa() {
		
		Document doc = null;
		try {
			DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
			doc = dBuilder.parse("perguntas.xml");			
			doc.getDocumentElement().normalize();			
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		
		char op;
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		do {
			System.out.println();
			System.out.println("** Menu **");
			System.out.println("0. Terminar");
			System.out.println("1. Escolher Pergunta");
			System.out.print("--> ");
			try {
				op = br.readLine().charAt(0);
			} catch (IOException e) {
				e.printStackTrace();
				break;
			}
			switch (op) {
			case '1':
				showQuestions(doc);
				break;
			case '0':
				break;
			default:
				System.out.println("Opção inválida. Tente outra vez.");
			}
		} while (op != '0');
		System.out.println("Terminou a execução.");
	}
	
	public void showQuestions(Document doc) {		
		
		int numbQuestions = 0;
		
		NodeList perguntas = doc.getElementsByTagName("perguntas");
		
		for (int i = 0; i < perguntas.getLength(); i++) {
			Element pergunta = (Element) perguntas.item(i);				
			NodeList texto = pergunta.getElementsByTagName("texto");
			System.out.println(numbQuestions + " - " + texto);
			numbQuestions++;
			
		}
	
	}
	
	
	// //pergunta[@id='2']/texto/text()
	public String choseQuestion(Document doc, int numb) {
		
		XPath xpath = XPathFactory.newInstance().newXPath();
		String expressao = "//pergunta[@id='" + numb + "']/texto/text()";
		String pergunta = null;
		
		try {
			pergunta = (String) xpath.evaluate(expressao, doc, XPathConstants.STRING);

		} catch (XPathExpressionException e) {
			e.printStackTrace();
		}
		
		return pergunta;
	}
	public static void main(String[] args) {
		@SuppressWarnings("unused")
		ClienteProfessor c = new ClienteProfessor();
	}
}