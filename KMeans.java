import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.util.*;

import org.knowm.xchart.*;
import org.knowm.xchart.XYSeries.XYSeriesRenderStyle;
import org.knowm.xchart.style.Styler.LegendPosition;
import org.knowm.xchart.style.markers.*;

public class KMeans {
	private static int countLines = 0;

	Connection conn = null;

	public static void main(String args[]) throws IOException {
		Scanner sc = new Scanner(System.in);
		// String filePath = "C:/Users/Mea/Documents/Pemprograman 1/P2/Clustering/";
		String filePath = "";
		System.out.print("Input nama file yang berformat .csv: ");
		String fileName = sc.nextLine();

		// Open the file just to count the number of records
		int records = getRecords(filePath, fileName);

		System.out.print("Input index dari atribut X: ");
		int xAttribute = sc.nextInt();
		System.out.print("Input index dari atribut Y: ");
		int yAttribute = sc.nextInt();

		// Open file again to read the records
		double[][] points = new double[records][2];
		readRecords(filePath, fileName, points, xAttribute, yAttribute);

		// Sort the points based on X-coordinate values
		sortPointsByX(points);

		// Input the number of iterations
		System.out.print("Input jumlah maksimal dari iterations: ");
		int maxIterations = sc.nextInt();

		// Input number of clusters
		System.out.print("Masukan jumlah cluster: ");
		int clusters = sc.nextInt();

		// Calculate initial means
		double[][] means = new double[clusters][2];
		for(int i=0; i<means.length; i++) {
			means[i][0] = points[(int) (Math.floor((records*1.0/clusters)/2) + i*records/clusters)][0];
			means[i][1] = points[(int) (Math.floor((records*1.0/clusters)/2) + i*records/clusters)][1];
		}

		// Create skeletons for clusters
		ArrayList<Integer>[] oldClusters = new ArrayList[clusters];
		ArrayList<Integer>[] newClusters = new ArrayList[clusters];

		for(int i=0; i<clusters; i++) {
			oldClusters[i] = new ArrayList<Integer>();
			newClusters[i] = new ArrayList<Integer>();
		}

		// Make the initial clusters
		formClusters(oldClusters, means, points);
		int iterations = 0;

		// Showtime
		while(true) {
			updateMeans(oldClusters, means, points);
			formClusters(newClusters, means, points);

			iterations++;

			if(iterations > maxIterations || checkEquality(oldClusters, newClusters))
				break;
			else
				resetClusters(oldClusters, newClusters);
		}

		// Display the output
		// System.out.println("\nData Clustering:");
		// displayOutputPoints(points);
		System.out.println("\nHasil Clustering:");
		displayOutput(oldClusters, points);
		System.out.println("\nJumlah baris pada .csv: " + countLines);
		System.out.println("\nJumlah Iterations yang di pakai = " + iterations);

		// Create Chart
		XYChart chart = new XYChartBuilder().width(600).height(600).title("Diagram Scatter (" + countLines + " data)").xAxisTitle("X").yAxisTitle("Y").build();

		// Customize Chart
		chart.getStyler().setDefaultSeriesRenderStyle(XYSeriesRenderStyle.Scatter);
		chart.getStyler().setChartTitleVisible(true);
		chart.getStyler().setLegendPosition(LegendPosition.OutsideE);
		chart.getStyler().setMarkerSize(8);

		// Series
		for(int i=0; i<oldClusters.length; i++) {
			List<Double> XData = new ArrayList<Double>();
			List<Double> YData = new ArrayList<Double>();
			ArrayList<Integer>[] TesterCluster = oldClusters;
			for(int index: TesterCluster[i]){
				XData.add(points[index][0]);
				YData.add(points[index][1]);
			}
			XYSeries series = chart.addSeries("Cluster ke-" + i, XData, YData);
			series.setMarker(SeriesMarkers.CIRCLE);
		}

		new SwingWrapper(chart).displayChart();

		sc.close();
	}

