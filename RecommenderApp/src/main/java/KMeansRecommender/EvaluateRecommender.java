package KMeansRecommender;

import java.io.File;

import org.apache.mahout.cf.taste.common.TasteException;
import org.apache.mahout.cf.taste.eval.RecommenderBuilder;
import org.apache.mahout.cf.taste.eval.RecommenderEvaluator;
import org.apache.mahout.cf.taste.impl.eval.AverageAbsoluteDifferenceRecommenderEvaluator;
import org.apache.mahout.cf.taste.impl.eval.RMSRecommenderEvaluator;
import org.apache.mahout.cf.taste.impl.model.file.FileDataModel;
import org.apache.mahout.cf.taste.impl.neighborhood.ThresholdUserNeighborhood;
import org.apache.mahout.cf.taste.impl.recommender.GenericItemBasedRecommender;
import org.apache.mahout.cf.taste.impl.recommender.GenericUserBasedRecommender;
import org.apache.mahout.cf.taste.impl.similarity.LogLikelihoodSimilarity;
import org.apache.mahout.cf.taste.impl.similarity.PearsonCorrelationSimilarity;
import org.apache.mahout.cf.taste.impl.similarity.TanimotoCoefficientSimilarity;
import org.apache.mahout.cf.taste.impl.similarity.UncenteredCosineSimilarity;
import org.apache.mahout.cf.taste.model.DataModel;
import org.apache.mahout.cf.taste.neighborhood.UserNeighborhood;
import org.apache.mahout.cf.taste.recommender.Recommender;
import org.apache.mahout.cf.taste.similarity.ItemSimilarity;
import org.apache.mahout.cf.taste.similarity.UserSimilarity;
import org.apache.mahout.common.RandomUtils;


public class EvaluateRecommender {

	public static void main(String[] args) throws Exception {
		RandomUtils.useTestSeed();
		
		DataModel model = new FileDataModel(new File("data/movies1m.csv"));		
		//RecommenderEvaluator evaluator = new AverageAbsoluteDifferenceRecommenderEvaluator();
		//originAlgEvaluator(model);
		
		RecommenderEvaluator evaluator = new RMSRecommenderEvaluator();		
		int begin = 1;
		int end = 400;
		int step = 20;
		
		/*
		System.out.println("Original method with PearsonCorrelationSimilarity, dataset : movies1m.csv, train-test ratio : 0.9");
		RecommenderBuilder builder2 = new OriginalRecommenderBuilder(new PearsonCorrelationSimilarity(model));
		double result2 = evaluator.evaluate(builder2, null, model, 0.9, 1.0);
		System.out.println(result2);
		*/
		
		System.out.println("Compute KMeansPlus with PearsonCorrelationSimilarity, dataset : movies1m.csv, train-test ratio : 0.9");
		for(int i = begin; i <= end; i = i + step){
			RecommenderBuilder builder = new MyRecommenderBuilderPlus(i, new PearsonCorrelationSimilarity(model));
			double result = evaluator.evaluate(builder, null, model, 0.9, 1.0);
			System.out.println(i + "\t" + result);
			//System.out.println("k = " + i + ", result = " + result);
			//System.out.println();
			i = (i == 1) ? 0 : i;
		}		
		
		System.out.println("Compute KMeansPlus with UncenteredCosineSimilarity, dataset : movies1m.csv, train-test ratio : 0.9");
		for(int i = begin; i <= end; i = i + step){
			RecommenderBuilder builder = new MyRecommenderBuilderPlus(i, new UncenteredCosineSimilarity(model));
			double result = evaluator.evaluate(builder, null, model, 0.9, 1.0);
			System.out.println(i + "\t" + result);
			//System.out.println("k = " + i + ", result = " + result);
			//System.out.println();
			i = (i == 1) ? 0 : i;
		}	
		
	}
	
