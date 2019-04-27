package Server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import xml.xmlUtil;

//o123
public class HandleConnectionThread extends Thread {
	
	private final int MAX_RETRIES = 200;
	private Server mainThread;

	private Socket connection;

	private BufferedReader is = null;
	private PrintWriter os = null;

	private DocumentLoader alunosdoc,perguntasDoc;
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
			System.out.println("Threads Active: " + mainThread.alunos);
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
					}else if (xmlUtil.verificarResponse(inputLine, "registo.xsd")) {
						pedidoRegisto();
					}else if (xmlUtil.verificarResponse(inputLine, "dateVerification.xsd")) {
						pedidoVericacaoData();
					}else if (xmlUtil.verificarResponse(inputLine, "login.xsd")) {
						pedidoLogin();
					}else if(xmlUtil.verificarResponse(inputLine, "perguntaListar.xsd")){
						pedidoListarPerguntas();
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

	private void pedidoListarPerguntas() {
		Document d = perguntasDoc.getAlunosDoc();
		Element root = d.getDocumentElement();
		NodeList l = root.getElementsByTagName("");
		
	}

	private void pedidoLogin() {
		String s = "<?xml version='1.0' encoding='ISO-8859-1' standalone='yes'?>" + "<Permissao>" + "<True/>"
				+ "</Permissao>";
		try {
			Thread.sleep(100);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
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
				if (Login.alunoExiste(alunosdoc.getAlunosDoc(), r)) {
					mainThread.alunos.add(r);
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

		String lista = mainThread.alunos.toString();
		os.println(lista);
	}

	private void pedidoVericacaoData() {
		String s = "<?xml version='1.0' encoding='ISO-8859-1' standalone='yes'?>" + "<Permissao>" + "<True/>"
				+ "</Permissao>";
		try {
			Thread.sleep(100);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
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
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if (waitMessage()) {
			try {
				String[] second = is.readLine().split("-");
				if (!Login.alunoExiste(alunosdoc.getAlunosDoc(), second[2])) {
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

} // end HandleConnectionThread
