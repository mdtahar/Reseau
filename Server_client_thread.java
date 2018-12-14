import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Iterator;
import java.util.LinkedList;

public class Server_client_thread implements Runnable {

	private Socket client;
	private LinkedList<struct_donne> list;


	public Server_client_thread(Socket client, LinkedList<struct_donne> list) {
		this.client = client;
		this.list = list;
	}

	@Override
	public void run() {
		PrintWriter pr = null;
		BufferedReader br = null;

		try {
			pr = new PrintWriter(new OutputStreamWriter(this.client.getOutputStream()));
			br = new BufferedReader(new InputStreamReader(this.client.getInputStream()));
			String id_client_connect = null;
			while (true) {
				String msg = br.readLine();
				System.out.println("msg recu du server : " + msg);
				if (msg == null) {
					break;
				}
				System.out.println(msg);
				String msg_split[] = msg.split("\\s");
				if (msg_split[0].equals("REGIS")) {
					id_client_connect = msg_split[1];
					if (!function_regis(msg_split, pr)) {
						break;
					}
				}
				if (msg_split[0].equals("CONNE")) {
					id_client_connect = msg_split[1];
					if (!function_conne(msg_split, pr)) {
						break;
					}
				}
				if (id_client_connect != null) {
					if (msg_split[0].equals("FRIE?")) {
						function_frie(msg_split, pr, id_client_connect);
					}
					if (msg_split[0].equals("IQUIT+++")) {
						pr.println("GOBYE+++");
						pr.flush();
						break;
					}
					if (msg_split[0].equals("debug") || msg_split[0].equals("DEBUG")) {
						function_debug();
					}
					if (msg_split[0].equals("LIST?+++")) {
						function_list(msg_split, pr);
					}
					if (msg_split[0].equals("CONSU+++")) {
						function_consu(br, pr, id_client_connect);
					}
					if (msg_split[0].equals("MESS?")) {
						function_message(msg_split, pr, br, id_client_connect);
					}
				}
			}
			pr.close();
			br.close();
			client.close();

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	private void function_message(String[] msg_split, PrintWriter pr, BufferedReader br, String id_client_connect) {
		try {
			if (msg_split.length != 3) { // a savoir
				pr.println("MESS<+++");
				pr.flush();
				return ;
			}
			String id_client_notification = msg_split[1];
			struct_donne client_notification = struct_donne.get_struct_donne(this.list, id_client_notification);
			if (client_notification.check_friend(id_client_connect)) {
				int nb_msg = Integer.parseInt(msg_split[2].substring(0, msg_split[2].length() - 3));
				if (client_notification != null) {
					client_notification.add_to_flux(msg_split[0] + " " + msg_split[1] + " " + msg_split[2]);
				}
				int counter = 0;
				while (counter < nb_msg) {
					String msg_partiel = br.readLine();
					client_notification.add_msg_amis(msg_partiel);
					counter++;
				}
				Server_ConCurent.notification_udp(client_notification.get_port_udp(), Server_ConCurent.MSG_CLIENT, client_notification.get_nb_flux());
				pr.println("MESS>+++");
				pr.flush();
			}else{
				pr.println("MESS<+++");
				pr.flush();
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}


	private synchronized void function_consu(BufferedReader br, PrintWriter pr, String id_client_connect) {
		try {

			struct_donne client = struct_donne.get_struct_donne(this.list, id_client_connect);
			LinkedList<String> flux = client.get_list_flux();
			Iterator<String> iterator = flux.iterator();
			if (client.get_nb_flux() == 0) {
				pr.println("NOCON+++");
				pr.flush();
				return;
			}
			while (iterator.hasNext()) { // je dois decrementer le nb de flux
				String msg_to_send = iterator.next();
				iterator.remove();
				client.decrement_flux();

				String msg_to_send_split[] = msg_to_send.split("\\s");
				if (msg_to_send_split[0].equals("EIRF>")) { // demande d'amitie
					pr.println(msg_to_send);
					pr.flush();
					struct_donne client_notification = struct_donne.get_struct_donne(this.list, msg_to_send_split[1]);
					int nb_flux = client_notification.get_nb_flux();
					int port_udp = client_notification.get_port_udp();
					while (true) {
//						notifyAll();
						String msg_client = br.readLine();
						if (msg_client.equals("OKIRF+++")) {
							pr.println("ACKRF+++");
							pr.flush();
							Server_ConCurent.notification_udp(port_udp, Server_ConCurent.ACCEPTE, nb_flux);
							client_notification.add_friend(id_client_connect);
							client.add_friend(client_notification.get_identifiant());
							client_notification.add_to_flux("FRIEN " + id_client_connect + "+++");
							break;
						}

						if (msg_client.equals("NOKRF+++")) {
							pr.println("ACKRF+++");
							pr.flush();
							Server_ConCurent.notification_udp(port_udp, Server_ConCurent.REFUSE, nb_flux);
							client_notification.add_to_flux("NOFRI " + id_client_connect + "+++");
							break;
						}

					}
				}

				if (msg_to_send_split[0].equals("MESS?")) {
					int counter = 0;
					// String id_client = msg_to_send_split[1] ;
					LinkedList<String> message_amis = client.get_list_msg_amis();
					System.out.println("message_amis " + message_amis);
					Iterator<String> iter_message = message_amis.iterator();
					int nb_mes = Integer.parseInt(msg_to_send_split[2].substring(0, msg_to_send_split[2].length() - 3));
					pr.println("SSEM>" + " " + msg_to_send_split[1] + " " + msg_to_send_split[2]);
					pr.flush();
					while (counter < nb_mes) {// < ou <=
						// System.out.println("achebeba achebeba ");
						String msg_to_send2 = iter_message.next();
						pr.println(msg_to_send2);
						pr.flush();
						iter_message.remove();
						counter++;
					}
				}
				if (msg_to_send_split[0].equals("FRIEN")) {
					pr.println(msg_to_send_split[0] + " " + msg_to_send_split[1]);
					pr.flush();
				}
				if(msg_to_send_split[0].equals("NOFRI")){
					pr.println(msg_to_send_split[0] + " " + msg_to_send_split[1]);
					pr.flush();
				}
				if(msg_to_send_split[0].equals("PUBL?")){
					pr.println(msg_to_send);
					pr.flush();
					}
			}
//			notifyAll();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private void function_list(String[] msg_split, PrintWriter pr) {
		int size_list = this.list.size();
		pr.println("RLIST " + size_list + "+++");
		pr.flush();
		String id_client = null;
		while (size_list > 0) {
			id_client = this.list.get(size_list - 1).get_identifiant();
			pr.println("LINUM " + id_client + "+++");
			pr.flush();
			size_list--;
		}

	}

	private void function_debug() {
		debug();

	}

	private void function_frie(String[] msg_split, PrintWriter pr, String id_client_connect) {

		String id = msg_split[1].substring(0, msg_split[1].length() - 3);
		System.out.println("demande d'ajour du id :  " + id);
		if (!check_id(id)) {
			pr.println("FRIE<+++");
			pr.flush();
		} else {
			pr.println("FRIE>+++");
			pr.flush();
			struct_donne client_to_send_flux = struct_donne.get_struct_donne(this.list, id);
			add_flux_friend(client_to_send_flux, id_client_connect);
			int nb_flux = client_to_send_flux.get_nb_flux();
			int port_udp = struct_donne.get_port(this.list, id);
			if (port_udp != -1) {
				Server_ConCurent.notification_udp(port_udp, Server_ConCurent.INVITATION, nb_flux);
			}
		}

	}



	private void add_flux_friend(struct_donne client_to_send_flux, String id_client_connect) {

		if (client_to_send_flux == null) {
			return;
		}
		client_to_send_flux.add_to_flux("EIRF>" + " " + id_client_connect);
		return;

	}

	private boolean function_regis(String[] msg_bis, PrintWriter pr) {
		if (this.list.size() > 100) {
			pr.println("GOBYE+++");
			pr.flush();
			return false;
		}
		Iterator<struct_donne> iterator = this.list.iterator();
		while (iterator.hasNext()) {
			if ((iterator.next().get_identifiant()).equals(msg_bis[1])) {
				pr.println("GOBYE+++");
				pr.flush();
				return false;
			}
		}
		pr.println("WELCO+++");
		this.list.add(new struct_donne(msg_bis[1], Integer.parseInt(msg_bis[2]),
				Integer.parseInt(msg_bis[3].substring(0, msg_bis[3].length() - 3))));
		pr.flush();
		return true;

	}

	public boolean function_conne(String[] msg_split, PrintWriter pr) {
		Iterator<struct_donne> iterator = this.list.iterator();
		while (iterator.hasNext()) {
			struct_donne donne = iterator.next();
			if (donne.get_identifiant().equals(msg_split[1])
					&& (donne.get_password() + "").equals(msg_split[2].substring(0, msg_split[2].length() - 3))) {
				pr.println("HELLO+++");
				pr.flush();
				return true;
			}
		}
		pr.println("GOBYE+++");
		pr.flush();
		return false;
	}

	private boolean check_id(String id) {
		Iterator<struct_donne> it = this.list.iterator();
		while (it.hasNext()) {
			struct_donne donnee = it.next();
			if ((donnee.get_identifiant()).equals(id)) {
				return true;
			}
		}
		return false;
	}

	/*
	 * for debug
	 */
	public void debug() {
		Iterator<struct_donne> iterator = this.list.iterator();
		while (iterator.hasNext()) {
			struct_donne donne = iterator.next();
			System.out.println("dans debug id :  " + donne.get_identifiant());
			System.out.println("dans debug password : " + donne.get_password());
			System.out.println("dans debug port_udp : " + donne.get_port_udp());
			System.out.println("dans debug list flux : " + donne.get_list_flux());
			System.out.println("dans debug list flux : " + donne.get_nb_flux());
			LinkedList<String> amis = donne.get_friend();
			Iterator<String> iterator_amis = amis.iterator();
			System.out.println("amis  : ");
			while (iterator_amis.hasNext()) {
				System.out.print(iterator_amis.next() + " ");
			}
			System.out.println();
			System.out.println("-----------------------------------------------");
			System.out.println("messages amis : ");
			LinkedList<String> msg_amis = donne.get_list_msg_amis();
			System.out.println(msg_amis);
			System.out.println();
			System.out.println("-----------------------------------------------");
		}
	}
}
