import java.util.Scanner;

public class Promoteur_run {

	public static void main(String[] args) {

		Scanner sc = new Scanner(System.in);

		try {
			Message.print_message_promoteur();
			String port_multidifusion = sc.nextLine();
			sc.reset() ; 
			String ip_multidifusion = sc.nextLine();
			 Message.print_message_client_to_connect() ;
			String host_server = sc.nextLine(); //  c'est au promoteur d ecrire ça 
			int port_server_tcp = Integer.parseInt(sc.nextLine()); // 4243
			Client client_promoteur = new Client(Integer.parseInt(port_multidifusion), ip_multidifusion);
			System.out.println("vous avez enter :"+port_multidifusion + " " + ip_multidifusion +" "+host_server+" "+port_server_tcp);
			EnvoiMulticast multicast = new EnvoiMulticast(Integer.parseInt(port_multidifusion), ip_multidifusion);
			
				client_promoteur.connect_client(host_server, port_server_tcp);
				while(true){
					String msg = sc.nextLine();
					if(msg == null){
						break  ; 
					}
					String msg_split[] = msg.split("\\s");
					if (msg_split[0].equals("PROM")) {
						function_prom(msg_split,multicast);
					}
					if(msg_split[0].equals("PUBL?")){
						function_publ(client_promoteur,msg_split, port_multidifusion, ip_multidifusion);
					}
				}
		} catch (Exception e) {
			e.printStackTrace();
		}
		sc.close();
		
	}

	private static void function_publ(Client client_promoteur, String msg[], String port , String ip) {
		try{
			client_promoteur.write_client(msg[0]+" "+ip+" "+port+" "+msg[1]+"+++");
			String msg_recu_server = client_promoteur.readligne_client() ; 
			System.out.println(msg_recu_server);
		}catch(Exception e){
			e.printStackTrace();
		}
	}

	private static void function_prom(String[] msg_split, EnvoiMulticast multicast) {
		Codage codage = new Codage() ; 
		String msg_to_send = codage.codage_message_promoteur(msg_split[0]+" "+msg_split[1]);
		multicast.send_multicast(msg_to_send);
	}
}
