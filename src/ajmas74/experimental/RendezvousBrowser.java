package ajmas74.experimental;

// Copyright (C) 2002  Strangeberry Inc.
// @(#)Browser.java, 1.13, 11/29/2002
//
// This library is free software; you can redistribute it and/or
// modify it under the terms of the GNU Lesser General Public
// License as published by the Free Software Foundation; either
// version 2.1 of the License, or (at your option) any later version.
// 
// This library is distributed in the hope that it will be useful,
// but WITHOUT ANY WARRANTY; without even the implied warranty of
// MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
// Lesser General Public License for more details.
// 
// You should have received a copy of the GNU Lesser General Public
// License along with this library; if not, write to the Free Software
// Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA

import java.io.*;
import java.net.*;
import java.util.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.table.*;
import javax.swing.text.html.HTMLDocument;
import javax.swing.text.html.HTMLFrameHyperlinkEvent;
import javax.swing.border.*;
import javax.swing.event.*;

import com.strangeberry.rendezvous.*;

/**
 * User Interface for browsing Rendezvous services.
 *
 * @author	Arthur van Hoff
 * @version 	1.13, 11/29/2002
 */
public class RendezvousBrowser
  extends JFrame
  implements ServiceListener, ListSelectionListener, HyperlinkListener {
  Rendezvous rendezvous;
  Vector headers;
  DefaultListModel services;
  String type;
  JList typeList;
  JList serviceList;
  JEditorPane info;

  RendezvousBrowser(Rendezvous rendezvous) {
    this(
      rendezvous,
      new String[] {
        "_http._tcp.local.",
        "_ftp._tcp.local.",
        "_tftp._tcp.local.",
        "_ssh._tcp.local.",
        "_smb._tcp.local.",
        "_printer._tcp.local.",
        "_airport._tcp.local.",
        "_afpovertcp._tcp.local.",
        "_ichat._tcp.local.",
        "_eppc._tcp.local." });
  }

  RendezvousBrowser(Rendezvous rendezvous, String types[]) {
    super("JRendezvous Browser");
    this.rendezvous = rendezvous;

    Color bg = new Color(230, 230, 230);
    EmptyBorder border = new EmptyBorder(5, 5, 5, 5);
    Container content = getContentPane();
    content.setLayout(new GridLayout(1, 3));
    Arrays.sort(types);
    type = types[0];

    typeList = new JList(types);
    typeList.setBorder(border);
    typeList.setBackground(bg);
    typeList.setSelectedIndex(0);
    typeList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
    typeList.addListSelectionListener(this);

    JPanel typePanel = new JPanel();
    typePanel.setLayout(new BorderLayout());
    typePanel.add("North", new JLabel("Types"));
    typePanel.add(
      "Center",
      new JScrollPane(
        typeList,
        JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
        JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED));
    content.add(typePanel);

    services = new DefaultListModel();
    serviceList = new JList(services);
    serviceList.setBorder(border);
    serviceList.setBackground(bg);
    serviceList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
    serviceList.addListSelectionListener(this);

    JPanel servicePanel = new JPanel();
    servicePanel.setLayout(new BorderLayout());
    servicePanel.add("North", new JLabel("Services"));
    servicePanel.add(
      "Center",
      new JScrollPane(
        serviceList,
        JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
        JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED));
    content.add(servicePanel);

    info = new JEditorPane("text/html","");
    info.setBorder(border);
    info.setBackground(bg);
    info.setEditable(false);
    info.addHyperlinkListener(this);
    //info.setContentType("text/html");
    //info.setLineWrap(true);

    JPanel infoPanel = new JPanel();
    infoPanel.setLayout(new BorderLayout());
    infoPanel.add("North", new JLabel("Details"));
    infoPanel.add(
      "Center",
      new JScrollPane(
        info,
        JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
        JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED));
    content.add(infoPanel);

    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    setLocation(100, 100);
    setSize(600, 400);
    show();

    rendezvous.addServiceListener(type, this);
  }

  /**
   * Add a service.
   */
  public void addService(Rendezvous rendezvous, String type, String name) {
    if (name.endsWith("." + type)) {
      name = name.substring(0, name.length() - (type.length() + 1));
    }
    //System.out.println("ADD: " + name);
    services.addElement(name);
  }

  /**
   * Remove a service.
   */
  public void removeService(Rendezvous rendezvous, String type, String name) {
    if (name.endsWith("." + type)) {
      name = name.substring(0, name.length() - (type.length() + 1));
    }
    //System.out.println("REMOVE: " + name);
    services.removeElement(name);
  }

  /**
   * List selection changed.
   */
  public void valueChanged(ListSelectionEvent e) {
    if (!e.getValueIsAdjusting()) {
      if (e.getSource() == typeList) {
        type = (String) typeList.getSelectedValue();
        rendezvous.removeServiceListener(this);
        services.setSize(0);
        info.setText("");
        rendezvous.addServiceListener(type, this);
      } else if (e.getSource() == serviceList) {
        String name = (String) serviceList.getSelectedValue();
        if (name == null) {
          info.setText("");
        } else {
          if (!name.endsWith(".")) {
            name = name + "." + type;
          }
          info.setText("<i>looking up...</i>");
          ServiceInfo service = rendezvous.getServiceInfo(type, name);
          info.setText(getDisplayText(service));

          //          if (service == null) {
          //            info.setText("service not found");
          //          } else {
          //            StringBuffer buf = new StringBuffer();
          //            buf.append(name);
          //            buf.append('\n');
          //            buf.append(service.getAddress());
          //            buf.append(':');
          //            buf.append(service.getPort());
          //            buf.append('\n');
          //            String txt = service.getTextString();
          //            if (txt != null) {
          //              buf.append('\n');
          //              buf.append(txt);
          //              buf.append('\n');
          //            }
          //
          //            info.setText(buf.toString());
          //          }

        }
      }
    }
  }

	/** */
  String getDisplayText(ServiceInfo info) {
    String protocol = null;
    StringBuffer buf = new StringBuffer();

    if (info == null) {
      buf.append("service not found");
      return buf.toString();
    } else if (info.getType().equals("_http._tcp.local.")) {
      protocol = "http://";
    } else if (info.getType().equals("_afpovertcp._tcp.local.")) {
      protocol = "afp://";
    } else {
      buf.append(info.getName());
      buf.append('\n');
      buf.append(info.getAddress());
      buf.append(':');
      buf.append(info.getPort());
      buf.append('\n');
      String txt = info.getTextString();
      if (txt != null) {
        buf.append('\n');
        buf.append(txt);
        buf.append('\n');
      }
    }

    if (protocol != null) {
      StringBuffer urlBuf = new StringBuffer();
      
      urlBuf.append(info.getType()+"<br>");
      urlBuf.append(info.toString()+"<br>");
      urlBuf.append(protocol);
      urlBuf.append(info.getAddress());
      if (info.getPort() != 80) {
        urlBuf.append(":");
        urlBuf.append(info.getPort());
      }
      if (info.getTextString() != null) {
        urlBuf.append(info.getTextString());
      } else {
        urlBuf.append("/");
      }
      buf.append("<a href=\"");
      buf.append(urlBuf);
      buf.append("\">");      
      buf.append(urlBuf);
      buf.append("</a>");      
    }
    return buf.toString();

  }

  /**
   * Table data.
   */
  class ServiceTableModel extends AbstractTableModel {
    public String getColumnName(int column) {
      switch (column) {
        case 0 :
          return "service";
        case 1 :
          return "address";
        case 2 :
          return "port";
        case 3 :
          return "text";
      }
      return null;
    }
    public int getColumnCount() {
      return 1;
    }
    public int getRowCount() {
      return services.size();
    }
    public Object getValueAt(int row, int col) {
      return services.elementAt(row);
    }
  }

  public String toString() {
    return "RVBROWSER";
  }

  public void hyperlinkUpdate(HyperlinkEvent e) {
    if (e.getEventType() == HyperlinkEvent.EventType.ACTIVATED) {
      URL theURL = e.getURL();

			if ( theURL != null ) {
	      //String command = "start";
	      String command = "explorer.exe";
				if ( System.getProperty("mrj.version") != null ) {
          command = "open ";
				}
	      String[] params = new String[] {theURL.toString()};
		    try {
		       System.out.println("will exec " + command + " " + params[0]);
		       //Runtime.getRuntime().
		       Process p = Runtime.getRuntime().exec(command,params);
		    } catch (Exception ex) {
		      ex.printStackTrace();
		    }
			} else {
			  System.out.println("no url");
			}

    }
  }
 


//  String theUrl = "www.yourServer.com";
//   // start the default browser (Win platform)
//   //  on listbox double click
//   String cmdLine = "start " + theUrl;
//  try {
//     Process p = Runtime.getRuntime().exec(cmdLine);
//  }
//  catch (Exception e) {
//     e.printStackTrace();
//  }
  
  /**
   * Main program.
   */
  public static void main(String argv[]) throws IOException {
    new RendezvousBrowser(new Rendezvous());
  }
}
