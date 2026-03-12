package org.adonis;

import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import java.io.File;

public class FileTreeModel extends DefaultTreeModel {
    public FileTreeModel(File root) {
        super(createNodes(root));
    }
    private static DefaultMutableTreeNode createNodes(File file) {
        DefaultMutableTreeNode node = new DefaultMutableTreeNode(file);

        File[] files = file.listFiles();
        if(files == null) return node;

        for(File f : files) {
            if(f.isDirectory()) {
                node.add(createNodes(f));
            } else {
                node.add(new DefaultMutableTreeNode(f));
            }
        }
        return node;
    }
}
