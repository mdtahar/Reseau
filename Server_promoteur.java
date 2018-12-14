
import java.net.ServerSocket;
import java.net.Socket;
import java.util.LinkedList;

public class Server_promoteur implements Runnable {

	private int port_tcp ;
	LinkedList<struct_donne> list ; 

	
	public Server_promoteur(int port , LinkedList<struct_donne> list ) {
		this.port_tcp = port ; 
		this.list = list ; 
	}
	@Override
	public void run() {
		ServerSocket server = null ;
		try {
			server = new ServerSocket(this.port_tcp);
			while(true){
				Socket promoteur = server.accept();
				System.out.println("promoteur connecter ");
				(new Thread(new Server_promoteur_thread(promoteur,this.list))).start();
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		
	}

}
