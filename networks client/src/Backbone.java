import java.io.IOException;
import java.util.Scanner;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import org.joda.time.DateTime;

import tcp.GetTicketsListCommand;
import tcp.SecureNetworkMessenger;
import tcp.SendNewTicketCommand;
import tcp.TasksManager;
import tcp.tickets.Ticket;
import tcp.tickets.TicketsFactory;
import udp.ConnectionStatusThread;


public class Backbone implements Runnable {
	/**
	 * Launch the application.
	 * TODO Revise concurrency. Events? p.s. event driven mvc SUCKS!
	 */
	public static void main(String[] args) {
		ExecutorService executor = Executors.newCachedThreadPool();
		ScheduledExecutorService scheduledExecutor = Executors.newScheduledThreadPool(2);
        System.setProperty("java.net.useSystemProxies", "false");	
		TasksManager tm = TasksManager.getInstance();
		try{
		scheduledExecutor.scheduleAtFixedRate(tm, 0, 200, TimeUnit.MILLISECONDS);
		System.out.println("task manager running");	
		}catch(Exception e){
			System.out.println("BAD BAD BAD!");
			e.printStackTrace();
		}
        
        //we want dependency injection here
		executor.execute((Runnable) SecureNetworkMessenger.getSecureInstance());
		System.out.println("netowork messenger running");

		
		Thread statusThread = null;
		try {
			statusThread = new ConnectionStatusThread("CLIENT");
		} catch (IOException e) {
			e.printStackTrace();
		}
		executor.execute(statusThread);
		System.out.println("connection status checker running");
		
		scheduledExecutor.schedule((Runnable) new Backbone(), 2, TimeUnit.SECONDS);
	}
	
	@Override
	public void run(){
		Scanner sc = new Scanner(System.in);
		int choice = 0;
		while(true){
			System.out.println("\nPlease choose an option: ");
			System.out.println("1. Add a new ticket" +
					"\n2. List all tickets");
			System.out.println("3. Drop every n-th package");
			choice = sc.nextInt();
			
			if(choice < 0 || choice > 3){
				continue;
			}

			switch(choice){
			case 1:
				System.out.println("Enter the time of the day: \nformat: HH:MM\n");
				int hour = 0;
				hour = sc.nextInt();
				int minute = 0;
				minute = sc.nextInt();
				
				DateTime dt = new DateTime();//.parse(dateStr);
				DateTime realDt = new DateTime(dt.getYear(), dt.getMonthOfYear(), dt.getDayOfMonth(), hour, minute);
				System.out.println(realDt);
				SendNewTicketCommand sntc = new SendNewTicketCommand(new Ticket(TicketsFactory.DEFAULT_TICKET_TYPE,
						realDt, 0));

				sntc.execute();

				break;
			case 2:
				System.out.println("requesting the tickets...");
				GetTicketsListCommand getListCommand = new GetTicketsListCommand();
				getListCommand.execute();
				break;
			case 3:
				int n;
				System.out.print("n= ");
				n = sc.nextInt();
				ConnectionStatusThread.dropEveryNthPackage = n;
				break;
			default:
				break;
			}
			if(choice==0){
				break;
			}
		}
		System.out.println("closing..");
		sc.close();
	}
}
