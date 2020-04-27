package store;

import java.util.Map;

public interface Storable {
    WindowPosition getDataForStore();
    void restoreOrDefault(Map<String, WindowPosition> store, WindowPosition defaultPosition);
}
