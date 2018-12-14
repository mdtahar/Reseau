import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.LinkedList;

public class Server_promoteur_thread implements Runnable {

	private Socket promoteur ;
	private LinkedList<struct_donne> list ; 
	public Server_promoteur_thread(Socket promotor,LinkedList<struct_donne> list){
		this.promoteur = promotor ; 
		this.list = list ; 
	}
	@Override
	public void run() {
		PrintWriter pr = null;
		BufferedReader br = null ;
		try {
			pr = new PrintWriter(new OutputStreamWriter(this.promoteur.getOutputStream()));
			br = new BufferedReader(new InputStreamReader(this.promoteur.getInputStream()));
			while (true) {
			String msg = br.readLine();
			System.out.println("mes recu : "+msg);
			if(msg==null){
				break ; 
			}
			String msg_split[] = msg.split("\\s");
			if(msg_split[0].equals("PROM")){
				function_prom(msg_split);
			}
			if(msg_split[0].equals("PUBL?")){
				function_publ(msg,pr);
			}
			
				
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	private void function_publ(String msg , PrintWriter pr ) {

		struct_donne.add_to_all_flux(this.list, msg); // je dois envoyer une notification a tous les client 
		pr.println("PUBL>+++");
		pr.flush();
		
	}
	private void function_prom(String[] msg_split) {
		
	}

}
