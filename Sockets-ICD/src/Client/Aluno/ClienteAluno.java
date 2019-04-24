package Client.Aluno;

import java.io.*;
import java.net.*;

import xml.xmlUtil;

public class ClienteAluno extends Thread {

	final String host = "localhost";
	final int port = 5025;
	private Socket s;
	private BufferedReader is;
	private PrintWriter os;

	public ClienteAluno() {
		this.start();
	}

	public boolean tryConnect() {
		try {
			s = new Socket(host, port);
			is = new BufferedReader(new InputStreamReader(s.getInputStream()));
			os = new PrintWriter(s.getOutputStream(), true);
			Executa();
			return true;
		} catch (IOException e1) {
			return false;
		}
	}

	private void Executa() {
		char op;
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		do {
			System.out.println();
			System.out.println("**** Menu ****");
			System.out.println("0. Terminar");
			System.out.println("1. Login");
			System.out.println("2. Registar");
			System.out.print("> ");
			try {
				op = br.readLine().charAt(0);
			} catch (IOException e) {
				e.printStackTrace();
				break;
			}
			switch (op) {
			case '1':
				Login(45170);
				break;
			case '2':
				String nome = "Pedro Dias";
				String data = "12/03/1945";
				int numero = 55555;
				Registar(nome, data, numero);
				break;
			case '0':
				os.println("0");
				try {
					Thread.sleep(100);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				break;
			default:
				System.out.println("Op��o inv�lida. Tente outra vez.");
			}

			try {
				System.out.println("Servidor > " + is.readLine());
			} catch (IOException e) {
				e.printStackTrace();
			}

		} while (op != '0');
		System.out.println("Terminou a execu��o.");
	}

	public static void main(String[] args) {
		@SuppressWarnings("unused")
		ClienteAluno c = new ClienteAluno();

	}

	private void Login(int numero) {
		try {
			String msg = "<?xml version='1.0' encoding='ISO-8859-1' standalone='yes'?>" + "<Login>" + "<Log/>"
					+ "</Login>";

			os.println(msg);

			System.out.println("Conectar com o servidor");
			if (!waitMessage()) {
				System.out.println("Servidor n�o respondeu a tempo");
				return;
			}

			String inputline = is.readLine();

			if (xmlUtil.verificarResponse(inputline, "accept.xsd")) {
				os.println(numero);
			} else {
				System.out.println("Servidor n�o aceitou o login");
			}

		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	private void Registar(String nome, String dataNascimento, int numero) {
		try {
			String msg = "<?xml version='1.0' encoding='ISO-8859-1' standalone='yes'?>" + "<Registo>" + "<Registar/>"
					+ "</Registo>";

			os.println(msg);

			System.out.println("Conectar com o servidor");
			if (!waitMessage()) {
				System.out.println("Servidor n�o respondeu a tempo");
				return;
			}

			String inputline = is.readLine();

			if (xmlUtil.verificarResponse(inputline, "Accept.xsd")) {
				msg = nome + "-" + dataNascimento + "-" + String.valueOf(numero);
				os.println(msg);
			} else {
				System.out.println("Servidor negou o Registo");
			}

		} catch (Exception ex) {
			ex.printStackTrace();
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
}