/*
 * $Workfile: WebTree.java $
 *
 * Copyright 2006 Simpler Networks Inc.
 *
 * $Author:  Mas $
 * $Date: Feb 21, 2006 $
 * $Revision: 1.0 $
 */

package ajmas74.experimental;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;


/**
 * Generates a tree for a web page. note that it depends on the style sheets
 * in the comments at the end of this class.
 *
 * I have so far tested it in Internet Explorer, Firefox and Opera on
 * MS-Windows. Need to test with Safari and Omniweb.
 *
 */
public class WebTreeJ5
{
    private static final String VERTICAL_LINE = "vl";

    private static final String NO_CHILDREN_LAST_NODE = "ncln";
    private static final String CHILDREN_LAST_NODE = "cln";
    private static final String CHILDREN_LAST_NODE_COLLAPSED = "cln_c";

    private static final String NO_CHILDREN_MIDDLE_NODE = "ncmn";
    private static final String CHILDREN_MIDDLE_NODE = "cmn";
    private static final String CHILDREN_MIDDLE_NODE_COLLAPSED = "cmn_c";

    private boolean collapsed = true;
    private boolean allowJavascript = true;
    private String  urlPrefix;
    //private boolean serverSideExpand = false;


    /**
     * The URL of the tree. This is used when javascript is not available
     * to expand / collapse the nodes. You should include the URL upto
     * and including the name of the parameter, so for example:
     *
     *   myurl/blah?node=
     *
     */
    public void setTreeUrlPrefix ( String urlPrefix )
    {
        this.urlPrefix = urlPrefix;
    }

    /**
     * whether the tree should be collapsed by default
     *
     * @param collapsed
     */
    public void setCollapsed ( boolean collapsed )
    {
        this.collapsed = collapsed;
    }

    /**
     * specify whether the tree allows javascript expansion
     * @param allowJavascript
     */
    public void setAllowJavascript ( boolean allowJavascript )
    {
        this.allowJavascript = allowJavascript;
    }

    /**
     *
     * TODO Description and implementation details.
     */
    public static class Node
    {
      /** */
      String icon;
      /** */
      String name;
      /** */
      boolean open;

      /**
       *
       * @param icon
       * @param name
       */
      Node ( String icon, String name, boolean open)
      {
        this.icon = icon;
        this.name = name;
        this.open = open;
      }

      public String toString()
      {
          return "Node:["+this.name+"]";
      }


    }

    /**
     *
     *
     */
    public WebTreeJ5()
    {
        super();
        // TODO Auto-generated constructor stub
    }

    /**
     *
     * TODO implementation details
     *
     * @return ...
     */
    Map<Node,Map> generateTestMap ()
    {
        //Object leafNode = null;
        HashMap<Node,Map> map = null;
        HashMap<Node,Map> map2 = null;

        HashMap<Node,Map> rootMap = new HashMap<Node,Map>();
        rootMap.put( new Node("mc.gif","L1-1",false), map = new HashMap<Node,Map>() );

        map.put( new Node("mc.gif","L2-1",false), map2 = new HashMap<Node,Map>() );

        map2.put( new Node("mc.gif","L3-1",false), null );
        map2.put( new Node("mc.gif","L3-2",false), null );

        map.put( new Node("mc.gif","L2-2",false), map2 = new HashMap<Node,Map>() );

        map2.put( new Node("mc.gif","L3-1",false), null );
        map2.put( new Node("mc.gif","L3-2",false), null );

        rootMap.put( new Node("mc.gif","L1-2",false), map = new HashMap<Node,Map>() );

        map.put( new Node("mc.gif","L2-1",false), map2 = new HashMap<Node,Map>() );

        map2.put( new Node("mc.gif","L3-1-OPEN",true), null );
        map2.put( new Node("mc.gif","L3-2",false), null );

        map.put( new Node("mc.gif","L2-2",false), map2 = new HashMap<Node,Map>() );

        map2.put( new Node("mc.gif","L3-1",false), null );
        map2.put( new Node("mc.gif","L3-2",false), null );

        return rootMap;
    }

    private boolean hasOpenChild ( Map<Node,Map> map )
    {
        //List childMaps = new ArrayList();

        Iterator iter = map.keySet().iterator();
        while ( iter.hasNext() )
        {
            Node node = (Node) iter.next();
            if ( node.open )
            {
                return true;
            }
            Map<Node,Map> childMap = map.get(node);
            if ( childMap != null && hasOpenChild(childMap) ) {
                return true;
            }
        }

        return false;
    }

    /**
     *
     * TODO implementation details
     *
     * @param map
     * @return ...
     */
    public String generateTree ( Map<Node,Map> map)
    {
        return generateTree("node_0",0,map);
    }

