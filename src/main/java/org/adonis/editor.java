package org.adonis;

import javax.swing.*;
import javax.swing.plaf.metal.MetalLookAndFeel;
import javax.swing.plaf.metal.MetalTheme;
import javax.swing.plaf.metal.OceanTheme;
import javax.tools.JavaCompiler;
import javax.tools.ToolProvider;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.net.URI;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

class editor extends JFrame implements ActionListener {
    JTextArea t;
    JFrame f;

    editor() {
        f = new JFrame("Editor");
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        try {
            UIManager.setLookAndFeel("javax.swing.plaf.metal.MetalLookAndFeel");

            MetalLookAndFeel.setCurrentTheme(new OceanTheme());
        } catch (Exception e) {
            e.printStackTrace();
        }

        t = new JTextArea();
        JMenuBar mb = new JMenuBar();
        JMenu m1 = new JMenu("File");

        JMenuItem mi1 = new JMenuItem("New");
        JMenuItem mi2 = new JMenuItem("Open");
        JMenuItem mi3 = new JMenuItem("Save");
        JMenuItem mi4 = new JMenuItem("Print");
        JMenuItem mi5 = new JMenuItem("Quit");

        mi1.addActionListener(this);
        mi2.addActionListener(this);
        mi3.addActionListener(this);
        mi4.addActionListener(this);
        mi5.addActionListener(this);

        m1.add(mi1);
        m1.add(mi2);
        m1.add(mi3);
        m1.add(mi4);
        m1.add(mi5);

        JMenu m2 = new JMenu("Edit");

        JMenuItem mi7 = new JMenuItem("cut");
        JMenuItem mi8 = new JMenuItem("copy");
        JMenuItem mi9 = new JMenuItem("paste");

        mi7.addActionListener(this);
        mi8.addActionListener(this);
        mi9.addActionListener(this);

        JMenu m3 = new JMenu("Run");
        JMenuItem mi10 = new JMenuItem("Compile");
        mi10.addActionListener(this);
        m3.add(mi10);
        JMenuItem mi11 = new JMenuItem("Run");
        mi11.addActionListener(this);
        m3.add(mi11);

        JButton button1 = new JButton("Click for sex");
        button1.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try {
                    java.awt.Desktop.getDesktop().browse(URI.create("https://github.com/alexandru-andoni"));
                } catch (IOException ex) {
                    throw new RuntimeException(ex);               }
            }
        });
        m3.add(button1);

        m2.add(mi7);
        m2.add(mi8);
        m2.add(mi9);

        mb.add(m3);
       mb.add(m1);
       mb.add(m2);
       mb.add(button1);


       f.setJMenuBar(mb);
       JScrollPane scroll = new JScrollPane(t);
       scroll.setRowHeaderView(new LineNumberPanel(t));
       f.add(scroll);
       f.setSize(500, 500);
       f.setVisible(true);

    }
    @Override
    public void actionPerformed(ActionEvent e) {
        String s = e.getActionCommand();

        if(s.equals("cut")) {
            t.cut();
        }
        else if(s.equals("copy")) {
            t.copy();
        }
        else if(s.equals("paste")) {
            t.paste();
        }
        else if (s.equals("Save")) {
            JFileChooser j = new JFileChooser("f:");

            int r = j.showSaveDialog(null);
            if (r == JFileChooser.APPROVE_OPTION) {
                File fi = new File(j.getSelectedFile().getAbsolutePath());
                try {
                    FileWriter wr = new FileWriter(fi, false);
                    BufferedWriter w = new BufferedWriter(wr);

                    w.write(t.getText());
                    w.flush();
                    w.close();
                } catch (Exception evt) {
                    JOptionPane.showMessageDialog(f, evt.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                }
            } else
                JOptionPane.showMessageDialog(f, "the user has cancelled the operation");

        }
        else if (s.equals("Print")) {
            try {
                t.print();

            }
            catch(Exception evt) {
                JOptionPane.showMessageDialog(f, evt.getMessage());
            }
        }
        else if (s.equals("Open")) {
            JFileChooser j = new JFileChooser("f:");
            int r = j.showOpenDialog(null);

            if (r == JFileChooser.APPROVE_OPTION) {
                File fi = new File(j.getSelectedFile().getAbsolutePath());

                try {
                    String s1 = "", sl = "";

                    FileReader fr = new FileReader(fi);

                    BufferedReader br = new BufferedReader(fr);

                    sl = br.readLine();

                    while ((s1 = br.readLine()) != null) {
                        sl = sl + "\n" + s1;
                    }

                    t.setText(sl);

                } catch (Exception evt) {
                    JOptionPane.showMessageDialog(f, evt.getMessage());
                }
            }
            else
                JOptionPane.showMessageDialog(f, "the user has cancelled the operation");

        }
        else if (s.equals("New")) {
            t.setText("");
        }

        else if (s.equals("Quit")) {
            f.setVisible(false);
        }
        else if (s.equals("compile")) {
            try {
                Pattern p = Pattern.compile("public\\s+class\\s+(\\w+)");
                Matcher m = p.matcher(t.getText());
                if (m.find()) {
                    String className = m.group(1);
                }
                File fi = new File(getClass() + ".java");
                try (FileWriter fw = new FileWriter(fi)) {
                    fw.write(t.getText());
                }
                JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
                if(compiler == null) {
                    JOptionPane.showMessageDialog(f, "No java compiler found!. run this program using a JDK, not a JRE");
                    return;
                }
                int result = compiler.run(null, null, null, fi.getPath());
                if (result == 0) {
                    JOptionPane.showMessageDialog(f, "Compiler successfully executed!");
                } else {
                    JOptionPane.showMessageDialog(f, "Compiler failed!");
                }
            } catch (Exception evt) {
                JOptionPane.showMessageDialog(f, evt.getMessage());
            }
        }
        else if (s.equals("run")) {
            try{
            Process process = Runtime.getRuntime().exec("java Program");
            BufferedReader input = new BufferedReader(new InputStreamReader(process.getInputStream()));
            BufferedReader error = new BufferedReader(new InputStreamReader(process.getErrorStream()));

            StringBuilder output = new StringBuilder();
            String line;

            while ((line = input.readLine()) != null) output.append(line + "\n");
            while ((line = error.readLine()) != null) output.append(line + "\n");

            JOptionPane.showMessageDialog(f, output.toString());

        } catch (Exception evt) {
            JOptionPane.showMessageDialog(f, evt.getMessage());
            }
        }
    }

    public static void main(String[] args) {
        editor e = new editor();
    }

    public void openWebPage(String url) {
        try {
            java.awt.Desktop.getDesktop().browse(java.net.URI.create(url));
        } catch(java.io.IOException e) {
            System.out.println(e.getMessage());
        }
    }

}
