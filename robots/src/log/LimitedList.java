package log;

import java.util.ArrayList;
import java.util.List;

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
            start += 1;
        }
    }

    synchronized public T get(int i) {
        return list.get(start + i);
    }

    synchronized public int getSize() {
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
