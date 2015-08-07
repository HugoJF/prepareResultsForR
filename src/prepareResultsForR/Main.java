package prepareResultsForR;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;

import javax.swing.JFileChooser;

import org.xml.sax.Attributes;

import configuration.Configuration;
import weka.classifiers.Classifier;
import weka.classifiers.Evaluation;
import weka.classifiers.functions.SMO;
import weka.classifiers.functions.supportVector.PolyKernel;
import weka.core.Attribute;
import weka.core.Instances;
import weka.core.Range;
import weka.core.converters.ArffLoader;
import weka.core.converters.ArffLoader.ArffReader;
import weka.core.converters.ConverterUtils.DataSource;

public class Main {

	public static void main(String[] args) {
		ArrayList<Arff> arffs = new ArrayList<Arff>();
		Scanner in = new Scanner(System.in);
		String name = null;
		
		final JFileChooser fc = new JFileChooser();
		fc.setCurrentDirectory(new File("D:\\Projects\\INOVISAOs\\artigo\\for_r_testing\\"));
		
		while(true) {
			int openResult = fc.showOpenDialog(null);
			if(openResult == JFileChooser.APPROVE_OPTION) {
				System.out.println("File to be added: " + fc.getSelectedFile().getAbsolutePath());
				System.out.println("Please input this file name: ");
				name = in.nextLine();
				System.out.println("=>> Adding: " + fc.getSelectedFile().getAbsolutePath() + " with name " + name);
				arffs.add(new Arff(fc.getSelectedFile().getAbsolutePath(), name));
			} else if (openResult == JFileChooser.CANCEL_OPTION) {
				break;
			}
		}
		
		new Main(arffs);
		
		System.out.println("Finishing");
	}
	
	public Main(ArrayList<Arff> arffs) {
		for(Arff a : arffs) {
			try {
				computeExperiment(a);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		PrintWriter writer = null;
		for(Metric m : Metric.values()) {
			try {
				writer = new PrintWriter(new File("D:\\Projects\\INOVISAOs\\artigo\\for_r_testing\\" + m.getExt()));
				writer.println("tecnica desempenho classe");
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}
			for(Arff a : arffs) {
				writer.print(a.getResultForMetric(m));
			}
			writer.flush();
		}
	}
	
	
	public void computeExperiment(Arff arff) throws Exception {
		String path = arff.getPath();
		String rName = arff.getName();
		String[] output = new String[6];
		for(int i = 0; i < 6; i++) {
			
			output[i] = "";
		}
		
		DataSource source = new DataSource(path);
		Instances instances = source.getDataSet();
		Attribute classValues = instances.attribute("class");
		instances.setClassIndex(instances.numAttributes() - 1);
		
		SMO smo = new SMO();
		smo.setC(1);
		smo.setChecksTurnedOff(false);
		smo.setEpsilon(1.0E-12D);
		smo.setKernel(new PolyKernel());
		smo.setNumFolds(-1);
		smo.setToleranceParameter(0.001D);
		smo.setBuildLogisticModels(true);
		
		Evaluation eval = new Evaluation(instances);
		String[] options = new String[0];
		
		eval.crossValidateModel(smo, instances, 10, new Random(), options);
		smo.buildClassifier(instances);
		//System.out.println(eval.toClassDetailsString());
		//System.out.println(instances.attribute("class").toString());
		//System.out.println(instances.attribute("class").value(0));
		//System.out.println(eval.toSummaryString());
		/*for(int i = 0; i < eval.confusionMatrix().length; i++) {
			for(int j = 0; j < eval.confusionMatrix()[0].length; j++) {
				System.out.print(eval.confusionMatrix()[i][j] + "   ");
			}
			System.out.println();
		}*/
		
		for(int i = 0; i < classValues.numValues(); i++) {
			output[0] += rName + " " + eval.truePositiveRate(i) 	+ " " + classValues.value(i) + "\n"; 
			output[1] += rName + " " + eval.falsePositiveRate(i) 	+ " " + classValues.value(i) + "\n"; 
			output[2] += rName + " " + eval.precision(i) 			+ " " + classValues.value(i) + "\n"; 
			output[3] += rName + " " + eval.recall(i) 				+ " " + classValues.value(i) + "\n"; 
			output[4] += rName + " " + eval.fMeasure(i) 			+ " " + classValues.value(i) + "\n"; 
			output[5] += rName + " " + eval.areaUnderROC(i) 		+ " " + classValues.value(i) + "\n"; 
		}
		/*
		PrintWriter writer = null;
		
		writer = new PrintWriter(Configuration.getConfiguration("path") + ".routput.truepositives");
		writer.write(output[0]);
		writer.close();
		
		writer = new PrintWriter(Configuration.getConfiguration("path") + ".routput.falsepositives");
		writer.write(output[1]);
		writer.close();
		
		writer = new PrintWriter(Configuration.getConfiguration("path") + ".routput.precision");
		writer.write(output[2]);
		writer.close();
		
		writer = new PrintWriter(Configuration.getConfiguration("path") + ".routput.recall");
		writer.write(output[3]);
		writer.close();
		
		writer = new PrintWriter(Configuration.getConfiguration("path") + ".routput.fmeasure");
		writer.write(output[4]);
		writer.close();
		
		writer = new PrintWriter(Configuration.getConfi2guration("path") + ".routput.rocarea");
		writer.write(output[5]);
		writer.close();
		*/
		
		arff.addResult(new Result(output[0], Metric.TRUE_POSITIVES));
		arff.addResult(new Result(output[1], Metric.FALSE_POSITIVES));
		arff.addResult(new Result(output[2], Metric.PRECISION));
		arff.addResult(new Result(output[3], Metric.RECALL));
		arff.addResult(new Result(output[4], Metric.FMEASURE));
		arff.addResult(new Result(output[5], Metric.AUC));
		
		
	}
}
