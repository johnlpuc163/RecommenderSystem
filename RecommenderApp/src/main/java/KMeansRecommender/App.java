package KMeansRecommender;

import java.io.File;
import java.util.List;

import org.apache.commons.math3.ml.clustering.Clusterable;
import org.apache.mahout.cf.taste.impl.common.FastIDSet;
import org.apache.mahout.cf.taste.impl.common.LongPrimitiveIterator;
import org.apache.mahout.cf.taste.impl.model.file.FileDataModel;
import org.apache.mahout.cf.taste.impl.neighborhood.ThresholdUserNeighborhood;
import org.apache.mahout.cf.taste.impl.recommender.GenericItemBasedRecommender;
import org.apache.mahout.cf.taste.impl.recommender.GenericUserBasedRecommender;
import org.apache.mahout.cf.taste.impl.similarity.LogLikelihoodSimilarity;
import org.apache.mahout.cf.taste.impl.similarity.PearsonCorrelationSimilarity;
import org.apache.mahout.cf.taste.model.DataModel;
import org.apache.mahout.cf.taste.model.PreferenceArray;
import org.apache.mahout.cf.taste.neighborhood.UserNeighborhood;
import org.apache.mahout.cf.taste.recommender.RecommendedItem;
import org.apache.mahout.cf.taste.recommender.UserBasedRecommender;
import org.apache.mahout.cf.taste.similarity.ItemSimilarity;
import org.apache.mahout.cf.taste.similarity.UserSimilarity;


public class App 
{
	public static void main( String[] args ) throws Exception
	{
		App.kmeans();		
	}
	
	public static void kmeans() throws Exception{
		DataModel model = new FileDataModel(new File("data/movies.csv"));
		
		ItemSimilarity itemSimilarity = new KMeansItemSimilarity(model, 2, 100, new PearsonCorrelationSimilarity(model));
		
		GenericItemBasedRecommender itemRecommender = new GenericItemBasedRecommender(model, itemSimilarity);
	}
	
	
}
