import java.util.Scanner;


public class Client_run {

	public static void main(String[] args) {
		Codage codage = new Codage(); 
		String id = "bachir"; // a modifier
		int port_udp = 4242;
		int mot_de_passe = 45687; // en litel indien
		String host_server = "localhost";
		int port_server_tcp = 4242;
		
		
		
		Message.print_message_client_info();
		Scanner sc2 = new Scanner(System.in);
		id = sc2.nextLine() ; 
		port_udp = Integer.parseInt(sc2.nextLine());
		mot_de_passe = Integer.parseInt(sc2.nextLine());
		System.out.println("vous avez entrer : id = "+id+" port_udp :"+port_udp+" mot de passe :"+mot_de_passe);
		Message.print_message_client_to_connect() ; 
		host_server = sc2.nextLine(); 
		port_server_tcp = Integer.parseInt(sc2.nextLine());
		Scanner sc = new Scanner(System.in);
		Client client = new Client(id, port_udp, mot_de_passe);
		
		
		try {
			client.connect_client(host_server, port_server_tcp);
			Client_udp_thread client_udp = new Client_udp_thread(port_udp);
			Thread thread_udp = new Thread(client_udp);
			thread_udp.start();
			boolean connect = false;
			while (true) {

				String msg = sc.nextLine();

				String msg_split[] = msg.split("\\s");
				if (msg_split[0].equals("REGIS")) {
					if (!function_regist(msg, client)) {
						System.exit(0);
					}
					connect = true;
				}
				if (msg_split[0].equals("CONNE")) {
					if (!function_conne(msg, client)) {
						break;
					}
					connect = true;
				}
				if (msg_split[0].equals("INFO") || msg_split[0].equals("info")) {
					Message.function_info(id, port_udp, mot_de_passe, host_server, port_server_tcp);
				}
				if (connect) {
					if (msg_split[0].equals("IQUIT")) {
						function_iquit(msg, client);
						break;
					}
					if (msg_split[0].equals("FRIE?")) {
						function_frien(msg_split, client);

					}
					if (msg_split[0].equals("debug")||msg_split[0].equals("DEBUG")) {
						function_debug("debug", client);
					}
					if (msg_split[0].equals("LIST?")) {
						function_list(msg_split, client);
					}
					if (msg_split[0].equals("CONSU")) {
						function_consu(msg_split[0], client, sc);
					}
					if (msg_split[0].equals("MESS?")) {
						function_msg_amis(msg_split, client, sc);
					}

				} else {
					System.out.println("vous devez vous connectez ou vous enregistrez ");
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		client.close_client();
		sc.close();
	}

	private static void function_msg_amis(String[] msg_split, Client client, Scanner sc) {
		try {
			System.out.println("pour qui ? (identifiant) : ");
			String id = sc.nextLine();
			System.out.println("ecrire votre message : ");
			String msg_to_send = sc.nextLine();
			int msg_size = msg_to_send.length();
			int nb_msg = (msg_size) / 200 + 1; // je dois rajouter +1
			int counter = 0;
			int counter_bas = 0;
			int counter_haut = 200;
			client.write_client("MESS? " + id + " " + nb_msg + "+++");
			while (counter < nb_msg) { // ça rentre pas je suis la
				client.write_client(
						"MENUM" + " " + counter + " " + msg_to_send.substring(0 + counter_bas, msg_size) + "+++"); // mazale dayi attention
				counter++;
				counter_bas += 200;
				counter_haut += 200;

			}
			System.out.println(client.readligne_client());
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	private static void function_consu(String msg, Client client, Scanner sc) {

		try {
			client.write_client(msg + "+++");
			String msg_recu = client.readligne_client();
			System.out.println(msg_recu);
			String[] msg_split = msg_recu.split("\\s");
			if (msg_split[0].equals("NOCON+++")) {
				return;
			}
			if (msg_split[0].equals("EIRF>")) {
				while (true) {
					String msg_to_send = sc.nextLine();

					if (msg_to_send.equals("OKIRF") || msg_to_send.equals("NOKRF")) {
						client.write_client(msg_to_send + "+++");
						msg_recu = client.readligne_client();
						System.out.println(msg_recu);
						break;
					} else {
						Message.print_msg_error_accept_refuse();
					}
				}
			}
			if (msg_split[0].equals("SSEM>")) {
				System.out.println("dans le if de SSEM> ");

				int counter = 0;
				int nb_message = Integer.parseInt(msg_split[2].substring(0, msg_split[2].length() - 3));
				while (counter < nb_message) {
					System.out.println(client.readligne_client());
					counter++;
				}
			}
			if(msg_split[0].equals("PUBL?")){
				function_publ(msg_split);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

	}
	private static void function_publ(String []msg_split){
		try{
			if(msg_split.length == 4){
				int port = Integer.parseInt(msg_split[2]); 
				String ip =msg_split[1]; 
					new Thread(new ReceiveMulticast(port, ip)).start();
			}
			
		}catch(Exception e){
			e.printStackTrace(); 
		}
	}

	private static void function_list(String[] msg_split, Client client) {
		try {
			client.write_client(msg_split[0] + "+++");
			String msg_recu = client.readligne_client();
			System.out.println("msg_recu du server " + msg_recu);
			String[] msg_recu_split = msg_recu.split("\\s");

			if (msg_recu_split[0].equals("RLIST")) {
				function_rlist(msg_recu_split, client);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	private static void function_rlist(String[] msg_recu_split, Client client) {
		try {
			if (msg_recu_split[1].matches("\\d+\\+\\+\\+")) {
				int num_item = Integer.parseInt(msg_recu_split[1].substring(0, msg_recu_split[1].length() - 3));
				while (num_item > 0) {
					System.out.println(client.readligne_client());
					num_item--;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private static void function_frien(String[] msg_split, Client client) {
		try {
			if(msg_split.length == 2){
				client.write_client(msg_split[0] + " " + msg_split[1] + "+++");
				String msg_recu = client.readligne_client();
				System.out.println(msg_recu);
			}else{
				Message.function_good_msg_invitation(); 
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	private static void function_debug(String msg, Client client) {
		try {
			client.write_client(msg);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private static void function_iquit(String msg, Client client) {
		try {
			client.write_client(msg + "+++");
			String msg_recu = client.readligne_client();
			System.out.println(msg_recu);
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	private static boolean function_conne(String msg, Client client) {
		try {
			client.write_client(msg + " " + client.get_identifiant() + " " + client.get_password() + "+++");
			System.out.println(msg + " " + client.get_identifiant() + " " + client.get_password() + "+++");
			String msg_recu = client.readligne_client();
			System.out.println("msg recu = " + msg_recu);
			if (msg_recu.equals("HELLO+++")) {
				return true;
			} else if (msg_recu.equals("GOBYE+++")) {
				return false;
			}

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return false;

	}

	private static boolean function_regist(String msg, Client client) {
		try {
			client.write_client(msg + " " + client.get_identifiant() + " " + client.get_port_udp_client() + " "
					+ client.get_password() + "+++");
			System.out.println("msg send to server = " + msg + " " + client.get_identifiant() + " "
					+ client.get_port_udp_client() + " " + client.get_password() + "+++");
			String msg_recu = client.readligne_client();
			System.out.println("msg recu = " + msg_recu);
			if (msg_recu.equals("GOBYE+++")) {
				return false;
			} else if (msg_recu.equals("WELCO+++")) {
				return true;
			}

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return false;
	}

}
