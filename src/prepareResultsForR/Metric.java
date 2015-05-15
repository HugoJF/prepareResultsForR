package prepareResultsForR;

public enum Metric {
	TRUE_POSITIVES ("truepositives"), 
	FALSE_POSITIVES ("falsepositives"), 
	PRECISION ("precision"), 
	RECALL ("recall"), 
	FMEASURE ("fmeasure"), 
	AUC ("auc");
	
	private String ext;

	Metric(String ext) {
		this.ext = ext;
	}
	
	String getExt() {
		return ext;
	}
}