	static int getRecords(String filePath, String fileName) throws IOException {
		int records = 0;
		// BufferedReader br = new BufferedReader(new FileReader("College.csv"));
		BufferedReader br = new BufferedReader(new FileReader(filePath + fileName + ".csv"));
		while (br.readLine() != null)
			records++;

		br.close();
		return records;
	}

	static void readRecords(String filePath, String fileName, double[][] points, int xAttribute, int yAttribute) throws IOException {
		BufferedReader br = new BufferedReader(new FileReader(filePath + fileName + ".csv"));
		// BufferedReader br = new BufferedReader(new FileReader("College.csv"));
		String line;
		int i = 0;
		while ((line = br.readLine()) != null) {
			points[i][0] = Double.parseDouble(line.split(",")[xAttribute]);
			points[i++][1] = Double.parseDouble(line.split(",")[yAttribute]);
		}

		countLines = i;

		br.close();
	}

	static void sortPointsByX(double[][] points) {
		double[] temp;

		// Bubble Sort
		for(int i=0; i<points.length; i++)
			for(int j=1; j<(points.length-i); j++)
				if(points[j-1][0] > points[j][0]) {
					temp = points[j-1];
					points[j-1] = points[j];
					points[j] = temp;
				}
	}

	static void updateMeans(ArrayList<Integer>[] clusterList, double[][] means, double[][] points) {
		double totalX = 0;
		double totalY = 0;
		for(int i=0; i<clusterList.length; i++) {
			totalX = 0;
			totalY = 0;
			for(int index: clusterList[i]) {
				totalX += points[index][0];
				totalY += points[index][1];
			}
			means[i][0] = totalX/clusterList[i].size();
			means[i][1] = totalY/clusterList[i].size();
		}
	}

	static void formClusters(ArrayList<Integer>[] clusterList, double[][] means, double[][] points) {
		double distance[] = new double[means.length];
		double minDistance = 999999999;
		int minIndex = 0;

		for(int i=0; i<points.length; i++) {
			minDistance = 999999999;
			for(int j=0; j<means.length; j++) {
				distance[j] = Math.sqrt(Math.pow((points[i][0] - means[j][0]), 2) + Math.pow((points[i][1] - means[j][1]), 2));
				if(distance[j] < minDistance) {
					minDistance = distance[j];
					minIndex = j;
				}
			}
			clusterList[minIndex].add(i);
		}
	}

	static boolean checkEquality(ArrayList<Integer>[] oldClusters, ArrayList<Integer>[] newClusters) {
		for(int i=0; i<oldClusters.length; i++) {
			// Check only lengths first
			if(oldClusters[i].size() != newClusters[i].size())
				return false;

			// Check individual values if lengths are equal
			for(int j=0; j<oldClusters[i].size(); j++)
				if(oldClusters[i].get(j) != newClusters[i].get(j))
					return false;
		}

		return true;
	}

	static void resetClusters(ArrayList<Integer>[] oldClusters, ArrayList<Integer>[] newClusters) {
		for(int i=0; i<newClusters.length; i++) {
			// Copy newClusters to oldClusters
			oldClusters[i].clear();
			for(int index: newClusters[i])
				oldClusters[i].add(index);

			// Clear newClusters
			newClusters[i].clear();
		}
	}

	static void displayOutputPoints(double[][] points) {
		for(int i=0; i<points.length; i++) {
			String clusterOutput = "\n\n[";
			clusterOutput += "(" + points[i][0] + ", " + points[i][1] + "), ";
			System.out.println(clusterOutput.substring(0, clusterOutput.length()-2) + "]");
		}
	}

	static void displayOutput(ArrayList<Integer>[] clusterList, double[][] points) {
		for(int i=0; i<clusterList.length; i++) {
			int CountPoints = 0;
			String clusterOutput = "\n\n[";
			for(int index: clusterList[i]){
				CountPoints++;
				clusterOutput += "(" + points[index][0] + ", " + points[index][1] + "), ";
			}
			System.out.println(clusterOutput.substring(0, clusterOutput.length()-2) + "]");
			System.out.println("Count data in cluster: " + CountPoints);
		}
	}
}
