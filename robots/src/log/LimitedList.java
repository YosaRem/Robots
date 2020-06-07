package log;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * Ограниченная коллекция с синхронностью.
 * Используется связные список, потому удаление записей выполняется за О(1). Добавление в начало списка
 * выполняется так же за О(1)
 */
public class LimitedList<T> {
    private final int limit;
    private final LinkedList<T> list;

    public LimitedList(int limit) {
        if (limit <= 0) {
            throw new IllegalArgumentException();
        }
        this.limit = limit;
        list = new LinkedList<>();
    }

    public synchronized void add(T element) {
        list.add(element);
        if (list.size() > limit) {
            list.remove(0);
        }
    }

    public synchronized T get(int i) {
        return list.get(i);
    }

    public synchronized List<T> all() {
        return new ArrayList<>(list);
    }

    public synchronized Iterable<T> range(int from, int to) {
        return new ArrayList<>(list.subList(from, to));
    }

    public synchronized int size() {
        return list.size();
    }
}
