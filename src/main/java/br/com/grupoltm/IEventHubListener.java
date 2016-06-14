package br.com.grupoltm;

import com.microsoft.azure.eventhubs.EventData;

public interface IEventHubListener {
    public void eventDataSent(EventData event);
}
