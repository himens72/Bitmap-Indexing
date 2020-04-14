package com.bitmap.indexing;

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
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

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
			sortingTime = 0;
			long blockSize = ((Constants.TOTAL_MEMORY * 5) / (100 * 1000));
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

	public ArrayList<String> buildBlock(String tuple, String path, String directory) {
		long data_count = 0;
		ArrayList<String> temp = new ArrayList<>();
		try {
			br = new BufferedReader(new FileReader(path));
			boolean run = true;
			long blockSize = ((Constants.TOTAL_MEMORY * 5) / (100 * 1000));
			while (run) {
				String record = null;
				ArrayList<String> subList = new ArrayList<>();

				while ((record = br.readLine()) != null) {
					subList.add(record);
					recordCount++;
					++data_count;
					if (data_count == blockSize) {
						data_count = 0;
						break;
					}
				}
				String outputFile = directory + "/Block-" + currentBlock;
				BufferedWriter write = new BufferedWriter(new FileWriter(outputFile));
				for (int i = 0; i < subList.size(); i++) {
					write.write(subList.get(i));
					write.newLine();
				}
				write.close();
				temp.add(outputFile);

				if (record == null)
					break;
				currentBlock++;
			}
		} catch (FileNotFoundException e) {
			System.out.println("The File doesn't Exist : " + path);
			System.exit(1);
		} catch (IOException e) {
			e.printStackTrace();
			System.exit(1);
		}
		return temp;
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
