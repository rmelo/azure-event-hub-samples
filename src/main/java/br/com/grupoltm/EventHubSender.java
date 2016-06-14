package br.com.grupoltm;

import com.microsoft.azure.eventhubs.EventData;
import com.microsoft.azure.eventhubs.EventHubClient;
import com.microsoft.azure.servicebus.ServiceBusException;

import java.util.Iterator;
import java.util.ArrayList;
import java.util.Scanner;

public class EventHubSender {

    private EventHubClient client;
    private IEventHubListener listener;
    private int index = 1;

    public  EventHubSender(EventHubClient client){
        this.client = client;
    }

    public void setIndex(int index){
        this.index = index;
    }

    public  int getIndex(){return this.index;}

    public void sendNextMessage() throws ServiceBusException {
        EventData message = getMessage();
        try {
            if (this.client != null) {
                this.client.sendSync(message);
            }
            this.index++;
            this.notifyEventListener(message);
        }catch (ServiceBusException ex){
            throw ex;
        }
    }

    public void sendBatchMessages(int count) throws ServiceBusException {
        ArrayList<EventData> events = new ArrayList<EventData>();
        for(int i = 0; i < count; i++){
            events.add(getMessage());
            this.index ++;
        }
        try {
            if (this.client != null) {
                this.client.sendSync(events);
            }
            this.notifyEventListener(events.iterator());
        }catch (ServiceBusException ex){
            throw ex;
        }
    }

    public void sendNextMessageContinuously(int count, long millis) throws ServiceBusException, InterruptedException {
        int i = 0;
        while (i<count){
            this.sendNextMessage();
            Thread.sleep(millis);
            i++;
        }
    }

    public void setEventListener(IEventHubListener listener){
        this.listener = listener;
    }

    private void notifyEventListener(EventData eventData){
        if(this.listener!=null)
            this.listener.eventDataSent(eventData);
    }

    private void notifyEventListener(Iterator<EventData> events){
        if(this.listener!=null) {
            while (events.hasNext()){
                notifyEventListener(events.next());
            }
        }
    }

    private EventData getMessage(){
        String message = "Event message " + this.index;
        byte[] payloadBytes = message.getBytes();
        EventData event = new EventData(payloadBytes);
        return  event;
    }
}
