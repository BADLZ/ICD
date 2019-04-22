package legacyCode;
import java.net.*;
import java.util.ArrayList;

import xml.xmlUtil;

import java.io.*;

public class Servidor extends Thread {
	final int MAX_ALUNOS = 30;
	final int port = 5025;

	ServerSocket serverSocket = null;
	Socket newSock = null;
	BufferedReader is = null;
	PrintWriter os = null;

	ArrayList<Thread> alunos = new ArrayList<Thread>();

	public Servidor() {
		this.start();
		try {
			serverSocket = new ServerSocket(port);

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Executa();
	}

	private void Executa() {
		for (;;) {
			System.out.println("Servidor TCP espera comando obter no porto " + port + "...");
			try {
				newSock = serverSocket.accept();
				System.out.println("Servidor aceitou a ligacao: " + newSock.getRemoteSocketAddress());
				Thread th = new Thread();
				alunos.add(th);
				th.start();
				System.out.println("Thread " + th.getId() + ": " + newSock.getRemoteSocketAddress());
			} catch (IOException e1) {
				e1.printStackTrace();
			}
			try {
				is = new BufferedReader(new InputStreamReader(newSock.getInputStream()));
				os = new PrintWriter(newSock.getOutputStream(), true);

				String inputLine = is.readLine();
				if (xmlUtil.verificarResponse(inputLine, "registo.xsd")) {
					pedidoRegisto();
				}

				if (xmlUtil.verificarResponse(inputLine, "login.xsd")) {
					pedidoLogin();
				}
			} catch (IOException e) {
				System.err.println("erro na ligaçao " + newSock + ": " + e.getMessage());
			} finally {
				// garantir que o socket é fechado
				try {
					if (is != null)
						is.close();
					if (os != null)
						os.close();

					if (newSock != null)
						newSock.close();
				} catch (IOException e) {
				}
			}
		}
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
			is = new BufferedReader(new InputStreamReader(newSock.getInputStream()));
		} catch (IOException e) {
			e.printStackTrace();
		}

		if (waitMessage()) {
			try {
				System.out.println(is.readLine());
			} catch (IOException e) {
				// TODO Auto-generated catch block
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
			is = new BufferedReader(new InputStreamReader(newSock.getInputStream()));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		if (waitMessage()) {
			try {
				System.out.println(is.readLine());
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	private void sendError() {
		String s = "<?xml version='1.0' encoding='ISO-8859-1' standalone='yes'?>" + "<Permissao>" + "<Error/>"
				+ "</Permissao>";
		try {
			Thread.sleep(100);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		os.println(s);

		try {
			is = new BufferedReader(new InputStreamReader(newSock.getInputStream()));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
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

	private boolean register() {
		return false;
	}

	public static void main(String[] args) {
		@SuppressWarnings("unused")
		Servidor s = new Servidor();
	}
}
