package prepareResultsForR;

public class Result {
	private String result;
	private Metric metric;

	public Result(String result, Metric metric) {
		this.result = result;
		this.metric = metric;
	}

	public String getResult() {
		return result;
	}

	public Metric getMetric() {
		return metric;
	}
}
