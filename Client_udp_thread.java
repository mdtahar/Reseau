import java.net.DatagramPacket;
import java.net.DatagramSocket;

public class Client_udp_thread implements Runnable {

	private int port_udp;

	public Client_udp_thread(int port_udp) {
		this.port_udp = port_udp;
	}

	@Override
	public void run() {
		try {
			DatagramSocket dso = new DatagramSocket(this.port_udp);
			byte[] data = new byte[100];
			DatagramPacket paquet = new DatagramPacket(data, data.length);
			while (true) {
				dso.receive(paquet);

				String st = new String(paquet.getData(), 0, paquet.getLength());
				System.out.println("notification :" + st);
			}
		} catch (Exception e) {
			e.printStackTrace();
			System.exit(-1);
		}

	}

}
