package Com.CrossValidation;

import Com.Build.LearnCPTs;
import norsys.netica.*;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by gebrekidan on 03.08.2017.
 * Com.CrossValidation$
 */
public class TestAccuracyTest {



    public static void main(String[] args) throws NeticaException {
        String folderName = "D:\\MEGAsync\\FromOldSciebo\\BBNForRevision\\FinalBBN\\CV4";
        String netName = "FinalBBN6.dne";
        File folder = new File(folderName);
        File[] listOfFiles = folder.listFiles();

        List<File> testFiles = new ArrayList<>();
        List<File> trainFile = new ArrayList<>();


        LearnCPTs learnCPTs = new LearnCPTs();


        Net myNet = null;

        try
        {
            Environ environ = new Environ(null);
            myNet = new Net(new Streamer(netName));
        } catch (NeticaException e)
        {
            e.printStackTrace();
        }
        for (File file: listOfFiles)
        {
           if (file.getName().contains("tast"))
               testFiles.add(file);
           else if (file.getName().contains("train"))
               trainFile.add(file);

        }
        Node intensification = myNet.getNode("intensificationStrategies");
        int i = 0;
        double [] errorRate = new double[trainFile.size()+1];

        while(i < trainFile.size())
        {
            Streamer casefile = new Streamer("D:\\\\MEGAsync\\\\FromOldSciebo\\\\BBNForRevision\\\\FinalBBN\\\\CV4\\"+ trainFile.get(i).getName());
            learnCPTs.LearnCPTs(myNet, casefile, 2);
            myNet.compile();
            double error = getErrorRate (myNet, intensification,"D:\\\\MEGAsync\\\\FromOldSciebo\\\\BBNForRevision\\\\FinalBBN\\\\CV4\\" + testFiles.get(i).getName() );
            errorRate[i] = error;
            casefile.finalize();
            i++;
        }

        System.out.println("The mean error from the cross validation is: " + mean(errorRate));


    }
    /*
    A method to get the error rate from each folds
     */
    public static double getErrorRate(Net theNet, Node testNode, String caseFileName) {

        double errorValue = 0;
        try {
            NodeList unobservedNodes = new NodeList(theNet);
            NodeList testNodeList = new NodeList(theNet);

            NodeList allNodes = theNet.getNodes();

            for (Object object : allNodes) {
                Node node = (Node) object;
                if (node.getName().equals(testNode.getName())) {
                    testNodeList.add(node);

                } else {
                    unobservedNodes.add(node);

                }
            }

            theNet.retractFindings();
            //theNet.compile();

            NetTester netTester = new NetTester(testNodeList, testNodeList, -1);

            Streamer testCaseFile = new Streamer(caseFileName);

            Caseset caseset = new Caseset();

            caseset.addCases(testCaseFile, 1, null);

            netTester.testWithCaseset(caseset);


            errorValue = netTester.getErrorRate(testNode);

            printConfusionMatrix (netTester, testNode);
            System.out.println ("Error rate for " + testNode.getName() + " = " + netTester.getErrorRate (testNode));
            System.out.println ("Logarithmic loss = " + netTester.getLogLoss (testNode));
            System.out.println("The test is made using " + caseFileName);
            netTester.finalize();


        } catch (NeticaException e) {
            System.out.println("Error while doing cross-validation:- " + e.getMessage());
        }
        return errorValue;
    }
    public static void printConfusionMatrix (NetTester nt, Node node) throws NeticaException {
        int numStates = node.getNumStates();
        System.out.println("\nConfusion matrix for " + node.getName() + ":");

        for (int i=0;  i < numStates;  ++i){
            System.out.print ("\t" + node.state(i).getName());
        }
        System.out.println( "\tActual");

        for (int a=0;  a < numStates;  ++a){
            for (int p=0;  p < numStates;  ++p){
                System.out.print ("\t" + (int) (nt.getConfusion(node, p, a )));
            }
            System.out.println ("\t" + node.state(a).getName());
        }

    }
    public static double mean(double[] m){
        double sum = 0;
        for (int i = 0; i < m.length; i++) {
            sum += m[i];
        }
        return 100.0d * sum / m.length;
    }

}
