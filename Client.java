
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;

public class Client {

	private String identifiant;
	private int port_udp;
	private int password;
	private int port_multidifusion ; 
	private String ip_multidifusion ; 
	private BufferedReader br_client;
	private PrintWriter pr_client;
	private Socket client;

	public Client(int port_multidifusion , String ip_multidifusion){
		this.port_multidifusion = port_multidifusion ; 
		this.ip_multidifusion = ip_multidifusion ; 
	}

	public Client(String id, int port_udp, int password) {
		this.identifiant = id;
		this.port_udp = port_udp;
		this.password = password;

	}

	public void connect_client(String host, int port) throws UnknownHostException, IOException {

		this.client = new Socket(host, port);
		create_buff_reader_client();
		creat_PrintWriter_client();

	}

	private void create_buff_reader_client() {
		try {
			this.br_client = new BufferedReader(new InputStreamReader(this.client.getInputStream()));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void creat_PrintWriter_client() {
		try {
			this.pr_client = new PrintWriter(new OutputStreamWriter(client.getOutputStream()));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public String readligne_client() throws Exception {

		if (client != null && this.br_client != null) {
			String recu = this.br_client.readLine();
			return recu;
		} else {
			throw new Exception("le client n'est pas initialser ou bien sont buff");
		}
	}

	public void write_client(String msg) throws Exception {
		if (msg != null) {
			this.pr_client.println(msg);
			this.pr_client.flush();
		} else {
			throw new Exception("pas de msg");
		}
	}

	private void close_br_client() {

		try {
			if (this.br_client != null)
				this.br_client.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void close_PrintWriter_client() {
		if (this.pr_client != null)
			this.pr_client.close();
	}

	public void close_client() {
		try {
			close_PrintWriter_client();
			close_br_client();
			if (client == null)
				client.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public int get_password() {
		return this.password;
	}

	public String get_identifiant() {
		return this.identifiant;
	}

	public int get_port_udp_client() {
		return this.port_udp;
	}
	
}
