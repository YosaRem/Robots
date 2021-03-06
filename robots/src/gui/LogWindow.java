package gui;

import java.awt.*;
import java.beans.PropertyVetoException;
import java.util.Map;

import javax.swing.JInternalFrame;
import javax.swing.JPanel;

import log.LogEntry;
import log.LogWindowSource;
import robot.Observer;
import store.HasState;
import store.WindowState;

public class LogWindow extends JInternalFrame implements Observer, HasState {
    private final LogWindowSource m_logSource;
    private final TextArea m_logContent;
    private static final String WINDOW_NAME = "LogWindow";

    public LogWindow(LogWindowSource logSource) 
    {
        super("Протокол работы", true, true, true, true);
        m_logSource = logSource;
        m_logSource.registerObserver(this);
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
    public void setState(Map<String, WindowState> store) {
        if (store.containsKey(WINDOW_NAME)) {
            WindowState data = store.get(WINDOW_NAME);
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
    }

    @Override
    public WindowState getState() {
        return new WindowState(
                WINDOW_NAME,
                this.getSize().width,
                this.getSize().height,
                this.isIcon,
                this.getLocation().x,
                this.getLocation().y
        );
    }

    @Override
    public void objectModified(Object obj) {
        EventQueue.invokeLater(this::updateLogContent);
    }
}
