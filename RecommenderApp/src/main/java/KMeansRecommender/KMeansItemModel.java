package KMeansRecommender;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.commons.math3.ml.clustering.CentroidCluster;
import org.apache.commons.math3.ml.distance.DistanceMeasure;
import org.apache.mahout.cf.taste.common.TasteException;
import org.apache.mahout.cf.taste.impl.common.LongPrimitiveIterator;
import org.apache.mahout.cf.taste.impl.similarity.PearsonCorrelationSimilarity;
import org.apache.mahout.cf.taste.model.DataModel;
import org.apache.mahout.cf.taste.similarity.ItemSimilarity;

public class KMeansItemModel{
	private int k;
	private int maxIterations;
	private DataModel dataModel;
	List<CentroidCluster<LocationWrapper>> clusterResults;
	private List<LocationWrapper> clusterInput;
	private HashMap<Long, Integer> clusterMap;
	private ItemSimilarity similarity;
	
	// constructor
	public KMeansItemModel(DataModel dataModel, int k, int maxIterations, ItemSimilarity similarity){
		this.dataModel = dataModel;
		this.k = k;
		this.similarity = similarity;
		this.maxIterations = maxIterations;
		//clusterMap for quick search item's cluster
		clusterMap = new HashMap<Long, Integer>();
		//establish kmeans input data
		clusterInput = new ArrayList<LocationWrapper>();
		try {
			LongPrimitiveIterator it = dataModel.getItemIDs();
			while(it.hasNext()){
				clusterInput.add(new LocationWrapper(new double[]{it.nextLong()}));
			}
		} catch (TasteException e) {
			// 
		}
	}
	
	// initialize KMeansModel
	public void buildModel(){
		MyKMeansPlusPlusClusterer<LocationWrapper> clusterer = 
					new MyKMeansPlusPlusClusterer<LocationWrapper>(k, maxIterations, new ItemDistanceMeasure());
		clusterResults = clusterer.cluster(clusterInput);
		
		//build clusterMap
		for (int i=0; i<clusterResults.size(); i++) {
			for (LocationWrapper locationWrapper : clusterResults.get(i).getPoints()){
				clusterMap.put((long)locationWrapper.getPoint()[0], i);
			}
		}
		
		//output for test
		/*
		int totalNum = 0;
		for (int i=0; i<clusterResults.size(); i++) {
		    System.out.println("Cluster " + i);
		    totalNum += clusterResults.get(i).getPoints().size();
		    for (LocationWrapper locationWrapper : clusterResults.get(i).getPoints())
		        System.out.println(locationWrapper.getPoint()[0]);
		    System.out.println();
		}
		System.out.println("total number for cluster: " + totalNum);
		**test end*/
	}
	
	
	public boolean isSameClusterItems(long itemID1, long itemID2){
		if(!clusterMap.containsKey(itemID1) || !clusterMap.containsKey(itemID2)){
			return false;
		}
		int clustForItem1 = clusterMap.get(itemID1);
		int clustForItem2 = clusterMap.get(itemID2);
		return clustForItem1 == clustForItem2;
	}
	
	
	public long[] itemsFromSameCluster(long itemID){
		if(!clusterMap.containsKey(itemID)){
			return null;
		}
		List <LocationWrapper> IDWrappers = clusterResults.get(clusterMap.get(itemID)).getPoints();
		long[] similarItemsID = new long[IDWrappers.size()];
		for(int i=0; i < IDWrappers.size(); i++){
			similarItemsID[i] = (long)IDWrappers.get(i).getPoint()[0];
		}
		return similarItemsID;
	}
	


	class ItemDistanceMeasure implements DistanceMeasure{	
		
		private static final long serialVersionUID = 1717556319784040014L;		

		public double compute(double[] a, double[] b) {
			// TODO Auto-generated method stub
			double distance = 0;
			try {
				distance = 1 - similarity.itemSimilarity((long)a[0], (long)b[0]);
			} catch (TasteException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return distance;
		}

	}
}
