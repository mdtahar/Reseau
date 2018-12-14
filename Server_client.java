import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.LinkedList;

public class Server_client implements Runnable{
	private int port_tcp ;
	private LinkedList<struct_donne> list ; 
	
	 public Server_client(int port_tcp ,LinkedList<struct_donne> list) {
		 this.port_tcp = port_tcp ; 
		 this.list = list ; 
	}

	@Override
	public void run() {
		ServerSocket serversocket = null;
		try {
			serversocket = new ServerSocket(this.port_tcp);
//			LinkedList<struct_donne> list = new LinkedList<struct_donne>(); 
			while (true) {
				Socket clientConso = serversocket.accept();
				System.out.println("client connecter");
				(new Thread(new Server_client_thread(clientConso,this.list))).start();
			}

		} catch (IOException e) {

			e.printStackTrace();
		}

	}
		
	}


