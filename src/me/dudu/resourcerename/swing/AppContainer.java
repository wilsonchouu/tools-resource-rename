package me.dudu.resourcerename.swing;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

/**
 * Author : zhouyx
 * Date   : 2017/8/20
 * Description :
 */
public class AppContainer {

    private JFrame frame;
    private JPanel containerPanel;
    private JButton mipmapButton;
    private JButton drawableButton;
    private JButton changePathButton;
    private JLabel locationTipsLabel;
    private JLabel locationPathLabel;
    private JTextArea progressDetailTextArea;
    private JScrollPane progressDetailScrollPane;

    private String location = "";

    private OnButtonClickListener onButtonClickListener;

    public AppContainer(JFrame frame, String location) {
        this.frame = frame;
        this.location = location;
        containerPanel = createPanel(new FlowLayout());
        mipmapButton = createButton("分类至mipmap");
        drawableButton = createButton("分类至drawable");
        changePathButton = createButton("更改");
        changePathButton.setPreferredSize(new Dimension(60, 33));
        locationTipsLabel = createLabel("位置：");
        locationPathLabel = createLabel(location);
        locationPathLabel.setPreferredSize(new Dimension(400, 33));
        locationPathLabel.setMinimumSize(new Dimension(400, 33));
        locationPathLabel.setMaximumSize(new Dimension(400, 33));
        locationPathLabel.setSize(new Dimension(400, 33));
        progressDetailTextArea = createTextArea();
        progressDetailScrollPane = createScrollPane(progressDetailTextArea);

        draw();
        setListener();
    }

    private void draw() {
        Box locationBox = Box.createHorizontalBox();
        locationBox.add(locationTipsLabel);
        locationBox.add(locationPathLabel);
        locationBox.add(Box.createHorizontalStrut(20));
        locationBox.add(changePathButton);

        Box detailBox = Box.createHorizontalBox();
        detailBox.add(progressDetailScrollPane);

        Box buttonBox = Box.createHorizontalBox();
        buttonBox.add(drawableButton);
        buttonBox.add(Box.createHorizontalStrut(50));
        buttonBox.add(mipmapButton);

        Box baseBox = Box.createVerticalBox();
        baseBox.add(Box.createVerticalStrut(25));
        baseBox.add(locationBox);
        baseBox.add(Box.createVerticalStrut(10));
        baseBox.add(detailBox);
        baseBox.add(Box.createVerticalStrut(30));
        baseBox.add(buttonBox);
        containerPanel.add(baseBox);
    }

    private void setListener() {
        changePathButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFileChooser fileChooser = createFileChooser();
                File selectFile = fileChooser.getSelectedFile();
                location = selectFile.getAbsolutePath();
                locationPathLabel.setText(location);
            }
        });
        mipmapButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (onButtonClickListener != null) {
                    onButtonClickListener.onMipmapClick(location);
                }
            }
        });
        drawableButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (onButtonClickListener != null) {
                    onButtonClickListener.onDrawableClick(location);
                }
            }
        });
    }

    public void initDetailMessage() {
        progressDetailTextArea.setText("");
        progressDetailTextArea.paintImmediately(progressDetailTextArea.getBounds());
    }

    public void appendDetailMessage(String text) {
        progressDetailTextArea.append(text + "\n");
        progressDetailTextArea.paintImmediately(progressDetailTextArea.getBounds());
        progressDetailTextArea.selectAll();
    }

    private JFileChooser createFileChooser() {
        JFileChooser fileChooser = new JFileChooser(location);
        fileChooser.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
        fileChooser.showDialog(frame, "选择");
        return fileChooser;
    }

    public JPanel getContainerPanel() {
        return containerPanel;
    }

    private JLabel createLabel(String text) {
        JLabel label = new JLabel(text);
        label.setBackground(new Color(255, 255, 255, 0));
        label.setForeground(new Color(255, 255, 255));
        return label;
    }

    private JTextArea createTextArea() {
        JTextArea textArea = new JTextArea();
        textArea.setBackground(new Color(255, 255, 255, 0));
        textArea.setEditable(false);
        textArea.setOpaque(false);
        textArea.setLineWrap(true);
        return textArea;
    }

    private JScrollPane createScrollPane(Component view) {
        JScrollPane scrollPane = new JScrollPane(view);
        scrollPane.setBackground(new Color(255, 255, 255, 0));
        scrollPane.setForeground(new Color(255, 255, 255, 0));
        scrollPane.setOpaque(false);
        scrollPane.setAutoscrolls(false);
        scrollPane.setPreferredSize(new Dimension(510, 200));
        scrollPane.setMinimumSize(new Dimension(510, 200));
        scrollPane.setMaximumSize(new Dimension(510, 200));
        scrollPane.setSize(new Dimension(510, 200));
        return scrollPane;
    }

    private JPanel createPanel(LayoutManager layoutManager) {
        JPanel panel = new JPanel(layoutManager);
        panel.setBackground(new Color(60, 63, 65));
        return panel;
    }

    private JButton createButton(String text) {
        JButton button = new JButton(text);
//        button.setBackground(new Color(80, 85, 85));
//        button.setForeground(new Color(255, 255, 255));
        button.setSize(new Dimension(90, 33));
        button.setFocusPainted(false);
        button.setHorizontalTextPosition(JButton.CENTER);
        button.setVerticalTextPosition(JButton.CENTER);
        return button;
    }

    public void setOnButtonClickListener(OnButtonClickListener onButtonClickListener) {
        this.onButtonClickListener = onButtonClickListener;
    }

    public interface OnButtonClickListener {
        void onDrawableClick(String location);

        void onMipmapClick(String location);
    }

}
