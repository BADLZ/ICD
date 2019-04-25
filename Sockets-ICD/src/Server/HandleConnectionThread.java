package Server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

import xml.xmlUtil;

//o123
public class HandleConnectionThread extends Thread {
	
	private Server mainThread;
	
	// Este nome tem de ser mudado quando o cliente dá login
	public String name = "Peter Days";
	
	private Socket connection;

	private BufferedReader is = null;
	private PrintWriter os = null;

	private DocumentLoader docload;
	@SuppressWarnings("unused")
	private boolean isOn = true;
	
	public HandleConnectionThread(Socket connection, Server mainThread) {
		this.connection = connection;
		docload = new DocumentLoader();
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

				if (inputLine.equals("0")) {
					System.out.println("Thread " + this.getId() + " disconectou-se");
					isOn = false;
					break;
				}

				if (inputLine == null || lastIn.equals(inputLine))
					Thread.sleep(1000);
				else {
					System.out.println("Instrucao -> " + inputLine);
					lastIn = inputLine;

					if (xmlUtil.verificarResponse(inputLine, "registo.xsd")) {
						pedidoRegisto();
						os.println("ACK");
					}
					if (xmlUtil.verificarResponse(inputLine, "login.xsd")) {
						pedidoLogin();
						os.println("ACK");
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
				if (Login.alunoExiste(docload.getAlunosDoc(), r)) {
					System.out.println("Foi Autenticado com sucesso");
				}
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
				String[] info = is.readLine().split("-");
				String[] data = info[1].split("/");
				if (Register.diaMesValido(Integer.parseInt(data[0]), data[1], Integer.parseInt((data[2])))) {
					if (!Login.alunoExiste(docload.getAlunosDoc(), info[2])) {
						Register.registarAluno(info[0], info[1], Integer.parseInt(info[2]));
						System.out.println("Foi registado com sucesso->" + info[0] + " nº" + info[2]);
					} else {
						System.out.println("Utilizador Existe->" + info[2]);
					}
				} else {
					System.out.println("Data não esta certa");
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

} // end HandleConnectionThread
