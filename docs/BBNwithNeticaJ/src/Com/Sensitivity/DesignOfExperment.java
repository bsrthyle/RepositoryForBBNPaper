/**
 * 
 */
package Com.Sensitivity;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;

import Com.Build.LearnCPTs;
import Com.Display.DisplayBBN;


import norsys.netica.Environ;
import norsys.netica.Net;
import norsys.netica.NeticaException;
import norsys.netica.Node;
import norsys.netica.Streamer;

/**
 * @author Bisrat Haile
 * 10.07.2017
 * DesignOfExperment.java
 * @ Version 
 */

/*
 * This class is used to do a Design of experiment to check the sensitivity of intensification to other values of
 * the node
 * First we read the bbn and then get all the nodes 
 * and add the nodes which we are interested to a node set 
 * learn the conditional probability table from the data using either count learning or expectation
   Maximization  algorithm
 * The compile the net 
 * Iterate over the the case file and add it as fining to the net(using the node set)
 * Retrieve the probabilities or beliefs of the intensification node 
 * write the vector of probabilities in a text file
 * First we read the data which is created using "Generating nearly orthogonal Latin Hypercube designs
   Copyright (c) 2005  Susan M. Sanchez" 
   
 */

public class DesignOfExperment {


    public static void main(String[] args) {
        Net myNet = null;
        DisplayBBN diBbn = new DisplayBBN();
        LearnCPTs learnCPTs = new LearnCPTs();

        try {

            Environ environ = new Environ(null);
            myNet = new Net(new Streamer("FinalBBN5.dne"));
        } catch (NeticaException e2) {

            e2.printStackTrace();
        }

        Streamer caseFile = null;
        try {
            //caseFile = new Streamer("SimulatedData.txt");
            caseFile = new Streamer("newBBN140517_2.txt");
        } catch (NeticaException e1) {

            System.out.println("Error while reading the training data set : " + e1.getMessage());
        }

        // an integer code for learning algorithms 1: Count learning, 2: EM 3: GD
        int learningAlgorithm = 2;
        learnCPTs.LearnCPTs(myNet, caseFile, learningAlgorithm);
        try {
            myNet.compile();
            diBbn.displayNet(myNet,learningAlgorithm);
        } catch (NeticaException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }


        try {
            getProbabilitiesOfIntensificationStrategiesFromNet(myNet);
        } catch (NeticaException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }


    }
    // A method to get the probabilities of intensification

    public static void getProbabilitiesOfIntensificationStrategiesFromNet(Net theNet) throws NeticaException, IOException {

        Node ageOfTheHouseholdHead = theNet.getNode("ageOfTheHouseholdHead");
        Node percentNonfarmIncome = theNet.getNode("percentNonfarmIncome");
        Node distanceToBigMarket = theNet.getNode("distanceToBigMarket");
        Node farmSize = theNet.getNode("farmSize");
        Node commercializationIndex = theNet.getNode("commercializationIndex");
        Node creditaccess = theNet.getNode("creditaccess");
        Node householdSize = theNet.getNode("householdSize");
        Node farmType = theNet.getNode("farmType");
        Node perCapitaIncome = theNet.getNode("perCapitaIncome");
        Node topgraphicWetnessIndex = theNet.getNode("topgraphicWetnessIndex");
        Node shareOfHiredLabour = theNet.getNode("shareOfHiredLabour");
        Node cropChoice = theNet.getNode("cropChoice");
        Node intensificationStrategies = theNet.getNode("intensificationStrategies");
        Node totallabourmandays = theNet.getNode("totalLaborManDays");


        // read the file
        FileWriter writer = new FileWriter("probabilitiesNew2.txt");
        BufferedWriter outWriter = new BufferedWriter(writer);
        File file = new File("myDoE4.txt");
        FileReader fileReader = new FileReader(file);
        BufferedReader bufferedReader = new BufferedReader(fileReader);
        String line;


        while ((line = bufferedReader.readLine()) != null) {

            String[] findings = line.split("\\s+");
            int age = Integer.valueOf(findings[0]);
            double hholdSize = Double.valueOf(findings[1]);
            double fSize = Double.valueOf(findings[2]);
            double sharelabour = Double.valueOf(findings[3]);
            double comm = Double.valueOf(findings[4]);
            double labour = Double.valueOf(findings[5]);
            String farmt = findings[6];
            String credit = findings[7];
            String crop = findings[8];
            double income = Double.valueOf(findings[9]);
            double offincome = Double.valueOf(findings[10]);
            double twi = Double.valueOf(findings[11]);
            double market = Double.valueOf(findings[12]);


            ageOfTheHouseholdHead.finding().enterReal(age);
            householdSize.finding().enterReal(hholdSize);
            farmSize.finding().enterReal(fSize);
            shareOfHiredLabour.finding().enterReal(sharelabour);
            commercializationIndex.finding().enterReal(comm);
            totallabourmandays.finding().enterReal(labour);
            farmType.finding().enterState(farmt);
            creditaccess.finding().enterState(credit);
            cropChoice.finding().enterState(crop);
            perCapitaIncome.finding().enterReal(income);
            topgraphicWetnessIndex.finding().enterReal(twi);
            distanceToBigMarket.finding().enterReal(market);
            percentNonfarmIncome.finding().enterReal(offincome);

            float[] intensificationBelife = intensificationStrategies.getBeliefs();
            //System.out.println(Arrays.toString(intensificationBelife));
            outWriter.write(Arrays.toString(findings));
            outWriter.write(",");
            outWriter.write(Arrays.toString(intensificationBelife));
            outWriter.newLine();


            theNet.retractFindings();
        }
        System.out.println("Finished writing the probabiliteis to a File");
        outWriter.close();
    }

}
