package udp;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.Date;

public class ConnectionStatusThread extends Thread {
	protected DatagramSocket socket = null;
	public static int dropEveryNthPackage = 3;
    
    public ConnectionStatusThread(String name) throws IOException {
        super(name);
        socket = new DatagramSocket(1516);
    }
    
    @Override
	public void run() {
    	int number = 0;
    	while(true){
    		try {
                byte[] buffer = new byte[512];

                // receive request
                DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
            	//System.out.println("client waiting for packages...");

                socket.receive(packet);

                // figure out response
                String dString = new Date().toString();

                buffer = dString.getBytes();

                // send the response to the client at "address" and "port"
                InetAddress address = packet.getAddress();
                //System.out.println(address);
                int port = packet.getPort();
                packet = new DatagramPacket(buffer, buffer.length, address, port);
               
                //dropEveryNthPackage = 4;
				if(dropEveryNthPackage == 0 || number++ % dropEveryNthPackage !=0){
                	socket.send(packet);
				}
            } catch (IOException e) {
                e.printStackTrace();
            }
    	}
        //socket.close();
    }
}