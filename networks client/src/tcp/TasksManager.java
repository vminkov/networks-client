package tcp;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Vicho
 * This should be singleton, as multiple instances might be on different threads
 * and read/write the same data objects.
 * 
 **/
public class TasksManager implements Runnable {
	private Map<String, MessageResponder> responders = new HashMap<String, MessageResponder>();
	public static final MessageQueue messageQueue = MessageQueue.getInstance();
	private static TasksManager instance;
	
	public static TasksManager getInstance(){
		if(instance == null){
			instance = new TasksManager();
		}
		return instance;
	}
	
	private TasksManager(){
		//THE CONSTRUCTORS ARE EXECUTED IN THE MAIN THREAD! (THIS MEANS IN SINGLE THREAD MODE)
        /*
         * Here we should add the various message responders?
         */
        registerResponder(new AddTicketTypeResponder());
        registerResponder(new UpdateTicketsListResponder());
        registerResponder(new SuccessResponseHandler());
        registerResponder(new FailureResponseHandler());
		
	}
	
	private void registerResponder(MessageResponder responder) {
		responders.put(responder.getType(), responder);
	}

	@Override
	public void run() {
		if(!messageQueue.isEmpty()){
			try {
				parseMessageTask(messageQueue.take());
			} catch (InterruptedException | MessageParsingException e) {
				e.printStackTrace();
			}
		}
	}

	private void parseMessageTask(final NetworkMessage messageTask) throws MessageParsingException {
		String msgType = messageTask.getType();
		
		//System.out.println(msgType);
		
		MessageResponder responder = this.responders.get(msgType);
		responder.handleMessage(messageTask.getData());		
	}

}
