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

import javax.jmdns.*;

/**
 * User Interface for browsing Rendezvous services.
 * 
 * @author Arthur van Hoff
 * @version 1.13, 11/29/2002
 */
public class JmDNSBrowser extends JFrame implements ServiceTypeListener, ServiceListener {
       
    //ListSelectionListener, HyperlinkListener {

    JmDNS jmdns;
    
    JmDNSBrowser () throws IOException
    {
        jmdns = new JmDNS();
        jmdns.addServiceTypeListener(this);
        jmdns.addServiceListener("_ssh._tcp.local.", this);
    }
    


    public void serviceTypeAdded(ServiceEvent event) {
        // TODO Auto-generated method stub
        System.out.println("Service Type Added: " + null);
        System.out.println("xxx    " + event.getType() );
    }

    public void serviceAdded(ServiceEvent event) {
        // TODO Auto-generated method stub
        System.out.println("Service Added: " + event);
        System.out.println("xxx    " + event.getDNS().getHostName());
        try {
            System.out.println("xxx     " + event.getDNS().getInterface());
            InetAddress addr = InetAddress.getByName(event.getDNS().getHostName());
            System.out.println(addr.getHostAddress());
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }        
    }

    public void serviceRemoved(ServiceEvent event) {
        // TODO Auto-generated method stub
        System.out.println("Service Removed: " + event);
        System.out.println("yyy     " + event.getDNS().getHostName());
    }

    public void serviceResolved(ServiceEvent event) {
        // TODO Auto-generated method stub
        System.out.println("Service Resolved: " + event);
        System.out.println("xxx     " + event.getDNS().getHostName());
        try {
            System.out.println("xxx     " + event.getDNS().getInterface());
            
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
//
//    public void valueChanged(ListSelectionEvent event) {
//        // TODO Auto-generated method stub
//        System.out.println("Value changed: " + event);
//    }
//
//    public void hyperlinkUpdate(HyperlinkEvent event) {
//        // TODO Auto-generated method stub
//        System.out.println("hyperlinkUpdate: " + event);
//    }
    
    /**
     * Main program.
     */
    public static void main(String argv[]) throws IOException {
        new JmDNSBrowser();
        
//        InetAddress addr = InetAddress.getByName("localhost");
        InetAddress[] addrs = InetAddress.getAllByName("localhost");
        for ( InetAddress addr : addrs )
        {
            System.out.println(addr.getClass() + " -- " + addr.getHostAddress());
        }
        try {
            Thread.sleep(10000);
        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
}
