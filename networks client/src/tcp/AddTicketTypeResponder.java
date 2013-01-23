package tcp;

import java.io.Serializable;

import tcp.tickets.TicketType;
import tcp.tickets.TicketsFactory;

public class AddTicketTypeResponder implements MessageResponder {
	@Override
	public String getType() {
		return Messages.ADD_TICKET_TYPE;
	}

	@Override
	public void handleMessage(Serializable data) {
		TicketType newTicketType = (TicketType) data;
		TicketsFactory tf = TicketsFactory.getInstance();
		System.out.println("Adding new ticket type: " + newTicketType.reason + "; " + newTicketType.duration + "; "  + newTicketType.type);
		tf.addTicketType(newTicketType.reason, newTicketType.duration, newTicketType.type);

	}

}
