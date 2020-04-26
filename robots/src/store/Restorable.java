package store;

import java.util.Map;

public interface Restorable {
    void restoreOrDefault(Map<String, WindowPosition> store, WindowPosition defaultPosition);
}
