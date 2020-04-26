package store;

import jdk.jshell.spi.ExecutionControl;

import java.awt.*;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class PositionStore {
    private File positionFile;
    private List<Storable> toStore = new ArrayList<>();

    public PositionStore(String homeDir) {
        positionFile = new File(homeDir, "positions.txt");
    }

    public void addToStore(Storable toStore) {
        this.toStore.add(toStore);
    }

    public void storePositions() throws IOException {
        FileWriter writer = new FileWriter(positionFile);
        try {
            for (Storable frame : toStore) {
                writePosition(frame, writer);
            }
        } catch (IOException e) {
            writer.close();
            throw e;
        }
        writer.close();
    }

    public WindowPosition[] getWindowsPosition() throws ExecutionControl.NotImplementedException {
        throw new ExecutionControl.NotImplementedException("dfd");
    }

    private void writePosition(Storable frame, Writer writer) throws IOException {
        WindowPosition frameInfo = frame.getDataForStore();
        String toWrite = String.format(
                "%s: %s\n%s: %d\n%s: %d\n%s: %f\n%s: %f\n%s: %b\n\n",
                "name", frameInfo.getName(),
                "height", frameInfo.getHeight(),
                "width", frameInfo.getWidth(),
                "x", frameInfo.getAlignmentX(),
                "y", frameInfo.getAlignmentY(),
                "isHide", frameInfo.isHide()
        );

        writer.append(toWrite);
    }
}
