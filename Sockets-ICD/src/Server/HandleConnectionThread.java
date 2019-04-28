package Server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.w3c.dom.Document;
import org.w3c.dom.NodeList;

import xml.xmlUtil;

//o123
public class HandleConnectionThread extends Thread {

	private final int MAX_RETRIES = 200;
	private Server mainThread;

	private Socket connection;

	private BufferedReader is = null;
	private PrintWriter os = null;

	private static String questaoAtual = "";
	private DocumentLoader alunosdoc, perguntasDoc;
	@SuppressWarnings("unused")
	private boolean isOn = true;

	public HandleConnectionThread(Socket connection, Server mainThread) {
		this.connection = connection;
		alunosdoc = new DocumentLoader("correctAlunos.xml");
		perguntasDoc = new DocumentLoader("perguntas.xml");

		this.mainThread = mainThread;
	}

	public void run() {

		try {
			// circuito virtual estabelecido: socket cliente na variavel newSock
			System.out.println("Thread " + this.getId() + ": " + connection.getRemoteSocketAddress());

			is = new BufferedReader(new InputStreamReader(connection.getInputStream()));

			os = new PrintWriter(connection.getOutputStream(), true);

			String inputLine = "";
			String lastIn = "";
			System.out.println("Threads Active: " + mainThread.getAlunos());
			for (;;) {
				inputLine = is.readLine();
				System.out.println("Instrucao -> " + inputLine);

				if (inputLine.equals("")) {
					continue;
				}

				if (inputLine.equals("0")) {
					System.out.println("Thread " + this.getId() + " disconectou-se");
					isOn = false;
					break;
				}

				if (inputLine == null || lastIn.equals(inputLine))
					Thread.sleep(1000);
				else {
					if (xmlUtil.verificarResponse(inputLine, "listar.xsd")) {
						pedidoListarAlunos();
					} else if (xmlUtil.verificarResponse(inputLine, "registo.xsd")) {
						pedidoRegisto();
					} else if (xmlUtil.verificarResponse(inputLine, "dateVerification.xsd")) {
						pedidoVericacaoData();
					} else if (xmlUtil.verificarResponse(inputLine, "login.xsd")) {
						pedidoLogin();
					} else if (xmlUtil.verificarResponse(inputLine, "perguntasListar.xsd")) {
						pedidoListarPerguntas();
					} else if (xmlUtil.verificarResponse(inputLine, "enviarPerguntas.xsd")) {
						enviarPerguntas();
					} else if (xmlUtil.verificarResponse(inputLine, "responderPergunta.xsd")) {
						responderPergunta();
					}
				}
			}

		} catch (IOException e) {
			System.err.println("Erro na ligaçao " + connection + ": " + e.getMessage());
		} catch (InterruptedException e) {
			e.printStackTrace();
		} finally {
			// garantir que o socket é fechado
			try {
				if (is != null)
					is.close();
				if (os != null)
					os.close();

				if (connection != null)
					connection.close();
			} catch (IOException e) {
			}
		}
	} // end run

