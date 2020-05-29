package log;

import java.util.ArrayList;
import java.util.List;

/**
 * Ограниченная коллекция с синхронностью.
 * По сути, используется простой список, но эта коллекция модифициорванна так, что
 * делается вид ограниченной коллекции. Мы просто смещаем индекс начала коллекции.
 * Это обеспечивает операции добавление в начало и удаление из конца списка скоростью О(1).
 * Когда переменная, которая отвечает за смещение подходит к переполнению, то создаётся новый список,
 * который хранит все старые данные, но не содержит все старые данные.
 */
public class LimitedList<T> {
    private final int limit;
    private volatile int start;
    private volatile List<T> list;

    public LimitedList(int limit) {
        if (limit <= 0) {
            throw new IllegalArgumentException();
        }
        this.limit = limit;
        list = new ArrayList<>();
        start = 0;
    }

    public synchronized void add(T element) {
        list.add(element);
        if (isOverflowing()) {
            toStart();
        } else {
            if (list.size() - start > limit) {
                start++;
            }
        }
    }

    public synchronized T get(int i) {
        return list.get(start + i);
    }

    public synchronized List<T> all() {
        return new ArrayList<>(list.subList(start, list.size()));
    }

    public synchronized Iterable<T> range(int from, int to) {
        return new ArrayList<>(list.subList(start + from, start + to));
    }

    public synchronized int size() {
        return list.size() - start;
    }

    private boolean isOverflowing() {
        return Integer.MAX_VALUE - limit <= start;
    }

    private synchronized void toStart() {
        list = new ArrayList<>(list.subList(start, start + limit));
        start = 0;
    }
}
