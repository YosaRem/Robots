package log;

import robot.Observable;
import robot.Observer;

import java.util.ArrayList;
import java.util.Collections;

public class LogWindowSource implements Observable
{
    private final LimitedList<LogEntry> messages;
    private final ArrayList<Observer> listeners;
    
    public LogWindowSource(int iQueueLength) {
        messages = new LimitedList<>(iQueueLength);
        listeners = new ArrayList<>();
    }
    
    public void append(LogLevel logLevel, String strMessage) {
        LogEntry entry = new LogEntry(logLevel, strMessage);
        messages.add(entry);
        notifyObservers();
    }
    
    public int size()
    {
        return messages.size();
    }

    public Iterable<LogEntry> all()
    {
        return messages.all();
    }

    @Override
    public void registerObserver(Observer observer) {
        synchronized(listeners) {
            listeners.add(observer);
        }
    }

    @Override
    public void unregisterObserver(Observer observer) {
        synchronized(listeners) {
            listeners.remove(observer);
        }
    }

    @Override
    public void notifyObservers() {
        for (Observer observer : listeners) {
            observer.objectModified(this);
        }
    }
}
