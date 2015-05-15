package prepareResultsForR;

import java.util.ArrayList;

public class Arff {
	private String path;
	private String name;
	private ArrayList<Result> results = new ArrayList<Result>();

	public Arff(String path, String name) {
		this.path = path;
		this.name = name;
	}
	
	public String getResultForMetric(Metric m) {
		for(Result r : results) {
			if(r.getMetric() == m) {
				return r.getResult();
			}
		}
		
		return null;
	}
	
	public void addResult(Result r) {
		this.results.add(r);
	}
	
	public String getPath() {
		return this.path;
	}

	public String getName() {
		return this.name;
	}
}
