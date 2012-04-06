/* Created on 3-Dec-2003 */
package ajmas74.experiments.gui.trees;

import java.awt.*;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.List;

import javax.swing.*;
import javax.swing.tree.*;
/**
 * no description
 * 
 * @author Andre-John Mas
 */
public class JTreeView {

  Map _map;
  /**
   * 
   */
  public JTreeView() {
    super();
    _map = new HashMap();
    _map.put("abc",new String("123"));
    _map.put("def",new String("456"));
    _map.put("ghi",new String("789"));
    
    HashMap map = new HashMap();
    map.put("hui",new Object());
    _map.put("jkl",map);
    // TODO Auto-generated constructor stub
  }

  public JTree getJTree () {
    return new JTree(new MapTreeNodeAdaptor("root",_map,null));
  }
  
  public static void main ( String[] args ) {
    Frame frame = new Frame();
    frame.add( (new JTreeView()).getJTree() );
    frame.setBounds(50,50,300,500);
    frame.setVisible(true);
  }
  
  // ---------------------------------------------------
  
  public static class MapTreeNodeAdaptor implements TreeNode {

    private String _displayName;
    private Map _map;
    private TreeNode _parent;
    private List _treeNodes;
    
    MapTreeNodeAdaptor ( String displayName, Map map, TreeNode parent ) {
      _displayName = displayName;
      _map = map;
      _parent = parent;
      init();
    }
    
    private void init() {
      _treeNodes = new ArrayList(_map.size());
      Iterator iter = _map.keySet().iterator();
      while ( iter.hasNext() ) {
        Object key = iter.next();
        Object value = _map.get(key);
        
        if ( value instanceof Map ) {
          _treeNodes.add(new MapTreeNodeAdaptor(
              key.toString(),(Map)value, this));
        } else {
          _treeNodes.add(new ObjectTreeNodeAdaptor(
              key.toString(), value, this));          
        }
      }
    }
    
    /* (non-Javadoc)
     * @see javax.swing.tree.TreeNode#getChildAt(int)
     */
    public TreeNode getChildAt(int childIndex) {
      return (TreeNode) _treeNodes.get(childIndex);
    }

    /* (non-Javadoc)
     * @see javax.swing.tree.TreeNode#getChildCount()
     */
    public int getChildCount() {
      return _treeNodes.size();
    }

    /* (non-Javadoc)
     * @see javax.swing.tree.TreeNode#getParent()
     */
    public TreeNode getParent() {
      return _parent;
    }

    /* (non-Javadoc)
     * @see javax.swing.tree.TreeNode#getIndex(javax.swing.tree.TreeNode)
     */
    public int getIndex(TreeNode node) {
      return _treeNodes.indexOf(node);
    }

    /* (non-Javadoc)
     * @see javax.swing.tree.TreeNode#getAllowsChildren()
     */
    public boolean getAllowsChildren() {
      return true;
    }

    /* (non-Javadoc)
     * @see javax.swing.tree.TreeNode#isLeaf()
     */
    public boolean isLeaf() {
      return false;
    }

    /* (non-Javadoc)
     * @see javax.swing.tree.TreeNode#children()
     */
    public Enumeration children() {
      return new Enumeration() {
        int _idx = 0;
        
        public boolean hasMoreElements() {
          return ( _idx < _treeNodes.size() );
        }

        public Object nextElement() {
          return _treeNodes.get(_idx++);
        }
      };
      
    }
    
    /** */
    public String toString () {
      return _displayName;
    }
    
    public Icon getIcon () {
      return null;
    }    
  }
  
  public static class ObjectTreeNodeAdaptor implements TreeNode {

    private String _displayName;
    private Object _object;
    private TreeNode _parent;
    
    ObjectTreeNodeAdaptor ( String displayName, Object object, TreeNode parent ) {
      _displayName = displayName;
      _object = object;
      _parent = parent;
      init();
    }
    
    private void init() {
    }
    
    /* (non-Javadoc)
     * @see javax.swing.tree.TreeNode#getChildAt(int)
     */
    public TreeNode getChildAt(int childIndex) {
      return null;
    }

    /* (non-Javadoc)
     * @see javax.swing.tree.TreeNode#getChildCount()
     */
    public int getChildCount() {
      return 0;
    }

    /* (non-Javadoc)
     * @see javax.swing.tree.TreeNode#getParent()
     */
    public TreeNode getParent() {
      return _parent;
    }

    /* (non-Javadoc)
     * @see javax.swing.tree.TreeNode#getIndex(javax.swing.tree.TreeNode)
     */
    public int getIndex(TreeNode node) {
      return -1;
    }

    /* (non-Javadoc)
     * @see javax.swing.tree.TreeNode#getAllowsChildren()
     */
    public boolean getAllowsChildren() {
      return false;
    }

    /* (non-Javadoc)
     * @see javax.swing.tree.TreeNode#isLeaf()
     */
    public boolean isLeaf() {
      return true;
    }

    /* (non-Javadoc)
     * @see javax.swing.tree.TreeNode#children()
     */
    public Enumeration children() {
      return new Enumeration() {
        int _idx = 0;
        
        public boolean hasMoreElements() {
          return false;
        }

        public Object nextElement() {
          return null;
        }
      };
      
    }
    
    public Object getUserObject() {
      return _object;      
    }
    
    /** */
    public String toString () {
      return _object.toString();
    }
    
    public Icon getIcon () {
      return null;
    }
  }  
}
