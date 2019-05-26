import javax.swing.*;
import javax.swing.event.*;
import javax.swing.table.*; 
import java.awt.*;
import java.awt.event.*;
import java.beans.PropertyVetoException;
import java.io.*;
import java.sql.*;
import java.util.*;
import java.util.List;
import org.knowm.xchart.*;
import org.knowm.xchart.XYSeries.XYSeriesRenderStyle;
import org.knowm.xchart.style.Styler.LegendPosition;
import org.knowm.xchart.style.markers.*;

/**
 * GUI
 */
public class GUI{
    final static String StringDriver="com.mysql.cj.jdbc.Driver";
    final static String StringConnection="jdbc:mysql://localhost:3306/tugas_clustering"; 
    final static String StringTabelNilai="data_csv_xy";
    static final String StringConnectionUser = "root";
    static final String StringConnectionPass = "";

    private static JFrame mainFrame = new JFrame();

    private static JPanel homePanel;

    private static JInternalFrame formClustering;

    private static String tabelDataJudul[] = {"No","ID","Nilai X","Nilai Y"}; 
    private static DefaultTableModel tabelDataModel = new DefaultTableModel(null, tabelDataJudul); 
    private static JTable tabelData = new JTable();
    private static JScrollPane tabelScrollBar = new JScrollPane(); 

    private static JLabel statusSQL = new JLabel();
    private static JLabel statusSQLText = new JLabel();
    
    private static JLabel databaseSQLText = new JLabel();
    private static JLabel databaseSQL = new JLabel();

    private static JLabel databaseTabel = new JLabel();
    private static JLabel databaseTabelXY = new JLabel();
    private static JLabel databaseTabelXYText = new JLabel();
    private static JLabel clusteringTitle = new JLabel();
    private static JLabel clusteringXText = new JLabel();
    private static JLabel clusteringYText = new JLabel();
    private static JLabel clusteringLiterationText = new JLabel();
    private static JLabel clusteringClusterText = new JLabel();
    private static JLabel clusteringNoteText = new JLabel();

    private static JTextField clusteringX = new JTextField(); 
    private static JTextField clusteringY = new JTextField(); 
    private static JTextField clusteringLiteration = new JTextField(); 
    private static JTextField clusteringCluster = new JTextField(); 

    private static JButton Btn_LiatTabel = new JButton(); 
    private static JButton Btn_BuatCluster = new JButton(); 
    private static JButton Btn_ClusterGen = new JButton();

    private static Connection conn = null;
    // private static Boolean is_Conected 

