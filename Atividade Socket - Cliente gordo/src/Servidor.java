import java.io.*;
import java.net.*;
import java.util.*;

public class Servidor extends Thread {

	private Socket clienteSocket;
	private List<String[]> dados = new ArrayList<>();

	public Servidor(Socket cliente) {

		this.clienteSocket = cliente;

	}

	public void run() {
		try {

			this.carregarArquivo();

			while (true) {
				// Continuamente fica recebendo requisições do cliente e consultando os dados
				this.consultarAlunos(clienteSocket);
			}

		} catch (Exception ex) {
			ex.printStackTrace();
		}

	}

	public void carregarArquivo() throws Exception {

		File f = new File("C:\\Users\\Rolim\\Desktop\\Atividade Socket - Cliente gordo\\dados.csv");

		if (f.exists()) {

			try (Scanner fileReader = new Scanner(f)) {

				while (fileReader.hasNext()) {

					dados.add(fileReader.nextLine().trim().split(","));
				}
			}

		} else {

			System.err.println("Arquivo não encontrado!");
		}

	}

	public void consultarAlunos(Socket clienteSocket) throws Exception {

		// Envia dados para o cliente
		PrintWriter saida = new PrintWriter(clienteSocket.getOutputStream(), true);
		
		// Lê dados do cliente
		Scanner sockReader = new Scanner(clienteSocket.getInputStream());

		//Recebe a requisição enviada pelo cliente via socket
		String[] query = sockReader.nextLine().trim().split(",");

		int qSexo = Integer.parseInt(query[0]); // Query Sexo
		String qStatus = query[1].trim(); // Query Status
		int soma = 0;

		for (int m = 1; m < dados.size(); m++) {

			int sexo = Integer.parseInt(dados.get(m)[1]); // Sexo
			String status = dados.get(m)[9]; // Status

			if (sexo == qSexo && status.equals(qStatus))
				soma++;
		}
		
		
        //Mostra na tela do servidor
		System.out.println(String.format("Quantidade de alunos do sexo \'%d\' e status \'%s\': %d", qSexo, qStatus, soma));

		// Devolve o resultado para o cliente via socket
		saida.println(String.format("Quantidade de alunos do sexo \'%d\' e status \'%s\': %d", qSexo, qStatus, soma));
	}

	public static void main(String[] args) throws Exception {

		// Porta onde o servidor irá escutar
		int porta = 8080;

		try {
			// Criar um soket servidor para aguardar conexões de clientes na porta
			ServerSocket servidor = new ServerSocket(porta);

		// Fica num loop infinito aguardando conexões de clientes
			while (true) {

				System.out.println("Aguardando conexão...");

				try {
					//Volta para aguardar até que um novo cliente se conecte
					Socket cliente = servidor.accept();
					//Cria uma nova thread para interagir com o novo cliente que se conectou
			
					Servidor s = new Servidor(cliente);
					
					// Inicia o módulo cliente
					s.start();

				} catch (Exception ex) {
					break;
				}
			}
			// Fecha o socket servidor
			servidor.close();
		} catch (Exception e) {
			System.err.println("Já existe uma cópia do servidor em execução...");
		}
	}

}
