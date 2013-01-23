package tcp;

import java.io.Serializable;

import tcp.tickets.Ticket;

public class SendNewTicketCommand implements Command {
	private Serializable data;
	private String msgType = Messages.NEW_QBOARD_TICKET;
	
	public SendNewTicketCommand(Ticket t){
		data = (Serializable) t;
	}
	
	public boolean execute(){
		Messenger snm = SecureNetworkMessenger.getInstance();

		return snm.sendMessage(this.msgType, this.data);
	}
}
