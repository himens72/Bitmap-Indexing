/*
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

package com.bitmap.indexing;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


public class ProgramController {
	static String fileName1 = Constants.INPUT_PATH + Constants.INPUT_FILE1;
	static String fileName2 = Constants.INPUT_PATH + Constants.INPUT_FILE2;
	static long blockSize = ((Constants.TOTAL_MEMORY * 5) / (100 * 1000));
	static QuickSort quickSort = new QuickSort();

	public static void main(String[] args) throws InterruptedException {
		String t1_employee_file = "";
		String t2_employee_file = "";
		String t1_gender_file = "";
		String t2_gender_file = "";
		String t1_department_file = "";
		String t2_department_file = "";
		System.out
		.println("****************************Cleaning Directory*********************************************");
		buildBlockDirectory(Constants.T1_BLOCK_PATH, "T1 Tuples");
		buildBlockDirectory(Constants.T2_BLOCK_PATH, "T1 Tuples");
		buildBlockDirectory(Constants.T1_T2, "T1_T2");
		buildBlockDirectory(Constants.COMPRESSED_PATH, "COMPRESSED BITMAP");
		buildBlockDirectory(Constants.T1_EMP, "T1 Employee");
		buildBlockDirectory(Constants.T2_EMP, "T2 Employee");
		buildBlockDirectory(Constants.T1_DEPT, "T1 Department");
		buildBlockDirectory(Constants.T2_DEPT, "T2 Department");
		buildBlockDirectory(Constants.T1_GEN, "T1 Gender");
		buildBlockDirectory(Constants.T2_GEN, "T2 Gender");
buildOutputDirectory();
		buildOutputDirectory();
		System.out.println("Diretory Cleaned");
		System.gc();
		System.out.println("Memory Size :  " + getMemorySize());
		System.out.println("Tuple Size : " + Constants.TUPLE_SIZE);
		System.gc();
		BuildIndex buildT1 = new BuildIndex();
		List<String> T1List = buildT1.buildBlock("T1", fileName1, Constants.T1_BLOCK_PATH);
		BuildIndex buildT2 = new BuildIndex();
		List<String> T2List = buildT2.buildBlock("T2", fileName2, Constants.T2_BLOCK_PATH);

		System.out.println(
				"****************************Bitmap Index for T1 Gender*********************************************");
		BuildIndex P1T1GENDER = new BuildIndex();
		List<String> T11 = P1T1GENDER.sortTuple("T1", fileName1, Constants.T1_GEN, 43, 44);
		MergeData P2T1GENDER = new MergeData(T11, new ArrayList<String>());
		P2T1GENDER.performMergeSort(Constants.T1_GEN, 0, 1);
		t1_gender_file = P2T1GENDER.getOutputPath();
		System.out.println("Read Count " + P2T1GENDER.getReadCount());
		System.out.println("Write Count "  + (P2T1GENDER.getWriteCount() - P2T1GENDER.getWrite()));
		System.out.println("Unique Data "  + P2T1GENDER.getWrite());
		System.out.println("Time Taken to merge data  " + (P2T1GENDER.getMergeTtime()) + " ms ( ~approx "
				+ ((P2T1GENDER.getMergeTtime()) / 1000) + " sec)");
		System.out.println("Total Time   " + (P1T1GENDER.getSortingTime() + P2T1GENDER.getMergeTtime()) + " ms ( ~approx "
				+ ((P2T1GENDER.getMergeTtime() + P1T1GENDER.getSortingTime()) / 1000) + " sec)");
		System.gc();
		System.out.println(
				"****************************Bitmap Index for T1 Departmemt *********************************************");
		BuildIndex P1T1DEPT = new BuildIndex();
		List<String> T12 = P1T1DEPT.sortTuple("T1", fileName1, Constants.T1_DEPT, 44, 47);
		MergeData P2T1DEPT = new MergeData(T12, new ArrayList<String>());
		P2T1DEPT.performMergeSort(Constants.T1_DEPT, 0, 3);
		System.gc();
		t1_department_file = P2T1DEPT.getOutputPath();
		System.out.println("Read Count " + P2T1DEPT.getReadCount());
		System.out.println("Write Count "  + (P2T1DEPT.getWriteCount() - P2T1DEPT.getWrite()));
		System.out.println("Unique Data "  + P2T1DEPT.getWrite());
		System.out.println("Time Taken to merge data  " + (P2T1DEPT.getMergeTtime()) + " ms ( ~approx "
				+ ((P2T1DEPT.getMergeTtime()) / 1000) + " sec)");
		System.out.println("Total Time   " + (P1T1DEPT.getSortingTime() + P2T1DEPT.getMergeTtime()) + " ms ( ~approx "
				+ ((P2T1DEPT.getMergeTtime() + P1T1DEPT.getSortingTime()) / 1000) + " sec)");
		System.out.println(
				"****************************Bitmap Index for T2 Gender*********************************************");
		BuildIndex P1T2GENDER = new BuildIndex();
		List<String> T21 = P1T2GENDER.sortTuple("T2", fileName2, Constants.T2_GEN, 43, 44);
		MergeData P2T2GENDER = new MergeData(T21, new ArrayList<String>());
		P2T2GENDER.performMergeSort(Constants.T2_GEN, 0, 1);
		t2_gender_file = P2T2GENDER.getOutputPath();
		System.out.println("Read Count " + P2T2GENDER.getReadCount());
		System.out.println("Write Count "  + (P2T2GENDER.getWriteCount() - P2T2GENDER.getWrite()));
		System.out.println("Unique Data "  + P2T2GENDER.getWrite());
		System.out.println("Time Taken to merge data  " + (P2T2GENDER.getMergeTtime()) + " ms ( ~approx "
				+ ((P2T2GENDER.getMergeTtime()) / 1000) + " sec)");
		System.out.println("Total Time   " + (P1T2GENDER.getSortingTime() + P2T2GENDER.getMergeTtime()) + " ms ( ~approx "
				+ ((P2T2GENDER.getMergeTtime() + P1T2GENDER.getSortingTime()) / 1000) + " sec)");
		System.gc();
		System.out.println(
				"****************************Bitmap Index for T2 Departmemt *********************************************");
		BuildIndex P1T2DEPT = new BuildIndex();
		List<String> T22 = P1T2DEPT.sortTuple("T2", fileName2, Constants.T2_DEPT, 44, 47);
		MergeData P2T2DEPT = new MergeData(T22, new ArrayList<String>());
		P2T2DEPT.performMergeSort(Constants.T2_DEPT, 0, 3);
		t2_department_file = P2T2DEPT.getOutputPath();
		System.out.println("Read Count " + P2T2DEPT.getReadCount());
		System.out.println("Write Count "  + (P2T2DEPT.getWriteCount() - P2T2DEPT.getWrite()));
		System.out.println("Unique Data "  + P2T2DEPT.getWrite());
		System.out.println("Time Taken to merge data  " + (P2T2DEPT.getMergeTtime()) + " ms ( ~approx "
				+ ((P2T2DEPT.getMergeTtime()) / 1000) + " sec)");
		System.out.println("Total Time   " + (P1T2DEPT.getSortingTime() + P2T2DEPT.getMergeTtime()) + " ms ( ~approx "
				+ ((P2T2DEPT.getMergeTtime() + P1T2DEPT.getSortingTime()) / 1000) + " sec)");
		System.gc();
		System.out.println(
				"****************************Bitmap Index for T1 Employee ID*********************************************");
		BuildIndex P1T1EMP = new BuildIndex();
		List<String> T13 = P1T1EMP.sortTuple("T1", fileName1, Constants.T1_EMP, 0, 8);
		MergeData P2T1EMP = new MergeData(T13, new ArrayList<String>());
		P2T1EMP.performMergeSort(Constants.T1_EMP, 0, 8);
		t1_employee_file = P2T1EMP.getOutputPath();
		System.out.println("Read Count " + P2T1EMP.getReadCount());
		System.out.println("Write Count "  + (P2T1EMP.getWriteCount() - P2T1EMP.getWrite()));
		System.out.println("Unique Data "  + P2T1EMP.getWrite());
		System.out.println("Time Taken to merge data  " + (P2T1EMP.getMergeTtime()) + " ms ( ~approx "
				+ ((P2T1EMP.getMergeTtime()) / 1000) + " sec)");
		System.out.println("Total Time   " + (P1T1EMP.getSortingTime() + P2T1EMP.getMergeTtime()) + " ms ( ~approx "
				+ ((P2T1EMP.getMergeTtime() + P1T1EMP.getSortingTime()) / 1000) + " sec)");
		System.gc();
		System.out.println(
				"****************************Bitmap Index for T2 Employee ID*********************************************");
		BuildIndex P1T2EMP = new BuildIndex();
		List<String> T23 = P1T2EMP.sortTuple("T2", fileName2, Constants.T2_EMP, 0, 8);
		MergeData P2T2EMP = new MergeData(T23, new ArrayList<String>());
		P2T2EMP.performMergeSort(Constants.T2_EMP, 0, 8);
		t2_employee_file = P2T2EMP.getOutputPath();
		System.out.println("Read Count " + P2T2EMP.getReadCount());
		System.out.println("Write Count "  + (P2T2EMP.getWriteCount() - P2T2EMP.getWrite()));
		System.out.println("Unique Data "  + P2T2EMP.getWrite());
		System.out.println("Time Taken to merge data  " + (P2T2EMP.getMergeTtime()) + " ms ( ~approx "
				+ ((P2T2EMP.getMergeTtime()) / 1000) + " sec)");
		System.out.println("Total Time   " + (P1T2EMP.getSortingTime() + P2T2EMP.getMergeTtime()) + " ms ( ~approx "
				+ ((P2T2EMP.getMergeTtime() + P1T2EMP.getSortingTime()) / 1000) + " sec)");
		System.gc();
		System.out
		.println("****************************Path  List*********************************************");

		System.out.println("T1 Employee File Path : " + t1_employee_file);
		System.out.println("T1 Department File Path : " + t1_department_file);
		System.out.println("T1 Gender File Path : " + t1_gender_file);
		System.out.println("T2 Employee File Path : " + t2_employee_file);
		System.out.println("T2 Department File Path : " + t2_department_file);
		System.out.println("T2 Gender File Path : " + t2_gender_file);
		CompressedBitmap compressedBitmap = new CompressedBitmap();
		System.out
		.println("****************************Compressed Bitmap Output*********************************************");

		compressedBitmap.generateBitmap(t1_employee_file, "T1_EMPLOYEE", 8);
		compressedBitmap.generateBitmap(t2_employee_file, "T2_EMPLOYEE", 8);
		compressedBitmap.generateBitmap(t1_department_file, "T1_DEPARTMENT", 3);
		compressedBitmap.generateBitmap(t2_department_file, "T2_DEPARTMENT", 3);
		compressedBitmap.generateBitmap(t1_gender_file, "T1_GENDER", 1);
		compressedBitmap.generateBitmap(t2_gender_file, "T2_GENDER", 1);
		System.out
		.println("****************************List of Unique Data*********************************************");
		System.out.println("Read Count " + (P2T1EMP.getWrite() + P2T2EMP.getWrite()));
		mergeSort(t1_employee_file, t2_employee_file, T1List, T2List);
		/*
		 * List<String> listT1T2 = new ArrayList<String>();
		 * listT1T2.add(t1_employee_file); listT1T2.add(t2_employee_file); MergeData
		 * mergeDataT1T2 = new MergeData(listT1T2, new ArrayList<String>());
		 * mergeDataT1T2.performMergeSort(Constants.T1_T2, 0, 8);
		 */
	}

	private static int getMemorySize() {
		return (int) (Runtime.getRuntime().totalMemory() / (1024 * 1024));
	}

	public static int getTotalBlocks(final int fileSize, final int blockSize) {
		return (int) Math.ceil((double) fileSize / blockSize);
	}

	public static void mergeSort(String file1, String file2, List<String> T1List, List<String> T2List) {
		long itertionStart = System.currentTimeMillis();
		long write = 0;
		try {
			BufferedReader br1 = new BufferedReader(new FileReader(file1));
			BufferedReader br2 = new BufferedReader(new FileReader(file2));
			BufferedWriter bw = new BufferedWriter(new FileWriter(Constants.OUTPUT_PATH +"output.txt"));
			String tuple1 = null;
			String tuple2 = null;
			long length1 = 0;
			long length2 = 0;
			while (true) {
				if (tuple1 == null) {
					tuple1 = br1.readLine();
					length1 = tuple1 == null || tuple1.trim().length() == 0 ? length1
							: tuple1.substring(8 + 1).trim().length();
				}
				if (tuple2 == null) {
					tuple2 = br2.readLine();
					length2 = tuple2 == null || tuple2.trim().length() == 0 ? length2
							: tuple2.substring(8 + 1).trim().length();
				}
				if (tuple1 == null && tuple2 == null) {
					break;
				}
				if (tuple1 != null && tuple2 != null) {
					String id1 = tuple1.substring(0, 8);
					String id2 = tuple2.substring(0, 8);
					if (id1.equals(id2)) {
						ArrayList<String> subList = new ArrayList<String>();
						List<Integer> temp = findWordUpgrade(tuple1.substring(8 + 1), "1");
						int count = 0;
						while (!temp.isEmpty()) {
							int tmpvalue = temp.get(0);
							// System.out.println("File Number " + tmpvalue);
							temp.remove(0);
							count++;
							while (!temp.isEmpty()) {
								if (temp.get(0) == tmpvalue) {
									temp.remove(0);
									count++;
								} else
									break;
							}
							// System.out.println("Count " + count);
							subList.add(getLatestTuple(id1, tmpvalue, "T1", count, Constants.T1_BLOCK_PATH));
							count = 0;
						}
						List<Integer> temp1 = findWordUpgrade(tuple2.substring(8 + 1), "1");
						int count1 = 0;
						while (!temp1.isEmpty()) {
							int tmpvalue = temp1.get(0);
							temp1.remove(0);
							count1++;
							while (!temp1.isEmpty()) {
								if (temp1.get(0) == tmpvalue) {
									temp1.remove(0);
									count1++;
								} else
									break;
							}
							subList.add(getLatestTuple(id1, tmpvalue, "T2", count1, Constants.T2_BLOCK_PATH));
							count1 = 0;
						}
						subList = quickSort.executeQuickSort(subList, 0, 18);
						bw.write(subList.get(subList.size() - 1));
						bw.newLine();
						tuple1 = null;
						tuple2 = null;
					} else if (id1.compareToIgnoreCase(id2) > 0) {
						ArrayList<String> subList = new ArrayList<String>();
						List<Integer> temp1 = findWordUpgrade(tuple2.substring(8 + 1), "1");
						// System.out.println(id2 + " : Case 2 " + temp1);
						int count1 = 0;
						while (!temp1.isEmpty()) {
							int tmpvalue = temp1.get(0);
							// System.out.println("File Number " + tmpvalue);
							temp1.remove(0);
							count1++;
							while (!temp1.isEmpty()) {
								if (temp1.get(0) == tmpvalue) {
									temp1.remove(0);
									count1++;
								} else
									break;
							}
							// System.out.println("Dulicates " + count1);
							subList.add(getLatestTuple(id2, tmpvalue, "T2", count1, Constants.T2_BLOCK_PATH));
							count1 = 0;
						}
						// System.out.println(subList.size());
						subList = quickSort.executeQuickSort(subList, 0, 18);
						write++;
						bw.write(subList.get(subList.size() - 1));
						bw.newLine();
						tuple2 = null;
					} else if (id1.compareToIgnoreCase(id2) < 0) {
						ArrayList<String> subList = new ArrayList<String>();
						List<Integer> temp = findWordUpgrade(tuple1.substring(8 + 1), "1");
						int count = 0;
						while (!temp.isEmpty()) {
							int tmpvalue = temp.get(0);
							temp.remove(0);
							count++;
							while (!temp.isEmpty()) {
								if (temp.get(0) == tmpvalue) {
									temp.remove(0);
									count++;
								} else
									break;
							}
							subList.add(getLatestTuple(id1, tmpvalue, "T1", count, Constants.T1_BLOCK_PATH));
							count = 0;
						}
						subList = quickSort.executeQuickSort(subList, 0, 18);
						write++;
						bw.write(subList.get(subList.size() - 1));
						bw.newLine();
						tuple1 = null;
					}
				} else {
					if (tuple1 != null) {
						ArrayList<String> subList = new ArrayList<String>();
						List<Integer> temp = findWordUpgrade(tuple1.substring(8 + 1), "1");
						int count = 0;
						while (!temp.isEmpty()) {
							int tmpvalue = temp.get(0);
							temp.remove(0);
							count++;
							while (!temp.isEmpty()) {
								if (temp.get(0) == tmpvalue) {
									temp.remove(0);
									count++;
								} else
									break;
							}
							subList.add(getLatestTuple(tuple1.substring(0, 8), tmpvalue, "T1", count,
									Constants.T1_BLOCK_PATH));
							count = 0;
						}
						subList = quickSort.executeQuickSort(subList, 0, 18);
						write++;
						bw.write(subList.get(subList.size() - 1));
						bw.newLine();
						tuple1 = null;
					} else {
						StringBuilder temp = new StringBuilder();
						ArrayList<String> subList = new ArrayList<String>();
						List<Integer> temp1 = findWordUpgrade(tuple2.substring(8 + 1), "1");
						int count1 = 0;
						while (!temp1.isEmpty()) {
							int tmpvalue = temp1.get(0);
							temp1.remove(0);
							count1++;
							while (!temp1.isEmpty()) {
								if (temp1.get(0) == tmpvalue) {
									temp1.remove(0);
									count1++;
								} else
									break;
							}
							subList.add(getLatestTuple(tuple2.substring(0, 8), tmpvalue, "T2", count1,
									Constants.T2_BLOCK_PATH));
							count1 = 0;
						}
						subList = quickSort.executeQuickSort(subList, 0, 18);
						write++;
						bw.write(subList.get(subList.size() - 1));
						bw.newLine();
						tuple2 = null;
					}
				}
			}
			bw.close();
			br1.close();
			br2.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		System.out.println("Write Count " + write);
		System.out.println(" Final Time to return output : " + (System.currentTimeMillis() - itertionStart) + "ms" + "("
				+ "~approx " + (System.currentTimeMillis() - itertionStart) / 1000.0 + " sec)");
	}

	public static String getLatestTuple(String empId, int fileNum, String tuple, int count, String directory) {
		String filePath = directory + "/Block-" + fileNum;
		// System.out.println("File Path " + filePath);
		BufferedReader br1 = null;
		try {
			br1 = new BufferedReader(new FileReader(filePath));
			String record = "";
			ArrayList<String> subList = new ArrayList<String>();
			while ((record = br1.readLine()) != null) {
				if (record.substring(0, 8).equals(empId.trim())) {
					subList.add(record);
					//System.out.println("True");
				}
				if (subList.size() == count)
					break;
			}
			subList = quickSort.executeQuickSort(subList, 0, 18);
			br1.close();
			return subList.get(subList.size() - 1);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return "";
	}

	public static List<Integer> findWordUpgrade(String textString, String word) {
		List<Integer> indexes = new ArrayList<Integer>();
		int wordLength = 0;
		int index = 0;
		while (index != -1) {
			index = textString.indexOf(word, index + wordLength);
			if (index != -1) {
				indexes.add((int) (index / blockSize));
			}
			wordLength = word.length();
		}
		return indexes;
	}

	public static void buildOutputDirectory() {
		File outputDir = new File(Constants.OUTPUT_PATH);
		if (!outputDir.exists()) {
			System.out.println("Output Directory Created : " + outputDir.mkdir());
		} else if (outputDir.isFile()) {
		} else {
			String fileList[] = outputDir.list();
			for (int i = 0; i < fileList.length; i++) {
				if (fileList[i].trim().length() >= 1) {
					File currentBlockFiles = new File(outputDir.getPath(), fileList[i]);
					currentBlockFiles.delete();
				}
			}
			System.out.println("Output Directory Deleted :- " + outputDir.delete());
			System.out.println("Output Directory Created :- " + outputDir.mkdir());
		}
	}

	public static void buildBlockDirectory(String folderPath, String folderType) {
		File deleteBlocks = new File(folderPath);
		if (!deleteBlocks.exists()) {
			System.out.println(folderType + " Directory Created : " + deleteBlocks.mkdir());
		} else if (deleteBlocks.isFile()) {
		} else {
			String fileList[] = deleteBlocks.list();
			for (int i = 0; i < fileList.length; i++) {
				if (fileList[i].trim().length() >= 1) {
					File currentBlockFiles = new File(deleteBlocks.getPath(), fileList[i]);
					currentBlockFiles.delete();
				}
			}
			System.out.println(folderType + " Directory Deleted :- " + deleteBlocks.delete());
			System.out.println(folderType + " Directory Created :- " + deleteBlocks.mkdir());
		}
	}

}
