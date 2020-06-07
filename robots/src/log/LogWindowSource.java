package log;

import robot.Observable;
import robot.Observer;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class LogWindowSource implements Observable
{
    private final LimitedList<LogEntry> messages;
    private final List<Observer> observers;
    
    public LogWindowSource(int iQueueLength) {
        messages = new LimitedList<>(iQueueLength);
        observers = new CopyOnWriteArrayList<>();
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
        observers.add(observer);
    }

    @Override
    public void unregisterObserver(Observer observer) {
        observers.remove(observer);
    }

    @Override
    public void notifyObservers() {
        for (Observer observer : observers) {
            observer.objectModified(this);
        }
    }
}
