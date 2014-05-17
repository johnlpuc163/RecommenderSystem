package KMeansRecommender;

import java.io.File;
import java.io.IOException;
import java.util.Collection;
import java.util.concurrent.Callable;

import org.apache.mahout.cf.taste.common.Refreshable;
import org.apache.mahout.cf.taste.common.TasteException;
import org.apache.mahout.cf.taste.impl.common.RefreshHelper;
import org.apache.mahout.cf.taste.impl.model.file.FileDataModel;
import org.apache.mahout.cf.taste.impl.similarity.GenericItemSimilarity;
import org.apache.mahout.cf.taste.model.DataModel;
import org.apache.mahout.cf.taste.model.PreferenceArray;
import org.apache.mahout.cf.taste.similarity.ItemSimilarity;

public class KMeansItemSimilarity  implements ItemSimilarity{
	
	private DataModel dataModel;
	private KMeansItemModel kmeansModel;
	
	public KMeansItemSimilarity(DataModel dataModel, int k, int maxIterations, ItemSimilarity similarity){
		this.dataModel = dataModel;			
		this.kmeansModel = new KMeansItemModel(dataModel, k, maxIterations, similarity);
	    
		kmeansModel.buildModel();
	}

	public void refresh(Collection<Refreshable> alreadyRefreshed) {
		// do nothing
	}

	public double itemSimilarity(long itemID1, long itemID2)
			throws TasteException {
		
		double simVal = kmeansModel.isSameClusterItems(itemID1, itemID2) ? 1.0 : 0.0;
		return simVal;
	}

	public double[] itemSimilarities(long itemID1, long[] itemID2s)
			throws TasteException {
		// TODO Auto-generated method stub
		double simVals[] = new double[itemID2s.length];
		for(int i=0; i<itemID2s.length; i++){
			simVals[i] = kmeansModel.isSameClusterItems(itemID1, itemID2s[i]) ? 1.0 : 0.0;
		}
		return simVals;
	}

	public long[] allSimilarItemIDs(long itemID) throws TasteException {
		// TODO Auto-generated method stub
		long[] similarItemIDs = kmeansModel.itemsFromSameCluster(itemID);
		return similarItemIDs;
	}

}
