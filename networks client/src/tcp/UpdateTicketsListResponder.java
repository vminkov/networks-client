package tcp;

import java.io.Serializable;
import java.util.List;

import tcp.tickets.Ticket;

public class UpdateTicketsListResponder implements MessageResponder {
	
	@Override
	public String getType() {
		return Messages.UPDATE_TICKETS_LIST;
	}

	@Override
	public void handleMessage(Serializable data) {
		System.out.println();
		List<Ticket> tickets = null;
		if(data instanceof List<?>)
			tickets = (List<Ticket>) data;
		
		for(Ticket tick : tickets){
			System.out.println(tick);
			System.out.println();
		}
		
		//XXX: should we send a success response?
	}

}
