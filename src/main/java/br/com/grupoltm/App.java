
package br.com.grupoltm;

import java.io.IOException;
import java.util.*;
import java.util.concurrent.ExecutionException;

import com.microsoft.azure.eventhubs.*;
import com.microsoft.azure.servicebus.*;


public class App implements IEventHubListener {

    private static final String SERVICEBUS_NAMESPACE = "rmelobus";
    private static final String EVENTHUB_NAME = "ltmlabs-events2";
    private static final String SAS_KEYNAME = "producer";
    private static final String SAS_KEY = "QMbkuku5WhTnbjQSrSgEltE/Mc1u1f3yS32cSFZiLfg=";
    private static final Boolean MOCK_EVENTHUB = false;

    public static void main(String[] args) throws ServiceBusException, ExecutionException, InterruptedException, IOException {

        ScreenHelper.writeInit();
        EventHubClient client = null;
        EventHubSender sender = null;

        try {

            ScreenHelper.writeln("Connecting to event hub...");
            client = getHubClient();
            ScreenHelper.writeln(client != null ? "Client connected!" : "Client simulated!");
            sender = new EventHubSender(client);
            App app = new App();
            sender.setEventListener(app);

        } catch (Exception ex) {
            ScreenHelper.writeError(ex.getMessage());
            System.exit(1);
        }

        HashMap<String, String> options = new HashMap<String, String>();
        options.put("0", "Exit.");
        options.put("1", "Set start index.");
        options.put("2", "Send next message.");
        options.put("3", "Send batch message.");
        options.put("4", "Send messages continuously.");

        int option = -1;


        while (option == -1) {

            Scanner in = new Scanner(System.in);
            ScreenHelper.showOptions("Choose one option below:", options);
            ScreenHelper.writeType();

            try {
                option = in.nextInt();
                if (options.get(Integer.toString(option)) == null) {
                    option = -1;
                    continue;
                }
            } catch (Exception ex) {
                continue;
            }

            executeCommand(option, sender);
            option = -1;
        }
    }

    public static void executeCommand(int option, EventHubSender sender) throws InterruptedException {

        try {

            switch (option) {
                case 1:
                    Integer index = getIntValue("Enter the index value(integer) or \"c\" to cancel:");
                    if (index != null) {
                        sender.setIndex(index);
                        ScreenHelper.writeln("\n\rThe current index is: " + ScreenHelper.YELLOW + sender.getIndex() + ScreenHelper.RESET);
                    }
                    break;
                case 2:
                    sender.sendNextMessage();
                    break;
                case 3: {
                    Integer count = getIntValue("Enter the count batch value(integer) or \"c\" to cancel:");
                    if (count != null) {
                        sender.sendBatchMessages(count);
                        ScreenHelper.writeln("\n\rThe current index is: " + ScreenHelper.YELLOW + sender.getIndex() + ScreenHelper.RESET);
                    }
                    break;
                }
                case 4: {
                    Integer count = getIntValue("Enter the count value(integer) or \"c\" to cancel:");
                    if (count != null) {
                        sender.sendNextMessageContinuously(count, 500);
                        ScreenHelper.writeln("\n\rThe current index is: " + ScreenHelper.YELLOW + sender.getIndex() + ScreenHelper.RESET);
                    }
                    break;
                }
                default:
                    System.exit(0);
                    break;
            }
        } catch (ServiceBusException ex) {
            ScreenHelper.writeError("Error when try to send message: " + ex.getMessage());
        }
    }

    private static Integer getIntValue(String message) {
        while (true) {
            try {
                Scanner in = new Scanner(System.in);
                Integer startIndex = ScreenHelper.tryGetInt(message, "c");
                return startIndex;
            } catch (Exception ex) {
                continue;
            }
        }
    }

    private static EventHubClient getHubClient() throws IOException, ServiceBusException {
        if (!MOCK_EVENTHUB) {
            ConnectionStringBuilder connStr = new ConnectionStringBuilder(SERVICEBUS_NAMESPACE, EVENTHUB_NAME, SAS_KEYNAME, SAS_KEY);
            return EventHubClient.createFromConnectionStringSync(connStr.toString());
        }
        return null;
    }

    public void eventDataSent(EventData event) {
        ScreenHelper.write("\n\rMessage sent:" + new String(event.getBody()));
    }
}
