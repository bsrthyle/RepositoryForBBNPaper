package Com.CrossValidation;

import Com.Build.LearnCPTs;
import norsys.netica.*;
import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class TestAccuracyNaive {

    public static void main(String[] args) throws NeticaException {


        File folder = new File("CV10/");
        File[] listofFiles = folder.listFiles();
        List<File> testFiles = new ArrayList<>();
        List<File> trainFile = new ArrayList<>();
        LearnCPTs learnCPTs = new LearnCPTs();

        Net myNet = null;
        try
        {
            Environ environ = new Environ(null);
            myNet = new Net(new Streamer("BBNs/BBNWithNaive.dne"));
        } catch (NeticaException e2)
        {

            e2.printStackTrace();
        }

        for (File file : listofFiles){
            if (file.getName().contains("tast") )
                testFiles.add(file);
            else if (file.getName().contains("train"))
                trainFile.add(file);
        }

		Node intensification = myNet.getNode("intensificationStrategies");
		int i = 0;
		double [] errorRate = new double[trainFile.size()];
		ArrayList<Double> errorRateList = new ArrayList<>();
		
	
		while (i < trainFile.size())
		{
			Streamer caseFile = new Streamer("CV10/"+ trainFile.get(i).getName());
			learnCPTs.LearnCPTs(myNet, caseFile, 1);
			myNet.compile();
			double error = getErrorRate(myNet,intensification,"CV10/" + testFiles.get(i).getName() );
            errorRate[i] = error;
            errorRateList.add(error);

			caseFile.finalize();
			i++;

		}
			
		double max_error = errorRateList.stream().mapToDouble(a -> a).max().getAsDouble();
		double min_error = errorRateList.stream().mapToDouble(a->a).min().getAsDouble();
		double mean_error = errorRateList.stream().mapToDouble(a->a).average().getAsDouble();
		
		System.out.println("Max=" + max_error);
		System.out.println("Min=" + min_error);
		System.out.println("Mean=" + mean_error);
		System.out.println("**************From arrayList****************");
		for (Double double1 : errorRateList) {
			System.out.printf("%.2f", double1);
			System.out.println();
		}
        //System.out.println("The mean error from the five fold cross validation for Naivebays with GD learning is :" + mean(errorRate));
        
      
		
        
        /*for (File trainfile:trainFile) {
            for (File testFile:testFiles) {

                Streamer caseFile = new Streamer("D:\\FinalBBN\\CV2\\"+ trainfile.getName());

                learnCPTs.LearnCPTs(myNet,caseFile, 2);
                myNet.compile();

                testModelPredictionAccuracy(myNet,myNet.getNode("intensificationStrategies"), "D:\\FinalBBN\\CV2\\"+testFile.getName());

                System.out.println("Trained with " + caseFile);
                 caseFile.finalize();
            }

        }
*/
    }




	public static void testModelPredictionAccuracy(Net theNet, Node testNode, String caseFileName)
	{
		
		try {
			NodeList unobservedNodes = new NodeList(theNet);
			NodeList testNodeList = new NodeList(theNet);
			
			NodeList allNodes = theNet.getNodes();
			
			for (Object object : allNodes)
			{
				Node node = (Node) object;
				if (node.getName().equals(testNode.getName()))
				{
					testNodeList.add(node);
					
				} else
				{
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


			printConfusionMatrix (netTester, testNode);
			System.out.println ("Error rate for " + testNode.getName() + " = " + netTester.getErrorRate (testNode));
			System.out.println ("Logarithmic loss = " + netTester.getLogLoss (testNode));
			System.out.println("The test is made using " + caseFileName);
			
			
			
			netTester.finalize();
			
			
		} catch (NeticaException e)
		{
			System.out.println("Error while doing cross-validation:- " + e.getMessage());
		}
		
	}

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
	
	/**
	 * A simple method to print a confusion matrix[the code is based on the Netica manual]
	 * @param @param nt
	 * @param @param node
	 * @param @throws NeticaException
	 * @return void
	 */
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

	  // a method to round double values
    public static double mean(double[] m) {
        double sum = 0;
        for (int i = 0; i < m.length; i++) {
            sum += m[i];
        }
        return 100.0d * sum / m.length;
    }
}