    public static void GUI(){
        mainFrame.setTitle("Clustering");
        homePanel.setLayout(null);
        mainFrame.setSize(450, 700);
        mainFrame.setResizable(false);
        mainFrame.setDefaultCloseOperation(mainFrame.DISPOSE_ON_CLOSE);
        // setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        mainFrame.setLocationRelativeTo(null); 
        mainFrame.setVisible(true);

        tabelData.setModel(tabelDataModel);
        tabelScrollBar.getViewport().add(tabelData); 
        tabelData.setEnabled(true); 
        tabelData.setAutoResizeMode(JTable.AUTO_RESIZE_OFF); 
        TableColumn col1 = tabelData.getColumnModel().getColumn(0);
        col1.setPreferredWidth(50); 
        TableColumn col2 = tabelData.getColumnModel().getColumn(1);
        col2.setPreferredWidth(50); 
        TableColumn col3 = tabelData.getColumnModel().getColumn(2);
        col3.setPreferredWidth(100);
        TableColumn col4 = tabelData.getColumnModel().getColumn(3);
        col4.setPreferredWidth(100);
        DefaultTableCellRenderer tableAlignCenter = new DefaultTableCellRenderer(); 
        tableAlignCenter.setHorizontalAlignment(JLabel.CENTER); 
        tabelData.getColumnModel().getColumn(0).setCellRenderer(tableAlignCenter); 
        tabelData.getColumnModel().getColumn(1).setCellRenderer(tableAlignCenter); 
        tabelData.getColumnModel().getColumn(2).setCellRenderer(tableAlignCenter); 
        tabelData.getColumnModel().getColumn(3).setCellRenderer(tableAlignCenter); 
        tabelData.setEnabled(false);
        
        statusSQLText.setBounds(75, 20, 90, 25); 
        statusSQL.setBounds(175, 20, 210, 25); 
        databaseSQLText.setBounds(75, 50, 90, 25); 
        databaseSQL.setBounds(175, 50, 210, 25); 
        databaseTabel.setBounds(75, 80, 390, 25);
        databaseTabelXYText.setBounds(75, 110, 90, 25);
        databaseTabelXY.setBounds(175, 110, 210, 25);

        Btn_LiatTabel.setBounds(75, 160, 300, 25);
        Btn_BuatCluster.setBounds(75, 190, 300, 25);

        tabelScrollBar.setBounds(75, 240, 300, 400);

        clusteringTitle.setBounds(75, 240, 300, 25);
        clusteringXText.setBounds(75, 270, 150, 25);
        clusteringYText.setBounds(75, 300, 150, 25);
        clusteringLiterationText.setBounds(75, 330, 150, 25);
        clusteringClusterText.setBounds(75, 360, 150, 25);
        clusteringNoteText.setBounds(75, 390, 300, 25);
        Btn_ClusterGen.setBounds(75, 420, 300, 25);
        clusteringX.setBounds(225, 270, 150, 25);
        clusteringY.setBounds(225, 300, 150, 25);
        clusteringLiteration.setBounds(225, 330, 150, 25);
        clusteringCluster.setBounds(225, 360, 150, 25);

        statusSQLText.setText("SQL Status:");
        statusSQL.setText("Loading");
        databaseSQLText.setText("Database:");
        databaseTabel.setText("Info Tabel Database");
        databaseTabelXYText.setText("data_csv_xy:");

        Btn_LiatTabel.setText("Buka Tabel Data");
        Btn_BuatCluster.setText("Buka Form Clustering");

        clusteringTitle.setText("Masukan data yang dibutuhkan");
        clusteringXText.setText("Set Title Untuk Nilai X");
        clusteringYText.setText("Set Title Untuk Nilai Y");
        clusteringLiterationText.setText("Set Jumlah Literasi");
        clusteringClusterText.setText("Set Jumlah Cluster");
        Btn_ClusterGen.setText("Hasilkan Clustering");
        clusteringNoteText.setText("Note: Literasi Min 25000, Cluster Min 2 Max 10");

        databaseSQLText.setVisible(false);
        databaseSQL.setVisible(false);
        databaseTabel.setVisible(false);
        databaseTabelXYText.setVisible(false);
        databaseTabelXY.setVisible(false);
        Btn_LiatTabel.setVisible(false);
        Btn_BuatCluster.setVisible(false);

        tabelScrollBar.setVisible(false);

        clusteringTitle.setVisible(false);
        clusteringXText.setVisible(false);
        clusteringYText.setVisible(false);
        clusteringLiterationText.setVisible(false);
        clusteringClusterText.setVisible(false);
        Btn_ClusterGen.setVisible(false);
        clusteringX.setVisible(false);
        clusteringY.setVisible(false);
        clusteringLiteration.setVisible(false);
        clusteringCluster.setVisible(false);
        clusteringNoteText.setVisible(false);

        Btn_LiatTabel.addActionListener(new ActionListener() {
            @Override public void actionPerformed(ActionEvent e) {
                if (tabelScrollBar.isVisible()) {
                    Btn_LiatTabel.setText("Buka Tabel Data");
                    VisTabel(false);
                } else {
                    if (clusteringTitle.isVisible()) {
                        Btn_BuatCluster.setText("Buka Form Clustering");
                        VisCluster(false);
                    }
                    getTabelData();
                    Btn_LiatTabel.setText("Tutup Tabel Data");
                    VisTabel(true);
                }
            }
        });

        Btn_BuatCluster.addActionListener(new ActionListener() {
            @Override public void actionPerformed(ActionEvent e) {
                if (clusteringTitle.isVisible()) {
                    Btn_BuatCluster.setText("Buka Form Clustering");
                    VisCluster(false);
                } else {
                    if (tabelScrollBar.isVisible()) {
                        Btn_LiatTabel.setText("Buka Tabel Data");
                        VisTabel(false);
                    }
                    Btn_BuatCluster.setText("Tutup Form Clustering");
                    VisCluster(true);
                }
            }
        });

        Btn_ClusterGen.addActionListener(new ActionListener() {
            @Override public void actionPerformed(ActionEvent e) {
                boolean Error = false;
                String nilaiX = clusteringX.getText();
                String nilaiY = clusteringY.getText();
                String nilaiLiteration = clusteringLiteration.getText();
                String nilaiCluster = clusteringCluster.getText();
                
                if (conn != null) {
                    if (nilaiX.trim().length() == 0) {
                        Error = true;
                        JOptionPane.showMessageDialog(null, "Nilai X harus di isi", "Error", JOptionPane.ERROR_MESSAGE);
                    }
    
                    if (!Error && nilaiY.trim().length() == 0) {
                        Error = true;
                        JOptionPane.showMessageDialog(null, "Nilai Y harus di isi", "Error", JOptionPane.ERROR_MESSAGE);
                    }
    
                    if (!Error && nilaiLiteration.trim().length() == 0) {
                        Error = true;
                        JOptionPane.showMessageDialog(null, "Jumlah literasi harus di isi", "Error", JOptionPane.ERROR_MESSAGE);
                    }
    
                    if (!Error && nilaiLiteration.trim().length() == 0) {
                        Error = true;
                        JOptionPane.showMessageDialog(null, "Jumlah cluster harus di isi", "Error", JOptionPane.ERROR_MESSAGE);
                    }
    
                    int literation = 0;
                    int cluster = 0;
    
                    if (!Error) {
                        try {
                            literation = Integer.parseInt(nilaiLiteration);
                            cluster = Integer.parseInt(nilaiCluster);
                        } catch (NumberFormatException ex) {
                            Error = true;
                            JOptionPane.showMessageDialog(null, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                        }
                    }
    
                    if (!Error && literation < 25000){
                        Error = true;
                        JOptionPane.showMessageDialog(null, "Literasi terlalu kecil", "Error", JOptionPane.ERROR_MESSAGE);
                    }
    
                    if (!Error && cluster < 2){
                        Error = true;
                        JOptionPane.showMessageDialog(null, "Cluster terlalu kecil", "Error", JOptionPane.ERROR_MESSAGE);
                    }
    
                    if (!Error && cluster > 10){
                        Error = true;
                        JOptionPane.showMessageDialog(null, "Cluster terlalu besar", "Error", JOptionPane.ERROR_MESSAGE);
                    }
    
                    if (!Error) {
                        runKMeans(nilaiX, nilaiY, literation, cluster);
                    }
                } else {
                    JOptionPane.showMessageDialog(null, "Database not connected", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        homePanel.add(statusSQLText);
        homePanel.add(statusSQL);
        homePanel.add(databaseSQLText);
        homePanel.add(databaseSQL);
        homePanel.add(databaseTabel);
        homePanel.add(databaseTabelXYText);
        homePanel.add(databaseTabelXY);
        
        homePanel.add(Btn_LiatTabel);
        homePanel.add(Btn_BuatCluster);

        homePanel.add(tabelScrollBar);

        homePanel.add(clusteringTitle);
        homePanel.add(clusteringXText);
        homePanel.add(clusteringYText);
        homePanel.add(clusteringLiterationText);
        homePanel.add(clusteringClusterText);
        homePanel.add(Btn_ClusterGen);
        homePanel.add(clusteringNoteText);
        homePanel.add(clusteringX);
        homePanel.add(clusteringY);
        homePanel.add(clusteringLiteration);
        homePanel.add(clusteringCluster);

        mainFrame.getContentPane().add(homePanel); 
    }

    public static void VisTabel(boolean vis) {
        tabelScrollBar.setVisible(vis);
    }

    public static void VisCluster(boolean vis) {
        clusteringTitle.setVisible(vis);
        clusteringXText.setVisible(vis);
        clusteringYText.setVisible(vis);
        clusteringLiterationText.setVisible(vis);
        clusteringClusterText.setVisible(vis);
        Btn_ClusterGen.setVisible(vis);
        clusteringNoteText.setVisible(vis);
        clusteringX.setVisible(vis);
        clusteringY.setVisible(vis);
        clusteringLiteration.setVisible(vis);
        clusteringCluster.setVisible(vis);
    }

    public static void DatabaseDriver() {
        try{
            statusSQL.setText("Mencari Driver MySQL");
            Class.forName(StringDriver);
        } catch (ClassNotFoundException ex){
            statusSQL.setText(ex.getMessage());
        }
    }

    public static void DatabaseKoneksi() {
        boolean KoneksiAman = false;
        DatabaseDriver();

        try {
            statusSQL.setText("Menghubungkan Koneksi Ke Database");
            conn =  DriverManager.getConnection(StringConnection, StringConnectionUser, StringConnectionPass);
            KoneksiAman = true;
        } catch (SQLException ex) {
            statusSQL.setText(ex.getMessage());
        }

        if (KoneksiAman) {
            statusSQL.setText("Terhubung ke Database");
        }
    }

    public static void DatabaseClose() {
        try {
            if (conn != null)
                conn.close();
        } catch (SQLException ex) {
            statusSQL.setText(ex.getMessage());
        }

        conn = null;
    }

    public static void DatabaseInfo(boolean isRefresh) {
        boolean TrySukses = true;
        int countTabelRow = 0;
        try {
            if (conn != null) {
                databaseSQL.setText(conn.getCatalog());

                String q1 = "SELECT COUNT(*) FROM `" + StringTabelNilai + "`";
                Statement st = conn.createStatement();
                ResultSet rs = st.executeQuery(q1);

                while (rs.next()) {
                    countTabelRow = rs.getInt("count(*)");
                }

                databaseTabelXY.setText(Integer.toString(countTabelRow));

                st.close();
            }
        } catch (SQLException ex) {
            TrySukses = false;
            statusSQL.setText(ex.getMessage());
        }

        if (TrySukses) {
            databaseSQL.setVisible(true);
            databaseSQLText.setVisible(true);
            databaseTabel.setVisible(true);
            databaseTabelXYText.setVisible(true);
            databaseTabelXY.setVisible(true);

            Btn_LiatTabel.setVisible(true);
            Btn_BuatCluster.setVisible(true);
        }
    }

    public static void rawData(int xAttribute, int yAttribute) {
        try {
            String line;

            BufferedReader br = new BufferedReader(new FileReader("College.csv"));
            System.out.println("Read record");
            ArrayList<String> queries = new ArrayList<String>();
            while ((line = br.readLine()) != null) {
                System.out.println("Read lines");
                queries.add("insert into `" + StringTabelNilai + "` (`nilai_x`, `nilai_y`) values (" + Double.parseDouble(line.split(",")[xAttribute]) + ", " + Double.parseDouble(line.split(",")[yAttribute]) +")");
            }

            Statement st = conn.createStatement();
            for (String query : queries) {
                st.addBatch(query);
            }
            st.executeBatch();
            st.close();
            br.close();
        } catch (IOException e) {
            System.out.println(e.getMessage());
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }
    }

    public static void getTabelData() {
        try {
            if (conn != null) {
                int index = 0,
                    nomor = 0;
                String q1 = "SELECT id, nilai_x, nilai_y FROM `" + StringTabelNilai + "`";
                Statement st = conn.createStatement();
                ResultSet rs = st.executeQuery(q1);
                tabelDataModel.setRowCount(0);
                while (rs.next()) {
                    nomor = index + 1;
                    tabelDataModel.insertRow(index, new Object[]{
                        nomor,
                        Integer.toString(rs.getInt("id")),
                        Double.toString(rs.getDouble("nilai_x")),
                        Double.toString(rs.getDouble("nilai_y")),
                    });
                    index++;
                }

                st.close();
            }
        } catch (SQLException ex) {
            statusSQL.setText(ex.getMessage());
        }
    }

    public static void runKMeans(String NilaiX, String NilaiY, int Literation, int Cluster) {
        try {
            if (conn != null) {
                int index = 0,
                    nomor = 0,
                    count = 0;

                String q1 = "SELECT COUNT(*) FROM `" + StringTabelNilai + "`";
                Statement st = conn.createStatement();
                ResultSet rs = st.executeQuery(q1);

                while (rs.next()) {
                    count = rs.getInt("count(*)");
                }
                st.close();

                String q2 = "SELECT id, nilai_x, nilai_y FROM `" + StringTabelNilai + "`";
                st = conn.createStatement();
                rs = st.executeQuery(q2);

                double[][] points = new double[count][2];
                int i = 0;

                while (rs.next()) {
                    points[i][0] = rs.getDouble("nilai_x");
                    points[i++][1] = rs.getDouble("nilai_y");
                }
                st.close();

                sortPointsByX(points);

                double[][] means = new double[Cluster][2];
                for(int i2=0; i2<means.length; i2++) {
                    means[i2][0] = points[(int) (Math.floor((count*1.0/Cluster)/2) + i2*count/Cluster)][0];
                    means[i2][1] = points[(int) (Math.floor((count*1.0/Cluster)/2) + i2*count/Cluster)][1];
                }

                ArrayList<Integer>[] oldClusters = new ArrayList[Cluster];
                ArrayList<Integer>[] newClusters = new ArrayList[Cluster];

                for(int i3=0; i3<Cluster; i3++) {
                    oldClusters[i3] = new ArrayList<Integer>();
                    newClusters[i3] = new ArrayList<Integer>();
                }

                formClusters(oldClusters, means, points);
                int itr = 0;

                while(true) {
                    updateMeans(oldClusters, means, points);
                    formClusters(newClusters, means, points);
        
                    itr++;
        
                    if(itr > Literation || checkEquality(oldClusters, newClusters))
                        break;
                    else
                        resetClusters(oldClusters, newClusters);
                }

                System.out.println("\nHasil Clustering:");
                displayOutput(oldClusters, points);
                System.out.println("\nJumlah baris pada .csv: " + count);
                System.out.println("\nJumlah Iterations yang di pakai = " + Literation);

                XYChart chart = new XYChartBuilder().width(600).height(600).title("Diagram Scatter (" + count + " data)").xAxisTitle(NilaiX).yAxisTitle(NilaiY).build();

                chart.getStyler().setDefaultSeriesRenderStyle(XYSeriesRenderStyle.Scatter);
                chart.getStyler().setChartTitleVisible(true);
                chart.getStyler().setLegendPosition(LegendPosition.OutsideE);
                chart.getStyler().setMarkerSize(8);

                for(int i4=0; i4<oldClusters.length; i4++) {
                    ArrayList<Double> XData = new ArrayList<Double>();
                    ArrayList<Double> YData = new ArrayList<Double>();
                    ArrayList<Integer>[] TesterCluster = oldClusters;
                    for(int index2: TesterCluster[i4]){
                        XData.add(points[index2][0]);
                        YData.add(points[index2][1]);
                    }
                    XYSeries series = chart.addSeries("Cluster ke-" + i4, XData, YData);
                    series.setMarker(SeriesMarkers.CIRCLE);
                }
                
                Thread t = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        Runtime.getRuntime().gc();
                        javax.swing.SwingUtilities.invokeLater(new Runnable() {
                            @Override public void run() {
                                JFrame frame = new JFrame("Diagram");
                                frame.setLayout(new BorderLayout());
                                frame.setDefaultCloseOperation(frame.DISPOSE_ON_CLOSE);

                                JPanel chartPanel = new XChartPanel<XYChart>(chart);
                                frame.add(chartPanel, BorderLayout.CENTER);

                                frame.pack();
                                frame.setLocationRelativeTo(null); 
                                frame.setVisible(true);

                            }
                        });
                    }
                
                });
                t.start();

                index = 0;
                nomor = 0;
                count = 0;
                oldClusters = null;
                newClusters = null;
                points = null;
                means = null;
                itr = 0;
                // chart = null;

            }
        }catch (SQLException ex) {
            statusSQL.setText(ex.getMessage());
        }
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

    public static void main(String args[]){
        homePanel = new JPanel(); 

        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                GUI();
            }
        });

        DatabaseKoneksi();
        DatabaseInfo(false);

        // runKMeans("T1", "T2", 25000, 4);

        // rawData(4, 15);
    } 

}