/* Created on 3-Dec-2003 */
package ajmas74.experimental.gui.trees;

//import java.awt.*;
//import org.eclipse.jface.window.*;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Shell;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.List;

import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.TreeItem;
import org.eclipse.swt.widgets.Tree;

import ajmas74.experimental.OrderedHashMap;

/**
 * no description
 * 
 * @author Andre-John Mas
 */
public class SWTTreeView {

  private static final String[] elements = new String[] {
	  "1 - Hydrogen", "2 - Helium", "3 - Lithium", "4 - Beryllium "
  };
  
  Map _map;
  Display _display;
  Map _iconMap;
  
  /**
   * 
   */
  public SWTTreeView() {
    super();
    _map = new OrderedHashMap();
    
    for ( int i=0; i<elements.length; i++ ) {
      _map.put(elements[i],new Object());
    }

    for ( int i=elements.length; i<112; i++ ) {
      _map.put(i + " - blahblah",new Object());
    }    
    HashMap map = new HashMap();
    map.put("hui",new Object());
    _map.put("jkl",map);
    // TODO Auto-generated constructor stub
  }

  private void initImages() {
    _iconMap = new HashMap();
    Image img = null;
    
    try {
      FileInputStream in = new FileInputStream("datafiles/atom16x16.gif");      
      img = new Image(_display,in);
      _iconMap.put("atom",img);
      in = new FileInputStream("datafiles/periodictable16x16.gif");      
      img = new Image(_display,in);
      _iconMap.put("table",img);      
    } catch ( IOException ex ) {
      ex.printStackTrace();
    }
  }
  public void createTree ( Shell parent) {
    _display = parent.getDisplay();
    initImages();
    Tree tree = new Tree(parent,0);
    swtTreeAdaptor("periodic table",_map,tree);
    //MapTreeItemAdaptor treeItem =  new MapTreeItemAdaptor("root",_map,tree);
//    TreeItem treeItem = new TreeItem(tree,0);
//    treeItem.setText("Hello");
//    treeItem = new TreeItem(treeItem,0);
//    treeItem.setText("gOODBYE");    
    //treeItem.
  }
  
  public static void main ( String[] args ) {
    SWTTreeView swtTreeView = new SWTTreeView();
    Display display = new Display();
    Shell shell = new Shell(display);
    shell.setText("Main");
    shell.setSize(300,500);
    shell.setLayout(new FillLayout());
    
    swtTreeView.createTree(shell);
    shell.setVisible(true);
 
    System.out.println("");
    while (!shell.isDisposed ()) {
      if (!display.readAndDispatch ()) display.sleep ();
    }
    display.dispose ();

//    Frame frame = new Frame();
//    frame.add( (new SWTTreeView()).getTree() );
//    frame.setBounds(50,50,300,500);
//    frame.setVisible(true);
  }
  
  
  private void swtTreeAdaptor( String rootName, Map map, Tree tree ) {
    TreeItem item = new TreeItem(tree,0);
    //item.
    item.setText(rootName);
    item.setExpanded(true);
    item.setData(map);
    item.setImage((Image)_iconMap.get("table"));
    
    //item.setImage()
    Iterator iter = map.keySet().iterator();
    while ( iter.hasNext() ) {
      Object key = iter.next();
      System.out.println(key);
      swtTreeItemAdaptor(key.toString(),map.get(key),item);
    }

  }
  
  private void swtTreeItemAdaptor( String name, Object obj, TreeItem parent ) {
    TreeItem item = new TreeItem(parent,0);
    item.setText(name);
    item.setData(obj);    
    if ( obj instanceof Map ) {
      Map map = (Map) obj;
      Iterator iter = map.keySet().iterator();
      while ( iter.hasNext() ) {
        Object key = iter.next();
        swtTreeItemAdaptor(key.toString(),map.get(key),item);
      }      
    } else {

      item.setImage((Image)_iconMap.get("atom"));
    }
  }
  
  // ---------------------------------------------------
  
  public static class MapTreeItemAdaptor extends TreeItem {

    private String _displayName;
    private Map _map;
    private List _treeItems;

    MapTreeItemAdaptor ( String displayName, Map map, Tree parent ) {
      super(parent,0);
      _displayName = displayName;
      _map = map;
      init();
    }
    
    MapTreeItemAdaptor ( String displayName, Map map, TreeItem parent ) {
      super(parent,0);
      _displayName = displayName;
      _map = map;
      init();
    }
    
    private void init() {
      _treeItems = new ArrayList(_map.size());
      Iterator iter = _map.keySet().iterator();
      while ( iter.hasNext() ) {
        Object key = iter.next();
        Object value = _map.get(key);
        
        if ( value instanceof Map ) {
          _treeItems.add(new MapTreeItemAdaptor(
              key.toString(),(Map)value, this));
        } else {
          _treeItems.add(new ObjectTreeItemAdaptor(
              key.toString(), value, this));          
        }
      }
    }

    /* (non-Javadoc)
     * @see org.eclipse.swt.widgets.TreeItem#getItemCount()
     */
    public int getItemCount() {
      return _treeItems.size();
    }

    /* (non-Javadoc)
     * @see org.eclipse.swt.widgets.TreeItem#getItems()
     */
    public TreeItem[] getItems() {
      return (TreeItem[]) _treeItems.toArray(new TreeItem[0]);
    }
    
  }
  
  public static class ObjectTreeItemAdaptor extends TreeItem {

    private String _displayName;
    private Object _object;
    private TreeItem[] _items;
    
    ObjectTreeItemAdaptor ( String displayName, Object object, Tree parent ) {
      super(parent,0);
      _displayName = displayName;
      _object = object;
      init();
    }
    
    ObjectTreeItemAdaptor ( String displayName, Object object, TreeItem parent ) {
      super(parent,0);
      _displayName = displayName;
      _object = object;
      init();
    }
    
    private void init() {
      _items = new TreeItem[0];
    }
    
    public Object getUserObject() {
      return _object;      
    }
    
    /** */
    public String toString () {
      return _object.toString();
    }
    
    public Image getIcon () {
      return null;
    }
    /* (non-Javadoc)
     * @see org.eclipse.swt.widgets.TreeItem#getItemCount()
     */
    public int getItemCount() {
      return _items.length;
    }

    /* (non-Javadoc)
     * @see org.eclipse.swt.widgets.TreeItem#getItems()
     */
    public TreeItem[] getItems() {
      return _items;
    }

  }  
}
