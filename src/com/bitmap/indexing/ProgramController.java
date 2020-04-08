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
		System.out
		.println("****************************Cleaning Directory*********************************************");
		buildBlockDirectory(Constants.T1_EMP, "T1 Employee");
		buildBlockDirectory(Constants.T2_EMP, "T2 Employee");
		buildBlockDirectory(Constants.T1_DEPT, "T1 Department");
		buildBlockDirectory(Constants.T2_DEPT, "T2 Department");
		buildBlockDirectory(Constants.T1_GEN, "T1 Gender");
		buildBlockDirectory(Constants.T1_GEN, "T2 Gender");
		buildOutputDirectory();
		System.out.println("Diretory Cleaned");
		System.out.println("****************************TPMMS Console*********************************************");
		System.gc();
		System.out.println("Memory Size :  " + getMemorySize());
		System.out.println("Tuple Size : " + Constants.TUPLE_SIZE);

		System.out.println(
				"****************************Bitmap Index for T1 Gender*********************************************");
		PhaseOne phaseOne1 = new PhaseOne();
		List<String> T11 = phaseOne1.sortTuple("T1", fileName1, Constants.T1_GEN, 43, 44);
		PhaseTwo two1 = new PhaseTwo(T11, new ArrayList<String>());
		two1.performMergeSort(Constants.T1_GEN, 0, 1);

		System.out.println(
				"****************************Bitmap Index for T1 Departmemt *********************************************");
		PhaseOne phaseOne2 = new PhaseOne();
		List<String> T12 = phaseOne2.sortTuple("T1", fileName1, Constants.T1_DEPT, 44, 47);
		PhaseTwo two2 = new PhaseTwo(T12, new ArrayList<String>());
		two2.performMergeSort(Constants.T1_DEPT, 0, 3);

		System.out.println(
				"****************************Bitmap Index for T1 Employee ID*********************************************");
		PhaseOne phaseOne = new PhaseOne();
		List<String> T13 = phaseOne.sortTuple("T1", fileName1, Constants.T1_EMP, 0, 8);
		PhaseTwo two3 = new PhaseTwo(T13, new ArrayList<String>());
		two3.performMergeSort(Constants.T1_EMP, 0, 8);
		System.out.println(
				"****************************Bitmap Index for T2 Gender*********************************************");
		PhaseOne phaseOne21 = new PhaseOne();
		List<String> T21 = phaseOne21.sortTuple("T2", fileName2, Constants.T2_GEN, 43, 44);
		PhaseTwo two12 = new PhaseTwo(T21, new ArrayList<String>());
		two12.performMergeSort(Constants.T2_GEN, 0, 1);

		System.out.println(
				"****************************Bitmap Index for T2 Departmemt *********************************************");
		PhaseOne phaseOne22 = new PhaseOne();
		List<String> T22 = phaseOne22.sortTuple("T2", fileName2, Constants.T2_DEPT, 44, 47);
		PhaseTwo two22 = new PhaseTwo(T22, new ArrayList<String>());
		two22.performMergeSort(Constants.T2_DEPT, 0, 3);

		System.out.println(
				"****************************Bitmap Index for T2 Employee ID*********************************************");
		PhaseOne phaseOne23 = new PhaseOne();
		List<String> T23 = phaseOne23.sortTuple("T2", fileName2, Constants.T2_EMP, 0, 8);
		PhaseTwo two33 = new PhaseTwo(T23, new ArrayList<String>());
		two33.performMergeSort(Constants.T2_EMP, 0, 8);
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
