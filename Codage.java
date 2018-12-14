
public class Codage {
	private final int SIZE_PORT = 4 ; 
	private final int SIZE_IP = 15 ;
	private final int SIZE_NUM_MSG = 4 ;
	private final int SIZE_NUM_ITEM = 3 ; 
	private final int SIZE_MESS_PROMO= 300  ;
	
	
	public Codage(){
		
	}
	
	public  String codage_ip(String ip){
		int size = ip.length() ; 
		for (int i = size; i < SIZE_IP; i++) {
			ip=ip+"#" ; 
		}
		return ip ; 
	}
	
	public String codage_port(String Port){
		int size = Port.length(); 
		for(int i = size ; i < SIZE_PORT ; i++){
			Port = "0"+Port ; 
		}
		return Port ; 
	}
	public String codage_num_mes(String num_msg){
		int size = num_msg.length(); 
	
		for(int i = size ; i < SIZE_NUM_MSG ; i++){
			num_msg = "0"+num_msg ; 
		}
		return num_msg ; 
	}
	public String codage_num_item(String num_item ){
		int size = num_item.length();
		
		for (int i = size; i < SIZE_NUM_ITEM; i++) {
			num_item = "0"+num_item ; 
		}
		return num_item ; 
	}
	public String codage_mot__dePasse(String mot_de_passe){
		return mot_de_passe ; 
	}
	public String codage_message_promoteur(String msg){
		int size = msg.length() ;
		for(int i = size ; i< SIZE_MESS_PROMO ; i++){
			msg +="#" ; 
		}
		return msg ; 
	}
	
	public static void main(String[] args) {
		Codage codage = new Codage () ;
		String ip = "salut ça bien " ;
		String port = "12" ; 
		String result = codage.codage_message_promoteur(ip);
		System.out.println(result.length());
	}

}
