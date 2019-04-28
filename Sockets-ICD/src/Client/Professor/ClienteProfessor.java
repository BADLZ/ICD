package Client.Professor;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Arrays;
import java.util.Scanner;

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
	}

	public void showQuestions(Document doc) {

		int numbQuestions = 0;

		NodeList perguntas = doc.getElementsByTagName("perguntas");

		for (int i = 0; i < perguntas.getLength(); i++) {
			Element pergunta = (Element) perguntas.item(i);
			NodeList texto = pergunta.getElementsByTagName("texto");

			for (int j = 0; j < texto.getLength(); j++) {
				System.out.println(numbQuestions + " - " + texto.item(j).getTextContent());
				numbQuestions++;
			}
		}

	}

	public String getAlunos() {
		try {
			String msg = "<?xml version='1.0' encoding='ISO-8859-1' standalone='yes'?>" + "<Listar>" + "<Alunos/>"
					+ "</Listar>";

			os.println(msg);
			os.println();

			if (!waitMessage()) {
				System.out.println("Servidor não respondeu a tempo");
				return null;
			}

			String inputline = is.readLine();
			return inputline;

		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return null;
	}

	// //pergunta[@id='2']/texto/text()
	public String getQuestionsFromServer() {
		try {
			String msg = "<?xml version='1.0' encoding='ISO-8859-1' standalone='yes'?>" + "<Listar>" + "<Perguntas/>"
					+ "</Listar>";

			os.println(msg);

			if (!waitMessage()) {
				System.out.println("Servidor não respondeu a tempo");
				return null;
			}
			
			String inputline = is.readLine();
			return inputline;
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return null;
	}

	public void SendQuestions(String question, String student) {
		String msg = "<?xml version='1.0' encoding='ISO-8859-1' standalone='yes'?>" + "<Questoes>" + "<Enviar/>"
				+ "</Questoes>";

		os.println(msg);
		
		try {
			Thread.sleep(50);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		os.println(question+"-"+student);
	}

	private boolean waitMessage() {
		int retry = 0;
		try {
			while (!is.ready() && retry < 500) {
				retry++;
				Thread.sleep(10);
			}
			if (retry == 500)
				return false;
			else
				return true;
		} catch (IOException | InterruptedException e) {
			System.out.println("Wait message error");
			return false;
		}
	}
}