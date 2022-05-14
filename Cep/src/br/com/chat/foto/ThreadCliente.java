package br.com.chat.foto;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;

class ThreadCliente extends Thread {

	private Socket cliente;

	private JTextField txtSaida;
	private JTextField txtServidor;
	private JTextField txtPorta;
	private JTextField txtArquivo;
	private JLabel lblImagem;

	public ThreadCliente(Socket cliente) {
		this.cliente = cliente;
	}

	public void run() {
		try {
			// ObjectInputStream para receber o nome do arquivo
			ObjectInputStream entrada = new ObjectInputStream(cliente.getInputStream());
			DataOutputStream saida = new DataOutputStream(cliente.getOutputStream());
			// Recebe o nome do arquivo
			String arquivo = (String) entrada.readObject();
			// Buffer de leitura dos bytes do arquivo
			byte buffer[] = new byte[512];
			// Leitura do arquivo solicitado
			FileInputStream file = new FileInputStream(arquivo);
			// DataInputStream para processar o arquivo solicitado
			DataInputStream arq = new DataInputStream(file);
			saida.flush();
			int leitura = arq.read(buffer);
			// Lendo os bytes do arquivo e enviando para o socket

			while (leitura != -1) {
				if (leitura != -2) {
					saida.write(buffer, 0, leitura);
				}
				leitura = arq.read(buffer);
			}

			System.out
					.println("Cliente atendido com sucesso: " + arquivo + cliente.getRemoteSocketAddress().toString());

			entrada.close();
			saida.close();
			cliente.close();
		}

		catch (Exception e) {
			System.out.println("Excecao ocorrida na thread: " + e.getMessage());
			try {
				cliente.close();
			}

			catch (Exception ec) {
			}
		}
	}

	private void btnBuscarActionPerformed(java.awt.event.ActionEvent evt) {

		try {
			// Cria o Socket para buscar o arquivo no servidor
			Socket rec = new Socket(txtServidor.getText(), Integer.parseInt(txtPorta.getText()));

			// Enviando o nome do arquivo a ser baixado do servidor
			ObjectOutputStream saida = new ObjectOutputStream(rec.getOutputStream());
			saida.writeObject(txtArquivo.getText());

			// DataInputStream para processar os bytes recebidos
			DataInputStream entrada = new DataInputStream(rec.getInputStream());
			// FileOuputStream para salvar o arquivo recebido
			FileOutputStream sarq = new FileOutputStream(txtSaida.getText());
			byte[] br = new byte[512];
			int leitura = entrada.read(br);
			while (leitura != -1) {
				if (leitura != -2) {
					sarq.write(br, 0, leitura);
				}
				leitura = entrada.read(br);
			}

			saida.close();
			entrada.close();
			sarq.close();
			rec.close();
			ImageIcon img = new ImageIcon(txtSaida.getText());
			lblImagem.setText("");
			lblImagem.setIcon(img);
		}

		catch (Exception e) {
			JOptionPane.showMessageDialog(null, "Exceção:" + e.getMessage(), "Erro", 2);
		}
	}
}
