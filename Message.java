
public class Message {
	
	
	
	static void print_message_client_info(){
		System.out.println("1: Entrez votre id [string] tapez [entrer]  ");
		System.out.println("2: Entrez votre port_udp [int] tapez [entrer] ");
		System.out.println("3: Entrez votre mot de passe [int] tapez [entrer]");
	}
	static void print_message_client_to_connect(){
		System.out.println("1: Entrez l'address du server [string] tapez [entrer]  ");
		System.out.println("2: Entrez le port tcp du server [int] tapez [entrer]  ");
		
	}
	static void print_msg_error_accept_refuse(){
		System.out.println("entrer le mot : OKIRF pour accepter ou NOKRF pour refuser ");
	}
	static void function_info(String id, int port_udp, int mot_de_passe, String host_server,
			int port_server_tcp) {
		System.out.println("id : " + id);
		System.out.println("port_udp : " + port_udp);
		System.out.println("mot de passe  : " + mot_de_passe);
		System.out.println("server connecter : " + host_server);
		System.out.println("port tcp server : " + port_server_tcp);
	}
	static void function_good_msg_invitation(){
		System.out.println("le bon message est FRIE? id_du_client ");
	}
	static void print_message_promoteur(){
		System.out.println("entrer un port multidifusion [String]");
		System.out.println("entrer une adresse ip de multidifusion [String]");
		System.out.println("exemple : 4561 [ENTRE] 225.1.2.4 [ENTRE]");
		
	}
	

}
