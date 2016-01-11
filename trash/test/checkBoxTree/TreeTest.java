package test.checkBoxTree;

import java.awt.Component;

import javax.swing.DefaultCellEditor;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;
import javax.swing.tree.MutableTreeNode;
import javax.swing.tree.TreePath;

class TrueFalseTreeNodeData {

  protected final String value;
  protected boolean booleanValue;

  public TrueFalseTreeNodeData(String quest) {
    value = quest;
  }

  public String getQuestion() {
    return value;
  }

  public boolean getAnswer() {
    return booleanValue;
  }

  public void setAnswer(boolean ans) {
    booleanValue = ans;
  }

  public String toString() {
    return value + " = " + booleanValue;
  }
}

public class TreeTest extends JFrame {

  protected final static String[] questions = { "A","B","C" };

  public static void main(String[] args) {
    TreeTest tt = new TreeTest();
    tt.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    tt.setSize(500, 200);
    tt.setVisible(true);
  }

  public TreeTest() {
    super();
    JTree tree = new JTree(getRootNode()) {
      public boolean isPathEditable(TreePath path) {
        Object comp = path.getLastPathComponent();
        if (comp instanceof DefaultMutableTreeNode) {
          DefaultMutableTreeNode node = (DefaultMutableTreeNode) comp;
          Object userObject = node.getUserObject();
          if (userObject instanceof TrueFalseTreeNodeData) {
            return true;
          }
        }
        return false;
      }
    };
    QuestionCellRenderer renderer = new QuestionCellRenderer();
    tree.setCellRenderer(renderer);
    QuestionCellEditor editor = new QuestionCellEditor();
    tree.setCellEditor(editor);
    tree.setEditable(true);
    JScrollPane jsp = new JScrollPane(tree);
    getContentPane().add(jsp);
  }

  protected MutableTreeNode getRootNode() {
    DefaultMutableTreeNode root, child;
    TrueFalseTreeNodeData question;
    root = new DefaultMutableTreeNode("Root");
    for (int i = 0; i < questions.length; i++) {
      question = new TrueFalseTreeNodeData(questions[i]);
      child = new DefaultMutableTreeNode(question);
      root.add(child);
    }
    return root;
  }

}

class QuestionCellRenderer extends DefaultTreeCellRenderer {

  protected JCheckBox checkBoxRenderer = new JCheckBox();

  public Component getTreeCellRendererComponent(JTree tree, Object value,
      boolean selected, boolean expanded, boolean leaf, int row,
      boolean hasFocus) {
    if (value instanceof DefaultMutableTreeNode) {
      DefaultMutableTreeNode node = (DefaultMutableTreeNode) value;
      Object userObject = node.getUserObject();
      if (userObject instanceof TrueFalseTreeNodeData) {
        TrueFalseTreeNodeData question = (TrueFalseTreeNodeData) userObject;
        prepareQuestionRenderer(question, selected);
        return checkBoxRenderer;
      }
    }
    return super.getTreeCellRendererComponent(tree, value, selected, expanded,
        leaf, row, hasFocus);
  }

  protected void prepareQuestionRenderer(TrueFalseTreeNodeData tfq, boolean selected) {
    checkBoxRenderer.setText(tfq.getQuestion());
    checkBoxRenderer.setSelected(tfq.getAnswer());
    if (selected) {
      checkBoxRenderer.setForeground(getTextSelectionColor());
      checkBoxRenderer.setBackground(getBackgroundSelectionColor());
    } else {
      checkBoxRenderer.setForeground(getTextNonSelectionColor());
      checkBoxRenderer.setBackground(getBackgroundNonSelectionColor());
    }
  }

}

class QuestionCellEditor extends DefaultCellEditor {

  protected TrueFalseTreeNodeData nodeData;

  public QuestionCellEditor() {
    super(new JCheckBox());
  }

  public Component getTreeCellEditorComponent(JTree tree, Object value,
      boolean selected, boolean expanded, boolean leaf, int row) {
    JCheckBox editor = null;
    nodeData = getQuestionFromValue(value);
    if (nodeData != null) {
      editor = (JCheckBox) (super.getComponent());
      editor.setText(nodeData.getQuestion());
      editor.setSelected(nodeData.getAnswer());
    }
    return editor;
  }

  public static TrueFalseTreeNodeData getQuestionFromValue(Object value) {
    if (value instanceof DefaultMutableTreeNode) {
      DefaultMutableTreeNode node = (DefaultMutableTreeNode) value;
      Object userObject = node.getUserObject();
      if (userObject instanceof TrueFalseTreeNodeData) {
        return (TrueFalseTreeNodeData) userObject;
      }
    }
    return null;
  }

  public Object getCellEditorValue() {
    JCheckBox editor = (JCheckBox) (super.getComponent());
    nodeData.setAnswer(editor.isSelected());
    return nodeData;
  }

}
