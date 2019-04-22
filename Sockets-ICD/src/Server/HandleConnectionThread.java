package Server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

import Client.Aluno.ClienteAluno;
import xml.xmlUtil;


public class HandleConnectionThread extends Thread {

    private Socket connection;
    
    private BufferedReader is = null;
    private PrintWriter os = null;

    public HandleConnectionThread (Socket connection) {
        this.connection = connection;
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
	        	
	        	if (inputLine == null || lastIn.equals(inputLine))
	        		Thread.sleep(1000);
	        	else {
	        		System.out.println("Instrucao -> " + inputLine);
		        	lastIn = inputLine;
		        	
		        	if (xmlUtil.verificarResponse(inputLine, "registo.xsd")) {
		        		pedidoRegisto ();
		        	} else
		        		System.out.println("Servidor recusou o registo");
		        	
		        	if (xmlUtil.verificarResponse(inputLine, "login.xsd")) {
		        		pedidoLogin ();
		        	}
		        	
	        	}
	        }
            
            
            //os.println("@" + inputLine.toUpperCase());
        }
        catch (IOException e) {
            System.err.println("Erro na ligaçao " + connection + ": " + e.getMessage());
        } catch (InterruptedException e) {
			e.printStackTrace();
		}
        finally {
            // garantir que o socket é fechado
            try {
                if (is != null) is.close();  
                if (os != null) os.close();

                if (connection != null) connection.close();                    
            } catch (IOException e) { } 
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
				Login l = new Login(Integer.parseInt(r));
				if(l.verifica()) {
					
				}
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
			is = new BufferedReader(new InputStreamReader(connection.getInputStream()));
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