	public static void originAlgEvaluator(DataModel model) throws Exception{
		RecommenderEvaluator evaluator = new RMSRecommenderEvaluator();	
		int begin = 1;
		int end = 9;
		int step = 1;
		System.out.println("Original method with PearsonCorrelationSimilarity, dataset : movies1m.csv, train-test ratio : 0.1 - 0.9");
		for(int i = begin; i <= end; i = i + step){
			RecommenderBuilder builder = new OriginalRecommenderBuilder(new PearsonCorrelationSimilarity(model));
			double result = evaluator.evaluate(builder, null, model, ((double)i)/10, 1.0);
			System.out.println(((double)i)/10 + "\t" + result);
		}
		
		System.out.println("Original method with UncenteredCosineSimilarity, dataset : movies1m.csv, train-test ratio : 0.1 - 0.9");
		for(int i = begin; i <= end; i = i + step){
			RecommenderBuilder builder = new OriginalRecommenderBuilder(new UncenteredCosineSimilarity(model));
			double result = evaluator.evaluate(builder, null, model, ((double)i)/10, 1.0);
			System.out.println(((double)i)/10 + "\t" + result);
		}
	}
	
	public static void kmeansAlgEvaluator(DataModel model) throws Exception{
		RecommenderEvaluator evaluator = new RMSRecommenderEvaluator();	
		int begin = 1;
		int end = 10;
		int step = 1;
		System.out.println("KMeans with LogLikelihoodSimilarity, dataset : movies1m.csv, model size ratio : 0.1 - 1.0");
		for(int i = begin; i <= end; i = i + step){
			RecommenderBuilder builder = new MyRecommenderBuilder(20, new LogLikelihoodSimilarity(model));
			double result = evaluator.evaluate(builder, null, model, 0.9, ((double)i)/10);
			System.out.println(((double)i)/10 + "\t" + result);
		}
		
		System.out.println("KMeans with TanimotoCoefficientSimilarity, dataset : movies1m.csv, model size ratio : 0.1 - 1.0");
		for(int i = begin; i <= end; i = i + step){
			RecommenderBuilder builder = new MyRecommenderBuilder(20, new TanimotoCoefficientSimilarity(model));
			double result = evaluator.evaluate(builder, null, model, 0.9, ((double)i)/10);
			System.out.println(((double)i)/10 + "\t" + result);
		}
	}

}

class MyRecommenderBuilder implements  RecommenderBuilder {
	
	private ItemSimilarity similarity;
	private int k;
	private int maxiteration;
	
	public MyRecommenderBuilder(int k, ItemSimilarity similarity){
		this.k = k;
		this.similarity = similarity;
		maxiteration = 20;
	}
	

	public Recommender buildRecommender(DataModel dataModel)
			throws TasteException {
		
		ItemSimilarity is = new KMeansItemSimilarity(dataModel, k, maxiteration, similarity);
		//PearsonCorrelationSimilarity
		//ItemSimilarity is = new LogLikelihoodSimilarity(dataModel);
		return new GenericItemBasedRecommender(dataModel, is);
	}
}

class MyRecommenderBuilderPlus implements  RecommenderBuilder {
	
	private ItemSimilarity similarity;
	private int k;
	private int maxiteration;
	
	public MyRecommenderBuilderPlus(int k, ItemSimilarity similarity){
		this.k = k;
		this.similarity = similarity;
		maxiteration = 20;
	}
	

	public Recommender buildRecommender(DataModel dataModel)
			throws TasteException {
		
		ItemSimilarity is = new KMeansItemSimilarityPlus(dataModel, k, maxiteration, similarity);
		//PearsonCorrelationSimilarity
		//ItemSimilarity is = new LogLikelihoodSimilarity(dataModel);
		return new GenericItemBasedRecommender(dataModel, is);
	}
}

class OriginalRecommenderBuilder implements  RecommenderBuilder {
	
	private ItemSimilarity similarity;
	
	public OriginalRecommenderBuilder(ItemSimilarity similarity){
		this.similarity = similarity;
	}

	public Recommender buildRecommender(DataModel dataModel)
			throws TasteException {
		return new GenericItemBasedRecommender(dataModel, similarity);
	}
}