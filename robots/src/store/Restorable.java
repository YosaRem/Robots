package store;

import java.util.Map;

public interface Restorable {
    void useStoreDataForRestore(Map<String, WindowPosition> store);
}
