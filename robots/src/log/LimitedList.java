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
    private final int LIMIT;
    private volatile int start;
    private volatile List<T> list;

    public LimitedList(int limit) {
        LIMIT = limit;
        list = new ArrayList<>();
        start = 0;
    }

    synchronized public void add(T element) {
        list.add(element);
        if (isOverflowing()) {
            toStart();
        } else {
            if (list.size() - start > LIMIT) {
                start++;
            }
        }
    }

    synchronized public T get(int i) {
        return list.get(start + i);
    }

    synchronized public List<T> all() {
        return new ArrayList<>(list.subList(start, list.size()));
    }

    synchronized public Iterable<T> range(int from, int to) {
        return new ArrayList<>(list.subList(start + from, start + to));
    }

    synchronized public int size() {
        return list.size() - start;
    }

    private boolean isOverflowing() {
        return Integer.MAX_VALUE - LIMIT <= start;
    }

    synchronized private void toStart() {
        list = new ArrayList<>(list.subList(start, start + LIMIT));
        start = 0;
    }
}
