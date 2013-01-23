package tcp;

public class GetTicketsListCommand implements Command {
	private SecureNetworkMessenger snm;
	public GetTicketsListCommand(){
		snm = (SecureNetworkMessenger) SecureNetworkMessenger.getInstance();
	}
	
	@Override
	public boolean execute() {
		boolean success;
		
		success = snm.sendMessage(Messages.UPDATE_TICKETS_LIST, null);
		if(!success)
			System.out.println("failed to get the tickets...");
		return success;
	}
	
}
