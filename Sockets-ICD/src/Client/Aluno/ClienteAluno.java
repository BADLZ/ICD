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
				Login(45410);
				break;
			case '2':
				String nome = "Pedro Dias";
				String data = "12/03/1945";
				int numero = 45410;
				Registar(nome, data, numero);
				break;
			case '0':
				break;
			default:
				System.out.println("Opção inválida. Tente outra vez.");
			}
		} while (op != '0');
		System.out.println("Terminou a execução.");
	}

	public static void main(String[] args) {
		@SuppressWarnings("unused")
		ClienteAluno c = new ClienteAluno();
	}

	private void Login(int numero) {
		try {
			String msg = "<?xml version='1.0' encoding='ISO-8859-1' standalone='yes'?>" + "<Registo>" + "<Registar/>"
					+ "</Registo>";
		
			os.println(msg);

			System.out.println("Conectar com o servidor");
			if (!waitMessage()) {
				System.out.println("Servidor não respondeu a tempo");
				return;
			}
			
			String inputline = is.readLine();
			
			if (xmlUtil.verificarResponse(inputline, "Accept.xsd")) {
				os.println(numero);
			}else {
				System.out.println("Servidor não aceitou o login");
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
				System.out.println("Servidor não respondeu a tempo");
				return;
			}
			
			String inputline = is.readLine();
			
			if (xmlUtil.verificarResponse(inputline, "Accept.xsd")) {
				msg = nome + " " + dataNascimento + " " + String.valueOf(numero);
				os.println(msg);
			}else {
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
