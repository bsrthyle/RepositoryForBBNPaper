package Com.MainModel;

import java.awt.TextField;
import java.util.Iterator;

import javax.swing.JTextField;

import Com.Build.BBNCreation;
import Com.Build.LearnCPTs;
import Com.CrossValidation.TestAccuracy;
import Com.Display.DisplayBBN;
import Com.Sensitivity.SensitivityAnalysis;


import norsys.netica.Environ;
import norsys.netica.Net;
import norsys.netica.NeticaException;
import norsys.netica.Node;
import norsys.netica.Streamer;
import norsys.netica.gui.NetPanel;
import norsys.netica.gui.NodePanel;

public class MainModel {
	
	public static void main(String[] args)
	{
		
		// create instances of the display and learner classes
		//BBNCreation bbnCreation = new BBNCreation();
		DisplayBBN diBbn = new DisplayBBN();
		LearnCPTs learnCPTs = new LearnCPTs();
		
		//Net myNet = bbnCreation.getNet();
		Net myNet = null;


		try
		{
			Environ environ = new Environ(null);
			myNet = new Net(new Streamer("FinalBBN6.dne"));
		} catch (NeticaException e2)
		{
			
			e2.printStackTrace();
		}


		Streamer caseFile = null;
		try
		{
			 //caseFile = new Streamer("SimulatedData.txt");
			caseFile = new Streamer("newBBN140517_2.txt");
		} catch (NeticaException e1)
		{
		
			System.out.println("Error while reading the training data set : " + e1.getMessage());
		}
		
		// an integer code for learning algorithms 1: Count learning, 2: EM 3: GD
		int learningAlgorithm = 2;
		learnCPTs.LearnCPTs(myNet, caseFile, learningAlgorithm);
		
		
		
		try
		{
			
			diBbn.displayNet(myNet, learningAlgorithm);
			
		} catch (Exception e)
		{
			System.out.println("Error while displaying the net: " + e.getMessage());
		}
		
		// create simulated data based on the distribution in the net



		
		/*String fileName = "SimulatedData.txt";
		int numCases = 50000;
		
		try {
			
		SimulateData.CreateSimulatedData(numCases, myNet, fileName);
			
		} catch (NeticaException e) {
			System.out.println("Error while simulating data: " + e.getMessage() );
		}*/
		
		try
		{
			Node targetNode = myNet.getNode("intensificationStrategies");
			
			SensitivityAnalysis.doSensitivityAnalysis(myNet, targetNode);
			
			//String testCase = "SimulatedData.txt";
			//TestAccuracy.testModelPredictionAccuracy(myNet, targetNode, testCase);
			
		} catch (NeticaException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
	}

}
