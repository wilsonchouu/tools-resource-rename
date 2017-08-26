package swing;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.plaf.ColorUIResource;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URL;

/**
 * Author : zhouyx
 * Date   : 2017/8/20
 * Description :
 */
public class UIContainer {

    private UIContainer uiContainer;

    private JFrame frame;
    private JPanel rootPanel;
    private JButton startButton;
    private JButton changePathButton;
    private JLabel locationPathLabel;
    private JLabel statusLabel;

    private boolean isWindowsStyle;
    private String location;
    private OnClickListener onClickListener;

    public UIContainer() {
        this.uiContainer = this;
        try {
            // 将LookAndFeel设置成Windows样式
            UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
            isWindowsStyle = true;
        } catch (Exception ex) {
            ex.printStackTrace();
            UIManager.put("Button.select", new ColorUIResource(new Color(80, 85, 85)));
            isWindowsStyle = false;
        }
        frame = createFrame();
        rootPanel = createPanel(new FlowLayout());
        locationPathLabel = createLabel("");
        locationPathLabel.setBorder(new EmptyBorder(0, 0, 0, 20));
        locationPathLabel.setPreferredSize(new Dimension(400, 25));
        changePathButton = createButton("更改");
        statusLabel = createLabel("");
        startButton = createButton("开始分类");

        draw();
        setListener();
        frame.setContentPane(rootPanel);
        frame.setVisible(true);
    }

    private void draw() {
        Box locationBox = Box.createHorizontalBox();
        JPanel locationPanel = createPanel(new BorderLayout());
        locationPanel.add(createLabel("位置："), "West");
        locationPanel.add(locationPathLabel, "Center");
        locationPanel.add(changePathButton, "East");
        locationBox.add(locationPanel);

        Box buttonBox = Box.createHorizontalBox();
        JPanel bottomPanel = createPanel(new BorderLayout());
        bottomPanel.add(statusLabel, "West");
        bottomPanel.add(startButton, "East");
        buttonBox.add(bottomPanel);

        Box baseBox = Box.createVerticalBox();
        baseBox.add(Box.createVerticalStrut(25));
        baseBox.add(locationBox);
        baseBox.add(Box.createVerticalStrut(50));
        baseBox.add(buttonBox);
        rootPanel.add(baseBox);
    }

    private void setListener() {
        changePathButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFileChooser fileChooser = createFileChooser();
                File selectFile = fileChooser.getSelectedFile();
                if (selectFile == null) {
                    return;
                }
                location = selectFile.getAbsolutePath();
                locationPathLabel.setText(location);
            }
        });
        startButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (onClickListener != null) {
                    onClickListener.onClick(uiContainer, location);
                }
            }
        });
    }

    public void setLocation(String location) {
        this.location = location;
        locationPathLabel.setText(location);
    }

    public void setMessage(String text) {
        statusLabel.setText(text);
    }

    private JFrame createFrame() {
        JFrame frame = new JFrame();
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.setTitle("Rename");
        frame.setSize(new Dimension(600, 200));
        frame.setLocationRelativeTo(null);
        try {
            URL url = getClass().getResource("/res/logo.png");
            BufferedImage image = ImageIO.read(url);
            frame.setIconImage(image);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return frame;
    }

    private JPanel createPanel(LayoutManager layoutManager) {
        JPanel panel = new JPanel(layoutManager);
        panel.setBackground(new Color(60, 63, 65));
        return panel;
    }

    private JFileChooser createFileChooser() {
        JFileChooser fileChooser = new JFileChooser(location);
        fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        fileChooser.showDialog(frame, "选择");
        return fileChooser;
    }

    private JLabel createLabel(String text) {
        JLabel label = new JLabel(text);
        label.setBackground(new Color(255, 255, 255, 0));
        label.setForeground(new Color(255, 255, 255));
        return label;
    }

    private JButton createButton(String text) {
        JButton button = new JButton(text);
        if (!isWindowsStyle) {
            button.setBackground(new Color(80, 85, 85));
            button.setForeground(new Color(255, 255, 255));
        }
        button.setSize(new Dimension(90, 33));
        button.setFocusPainted(false);
        button.setHorizontalTextPosition(JButton.CENTER);
        button.setVerticalTextPosition(JButton.CENTER);
        return button;
    }

    public void setOnClickListener(OnClickListener onClickListener) {
        this.onClickListener = onClickListener;
    }

    public interface OnClickListener {
        void onClick(UIContainer uiContainer, String location);
    }

}
