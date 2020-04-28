package store;

import java.util.Map;

public interface Storable {
    WindowState getDataForStore();
    void restore(Map<String, WindowState> store);
}
