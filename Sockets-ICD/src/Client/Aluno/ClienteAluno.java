package Client.Aluno;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

import xml.xmlUtil;

public class ClienteAluno {
	private final int MAX_RETRIES = 350;
	
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

	public String ReceiveQuestion() {
		try {
			return is.readLine();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
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
				
				if (xmlUtil.verificarResponse(is.readLine(), "error.xsd")) {
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

	public boolean VerificarData(String dataNascimento) {
		try {
			String msg = "<?xml version='1.0' encoding='ISO-8859-1' standalone='yes'?>" + "<Verificar>" + "<Verifica/>"
					+ "</Verificar>";

			os.println(msg);

			System.out.println("Conectar com o servidor");
			if (!waitMessage()) {
				System.out.println("Servidor não respondeu a tempo");
				return false;
			}
			String inputline = is.readLine();

			if (xmlUtil.verificarResponse(inputline, "accept.xsd")) {
				os.println(dataNascimento);
				
				if (waitMessage()) {
					return false;
				}
				System.out.println("Fine with date verification");
				return true;
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return false;
	}

	public boolean Registar(String nome, String dataNascimento, String numero) {
		try {
			String msg = "<?xml version='1.0' encoding='ISO-8859-1' standalone='yes'?>" + "<Registo>" + "<Registar/>"
					+ "</Registo>";

			os.println(msg);

			System.out.println("Conectar com o servidor");
			if (!waitMessage()) {
				System.out.println("Servidor não respondeu a tempo");
				return false;
			}

			String inputline = is.readLine();

			if (xmlUtil.verificarResponse(inputline, "accept.xsd")) {
				msg = nome + "-" + dataNascimento + "-" + numero;
				os.println(msg);

				if (waitMessage()) {
					return false;
				}
				return true;
			} else {
				System.out.println("Servidor negou o Registo");
			}

		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return false;
	}

	private boolean waitMessage() {
		int retry = 0;
		try {
			while (!is.ready() && retry < MAX_RETRIES ) {
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

	public boolean answerQuestion(int qnumber) {
		try {
			String msg = "<?xml version='1.0' encoding='ISO-8859-1' standalone='yes'?>" + "<Responder>" + "<Pergunta/>"
					+ "</Responder>";

			os.println(msg);

			System.out.println("Conectar com o servidor");
			if (!waitMessage()) {
				System.out.println("Servidor não respondeu a tempo");
				return false;
			}

			String inputline = is.readLine();

			if (xmlUtil.verificarResponse(inputline, "accept.xsd")) {
				
				os.println(qnumber);

				if (waitMessage()) {
					return false;
				}
				return true;
			} else {
				System.out.println("Servidor negou o Registo");
			}

		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return false;
	}
}