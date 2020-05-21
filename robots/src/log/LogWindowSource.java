package log;

import robot.Observable;
import robot.Observer;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;

/**
 * Что починить:
 * 1. Этот класс порождает утечку ресурсов (связанные слушатели оказываются
 * удерживаемыми в памяти)
 * 2. Этот класс хранит активные сообщения лога, но в такой реализации он 
 * их лишь накапливает. Надо же, чтобы количество сообщений в логе было ограничено 
 * величиной m_iQueueLength (т.е. реально нужна очередь сообщений 
 * ограниченного размера) 
 */
public class LogWindowSource implements Observable
{
    private int m_iQueueLength;

    private ArrayList<LogEntry> m_messages;
    private final ArrayList<Observer> listeners;
    private volatile Observer[] m_activeListeners;
    
    public LogWindowSource(int iQueueLength) 
    {
        m_iQueueLength = iQueueLength;
        m_messages = new ArrayList<LogEntry>(iQueueLength);
        listeners = new ArrayList<Observer>();
    }
    
    public void append(LogLevel logLevel, String strMessage) {
        LogEntry entry = new LogEntry(logLevel, strMessage);
        m_messages.add(entry);
        Observer[] activeListeners = m_activeListeners;
        if (activeListeners == null)
        {
            synchronized (listeners)
            {
                if (m_activeListeners == null)
                {
                    activeListeners = listeners.toArray(new Observer[0]);
                    m_activeListeners = activeListeners;
                }
            }
        }
        for (Observer listener : activeListeners)
        {
            listener.objectModified(this);
        }
    }
    
    public int size()
    {
        return m_messages.size();
    }

    public Iterable<LogEntry> range(int startFrom, int count)
    {
        if (startFrom < 0 || startFrom >= m_messages.size())
        {
            return Collections.emptyList();
        }
        int indexTo = Math.min(startFrom + count, m_messages.size());
        return m_messages.subList(startFrom, indexTo);
    }

    public Iterable<LogEntry> all()
    {
        return m_messages;
    }

    @Override
    public void registerObserver(Observer observer) {
        synchronized(listeners)
        {
            listeners.add(observer);
            m_activeListeners = null;
        }
    }

    @Override
    public void unregisterObserver(Observer observer) {
        synchronized(listeners)
        {
            listeners.remove(observer);
            m_activeListeners = null;
        }
    }

    @Override
    public void notifyObservers() {
        for (Observer observer : m_activeListeners) {
            observer.objectModified(this);
        }
    }
}
