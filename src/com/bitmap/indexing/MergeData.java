package com.bitmap.indexing;

/*
1.	https://www.geeksforgeeks.org/java-program-for-quicksort/
2.	https://examples.javacodegeeks.com/core-java/nio/bytebuffer/write-append-to-file-with-byte-buffer/
3.	https://www.geeksforgeeks.org/bytebuffer-get-method-in-java-with-examples/
4.	http://rosettacode.org/wiki/Binary_search#Java
5.	https://en.wikipedia.org/wiki/Sorting_algorithm
6.	https://crunchify.com/increase-eclipse-memory-size-to-avoid-oom-on-startup/
7.	https://github.com/sagarvetal/ADB_Project_1_TPMMS
8.	https://howtodoinjava.com/java/io/how-to-check-if-file-exists-in-java/
9.	https://www.javadevjournal.com/java/java-copy-file-directory/
10.	https://mkyong.com/java/how-to-delete-directory-in-java/
11.	https://www.tutorialspoint.com/how-to-create-a-new-directory-by-using-file-object-in-java
12.	https://www.tutorialspoint.com/java/io/file_isfile.htm
13.	http://www.mathcs.emory.edu/~cheung/Courses/554/Syllabus/4-query-exec/2-pass=TPMMS.html
14.	http://www.mathcs.emory.edu/~cheung/Courses/554/Syllabus/4-query-exec/TPMMS=join2.html
*/
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.List;

public class MergeData {
	long mergeTtime = 0;
	int readCount = 0;
	public int getReadCount() {
		return readCount;
	}

	public void setReadCount(int readCount) {
		this.readCount = readCount;
	}

	public int getWriteCount() {
		return WriteCount;
	}

	public void setWriteCount(int writeCount) {
		WriteCount = writeCount;
	}

	int WriteCount = 0;

	public long getMergeTtime() {
		return mergeTtime;
	}

	public void setMergeTtime(long mergeTtime) {
		this.mergeTtime = mergeTtime;
	}

	int tupleCount1 = 1;

	public int getTupleCount1() {
		return tupleCount1;
	}

	public void setTupleCount1(int tupleCount1) {
		this.tupleCount1 = tupleCount1;
	}

	public int getTupleCount2() {
		return tupleCount2;
	}

	public void setTupleCount2(int tupleCount2) {
		this.tupleCount2 = tupleCount2;
	}

	int tupleCount2 = 1;

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
	}

	public void mergeSort(List<String> blockList, String directory, int startIndex, int endIndex) {
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
				String currentTuple = "";
				if (br2 != null) {
					while (true) {
						if (tuple1 == null) {
							tuple1 = br1.readLine();
							tupleCount1++;
							length1 = tuple1 == null || tuple1.trim().length() == 0 ? length1
									: tuple1.substring(endIndex + 1).trim().length();
							if (tupleCount1 == 40) {
								++readCount;
								tupleCount1 = 0;
							}
						}
						if (tuple2 == null) {
							tuple2 = br2.readLine();
							// System.out.println("Here " + tuple2);
							length2 = tuple2 == null || tuple2.trim().length() == 0 ? length2
									: tuple2.substring(endIndex + 1).trim().length();
							tupleCount2++;
							if (tupleCount2 == 40) {
								++readCount;
								tupleCount2 = 0;
							}
						}
						if (tuple1 == null && tuple2 == null) {
							break;
						}
						if (tuple1 != null && tuple2 != null) {
							String id1 = tuple1.substring(0, endIndex);
							String id2 = tuple2.substring(0, endIndex);
							if (id1.equals(id2)) {
								bw.write(id1 + ":" + tuple1.substring(endIndex + 1) + tuple2.substring(endIndex + 1));
								bw.newLine();
								tuple1 = null;
								tuple2 = null;
							} else if (id1.compareToIgnoreCase(id2) > 0) {// id1 < id2
								StringBuilder temp = new StringBuilder();
								for (int k = 0; k < length1; k++) {
									temp.append(0);
								}
								bw.write(tuple2.substring(0, endIndex + 1) + temp.toString()
										+ tuple2.substring(endIndex + 1));
								bw.newLine();
								tuple2 = null;
							} else if (id1.compareToIgnoreCase(id2) < 0) { // id1 > id2
								StringBuilder temp = new StringBuilder();
								for (int k = 0; k < length2; k++) {
									temp.append(0);
								}
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
								// System.out.println("Length 2 " + length2);
								bw.write(tuple1 + temp.toString());
								bw.newLine();
								tuple1 = null;
							} else {
								StringBuilder temp = new StringBuilder();
								for (int k = 0; k < length1; k++) {
									temp.append(0);
								}
								bw.write(tuple2.substring(0, endIndex + 1) + temp.toString()
										+ tuple2.substring(endIndex + 1));
								bw.newLine();
								tuple2 = null;
							}
						}

					}
				} else {
					while ((tuple1 = br1.readLine()) != null) {
						bw.write(tuple1);
						bw.newLine();
					}
				}
				bw.write(currentTuple);
				bw.close();
				mergedFiles.add(currentMergeFile);
				br1.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		mergeTtime += (System.currentTimeMillis() - itertionStart);
		System.out.println("Round " + itertion + " Merging Time : " + (System.currentTimeMillis() - itertionStart)
				+ "ms" + "(" + "~approx " + (System.currentTimeMillis() - itertionStart) / 1000.0 + "sec)");

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
