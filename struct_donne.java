import java.util.Iterator;
import java.util.LinkedList;


public class struct_donne {

	private String identifiant;
	private int port_udp;
	private int password;
	private LinkedList<String> flux;
	private int nb_flux_no_Consulter;
	private LinkedList<String> amis ; 
	private LinkedList<String> msg_amis ; 

	public struct_donne(String id, int port_udp, int password) {
		this.identifiant = id;
		this.port_udp = port_udp;
		this.password = password;
		this.flux = new LinkedList<String>();
		this.nb_flux_no_Consulter = flux.size();
		this.amis = new LinkedList<String>(); 
		this.msg_amis = new LinkedList<String>();
	}

	static public struct_donne get_struct_donne(LinkedList<struct_donne> donne , String id){
		Iterator<struct_donne> iterator = donne.iterator();
		while(iterator.hasNext()){
			struct_donne client = iterator.next();
			if(client.get_identifiant().equals(id)){
				return client ; 
			}
		}
		return null ;
	}
	
	static public int get_port(LinkedList<struct_donne> donne, String id) {
		Iterator<struct_donne> iterator = donne.iterator();
		while (iterator.hasNext()) {
			struct_donne client = iterator.next();
			if (client.get_identifiant().equals(id)) {
				return client.get_port_udp();
			}
		}
		return -1;
	}
	static public void add_to_all_flux(LinkedList<struct_donne> list , String msg){
		Iterator<struct_donne> iterator = list.iterator();
		while (iterator.hasNext()) {
			struct_donne client = iterator.next();
			int port = client.get_port_udp() ; 
			client.add_to_flux(msg);
			int nb_flux = client.get_nb_flux() ; 
			Server_ConCurent.notification_udp(port, Server_ConCurent.MSG_PUB, nb_flux);
		}

	}
	
	public  void add_msg_amis(String msg_partiel){
		this.msg_amis.add(msg_partiel);
	}
	
	public  void decrement_flux(){
		this.nb_flux_no_Consulter-- ; 
	}
	
	public  int get_nb_flux(){
		return this.nb_flux_no_Consulter;
	}

	public  void add_to_flux(String flux) {
		this.nb_flux_no_Consulter++ ;
		this.flux.add(flux);
	}

	public  LinkedList<String> get_list_flux() {
		return this.flux;
	}

	public  String get_identifiant() {
		return this.identifiant;
	}

	public int get_port_udp() {
		return this.port_udp;
	}

	public int get_password() {
		return this.password;
	}
	public  void add_friend(String id_friend){
		this.amis.add(id_friend);
	}
	public  LinkedList<String> get_friend(){
		return this.amis ; 
	}
	public LinkedList<String> get_list_msg_amis(){
		return this.msg_amis ; 
	}

	public boolean check_friend( String id_friend){
		LinkedList<String> amis = this.amis ;
		Iterator<String> iterator = amis.iterator(); 
		while(iterator.hasNext()){
			if(iterator.next().equals(id_friend)){
				return true ;
			}
		}
		return false;
	}

}
