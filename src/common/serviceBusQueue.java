package common;

import com.microsoft.windowsazure.Configuration;
import com.microsoft.windowsazure.exception.ServiceException;
import com.microsoft.windowsazure.services.servicebus.ServiceBusConfiguration;
import com.microsoft.windowsazure.services.servicebus.ServiceBusContract;
import com.microsoft.windowsazure.services.servicebus.ServiceBusService;
import com.microsoft.windowsazure.services.servicebus.models.BrokeredMessage;
import com.microsoft.windowsazure.services.servicebus.models.ReceiveMessageOptions;
import com.microsoft.windowsazure.services.servicebus.models.ReceiveMode;
import com.microsoft.windowsazure.services.servicebus.models.ReceiveQueueMessageResult;

public class serviceBusQueue {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Configuration config =
	            ServiceBusConfiguration.configureWithSASAuthentication(
	                    "willtestcsMsg-ns",
	                   "testjambor",
	                    "3PrIjVrdt6IA206boo4LApKq1zAcGZnA/vF7pTfOMlg=",
	                    ".servicebus.windows.net"
	            );

	    ServiceBusContract service = ServiceBusService.create(config);

	    try
	    {
	        ReceiveMessageOptions opts = ReceiveMessageOptions.DEFAULT;
	        opts.setReceiveMode(ReceiveMode.PEEK_LOCK);

	        while(true)  {
	            ReceiveQueueMessageResult resultQM =
	                   service.receiveQueueMessage("TestQueue", opts);
	           BrokeredMessage message = resultQM.getValue();
	           if (message != null && message.getMessageId() != null)
	           {
	               System.out.println("MessageID: " + message.getMessageId());
	               // Display the queue message.
	               System.out.print("From queue: ");
	               byte[] b = new byte[200];
	               String s = null;
	               int numRead = message.getBody().read(b);
	               while (-1 != numRead)
	               {
	                   s = new String(b);
	                   s = s.trim();
	                   System.out.print(s);
	                   numRead = message.getBody().read(b);
	               }
	               System.out.println();
	               System.out.println("Custom Property: " +
	                   message.getProperty("MyProperty"));
	               // Remove message from queue.
	               System.out.println("Deleting this message.");
	              // service.deleteMessage(resultQM.getValue());
	           }  
	           else  
	           {
	               System.out.println("Finishing up - no more messages.");
	               break;
	               // Added to handle no more messages.
	               // Could instead wait for more messages to be added.
	           }
	       }
	   }
	   catch (ServiceException e) {
	       System.out.print("ServiceException encountered: ");
	       System.out.println(e.getMessage());
	       System.exit(-1);
	   }
	   catch (Exception e) {
	       System.out.print("Generic exception encountered: ");
	       System.out.println(e.getMessage());
	       System.exit(-1);
	   }
	}

}
