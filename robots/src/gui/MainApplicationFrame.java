package gui;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.swing.*;

import lacal.Localizator;
import log.Logger;
import store.HaveStorableFrames;
import store.PositionStore;
import store.HasState;
import store.WindowState;
import robot.Robot;

public class MainApplicationFrame extends JFrame implements HaveStorableFrames {
    private final JDesktopPane desktopPane = new JDesktopPane();

    public MainApplicationFrame(Robot robot) {
        int inset = 50;
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        setBounds(inset, inset,
                screenSize.width - inset * 2,
                screenSize.height - inset * 2);

        setContentPane(desktopPane);

        RobotInfoWindow robotInfoWindow = new RobotInfoWindow();
        robot.registerObserver(robotInfoWindow);
        addWindow(robotInfoWindow);

        LogWindow logWindow = createLogWindow();
        addWindow(logWindow);

        GameWindow gameWindow = new GameWindow(robot);
        addWindow(gameWindow);


        addWindowListener(initExitListener());

        setJMenuBar(generateMenuBar());
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
    }

    protected WindowAdapter initExitListener() {
        return new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                Object[] options = {"Да", "Нет"};
                int result = JOptionPane.showOptionDialog(
                        desktopPane,
                        "Закрыть программу?",
                        "Закрыть программу?",
                        JOptionPane.YES_NO_OPTION,
                        JOptionPane.QUESTION_MESSAGE,
                        null,
                        options,
                        options[0]
                );
                if (result == 0) {
                    try {
                        PositionStore store = new PositionStore(MainApplicationFrame.this, System.getProperty("user.home"));
                        store.storePositions();
                    } catch (IOException exc) {
                        JOptionPane.showMessageDialog(
                                desktopPane,
                                "Во время сохранения данных произошла ошибка."
                        );
                    }
                    setDefaultCloseOperation(EXIT_ON_CLOSE);
                }
            }
        };
    }

    private static LogWindow createLogWindow() {
        LogWindow logWindow = new LogWindow(Logger.getDefaultLogSource());
        logWindow.setMinimumSize(logWindow.getSize());
        logWindow.pack();
        Logger.debug("Протокол работает");
        return logWindow;
    }

    private void addWindow(JInternalFrame frame) {
        desktopPane.add(frame);
        frame.setVisible(true);
    }

    private JMenuBar generateMenuBar() {
        JMenuBar menuBar = new JMenuBar();

        JMenu optionMenu = new JMenu("Опции");
        optionMenu.setMnemonic(KeyEvent.VK_F);
        optionMenu.getAccessibleContext().setAccessibleDescription("Управление программой");
        {
            JMenuItem exitItem = new JMenuItem("Выйти", KeyEvent.VK_Q);
            exitItem.addActionListener((event) -> {
                Toolkit.getDefaultToolkit().getSystemEventQueue().postEvent(
                        new WindowEvent(this, WindowEvent.WINDOW_CLOSING));
            });
            optionMenu.add(exitItem);
        }

        {
            JMenuItem switchItem = new JMenuItem(Localizator.getLangBundle().getString("ChangeLocale"), KeyEvent.VK_C);
            switchItem.addActionListener((event) -> {
                JOptionPane.showMessageDialog(
                        desktopPane,
                        Localizator.getLangBundle().getString("ConfirmChanging")
                );
                Localizator.switchLocale();
            });
            optionMenu.add(switchItem);
        }

        JMenu lookAndFeelMenu = new JMenu("Режим отображения");
        lookAndFeelMenu.setMnemonic(KeyEvent.VK_V);
        lookAndFeelMenu.getAccessibleContext().setAccessibleDescription(
                "Управление режимом отображения приложения");

        {
            JMenuItem systemLookAndFeel = new JMenuItem("Системная схема", KeyEvent.VK_S);
            systemLookAndFeel.addActionListener((event) -> {
                setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
                this.invalidate();
            });
            lookAndFeelMenu.add(systemLookAndFeel);
        }

        {
            JMenuItem crossplatformLookAndFeel = new JMenuItem("Универсальная схема", KeyEvent.VK_S);
            crossplatformLookAndFeel.addActionListener((event) -> {
                setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
                this.invalidate();
            });
            lookAndFeelMenu.add(crossplatformLookAndFeel);
        }

        JMenu testMenu = new JMenu("Тесты");
        testMenu.setMnemonic(KeyEvent.VK_T);
        testMenu.getAccessibleContext().setAccessibleDescription(
                "Тестовые команды");

        {
            JMenuItem addLogMessageItem = new JMenuItem("Сообщение в лог", KeyEvent.VK_S);
            addLogMessageItem.addActionListener((event) -> Logger.debug("Новая строка"));
            testMenu.add(addLogMessageItem);
        }

        menuBar.add(optionMenu);
        menuBar.add(lookAndFeelMenu);
        menuBar.add(testMenu);
        return menuBar;
    }

    private void setLookAndFeel(String className) {
        try {
            UIManager.setLookAndFeel(className);
            SwingUtilities.updateComponentTreeUI(this);
        } catch (ClassNotFoundException | InstantiationException
                | IllegalAccessException | UnsupportedLookAndFeelException e) {
            // just ignore
        }
    }

    @Override
    public List<HasState> getDataForStore() {
        JInternalFrame[] allFrames = desktopPane.getAllFrames();
        List<HasState> toStore = new ArrayList<>();
        for (JInternalFrame frame : allFrames) {
            if (frame instanceof HasState) {
                toStore.add((HasState) frame);
            }
        }
        return toStore;
    }

    @Override
    public void restore(PositionStore store) {
        Map<String, WindowState> data = store.getStoredData();
        List<HasState> framesToRestore = getDataForStore();
        for (HasState frame : framesToRestore) {
            frame.setState(data);
        }
    }
}
