package Client.Aluno;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

import xml.xmlUtil;

public class ClienteAluno {

	final String host = "localhost";
	final int port = 5025;
	private Socket s;
	private BufferedReader is;
	private PrintWriter os;

	public ClienteAluno() {
		try {
			s = new Socket(host, port);
			is = new BufferedReader(new InputStreamReader(s.getInputStream()));
			os = new PrintWriter(s.getOutputStream(), true);
		} catch (IOException e1) {
		}
	}

	public boolean Login(String numero) {
		try {
			String msg = "<?xml version='1.0' encoding='ISO-8859-1' standalone='yes'?>" + "<Login>" + "<Log/>"
					+ "</Login>";

			os.println(msg);

			System.out.println("Conectar com o servidor");
			if (!waitMessage()) {
				System.out.println("Servidor não respondeu a tempo");
				return false;
			}

			String inputline = is.readLine();

			if (xmlUtil.verificarResponse(inputline, "accept.xsd")) {
				os.println(numero);
				if (!waitMessage()) {
					System.out.println("Servidor não respondeu a tempo");
					return false;
				}
				
				if(xmlUtil.verificarResponse(is.readLine(), "error.xsd")) {
					return false;
				}
				
				return true;
			} else {
				System.out.println("Servidor não aceitou o login");
			}

		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return false;
	}

	public void Registar(String nome, String dataNascimento, int numero) {
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
			while (!is.ready() && retry < 200) {
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