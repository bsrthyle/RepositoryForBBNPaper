package Com.Build;

import norsys.netica.*;

import java.awt.*;
import java.io.File;

/**
 * Created by Bisrat on 15/07/17.
 */
public class BBNCreation {


    private static String orginalBBNNFileName = "newMyBBN14052017.dne";
    private static String newBBNFileName = "FinalBBN2.dne";
    private static Net net;

    public static void main(String[] args)
    {
        try {
            Environ environ = new Environ(null);
            net = new Net(new Streamer(orginalBBNNFileName));
            NodeList nodes = net.getNodes();
            Node intensificationStrategy = net.getNode("intensificationStrategies");

            // add two new nodes for expected price of rice and maize

            Node ExpectedPriceOfRice = new Node("ricePrice", 0, net);
            ExpectedPriceOfRice.setTitle("ExpectedPriceOfRice");
            Node ExpectedPriceOfMaize = new Node("maizePrice",0, net);
            ExpectedPriceOfMaize.setTitle("ExpectedPriceOfMaize");

            //discretized the two price nodes

            double[] levels = new double[5];
            levels[0] = 0.0;
            levels[1] = 0.0;
            levels[2] = 810;
            levels[3] = 1120;
            levels[4] = Environ.INFINITY;
            ExpectedPriceOfMaize.setLevels(levels); // discretizes to 3 states
            ExpectedPriceOfMaize.setStateNames("None, Low, Medium, High"); /* naming the states is optional*/

            double[] levels2 = new double[5];
            levels2[0] = 0.0;
            levels2[1] = 0.0;
            levels2[2] = 1000;
            levels2[3] = 1500;
            levels2[4] = Environ.INFINITY;
            ExpectedPriceOfRice.setLevels(levels2); // discretizes to 3 states
            ExpectedPriceOfRice.setStateNames("None, Low, Medium, High");

            //get the nodes of crop choice and distanceFromTheMarket

            Node cropChoice = net.getNode("cropChoice");
            Node distanceToBigMarket = net.getNode("distanceToBigMarket");
            cropChoice.addLink(ExpectedPriceOfMaize);
            cropChoice.addLink(ExpectedPriceOfRice);
            ExpectedPriceOfMaize.addLink(distanceToBigMarket);
            ExpectedPriceOfRice.addLink(distanceToBigMarket);
            // get the position of one of the nodes and set the positions of the
            // newly created nodes relative to this node
            Node twd = net.getNode("topgraphicWetnessIndex");

            // get the position of the node in the gui just to set the positions
            // of the new nodes at a relative position
            double[] pos = twd.visual().getPosition();

            ExpectedPriceOfRice.visual().setPosition((pos[0]), pos[1] + 200);
            ExpectedPriceOfMaize.visual().setPosition((pos[0]), pos[1] + 100);
            intensificationStrategy.addToNodeset("TargetNode");
            net.setNodesetColor("TargetNode", Color.RED);
            intensificationStrategy.addStates(6, "Other", 1, -1);
            net.setName("NewBBNwith" + net.getNodes().size()+ "Nodes");


            int numericState = intensificationStrategy.getNumStates();
            for (int i = 0; i < numericState - 1; i++) {
                intensificationStrategy.state(i).setNumeric(i);
            }
/*
set the name of the states
 */
            Node ageOfTheHouseholdHead = net.getNode("ageOfTheHouseholdHead");
            Node farmSize = net.getNode("farmSize");
            Node commercializationIndex = net.getNode("commercializationIndex");
            Node householdSize = net.getNode("householdSize");
            Node shareOfHiredLabour = net.getNode("shareOfHiredLabour");
            Node totallabourmandays = net.getNode("totalLaborManDays");

            ageOfTheHouseholdHead.setStateNames("between_24And35,between_35and45,between_45And55,Above_55");
            shareOfHiredLabour.setStateNames("LessThan_10Percent,Between_10And60Percent,Above_60Percent");
            farmSize.setStateNames("LessThan_3Ha,Between_3And6Ha,Between_6And9Ha,MoreThan_9Ha");
            householdSize.setStateNames("LessThan4_,Between_4And7,MoreThan_7");
            commercializationIndex.setStateNames("LessThan_30Percent,Between_30And60Percent, MoreThan_60Percent");
            totallabourmandays.setStateNames("lessThan_120, Between_120And220, Between_220And400,MoreThan_400");

            // set the postion of Labourmandays and wardCode
            double[] laborPos = totallabourmandays.visual().getPosition();
            totallabourmandays.visual().setPosition(laborPos[0], laborPos[1]+50);
            double[] commePos = commercializationIndex.visual().getPosition();
            commercializationIndex.visual().setPosition(commePos[0]-50, commePos[1]);
            // write the network to a new net file


            (new File(newBBNFileName)).delete();
            net.write(new Streamer(newBBNFileName));

            net.finalize();
        } catch (NeticaException e) {
            e.printStackTrace();
        }
    }
}
