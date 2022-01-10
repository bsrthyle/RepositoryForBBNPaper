package Com.Build;

import norsys.netica.*;

/**
 * Created by Bisrat on 15/07/17.
 * A class contains methods to learn the CPTs from a case File
 * Three learning algorithms are specified [Count learning, Expectation Maximization and Gradiant Decent]
 */
public class LearnCPTs {

    public int COUNTLEARNING =1;
    public int EXPECTATION_MAXIMIZATION = 2;
    public int GRADIENT_DECENT = 3;

    public void LearnCPTs(Net theNet, Streamer caseFile, int method)
    {
        if (method == COUNTLEARNING)
        {
            try
            {
                LearnCPTsUsingCountLearning(theNet, caseFile);
            } catch (NeticaException e) {
                System.out.println("Error while learning the CPTs using Count Learning: " + e.getMessage());
            }
            System.out.println();
            System.out.println("The Conditional probabilities are estimated based on Count learning");
            try {
                System.out.println("The data used for training the model is " + caseFile.getFileName());
            } catch (NeticaException e) {
                e.printStackTrace();
            }
        } else if (method == EXPECTATION_MAXIMIZATION)
        {
            try
            {
                LearnCPTsUsingEM(theNet, caseFile);
            } catch (NeticaException e)
            {
                System.out.println("Error while learning the CPTs using EM Learning: " + e.getMessage());
            }
            System.out.println();
            System.out.println("The Conditional probabilities are estimated based on EM learning");
            try {
                System.out.println("The data used for training the model is " + caseFile.getFileName().toString());
            } catch (NeticaException e) {
                e.printStackTrace();
            }
        } else if (method == GRADIENT_DECENT)
        {
            try
            {
                LearnCPTsUsingGD(theNet, caseFile);
            } catch (NeticaException e)
            {
                System.out.println("Error while learning the CPTs using GD Learning: " + e.getMessage());
            }
            System.out.println();
            System.out.println("The Conditional probabilities are estimated based on GD learning");
        } else
            {
            throw new IllegalArgumentException("Wrong method parameter" + new Throwable().getMessage());
        }
    }

    private void LearnCPTsUsingCountLearning(Net net, Streamer caseFile) throws NeticaException
    {
        System.out.println();
        NodeList nodeList = net.getNodes();
        for (int i = 0; i < nodeList.size(); i++)
        {
            Node node = nodeList.getNode(i);
            node.deleteTables();
        }
        net.reviseCPTsByCaseFile(caseFile, nodeList, 1.0);

    }

    private void LearnCPTsUsingEM(Net theNet, Streamer caseFile) throws NeticaException
    {
        System.out.println();
        System.out.println("Learning the conditional probabiliites from the data using the Expectation maximization algorithm........");
        NodeList nodeList = theNet.getNodes();

        Caseset caseset = new Caseset();
        caseset.addCases(caseFile, 1.0, null);

        for (Object node : nodeList)
        {
            Node node2 = (Node) node;
            node2.deleteTables();
        }
        Learner learner = new Learner(Learner.EM_LEARNING);
        learner.learnCPTs(nodeList, caseset, 1.0);

        System.out.println("EM_ get the learner method: " + learner.getMethod() + " Get the maximum Itrations "
                + learner.getMaxIterations() + " get the maximum tolerance " + learner.getMaxTolerance());
        learner.finalize();
        caseset.finalize();

        System.out.println("Finished Learning the Probabilities");
    }

    private void LearnCPTsUsingGD(Net theNet, Streamer caseFile) throws NeticaException
    {
        System.out.println();
        System.out.println("Learning the conditional probabilities using Gradiant discent algorithm......");

        NodeList nodeList = theNet.getNodes();

        Caseset caseset = new Caseset();
        caseset.addCases(caseFile, 1.0, null);

        for (Object node : nodeList)
        {
            Node node2 = (Node) node;
            node2.deleteTables();
        }

        Learner learner = new Learner(Learner.GRADIENT_DESCENT_LEARNING);
        learner.learnCPTs(nodeList, caseset, 1.0);
        learner.finalize();
        caseset.finalize();

        System.out.println("Finished learning the probabilities with GD");
    }
}
