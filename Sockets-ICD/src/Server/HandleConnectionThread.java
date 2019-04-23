package Server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Arrays;

import xml.xmlUtil;

//o123
public class HandleConnectionThread extends Thread {

	private Socket connection;

	private BufferedReader is = null;
	private PrintWriter os = null;

	private DocumentLoader docload;
	@SuppressWarnings("unused")
	private boolean isOn = true;

	public HandleConnectionThread(Socket connection) {
		this.connection = connection;
		docload = new DocumentLoader();
	}

	public void run() {

		try {
			// circuito virtual estabelecido: socket cliente na variavel newSock
			System.out.println("Thread " + this.getId() + ": " + connection.getRemoteSocketAddress());

			is = new BufferedReader(new InputStreamReader(connection.getInputStream()));

			os = new PrintWriter(connection.getOutputStream(), true);

			String inputLine = "";
			String lastIn = "";
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
					} else
						System.out.println("Servidor recusou o registo");

					if (xmlUtil.verificarResponse(inputLine, "login.xsd")) {
						pedidoLogin();
						os.println("ACK");
					}

				}
			}

			// os.println("@" + inputLine.toUpperCase());
		} catch (IOException e) {
			System.err.println("Erro na liga�ao " + connection + ": " + e.getMessage());
		} catch (InterruptedException e) {
			e.printStackTrace();
		} finally {
			// garantir que o socket � fechado
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
				if (Login.alunoExiste(docload.getAlunosDoc(), Integer.parseInt(r))) {
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
				String[] info = is.readLine().split(" ");
				//12/03/1945
				String[] data = info[1].split("/");
				if(Register.diaMesValido(Integer.parseInt(data[0]), data[1], 
						Integer.parseInt((data[2])))) {
					if(Login.alunoExiste(docload.getAlunosDoc(), Integer.parseInt(info[2]))) {
						
					}else {
						System.out.println("Utilizador Existe");
					}
				}else {
					System.out.println("Data n�o esta certa");
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
