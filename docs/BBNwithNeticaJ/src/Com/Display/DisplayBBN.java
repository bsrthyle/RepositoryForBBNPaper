package Com.Display;

import norsys.netica.Net;
import norsys.netica.gui.NetPanel;
import norsys.netica.gui.NodePanel;

import javax.swing.*;
import java.awt.*;

/**
 * Created by Bisrat on 12/07/17.
 */
public class DisplayBBN extends JFrame {
    private NetPanel netPanel;
    private NodePanel[] nodePanels;


    public void displayNet(Net net, int learningMethod) throws Exception
    {
        net.compile();
        netPanel = new NetPanel(net, NodePanel.NODE_STYLE_BELIEF_BARS);

        getContentPane().add(new JScrollPane(netPanel));
        netPanel.setLinkPolicy(NetPanel.LINK_POLICY_BELOW);
        //netPanel.setBounds(50,50,800,800);
        //getContentPane().setBounds(0,0,800,800);
        nodePanels = netPanel.getNodePanels();
        netPanel.getNet().setNodesetColor("TargetNode", Color.RED);
        netPanel.refreshDataDisplayed();
        setTitle(net.getName());
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1400,900);
        setVisible(true);
        setBackground(new Color(255, 250, 220));

        if (learningMethod == 1)
        {
            netPanel.setBackground(new Color(242, 122, 17));
        } else if (learningMethod == 2)
        {
            netPanel.setBackground(new Color(47, 30, 66));
        } else if (learningMethod == 3)
        {
            netPanel.setBackground(new Color(57, 119, 7));
        }


    }

    public NetPanel getNetPanel() {
        return netPanel;
    }

    public NodePanel[] getNodePanels() {
        return nodePanels;
    }


}
