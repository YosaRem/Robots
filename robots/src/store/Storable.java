package store;

import java.util.Map;

public interface Storable {
    WindowPosition getDataForStore();
    void restore(Map<String, WindowPosition> store);
}
