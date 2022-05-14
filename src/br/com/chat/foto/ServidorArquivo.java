package br.com.chat.foto;

import java.net.ServerSocket;
import java.net.Socket;

public class ServidorArquivo {
	
	 public static void main(String[] args) {
	      if(args.length < 1) {
	        System.out.println("Informe a porta a ser ouvida pelos servidor");
	        System.exit(0);
	      }

	      try {
	        //Converte o parametro recebido para int (número da porta)
	        int port = Integer.parseInt(args[0]);
	        System.out.println("Incializando o servidor...");
	        //Iniciliza o servidor
	        ServerSocket serv = new ServerSocket(port);
	        System.out.println("Servidor iniciado, ouvindo a porta " + port);
	        //Aguarda conexões
	        while(true) {
	             Socket clie = serv.accept();
	             //Inicia thread do cliente
	             new ThreadCliente(clie).start();
	        }
	     }
	    catch(Exception e) {
	    	
	    }
	  }
	}
