package store;

import java.io.*;
import java.util.*;
import java.util.List;

public class PositionStore {
    private final File positionFile;
    private final List<Storable> toStore;
    private final Map<String, WindowPosition> data;

    public PositionStore(HaveStorableFrames frame, String homeDir) {
        positionFile = new File(homeDir, "positions.txt");
        toStore = frame.getDataForStore();
        data = new HashMap<>();
        getWindowsPositions();
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

    public Map<String, WindowPosition> getStoredData() {
        return data;
    }

    private void getWindowsPositions() {
        try {
            FileReader reader = new FileReader(positionFile);
            BufferedReader bufferedReader = new BufferedReader(reader);
            String line = bufferedReader.readLine();
            while (line != null) {
                String[] parameters = line.split(": ");
                if (parameters[0].equals("name")) {
                    extractOneWindowPosition(bufferedReader, parameters[1]);
                    line = bufferedReader.readLine();
                }
            }
        } catch (IOException e) { data.clear(); }
    }

    private void extractOneWindowPosition(BufferedReader reader, String name) throws IOException {
        List<String> windowData = new ArrayList<>();
        windowData.add(name);
        String line = reader.readLine();
        while (!line.equals("")) {
            windowData.add(line.split(": ")[1]);
            line = reader.readLine();
        }
        data.put(windowData.get(0),
                new WindowPosition(
                        windowData.get(0),
                        Integer.parseInt(windowData.get(1)),
                        Integer.parseInt(windowData.get(2)),
                        windowData.get(5).equals("true"),
                        Float.parseFloat(windowData.get(3)),
                        Float.parseFloat(windowData.get(4))
                ));
    }

    private void writePosition(Storable frame, Writer writer) throws IOException {
        WindowPosition frameInfo = frame.getDataForStore();
        String toWrite = String.format(
                "%s: %s\n%s: %d\n%s: %d\n%s: %d\n%s: %d\n%s: %b\n\n",
                "name", frameInfo.getName(),
                "height", frameInfo.getHeight(),
                "width", frameInfo.getWidth(),
                "x", frameInfo.getX(),
                "y", frameInfo.getY(),
                "isHide", frameInfo.isHide()
        );
        writer.append(toWrite);
    }

}
