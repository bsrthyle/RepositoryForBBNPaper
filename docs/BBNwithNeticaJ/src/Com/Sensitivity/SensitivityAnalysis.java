package Com.Sensitivity;

import norsys.netica.Net;
import norsys.netica.NeticaException;
import norsys.netica.Node;
import norsys.netica.NodeList;
import norsys.netica.Sensitivity;

/**
 * @author Bisrat Haile
 * 27.06.2017
 * SensitivityAnalysis.java
 * @ Version 
 */
public class SensitivityAnalysis {
	
	/**
	 * A method that can be used to make a sensitivity analysis with respect to one target node
	 * @param @param theNet
	 * @param @param targetNode
	 * @return void
	 */
	public static void doSensitivityAnalysis(Net theNet, Node targetNode)
	{
		
		try
		{
			
			Sensitivity sensitivity = new Sensitivity(targetNode	, theNet.getNodes(), Sensitivity.ENTROPY_SENSV);
			Sensitivity sensitivity1 = new Sensitivity(targetNode, theNet.getNodes(), Sensitivity.REAL_SENSV | Sensitivity.VARIANCE_OF_REAL_SENSV);
			
			NodeList nodes = theNet.getNodes();
			System.out.println();
			System.out.println("=====================================================================================================================");
			System.out.println("Sensetivity to finding of the intensification node[measure mutual information]");
			System.out.println("=====================================================================================================================");
			System.out.println();
			System.out.format("%-27s%-25s%-25s%-25s%-25s\n", "Node", "MutualInfo", "PercentMutInfo", "VarianceReduction", "PercentVarRed");
			System.out.println("=====================================================================================================================");
			double mutualInfoTargetNode = sensitivity.getMutualInfo(targetNode);
			double varianceReductionTargetNode = sensitivity1.getVarianceOfReal(targetNode);
			for (Object object : nodes) {
				Node node = (Node) object;
				double mutualInfoNode = sensitivity.getMutualInfo(node);
				double varianceReduction = sensitivity1.getVarianceOfReal(node);

				//System.out.format("%-15s%-15s%-15s%-15s%-15s\n", node.getName(), mutualInfoNode, ((mutualInfoNode/mutualInfoTargetNode)/100), varianceReduction , ((varianceReduction/varianceReductionTargetNode)*100));
				System.out.format("%-27s||%-25.5f||%-25.5f||%-25.5f||%-25.5f%n", node.getName(), mutualInfoNode, ((mutualInfoNode/mutualInfoTargetNode)*100), varianceReduction , ((varianceReduction/varianceReductionTargetNode)*100));

				
			}
			System.out.println("=====================================================================================================================");

		} catch (NeticaException e) {
			System.out.println("Error while performing sensitivity analysis: " + e.getMessage());
		}
	}

}
