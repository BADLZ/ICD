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

	private void getAlunos() {
		try {
			String msg = "<?xml version='1.0' encoding='ISO-8859-1' standalone='yes'?>" + "<Listar>" + "<Alunos/>"
					+ "</Listar>";

			os.println(msg);
			os.println();

			if (!waitMessage()) {
				System.out.println("Servidor não respondeu a tempo");
				return;
			}

			String inputline = is.readLine();
			System.out.println(inputline);

		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	// //pergunta[@id='2']/texto/text()
	private String choseQuestion(Document doc, int numb) {

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

	private void questionTo() {

		Scanner kb = new Scanner(System.in);

		System.out.println("Para quem?");
		System.out.println("1 - todos\n2 - aluno em particular");
		int opcao = kb.nextInt();
		while (opcao < 1 || opcao > 2) {
			System.out.print("Opcao errada ");
			opcao = kb.nextInt();
		}

		switch (opcao) {

		case 1:
			System.out.println(Arrays.toString(todosAlunos()));
			break;

		case 2:
			System.out.print("Número do aluno? ");
			int num = kb.nextInt();
			alunoParticular(num);
			break;
		}
	}

	// //aluno[@numero='45170']/nome/text()
	private String alunoParticular(int numeroAluno) {

		Document doc = null;
		try {
			DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
			doc = dBuilder.parse("correctAlunos.xml");
			doc.getDocumentElement().normalize();
		} catch (Exception e) {
			e.printStackTrace();
		}

		XPath xpath = XPathFactory.newInstance().newXPath();
		String expressao = "//aluno[@numero='" + numeroAluno + "']/nome/text()";
		String nomeAluno = null;

		try {
			nomeAluno = (String) xpath.evaluate(expressao, doc, XPathConstants.STRING);

		} catch (XPathExpressionException e) {
			e.printStackTrace();
		}

		System.out.println(nomeAluno);
		return nomeAluno;
	}

	// nome/text()
	private String[] todosAlunos() {

		Document doc = null;
		try {
			DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
			doc = dBuilder.parse("correctAlunos.xml");
			doc.getDocumentElement().normalize();
		} catch (Exception e) {
			e.printStackTrace();
		}

		XPath xpath = XPathFactory.newInstance().newXPath();
		String expressao = "//nome/text()";

		NodeList nodes;
		try {
			nodes = (NodeList) xpath.evaluate(expressao, doc, XPathConstants.NODESET);
			// return nodes;
		} catch (XPathExpressionException e) {
			e.printStackTrace();
			nodes = null;
		}

		if (nodes == null) {
			return null;
		} else {
			String[] retorno = new String[nodes.getLength()];
			for (int i = 0; i < nodes.getLength(); i++) {
				retorno[i] = nodes.item(i).getNodeValue();
			}
			// System.out.println(retorno);
			return retorno;
		}
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

	public static void main(String[] args) {
		@SuppressWarnings("unused")
		ClienteProfessor c = new ClienteProfessor();
	}
}