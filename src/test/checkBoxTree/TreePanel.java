package test.checkBoxTree;
import java.awt.*;
import java.util.Random;
import javax.swing.*;
import javax.swing.event.*;
import javax.swing.tree.*;
 
public class TreePanel
{
    Random seed;
 
    public TreePanel()
    {
        seed = new Random();
 
        // south section
        final JLabel colorLabel = new JLabel();
        colorLabel.setHorizontalAlignment(JLabel.CENTER);
        Dimension d = colorLabel.getPreferredSize();
        d.height = 25;
        colorLabel.setPreferredSize(d);
 
        // center section
        JTree tree = createTree();
        tree.setCellRenderer(new PanelRenderer());
        tree.getSelectionModel().addTreeSelectionListener(
                                         new TreeSelectionListener()
        {
            public void valueChanged(TreeSelectionEvent e)
            {
                TreePath path = e.getNewLeadSelectionPath();
                DefaultMutableTreeNode node = 
                    (DefaultMutableTreeNode)path.getLastPathComponent();
                Color color = (Color)node.getUserObject();
                String s = "" + color.getRed() + ", " + color.getGreen() +
                                                 ", " + color.getBlue();
                colorLabel.setText(s);
            }
        });
 
        JFrame f = new JFrame();
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        f.add(new JScrollPane(tree));
        f.add(colorLabel, "South");
        f.setSize(400,400);
        f.setLocation(200,200);
        f.setVisible(true);
    }
 
    private JTree createTree()
    {
        int children = 4;
        int grandChildren = 3;
        DefaultMutableTreeNode root = new DefaultMutableTreeNode(getColor());
        DefaultMutableTreeNode node;
        for(int j = 0; j < children; j++)
        {
            node = new DefaultMutableTreeNode(getColor());
            root.add(node);
                for(int k = 0; k < grandChildren; k++)
                    node.add(new DefaultMutableTreeNode(getColor()));
        }
        DefaultTreeModel model = new DefaultTreeModel(root);
        return new JTree(model);
    }
 
    private Color getColor()
    {
        return new Color(seed.nextInt(0xffffff));
    }
 
    public static void main(String[] args)
    {
        new TreePanel();
    }
}
 
class PanelRenderer implements TreeCellRenderer
{
    JPanel panel;
 int i=0;
    public PanelRenderer()
    {
        panel = new JPanel(new BorderLayout());
    }
 
    public Component getTreeCellRendererComponent(JTree tree,
                                                  Object value,
                                                  boolean selected,
                                                  boolean expanded,
                                                  boolean leaf,
                                                  int row,
                                                  boolean hasFocus)
    {
        Color color = (Color)((DefaultMutableTreeNode)value).getUserObject();
        panel.setBackground(color);
        panel.setPreferredSize(new Dimension(75, 25));
        System.out.println(i);
        i++;
        return panel;
    }
}

