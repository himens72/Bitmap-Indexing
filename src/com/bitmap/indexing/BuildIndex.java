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
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

public class BuildIndex {
	QuickSort quickSort = new QuickSort();
	static int recordCount;
	long sortingTime = 0;
	long firstFile = 0;
	long lastFile = 0;
	ArrayList<String> subListName = new ArrayList<>();
	int currentBlock = 0;
	BufferedReader br;

	public ArrayList<String> sortTuple(String tuple, String path, String directory, int startIndex, int endIndex) {
		try {
			br = new BufferedReader(new FileReader(path));
			boolean run = true;

			long blockSize = ((Constants.TOTAL_MEMORY * 7) / (100 * 1000));
			firstFile = blockSize;
			long begin = System.currentTimeMillis();
			while (run) {
				String record = null;
				ArrayList<String> uniqueList = new ArrayList<>();
				ArrayList<String> sortedList = new ArrayList<>();
				long[][] bitmap = new long[(int) blockSize][(int) (blockSize + 1)];
				long data_count = 0;
				int subListRecord = 0;
				while ((record = br.readLine()) != null) {
					String empId = record.substring(startIndex, endIndex);
					if (uniqueList.contains(empId)) {
						bitmap[uniqueList.indexOf(empId)][(int) (data_count + 1)] = 1;
					} else {
						bitmap[uniqueList.size()][0] = Long.parseLong(empId);
						bitmap[uniqueList.size()][(int) (data_count + 1)] = 1;
						uniqueList.add(empId);
						sortedList.add(empId);
					}
					recordCount++;
					subListRecord++;
					++data_count;
					if (data_count == blockSize) {
						data_count = 0;
						break;
					}
				}
				lastFile = data_count;
				sortedList = quickSort.executeQuickSort(sortedList, startIndex, endIndex);
				String outputFile = directory + "/Block-" + currentBlock;
				BufferedWriter write = new BufferedWriter(new FileWriter(outputFile));
				for (int i = 0; i < sortedList.size(); i++) {
					StringBuilder tempBuilder = new StringBuilder();
					int index = uniqueList.indexOf(sortedList.get(i));
					tempBuilder.append(sortedList.get(i) + ":");
					for (int j = 1; j <= subListRecord; j++) {
						tempBuilder.append(bitmap[index][j]);
					}
					write.write(tempBuilder.toString());
					tempBuilder = new StringBuilder();
					if (i < uniqueList.size() - 1)
						write.newLine();
				}
				write.close();
				subListName.add(outputFile);
				if (record == null)
					break;
				currentBlock++;
			}
			sortingTime += (System.currentTimeMillis() - begin);
			System.out.println("Time taken to create block " + tuple + " : " + (System.currentTimeMillis() - begin)
					+ "ms (" + (System.currentTimeMillis() - begin) / 1000.0 + "sec)");
			System.out.println("First File " + firstFile);
			System.out.println("Last File " + lastFile);
			System.gc();
		} catch (FileNotFoundException e) {
			System.out.println("The File doesn't Exist : " + e);
			System.exit(1);
		} catch (IOException e) {
			e.printStackTrace();
			System.exit(1);
		}
		return subListName;
	}

	public long getSortingTime() {
		return sortingTime;
	}

	public void setSortingTime(long sortingTime) {
		this.sortingTime = sortingTime;
	}

	public int getRecordCount() {
		return recordCount;
	}

	public static void setRecordCount(int recordCount) {
		BuildIndex.recordCount = recordCount;
	}

	public int getCurrentBlock() {
		return currentBlock;
	}

	public void setCurrentBlock(int currentBlock) {
		this.currentBlock = currentBlock;
	}

	public static int getTotalBlocks(int fileSize) {
		return (int) Math.ceil((double) fileSize / Constants.BLOCK_SIZE);
	}
}