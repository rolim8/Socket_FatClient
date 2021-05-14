import java.util.*;
import java.io.*;
import java.net.*;

public class ClienteGordo extends Thread {

	private Socket socket;

	public ClienteGordo(String host, int porta) throws Exception {

		this.socket = new Socket(host, porta);
	}

	public void run() {
		try {
			
			// Envia dados para o servidor via socket
			PrintWriter saida = new PrintWriter(socket.getOutputStream(), true);
			
			// Lê os dados do do servidor via socket
			Scanner entrada = new Scanner(socket.getInputStream());
			
			// Leitor do teclado
			Scanner leitor = new Scanner(System.in);

			while (true) {
				
				// Se for vazia a linha  lida "query", então interrompa o  cliente e a conexão com o servidor.
				String query = leitor.nextLine();
				
				//
				if (query.equals(""))
					break;
				
				// Envia query para o servidor
				saida.println(query);
				
				// Mostra na tela a resposta lida do servidor
				System.out.println(entrada.nextLine());
			}
			
			// Fecha a conexão com o servidor
			socket.close();
			
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	public static void main(String args[]) throws Exception {
		ClienteGordo c = new ClienteGordo("localhost", 8080);

		c.start();
	}

}
