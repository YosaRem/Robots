package gui;

import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.awt.TextArea;
import java.util.AbstractMap;
import java.util.Map;

import javax.swing.JInternalFrame;
import javax.swing.JPanel;

import log.LogChangeListener;
import log.LogEntry;
import log.LogWindowSource;
import store.Restorable;
import store.Storable;
import store.WindowPosition;

public class LogWindow extends JInternalFrame implements LogChangeListener, Storable, Restorable
{
    private LogWindowSource m_logSource;
    private TextArea m_logContent;
    private String windowName = "GameWindow";

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
    public void useStoreDataForRestore(Map<String, WindowPosition> store) {

    }

    @Override
    public WindowPosition getDataForStore() {
        final WindowPosition position = new WindowPosition(
                windowName,
                this.getSize().width,
                this.getSize().height,
                this.isIcon,
                this.getHeight(),
                this.getWidth()
        );
        return position;
    }
}
