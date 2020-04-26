package gui;

import java.awt.*;
import java.beans.PropertyVetoException;
import java.util.AbstractMap;
import java.util.Map;

import javax.swing.JInternalFrame;
import javax.swing.JPanel;

import log.LogChangeListener;
import log.LogEntry;
import log.LogWindowSource;
import store.Restorable;
import store.Restorer;
import store.Storable;
import store.WindowPosition;

public class LogWindow extends JInternalFrame implements LogChangeListener, Storable, Restorable
{
    private LogWindowSource m_logSource;
    private TextArea m_logContent;
    public String windowName = "LogWindow";

    public LogWindow(LogWindowSource logSource) 
    {
        super("Протокол работы", true, true, true, true);
        m_logSource = logSource;
        m_logSource.registerListener(this);
        m_logContent = new TextArea("");
        m_logContent.setSize(200, 500);
        
        JPanel panel = new JPanel(new BorderLayout());
        panel.add(m_logContent, BorderLayout.CENTER);
        getContentPane().add(panel);
        pack();
        updateLogContent();
    }

    private void updateLogContent()
    {
        StringBuilder content = new StringBuilder();
        for (LogEntry entry : m_logSource.all())
        {
            content.append(entry.getMessage()).append("\n");
        }
        m_logContent.setText(content.toString());
        m_logContent.invalidate();
    }
    
    @Override
    public void onLogChanged()
    {
        EventQueue.invokeLater(this::updateLogContent);
    }

    @Override
    public void restoreOrDefault(Map<String, WindowPosition> store, WindowPosition defaultPosition) {
        restore(store.getOrDefault(windowName, defaultPosition));
    }

    private void restore(WindowPosition data) {
        Dimension size = new Dimension();
        size.width = data.getWidth();
        size.height = data.getHeight();
        m_logContent.setSize(size);
        this.setSize(size);
        try {
            this.setIcon(data.isHide());
        } catch (PropertyVetoException ignored) {}
        this.setLocation(data.getX(), data.getY());
        this.setVisible(true);
    }

    @Override
    public WindowPosition getDataForStore() {
        final WindowPosition position = new WindowPosition(
                windowName,
                this.getSize().width,
                this.getSize().height,
                this.isIcon,
                this.getLocation().x,
                this.getLocation().y
        );
        return position;
    }
}
