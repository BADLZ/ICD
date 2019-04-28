package legacyCode;

import java.util.Arrays;
import java.util.Scanner;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.w3c.dom.Document;
import org.w3c.dom.NodeList;

public class Codigo {
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
}
