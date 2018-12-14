import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetSocketAddress;

public class EnvoiMulticast {
	private InetSocketAddress ia;
	private DatagramSocket dso;
	private DatagramPacket paquet;

	public EnvoiMulticast(int port_multi, String ip_multi) {
		try {
			this.ia = new InetSocketAddress(ip_multi, port_multi);
			this.dso = new DatagramSocket();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void send_multicast(String msg_to_send) {
		try {
			byte data[] = msg_to_send.getBytes();
			this.paquet = new DatagramPacket(data, data.length, this.ia);
			dso.send(this.paquet);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		try {
			DatagramSocket dso = new DatagramSocket();
			byte[] data;
			for (int i = 0; i <= 10; i++) {
				// Thread.sleep(1000);
				String s = "MESSAGE " + i + " \n";
				data = s.getBytes();
				InetSocketAddress ia = new InetSocketAddress("225.1.2.4", 9999);
				DatagramPacket paquet = new DatagramPacket(data, data.length, ia);
				dso.send(paquet);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}