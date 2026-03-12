package org.adonis;

import javax.swing.*;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreeModel;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

public class FileTree extends JTree {
    public FileTree(File root) {
        super((TreeModel) new FileTreeModel(root));
        setRootVisible(false);
        setShowsRootHandles(true);
        addTreeSelectionListener(new FileOpenListener());
    }
    private class FileOpenListener implements TreeSelectionListener {
        @Override
        public void valueChanged(TreeSelectionEvent e) {
            DefaultMutableTreeNode node = (DefaultMutableTreeNode)getLastSelectedPathComponent();

            if(node == null) return;

            File file = (File) node.getUserObject();
            if(file.isFile()) {
                try {
                    String content = Files.readString(file.toPath());
                    editor.getInstance().setEditorText(content);
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
        }
    }
}
