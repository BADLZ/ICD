package Server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

public class Server {

	public final static int DEFAULT_PORT = 5025;

	static ArrayList<InetAddress> addresses = new ArrayList<InetAddress>();
	static ArrayList<HandleConnectionThread> clients = new ArrayList<HandleConnectionThread>();
	static ServerSocket serverSocket;

	public static void main(String[] args) {
		int port = DEFAULT_PORT;

		if (args.length > 0) {
			try {
				port = Integer.parseInt(args[0]);
			} catch (NumberFormatException e) {
				System.err.println("Erro no porto indicado");
			}
		}

		serverSocket = null;

		try {
			// Criar socket com uma determinada porta
			serverSocket = new ServerSocket(port);
			System.out.println("Servidor aberto na porta: " + port);
			Socket newSock = null;

			for (;;) {
				// Espera connect do cliente
				newSock = serverSocket.accept();
				if (!addresses.contains(newSock.getInetAddress())) {
					addresses.add(newSock.getInetAddress());
					HandleConnectionThread th = new HandleConnectionThread(newSock);
					clients.add(th);
					th.start();
				}
			}
		} catch (IOException e) {
			System.err.println("Excepção no servidor: " + e);
		}

	} // end main

	public static ArrayList<HandleConnectionThread> getAlive() {
		try {
			System.out.println(clients.size());
			for (HandleConnectionThread hct : clients) {
				PrintWriter send = hct.getOs();
				BufferedReader receive = hct.getIs();
				send.println("ping");

				Thread.sleep(150);
				System.out.println("here");
				if (!receive.readLine().equals("alive")) {
					clients.remove(hct);
				}
			}
			return clients;
		} catch (Exception e) {
			System.out.println("Server GetAlive() Error");
			e.printStackTrace();
		}
		return null;
	}
} // end ServidorTCP
