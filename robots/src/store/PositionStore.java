package store;

import jdk.jshell.spi.ExecutionControl;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.nio.file.Path;

public class PositionStore {
    private File positionFile;

    public PositionStore(String homeDir) {
        //System.getProperty("user.home")
        positionFile = new File(homeDir, "positions.txt");
    }

    public void storePosition(Window[] windows) throws IOException {
        FileWriter writer = new FileWriter(positionFile);
        try {
            for (Window window : windows) {
                writePosition(window, writer);
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

    private void writePosition(Window window, Writer writer) throws IOException {
        String toWrite = String.format(
                "%s: %s\n%s: %d\n%s: %d\n%s: %b\n",
                "name", window.getName(),
                "width", window.getWidth(),
                "height", window.getHeight(),
                "isHide", window.isShowing()
        );
        writer.append(toWrite);
    }
}
