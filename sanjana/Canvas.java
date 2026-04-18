package sanjana;

import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.File;
import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;

public class Canvas implements MouseMotionListener {

    JFrame frame = new JFrame();
    JPanel canvasPanel, toolBar;
    JButton btn;
    JTextField col, sz;

    String prev, back;
    String currentTool = "BRUSH";

    public Canvas() {
        canvasPanel = new JPanel();
        canvasPanel.addMouseMotionListener(this);
    }

    public void CanvasView() {

        int left = 30;
        int gap = 15;
        int btnSize = 50;

        // ----------- CANVAS -------------
        canvasPanel.setBounds(300, 30, 900, 600);
        canvasPanel.setBackground(Color.white);
        canvasPanel.setLayout(null);
        frame.add(canvasPanel);

        canvasPanel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                String cl = col.getText();
                int rad = Integer.parseInt(sz.getText());

                Graphics g = canvasPanel.getGraphics();
                g.setColor(Color.decode(cl));

                if (currentTool.equals("BRUSH")) {
                    g.fillRect(e.getX(), e.getY(), rad, rad);
                } else if (currentTool.equals("RECT")) {
                    g.drawRect(e.getX(), e.getY(), rad * 5, rad * 5);
                } else if (currentTool.equals("CIRCLE")) {
                    g.drawOval(e.getX(), e.getY(), rad * 5, rad * 5);
                }
            }
        });

       // Added toolbar improvement
        toolBar = new JPanel();
        toolBar.setBounds(30, 30, 240, 600);
        toolBar.setBackground(Color.white);
        toolBar.setLayout(null);
        frame.add(toolBar);

        // ----------- COLOR GRID -------------
        int x = left, y = 30;

        String[] colors = {
            "#000000","#FFFFFF","#808080",
            "#FF0000","#00FF00","#0000FF",
            "#FFFF00","#FFA500","#A020F0",
            "#FFC0CB","#964B00","#C32148"
        };

        int count = 0;
        for (String c : colors) {
            addButton(x, y, c);
            x += btnSize + gap;
            count++;

            if (count % 3 == 0) {
                x = left;
                y += btnSize + gap;
            }
        }

        int currentY = y + 20;

        // ----------- CUSTOM COLOR -------------
        JLabel cllabel = new JLabel("Custom Color:");
        cllabel.setBounds(left, currentY, 150, 25);
        toolBar.add(cllabel);

        currentY += 30;

        col = new JTextField("#000000");
        col.setBounds(left, currentY, 180, 30);
        toolBar.add(col);

        currentY += 40;

        // ----------- BACKGROUND -------------
        back = "#FFFFFF";
        JButton bc = new JButton("SET BACKGROUND");
        bc.setBounds(left, currentY, 180, 30);
        toolBar.add(bc);

        bc.addActionListener(e -> {
            back = col.getText();
            canvasPanel.setBackground(Color.decode(back));
        });

        currentY += 40;

        // ----------- SIZE -------------
        JLabel szlabel = new JLabel("Size:");
        szlabel.setBounds(left, currentY, 100, 25);
        toolBar.add(szlabel);

        currentY += 30;

        JButton sub = new JButton("-");
        sub.setBounds(left, currentY, 50, 30);
        toolBar.add(sub);

        sz = new JTextField("5");
        sz.setBounds(left + 65, currentY, 50, 30);
        toolBar.add(sz);

        JButton add = new JButton("+");
        add.setBounds(left + 130, currentY, 50, 30);
        toolBar.add(add);

        sub.addActionListener(e -> {
            int val = Integer.parseInt(sz.getText());
            if (val > 1) sz.setText((val - 1) + "");
        });

        add.addActionListener(e -> {
            int val = Integer.parseInt(sz.getText());
            sz.setText((val + 1) + "");
        });

        currentY += 50;

        // ----------- TOOLS -------------
        JButton brush = new JButton("BRUSH");
        brush.setBounds(left, currentY, 80, 30);
        toolBar.add(brush);

        JButton erase = new JButton("ERASE");
        erase.setBounds(left + 100, currentY, 80, 30);
        toolBar.add(erase);

        prev = "#000000";

        brush.addActionListener(e -> currentTool = "BRUSH");

        erase.addActionListener(e -> {
            currentTool = "BRUSH";
            col.setText(back);
        });

        currentY += 40;

        JButton rect = new JButton("RECT");
        rect.setBounds(left, currentY, 80, 30);
        toolBar.add(rect);

        rect.addActionListener(e -> currentTool = "RECT");

        JButton circle = new JButton("CIRCLE");
        circle.setBounds(left + 100, currentY, 80, 30);
        toolBar.add(circle);

        circle.addActionListener(e -> currentTool = "CIRCLE");

        currentY += 40;

        // ----------- CLEAN -------------
        JButton clean = new JButton("CLEAN");
        clean.setBounds(left, currentY, 180, 30);
        toolBar.add(clean);

        clean.addActionListener(e -> {
            canvasPanel.removeAll();
            canvasPanel.repaint();
        });

        currentY += 40;

        // ----------- SAVE -------------
        JButton save = new JButton("SAVE / EXPORT");
        save.setBounds(left, currentY, 180, 30);
        toolBar.add(save);

        save.addActionListener(e -> {
            try {
                BufferedImage image = getImg(canvasPanel);

                JFileChooser chooser = new JFileChooser();
                chooser.setDialogTitle("Save Drawing");

                chooser.addChoosableFileFilter(
                    new FileNameExtensionFilter("PNG Image", "png"));
                chooser.addChoosableFileFilter(
                    new FileNameExtensionFilter("JPEG Image", "jpg", "jpeg"));

                int result = chooser.showSaveDialog(frame);

                if (result == JFileChooser.APPROVE_OPTION) {
                    File file = chooser.getSelectedFile();
                    String path = file.getAbsolutePath();

                    String format = "png";
                    if (chooser.getFileFilter().getDescription().contains("JPEG")) {
                        format = "jpg";
                    }

                    if (!path.toLowerCase().endsWith("." + format)) {
                        path += "." + format;
                    }

                    ImageIO.write(image, format, new File(path));
                }

            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });

        // ----------- FRAME SETTINGS -------------
        frame.setSize(1250, 700);
        frame.setLocationRelativeTo(null);
        frame.setLayout(null);
        frame.getContentPane().setBackground(Color.decode("#001122"));
        frame.setResizable(false);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }

    // ----------- MOUSE DRAG -------------
    @Override
    public void mouseDragged(MouseEvent e) {
        if (currentTool.equals("BRUSH")) {
            String cl = col.getText();
            int rad = Integer.parseInt(sz.getText());

            Graphics g = canvasPanel.getGraphics();
            g.setColor(Color.decode(cl));
            g.fillRect(e.getX(), e.getY(), rad, rad);
        }
    }

    @Override
    public void mouseMoved(MouseEvent e) {}

    // ----------- COLOR BUTTON -------------
    public void addButton(int x, int y, String clr) {
        btn = new JButton();
        btn.setBounds(x, y, 50, 50);
        btn.setBackground(Color.decode(clr));
        btn.addActionListener(e -> col.setText(clr));
        toolBar.add(btn);
    }

    // ----------- IMAGE CAPTURE -------------
    public BufferedImage getImg(Component comp) throws AWTException {
        Point p = comp.getLocationOnScreen();
        Dimension dim = comp.getSize();
        return new Robot().createScreenCapture(new Rectangle(p, dim));
    }
}