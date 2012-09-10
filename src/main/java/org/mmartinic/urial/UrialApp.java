package org.mmartinic.urial;

import java.awt.Dimension;
import java.awt.Image;
import java.awt.MenuItem;
import java.awt.Panel;
import java.awt.PopupMenu;
import java.awt.SystemTray;
import java.awt.TrayIcon;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.net.URL;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.swing.BoxLayout;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.SwingWorker;
import javax.swing.plaf.metal.MetalIconFactory;

import org.mmartinic.urial.filesystem.SeriesDirectoryPoller;
import org.mmartinic.urial.model.RenameResult;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class UrialApp extends JFrame {

    private static final long serialVersionUID = 1L;

    private static final int WIDTH = 400;
    private static final int HEIGHT = 400;
    private static final String ICON_PATH = "/renamer_logo.png";
    private JTextArea m_textArea;

    private final SeriesDirectoryPoller m_seriesDirectoryPoller;
    private final long m_pollInterval;

    public UrialApp(final String p_string) {
        super(p_string);
        createGUI();

        ApplicationContext context = new ClassPathXmlApplicationContext("applicationContext.xml");
        m_seriesDirectoryPoller = (SeriesDirectoryPoller) context.getBean("seriesDirectoryPoller");
        m_pollInterval = m_seriesDirectoryPoller.getPollInterval();

        m_textArea.append(new Date() + " Started\n");

        new RenameTask().execute();
    }

    private void createGUI() {

        setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
        setMinimumSize(new Dimension(WIDTH, HEIGHT));
        setResizable(false);

        getContentPane().setLayout(new BoxLayout(getContentPane(), BoxLayout.Y_AXIS));

        createMenuBar();
        createTextArea();
        createSystemTray();
    }

    private void createMenuBar() {
        JMenuBar menuBar = new JMenuBar();
        setJMenuBar(menuBar);
        JMenu fileMenu = new JMenu("File");
        menuBar.add(fileMenu);
        JMenuItem exitAction = new JMenuItem("Exit");
        fileMenu.add(exitAction);
        exitAction.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(final ActionEvent p_event) {
                exitAction();
            }
        });
    }

    private void createTextArea() {
        m_textArea = new JTextArea();
        m_textArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(m_textArea);
        getContentPane().add(scrollPane);
    }

    private void createSystemTray() {

        try {
            MenuItem exitItem = new MenuItem("Exit");
            exitItem.addActionListener(new ActionListener() {

                @Override
                public void actionPerformed(final ActionEvent p_event) {
                    exitAction();
                }
            });

            PopupMenu popupMenu = new PopupMenu();
            popupMenu.add(exitItem);
            TrayIcon trayIcon = new TrayIcon(getImage());
            trayIcon.setPopupMenu(popupMenu);
            trayIcon.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(final MouseEvent p_event) {
                    if (p_event.getButton() == MouseEvent.BUTTON1) {
                        setVisible(!isVisible());
                    }
                }
            });
            trayIcon.setToolTip("Renamer");
            SystemTray systemTray = SystemTray.getSystemTray();
            systemTray.add(trayIcon);
        }
        catch (Exception e) {
            e.printStackTrace();
            setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        }
    }

    private Image getImage() {
        URL imageURL = UrialApp.class.getResource(ICON_PATH);
        if (imageURL == null) {
            return getBackupImage();
        }
        else {
            return (new ImageIcon(imageURL)).getImage();
        }
    }

    private static Image getBackupImage() {
        Icon defaultIcon = MetalIconFactory.getTreeComputerIcon();
        Image img = new BufferedImage(defaultIcon.getIconWidth(), defaultIcon.getIconHeight(), BufferedImage.TYPE_4BYTE_ABGR);
        defaultIcon.paintIcon(new Panel(), img.getGraphics(), 0, 0);
        return img;
    }

    private void exitAction() {
        System.exit(0);
    }

    public static void main(final String[] p_args) {
        // Schedule a job for the event-dispatching thread:
        // creating and showing this application's GUI.
        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                JFrame frame = new UrialApp("Renamer");
                frame.pack();
                frame.setVisible(true);
            }
        });
    }

    private class RenameTask extends SwingWorker<Void, String> {

        @Override
        protected Void doInBackground() throws Exception {

            while (!isCancelled()) {
                RenameResult renameResult = m_seriesDirectoryPoller.pollAndRename();
                if (renameResult != null) {
                    publish(new Date().toString(), "\n", renameResult.getSuccessResult(), renameResult.getFailResult(), "\n");
                    // publish(renameResult.getNoNameResult());
                }
                TimeUnit.SECONDS.sleep(m_pollInterval);
            }
            return null;
        }

        @Override
        protected void process(final List<String> p_chunks) {
            for (String chunk : p_chunks) {
                m_textArea.append(chunk);
            }
        }

    }
}