	private void responderPergunta() {
		String s = "<?xml version='1.0' encoding='ISO-8859-1' standalone='yes'?>" + "<Permissao>" + "<True/>"
				+ "</Permissao>";
		try {
			Thread.sleep(100);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		os.println(s);

		if (!waitMessage()) {
			System.out.println("Servidor não respondeu a tempo");
			return;
		}

		try {
			String qnumber = is.readLine();

			XPath xpath = XPathFactory.newInstance().newXPath();
			System.out.println(questaoAtual);
			String answer = "//pergunta[texto='" + questaoAtual + "']/respostaCerta/text()";
			String valor = null;
			try {
				valor = (String) xpath.evaluate(answer, perguntasDoc.getInfo(), XPathConstants.STRING);
			} catch (XPathExpressionException e) {
				e.printStackTrace();
			}
			if(!qnumber.equalsIgnoreCase(valor)) {
				os.println("ERROR");
			}
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	private void enviarPerguntas() {
		Document doc = perguntasDoc.getInfo();
		HashMap<String, HandleConnectionThread> h = mainThread.getAlunos();

		try {
			String[] s = is.readLine().split("-");
			StringBuilder ops = new StringBuilder();
			for (int i = 1; i < 4; i++) {
				XPath xpath = XPathFactory.newInstance().newXPath();
				String opcoes = "//pergunta[texto='" + s[0] + "']/opcoes/opcao[" + i + "]";
				String valor = null;
				try {
					valor = (String) xpath.evaluate(opcoes, doc, XPathConstants.STRING);
				} catch (XPathExpressionException e) {
					e.printStackTrace();
				}
				ops.append(valor);
				ops.append("-");
			}
			questaoAtual = s[0];
			ops.insert(0, s[0] + "-");
			if (s[1].equalsIgnoreCase("todos")) {
				for (Map.Entry<String, HandleConnectionThread> entry : h.entrySet()) {
					entry.getValue().getOs().println(ops.toString());
				}
			} else {
				for (Map.Entry<String, HandleConnectionThread> entry : h.entrySet()) {
					if (entry.getKey().equalsIgnoreCase(s[1])) {
						entry.getValue().getOs().println(ops.toString());

					}
				}
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	private void pedidoListarPerguntas() {
		Document doc = perguntasDoc.getInfo();

		StringBuilder out = new StringBuilder();

		NodeList p = doc.getElementsByTagName("pergunta");
		for (int i = 0; i < p.getLength(); i++) {
			XPath xpath = XPathFactory.newInstance().newXPath();
			String expressao = "/perguntas/pergunta[" + (i + 1) + "]/@categoria";
			String perguntaTitleexp = "/perguntas/pergunta[" + (i + 1) + "]/texto";
			String ret = null;
			String ret1 = null;
			try {
				ret1 = (String) xpath.evaluate(perguntaTitleexp, doc, XPathConstants.STRING);
				ret = (String) xpath.evaluate(expressao, doc, XPathConstants.STRING);
			} catch (XPathExpressionException e) {
				e.printStackTrace();
			}
			out.append(ret1 + "-" + ret + "_");
		}
		os.println(out);
	}

	private void pedidoLogin() {
		String s = "<?xml version='1.0' encoding='ISO-8859-1' standalone='yes'?>" + "<Permissao>" + "<True/>"
				+ "</Permissao>";
		try {
			Thread.sleep(100);
		} catch (InterruptedException e) {

			e.printStackTrace();
		}
		os.println(s);

		try {
			is = new BufferedReader(new InputStreamReader(connection.getInputStream()));
		} catch (IOException e) {
			e.printStackTrace();
		}

		if (waitMessage()) {
			try {
				String r = is.readLine();
				if (Login.alunoExiste(alunosdoc.getInfo(), r)) {
					mainThread.getAlunos().put(r, this);
					os.println("<?xml version='1.0' encoding='ISO-8859-1' standalone='yes'?>" + "<Permissao>"
							+ "<Enter/>" + "</Permissao>");
					System.out.println("Novo aluno foi adicionado ao servidor");
				} else {
					s = "<?xml version='1.0' encoding='ISO-8859-1' standalone='yes'?>" + "<Permissao>" + "<Error/>"
							+ "</Permissao>";
					os.println(s);
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	private void pedidoListarAlunos() {

		Set<String> lista = mainThread.getAlunos().keySet();
		StringBuilder list = new StringBuilder();
		for (String s : lista) {
			list.append(s);
			list.append("-");
		}
		os.println(list);
	}

	private void pedidoVericacaoData() {
		String s = "<?xml version='1.0' encoding='ISO-8859-1' standalone='yes'?>" + "<Permissao>" + "<True/>"
				+ "</Permissao>";
		try {
			Thread.sleep(100);
		} catch (InterruptedException e) {

			e.printStackTrace();
		}
		os.println(s);

		try {
			is = new BufferedReader(new InputStreamReader(connection.getInputStream()));
		} catch (IOException e) {
			e.printStackTrace();
		}

		if (waitMessage()) {
			try {
				String[] first = is.readLine().split("/");
				if (!Register.diaMesValido(Integer.parseInt(first[0]), first[1], Integer.parseInt(first[2]))) {
					s = "<?xml version='1.0' encoding='ISO-8859-1' standalone='yes'?>" + "<Permissao>" + "<Error/>"
							+ "</Permissao>";
					os.println(s);
				}
				System.out.println("Fine with date verification");
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	private void pedidoRegisto() {
		String s = "<?xml version='1.0' encoding='ISO-8859-1' standalone='yes'?>" + "<Permissao>" + "<True/>"
				+ "</Permissao>";
		try {
			Thread.sleep(100);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		os.println(s);

		try {
			is = new BufferedReader(new InputStreamReader(connection.getInputStream()));
		} catch (IOException e) {
			e.printStackTrace();
		}
		if (waitMessage()) {
			try {
				String[] second = is.readLine().split("-");
				if (!Login.alunoExiste(alunosdoc.getInfo(), second[2])) {
					Register.registarAluno(second[0], second[1], Integer.parseInt(second[2]));
				} else {
					os.println("error");
					return;
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	private boolean waitMessage() {
		int retry = 0;
		try {
			while (!is.ready() && retry < MAX_RETRIES) {
				retry++;
				Thread.sleep(10);
			}
			if (retry == MAX_RETRIES)
				return false;
			else
				return true;
		} catch (IOException | InterruptedException e) {
			System.out.println("Wait message error");
			return false;
		}
	}

	public BufferedReader getIs() {
		return is;
	}

	public PrintWriter getOs() {
		return os;
	}

} // end HandleConnectionThread