    /**
     *
     * TODO implementation details
     *
     * @param name
     * @param depth
     * @param map
     * @return ...
     */
    public String generateTree ( String name, int depth, Map<Node,Map> map )
    {
        StringBuffer strBuf = new StringBuffer();
        Iterator iter = map.keySet().iterator();
        boolean collapsed = this.collapsed;

        strBuf.append("<table style=\"border-style: none;border-width: 0px;\" cellspacing=\"0\">\n");

        int size = map.size();
        int i = 1;
        while ( iter.hasNext() )
        {
            String expandableText = "";
            boolean expandable = false;
            Node node = (Node)iter.next();

            String cssClass = VERTICAL_LINE;

            Map<Node,Map> childMap = map.get(node);
            boolean openChild = childMap!=null && hasOpenChild(childMap);

            cssClass = NO_CHILDREN_MIDDLE_NODE;
            if ( childMap != null && childMap.size() > 0 )
            {
                cssClass = (collapsed&&!openChild?CHILDREN_MIDDLE_NODE_COLLAPSED:CHILDREN_MIDDLE_NODE);
                expandable = true;
            }
            if ( i == size )
            {
                cssClass = NO_CHILDREN_LAST_NODE;
                if ( childMap != null && childMap.size() > 0 )
                {
                    cssClass = (collapsed&&!openChild?CHILDREN_LAST_NODE_COLLAPSED:CHILDREN_LAST_NODE);
                    expandable = true;
                }
            }

            String tmpName = name+"_"+i;
            String controlText = "<img src=\"blank.gif\" border=\"0\">";
            controlText = "<a ";
            if ( expandable && this.allowJavascript )
            {
                //expandableText = "onClick=\"javascript:toggle('"+tmpName+"');\"";
                controlText += "onClick=\"javascript:return toggle('"+tmpName+"');\" ";
            }
            //else if ( ! this.allowJavascript )

            if ( this.urlPrefix != null )
            {
                //controlText = "<ahref=\""+this.urlPrefix+node.name+"\"><img src=\"blank.gif\"border=\"0\"></a>";
                controlText += "href=\""+this.urlPrefix+node.name;
            }
            controlText += "\"><img src=\"blank.gif\" border=\"0\"></a>";
            //TODO add support for mixing both non-JS and JS approach on client side. so a client that has JS uses it
            //     and those that don't make use of the server
            strBuf.append("<tr>");
            strBuf.append("<td id='"+tmpName+"_node'class=\""+cssClass+"\" "+expandableText+">"+controlText+"</td>");
            strBuf.append("<td><table cellpadding='0'cellspacing='0'><tr><td><img src=\""+node.icon+"\"></td><tdclass='auto'>&nbsp;"+node.name+"</td></tr></table></td></tr>\n");


            cssClass = VERTICAL_LINE;

            if ( i == size )
            {
                cssClass = "empty";
            }
            if ( childMap != null)
            {
                String visibilityCssClass = "vis";
                if ( collapsed && !openChild)
                {
                    visibilityCssClass = "hid";
                }
                String subName = name+"_"+i;
                strBuf.append("<tr id=\""+subName+"\"class=\""+visibilityCssClass+"\"><tdclass=\""+cssClass+"\">&nbsp;</td><tdcolspan=\"2\">"+generateTree(subName,depth+1,childMap)+"</td></tr>\n");
            }
            i++;
        }
        strBuf.append("</table>\n");
        return strBuf.toString();
    }
    
    public String getTree()
    {
        return generateTree(generateTestMap());
    }

    /**
     * @param args
     *
     */
    public static void main ( String[] args )
    {
        WebTreeJ5 wt = new WebTreeJ5();
        //System.out.println(wt.generateTestMap());

        //System.out.println("-------------");

        //wt.setAllowJavascript(false);
        wt.setCollapsed(true);
        //wt.setTreeUrlPrefix("toto?node=");
        System.out.println(wt.generateTree(wt.generateTestMap()));
    }
}

//<html>
//<head>
//
//<style type="text/css">
//
//  tr.vis {
//      display: ;
//  }
//
//  tr.hid {
//      display: none;
//  }
//
//  td.vl {
//      background-repeat: repeat-y;
//      background-image: url("verticalLine.gif");
//      width: 20;
//  }
//
//  td.ncmn {
//      background-repeat: repeat-y;
//      background-image: url("noChildrenMidNode.gif");
//      width: 20;
//  }
//
//  td.cmn {
//      background-repeat: repeat-y;
//      background-image: url("expandedMidNode.gif");
//      width: 20;
//      cursor: pointer;
//  }
//
//  td.cmn_c {
//      background-repeat: repeat-y;
//      background-image: url("collapsedMidNode.gif");
//      width: 20;
//      cursor: pointer;
//  }
//
//  td.ncln {
//      background-repeat: no-repeat;
//      background-image: url("noChildrenLastNode.gif");
//      width: 20;
//  }
//
//  td.cln {
//        background-repeat: no-repeat;
//        background-image: url("expandedLastNode.gif");
//        width: 20;
//        cursor: pointer;
//  }
//
//  td.cln_c {
//        background-repeat: no-repeat;
//        background-image: url("collapsedLastNode.gif");
//        width: 20;
//        cursor: pointer;
//  }
//
//  td.empty {
//      background-repeat: repeat;
//      background-image: url("blankSpace.gif");
//      width: 20;
//  }
//
//  td.icon {
//      width: 32;
//  }
//
// td {
//       margin-left: 0;
// }
//</style>
//
//</head>
//
//
//<script lang="javascript" type="text/javascript">
// <!--
//   function toggleX ( name ) {
//      var element = document.getElementById(name+"");
//      var imgNode = document.getElementById(name+"_node");
//      if ( element.style.visibility != 'visible' ) {
//        //element.style.visibility = 'visible';
//        imgNode.className = imgNode.className + "_c";
//      } else {
//        element.style.visibility = '';
//        imgNode.className = imgNode.className.substring(0,imgNode.className.length - 2);
//      }
//   }
//
//  function toggle ( name ) {
//  //confirm (name);
//     var element = document.getElementById(name+"");
//     var imgNode = document.getElementById(name+"_node");
//      //if ( element.style.display != 'none' ) {
//      //if ( element.className != 'hid' ) {
//      if ( ! imgNode.className.match(/_c$/) ) {
//        //element.style.display = 'none';
//        element.className = "hid";
//        imgNode.className = imgNode.className + "_c";
//      } else {
//        //element.style.display = '';
//        element.className = "vis";
//        imgNode.className = imgNode.className.substring(0,imgNode.className.length - 2);
//      }
//   }
//// -->
//</script>
//
//<body>
//</body>
//</html>
