package com.bitmap.indexing;

/*
 * https://www.geeksforgeeks.org/java-program-for-quicksort/
 * https://oracle-base.com/articles/9i/bitmap-join-indexes
 * https://logicalread.com/2013/06/03/oracle-11g-bitmap-join-indexes-mc02/
 * https://www.youtube.com/watch?v=sMbQW7XNUZs
 * http://www.dba-oracle.com/art_builder_bitmap_join_idx.htm
 * https://www.ncbi.nlm.nih.gov/pmc/articles/PMC7030998/
 * https://www.geeksforgeeks.org/bitmap-indexing-in-dbms/
 * https://www.geeksforgeeks.org/indexing-in-databases-set-1/
 * https://en.wikipedia.org/wiki/Bitmap_index
 * https://roaringbitmap.org/about/
 * https://sdm.lbl.gov/~kewu/ps/LBNL-49627.pdf
 */
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.List;

public class MergeData {
	long mergeTtime = 0;

	public long getMergeTtime() {
		return mergeTtime;
	}

	public void setMergeTtime(long mergeTtime) {
		this.mergeTtime = mergeTtime;
	}

	long readCount = 0;
	public long getReadCount() {
		return readCount;
	}

	public void setReadCount(long readCount) {
		this.readCount = readCount;
	}

	long writeCount = 0;
	public long getWriteCount() {
		return writeCount;
	}

	public void setWriteCount(long writeCount) {
		this.writeCount = writeCount;
	}

	static int itertion = 0;
	static String currentMergeFile = "";
	static List<String> listOfFiles;
	static String outputPath = "";
	int write = 0;

	public int getWrite() {
		return write;
	}

	public void setWrite(int write) {
		this.write = write;
	}

	public MergeData(List<String> T1, List<String> T2) {
		listOfFiles = new ArrayList<>();
		listOfFiles.addAll(T1);
		listOfFiles.addAll(T2);
		writeCount = 0;
		readCount = 0;
	}
	

	public void mergeSort(List<String> blockList, String directory, int startIndex, int endIndex) {
		write = 0;
		long itertionStart = System.currentTimeMillis();
		ArrayList<String> mergedFiles = new ArrayList<>();
		System.lineSeparator();
		for (int i = 0; i < blockList.size(); i = i + 2) {
			currentMergeFile = directory + itertion + "-Block-" + i + "_" + (i + 1);
			try {
				BufferedReader br1 = new BufferedReader(new FileReader(blockList.get(i)));
				BufferedReader br2 = null;
				if (i + 1 < blockList.size()) 
					br2 = new BufferedReader(new FileReader(blockList.get(i + 1)));

				BufferedWriter bw = new BufferedWriter(new FileWriter(currentMergeFile));
				String tuple1 = null;
				String tuple2 = null;
				long length1 = 0;
				long length2 = 0;
				if (br2 != null) {
					while (true) {
						if (tuple1 == null) {
							tuple1 = br1.readLine();
							readCount++;
							length1 = tuple1 == null || tuple1.trim().length() == 0 ? length1
									: tuple1.substring(endIndex + 1).trim().length();
						}
						if (tuple2 == null) {
							tuple2 = br2.readLine();
							readCount++;
							// System.out.println("Here " + tuple2);
							length2 = tuple2 == null || tuple2.trim().length() == 0 ? length2
									: tuple2.substring(endIndex + 1).trim().length();
						}
						if (tuple1 == null && tuple2 == null) {
							break;
						}
						if (tuple1 != null && tuple2 != null) {
							String id1 = tuple1.substring(0, endIndex);
							String id2 = tuple2.substring(0, endIndex);
							if (id1.equals(id2)) {
								write++;
								bw.write(id1 + ":" + tuple1.substring(endIndex + 1) + tuple2.substring(endIndex + 1));
								bw.newLine();
								tuple1 = null;
								tuple2 = null;
							} else if (id1.compareToIgnoreCase(id2) > 0) {// id1 < id2
								StringBuilder temp = new StringBuilder();
								for (int k = 0; k < length1; k++) {
									temp.append(0);
								}
								write++;
								bw.write(tuple2.substring(0, endIndex + 1) + temp.toString()
								+ tuple2.substring(endIndex + 1));
								bw.newLine();
								tuple2 = null;
							} else if (id1.compareToIgnoreCase(id2) < 0) { // id1 > id2
								StringBuilder temp = new StringBuilder();
								for (int k = 0; k < length2; k++) {
									temp.append(0);
								}
								write++;
								bw.write(tuple1 + temp.toString());
								bw.newLine();
								tuple1 = null;
							}
						} else {
							if (tuple1 != null) {
								StringBuilder temp = new StringBuilder();
								for (int k = 0; k < length2; k++) {
									temp.append(0);
								}
								write++;
								// System.out.println("Length 2 " + length2);
								bw.write(tuple1 + temp.toString());
								bw.newLine();
								tuple1 = null;
							} else {
								StringBuilder temp = new StringBuilder();
								for (int k = 0; k < length1; k++) {
									temp.append(0);
								}
								write++;
								bw.write(tuple2.substring(0, endIndex + 1) + temp.toString()
								+ tuple2.substring(endIndex + 1));
								bw.newLine();
								tuple2 = null;
							}
						}

					}
				} else {
					while ((tuple1 = br1.readLine()) != null) {
						write++;
						readCount++;
						bw.write(tuple1);
						bw.newLine();
					}
				}
				bw.close();
				mergedFiles.add(currentMergeFile);
				br1.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		writeCount += write;
		mergeTtime += (System.currentTimeMillis() - itertionStart);
		/*
		 * System.out.println("Round " + itertion + " Merging Time : " +
		 * (System.currentTimeMillis() - itertionStart) + "ms" + "(" + "~approx " +
		 * (System.currentTimeMillis() - itertionStart) / 1000.0 + "sec)");
		 */
		if (mergedFiles.size() > 1) {
			itertion++;
			mergeSort(mergedFiles, directory, startIndex, endIndex);
		} else {
			setOutputPath(currentMergeFile);
		}
	}

	public String getOutputPath() {
		return outputPath;
	}

	public static void setOutputPath(String outputPath) {
		MergeData.outputPath = outputPath;
	}

	public void performMergeSort(String directory, int startIndex, int endIndex) {
		itertion = 0;
		mergeSort(listOfFiles, directory, startIndex, endIndex);
	}

}
