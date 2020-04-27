package store;

import java.util.List;
import java.util.Map;

public interface HaveStorableFrames {
    List<Storable> getDataForStore();
    void restore(PositionStore store);
}
