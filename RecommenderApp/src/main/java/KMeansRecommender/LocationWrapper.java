package KMeansRecommender;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.math3.ml.clustering.CentroidCluster;
import org.apache.commons.math3.ml.clustering.Clusterable;
import org.apache.commons.math3.ml.clustering.KMeansPlusPlusClusterer;
import org.apache.commons.math3.ml.distance.EuclideanDistance;
import org.apache.mahout.cf.taste.impl.model.file.FileDataModel;
import org.apache.mahout.cf.taste.model.DataModel;
import org.apache.mahout.cf.taste.model.PreferenceArray;

public class LocationWrapper implements Clusterable{
	
	private double[] points;
	
	
	public LocationWrapper(double[] inputs){
		points = new double[inputs.length];
		for(int i=0; i<inputs.length; i++){
			points[i] = inputs[i];
		}
	}

	public double[] getPoint() {
		// TODO Auto-generated method stub
		return points;
	}

	public static void main(String[] args) throws Exception {
		// TODO Auto-generated method stub
		DataModel model = new FileDataModel(new File("data/dataset.csv"));
		PreferenceArray preferenceForItem = model.getPreferencesForItem(12);
		model.getItemIDs();
		List<LocationWrapper> clusterInput = new ArrayList<LocationWrapper>();
		EuclideanDistance ec;
		clusterInput.add(new LocationWrapper(new double[]{1,2}));
		clusterInput.add(new LocationWrapper(new double[]{2,2}));
		clusterInput.add(new LocationWrapper(new double[]{3,2}));
		clusterInput.add(new LocationWrapper(new double[]{2,3}));
		clusterInput.add(new LocationWrapper(new double[]{8,9}));
		clusterInput.add(new LocationWrapper(new double[]{8,8}));
		MyKMeansPlusPlusClusterer<LocationWrapper> clusterer = new MyKMeansPlusPlusClusterer<LocationWrapper>(2, 100);
		List<CentroidCluster<LocationWrapper>> clusterResults = clusterer.cluster(clusterInput);

		// output the clusters
		for (int i=0; i<clusterResults.size(); i++) {
		    System.out.println("Cluster " + i);
		    for (LocationWrapper locationWrapper : clusterResults.get(i).getPoints())
		        System.out.println(locationWrapper.getPoint()[0] + "," + locationWrapper.getPoint()[1]);
		    System.out.println();
		}
	}
}
