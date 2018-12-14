import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.LinkedList;

public class Server_ConCurent {
	public static final String INVITATION = "0";
	public static final String ACCEPTE = "1";
	public static final String REFUSE = "2";
	public static final String MSG_INODATION = "4";
	public static final String MSG_CLIENT = "3";
	public static final String MSG_PUB = "5" ;
	
	public static void main(String[] args) {
		/*
		 * le server pour les client ecoute sur le port tcp 4242
		 * le server pour les promoteur ecoute sur le port tcp 4243
		 */
		int port_tcp_server_client  = 4242 ; 
		int port_tcp_server_promoteur  = 4243 ; 
		LinkedList<struct_donne> list = new LinkedList<struct_donne>(); 

		Server_promoteur server_promoteur = new Server_promoteur(port_tcp_server_promoteur,list);
		Server_client server_client = new Server_client(port_tcp_server_client,list) ; 
		(new Thread(server_promoteur)).start();
		(new Thread(server_client)).start();
		
		
	}
	public static void notification_udp(int port, String msg, int nb_flux) {
		try {
			DatagramSocket dso = new DatagramSocket();
			byte[] data;
			String s = msg + nb_flux;
			data = s.getBytes();
			DatagramPacket paquet = new DatagramPacket(data, data.length, InetAddress.getByName("localhost"), port);
			dso.send(paquet);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
