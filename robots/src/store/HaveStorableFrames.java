package store;

import java.util.List;

public interface HaveStorableFrames {
    List<Storable> getDataForStore();
    void restore(PositionStore store);
}
