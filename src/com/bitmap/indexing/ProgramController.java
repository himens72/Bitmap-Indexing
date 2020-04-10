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
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ProgramController {
	static String fileName1 = Constants.INPUT_PATH + Constants.INPUT_FILE1;
	static String fileName2 = Constants.INPUT_PATH + Constants.INPUT_FILE2;

	public static void main(String[] args) throws InterruptedException {
		String t1_employee_file ="";
		String t2_employee_file ="";
		String t1_gender_file ="";
		String t2_gender_file ="";
		String t1_department_file ="";
		String t2_department_file ="";
		System.out
		.println("****************************Cleaning Directory*********************************************");
		buildBlockDirectory(Constants.T1_EMP, "T1 Employee");
		buildBlockDirectory(Constants.T2_EMP, "T2 Employee");
		buildBlockDirectory(Constants.T1_DEPT, "T1 Department");
		buildBlockDirectory(Constants.T2_DEPT, "T2 Department");
		buildBlockDirectory(Constants.T1_GEN, "T1 Gender");
		buildBlockDirectory(Constants.T2_GEN, "T2 Gender");
		buildOutputDirectory();
		System.out.println("Diretory Cleaned");
		System.out.println("****************************TPMMS Console*********************************************");
		System.gc();
		System.out.println("Memory Size :  " + getMemorySize());
		System.out.println("Tuple Size : " + Constants.TUPLE_SIZE);
		System.gc();
		System.out.println(
				"****************************Bitmap Index for T1 Gender*********************************************");
		BuilldIndex phaseOne1 = new BuilldIndex();
		List<String> T11 = phaseOne1.sortTuple("T1", fileName1, Constants.T1_GEN, 43, 44);
		MergeData two1 = new MergeData(T11, new ArrayList<String>());
		two1.performMergeSort(Constants.T1_GEN, 0, 1);
		t1_gender_file = two1.getOutputPath();
		System.gc();
		System.out.println(
				"****************************Bitmap Index for T1 Departmemt *********************************************");
		BuilldIndex phaseOne2 = new BuilldIndex();
		List<String> T12 = phaseOne2.sortTuple("T1", fileName1, Constants.T1_DEPT, 44, 47);
		MergeData two2 = new MergeData(T12, new ArrayList<String>());
		two2.performMergeSort(Constants.T1_DEPT, 0, 3);
		System.gc();
		t1_department_file = two2.getOutputPath();
		System.out.println(
				"****************************Bitmap Index for T2 Gender*********************************************");
		BuilldIndex phaseOne21 = new BuilldIndex();
		List<String> T21 = phaseOne21.sortTuple("T2", fileName2, Constants.T2_GEN, 43, 44);
		MergeData two12 = new MergeData(T21, new ArrayList<String>());
		two12.performMergeSort(Constants.T2_GEN, 0, 1);
		t2_gender_file = two12.getOutputPath();
		System.gc();
		System.out.println(
				"****************************Bitmap Index for T2 Departmemt *********************************************");
		BuilldIndex phaseOne22 = new BuilldIndex();
		List<String> T22 = phaseOne22.sortTuple("T2", fileName2, Constants.T2_DEPT, 44, 47);
		MergeData two22 = new MergeData(T22, new ArrayList<String>());
		two22.performMergeSort(Constants.T2_DEPT, 0, 3);
		t2_department_file = two22.getOutputPath();

		System.gc();
		System.out.println(
				"****************************Bitmap Index for T1 Employee ID*********************************************");
		BuilldIndex phaseOne = new BuilldIndex();
		List<String> T13 = phaseOne.sortTuple("T1", fileName1, Constants.T1_EMP, 0, 8);
		MergeData two3 = new MergeData(T13, new ArrayList<String>());
		two3.performMergeSort(Constants.T1_EMP, 0, 8);
		t1_employee_file = two3.getOutputPath();
		System.gc();
		System.out.println(
				"****************************Bitmap Index for T2 Employee ID*********************************************");
		BuilldIndex phaseOne23 = new BuilldIndex();
		List<String> T23 = phaseOne23.sortTuple("T2", fileName2, Constants.T2_EMP, 0, 8);
		MergeData two33 = new MergeData(T23, new ArrayList<String>());
		two33.performMergeSort(Constants.T2_EMP, 0, 8);
		t2_employee_file = two33.getOutputPath();
		System.gc();
		System.out.println("T1 Employee File Path : " +t1_employee_file);
		System.out.println("T1 Department File Path : " +t1_department_file);
		System.out.println("T1 Gender File Path : " +t1_gender_file);
		System.out.println("T2 Employee File Path : " +t2_employee_file);
		System.out.println("T2 Department File Path : " +t2_department_file);
		System.out.println("T2 Gender File Path : " +t2_gender_file);
		CompressedBitmap compressedBitmap = new CompressedBitmap();
		compressedBitmap.generateBitmap(t1_employee_file, t2_employee_file);
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

	private static int getMemorySize() {
		return (int) (Runtime.getRuntime().totalMemory() / (1024 * 1024));
	}

	public static int getTotalBlocks(final int fileSize, final int blockSize) {
		return (int) Math.ceil((double) fileSize / blockSize);
	}
}
