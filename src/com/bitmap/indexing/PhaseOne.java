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
import java.util.Iterator;
import java.util.Map;

import sun.font.CreatedFontTracker;

public class PhaseOne {
	QuickSort quickSort = new QuickSort();
	static int recordCount;
	long sortingTime = 0;
	long firstFile = 0;
	long lastFile = 0;
	ArrayList<String> subListName = new ArrayList<>();
	int currentBlock = 0;
	BufferedReader br;

	public ArrayList<String> sortTuple(String tuple, String path) {
		try {
			br = new BufferedReader(new FileReader(path));
			boolean run = true;

			long blockSize = ((Constants.TOTAL_MEMORY * 3) / (100 * 10)); // Using 10% memory for reading data
								// from disk
			firstFile = blockSize;
			long begin = System.currentTimeMillis();
			while (run) {
				String record = null;
				//ArrayList<String> subList = new ArrayList<String>();
				ArrayList<String> uniqueList = new ArrayList<>();
				ArrayList<String> sortedList = new ArrayList<>();
				HashMap<String, ArrayList<Integer>> bitmap = new HashMap<String, ArrayList<Integer>>(); 
				long data_count = 0;
				int subListRecord = 0;
				while ((record = br.readLine()) != null) {
					//subList.add(record);
					String empId = record.substring(0, 8);
					if (uniqueList.contains(empId)) {
						ArrayList<Integer> temp = bitmap.get(empId);
						temp.add(recordCount);
						bitmap.replace(empId, temp);
					} else {
						ArrayList<Integer> temp = new ArrayList<Integer>();
						temp.add(recordCount);
						bitmap.put(empId, temp);
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
				sortedList = quickSort.executeQuickSort(sortedList);				
				String outputFile = Constants.BLOCK_PATH + "/Block-" + currentBlock;
				BufferedWriter write = new BufferedWriter(new FileWriter(outputFile));
				
				for (int i = 0; i < sortedList.size(); i++) {
					StringBuilder tempBuilder = new StringBuilder();
					String currentEmp = sortedList.get(i);
					ArrayList<Integer> curremtEmpIndex = bitmap.get(currentEmp);
					tempBuilder.append(currentEmp + ":");
					for (int j = 0; j < curremtEmpIndex.size(); j++) {
						tempBuilder.append(curremtEmpIndex.get(j)+",");
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
			System.out.println("Time taken by Phase 1 for " + tuple + " : " + (System.currentTimeMillis() - begin)
					+ "ms (" + (System.currentTimeMillis() - begin) / 1000.0 + "sec)");
			System.out.println("First File " + firstFile);
			System.out.println("Last File " + lastFile);
			System.gc();
			mergeIndex();
		} catch (FileNotFoundException e) {
			System.out.println("The File doesn't Exist : " + path);
			System.exit(1);
		} catch (IOException e) {
			e.printStackTrace();
			System.exit(1);
		}
		return subListName;
	}
	public void mergeIndex() {
		try {
			long begin = System.currentTimeMillis();
			BufferedReader br1 = null;
			BufferedReader br2 = null;
			BufferedWriter bw = new BufferedWriter(new FileWriter(Constants.BLOCK_PATH + "/BITMAP_INDEX_T1"));
			if (subListName.size() == 1) {
				br1 = new BufferedReader(new FileReader(subListName.get(0)));
				String record1 = "";
				while ((record1 = br1.readLine()) != null) {
					bw.write(record1);
					bw.newLine();
				}
			} else {
				for (int i = 0; i	 < subListName.size() - 1; i++) {
					br1 = new BufferedReader(new FileReader(subListName.get(i)));
					System.out.println((System.currentTimeMillis() - begin) +  " " + subListName.get(i));
					String record1 = "";
					while ((record1 = br1.readLine()) != null) {
							StringBuilder currentBitmap = new StringBuilder();
							currentBitmap.append(record1.substring(0, 8 + 1));
							currentBitmap.append(record1.substring(8 + 1));
							String tempRecord1 = record1.substring(0, 8);
							for (int j = i + 1; j < subListName.size(); j++) {
								br2 = new BufferedReader(new FileReader(subListName.get(j)));
								BufferedWriter tempBW = new BufferedWriter(
										new FileWriter(Constants.BLOCK_PATH + "/temp"));
								String record2 = "";
								boolean flag = false;
								while ((record2 = br2.readLine()) != null) {
									String tempRecord2 = record2.substring(0, 8);
									if(flag) {
										tempBW.write(record2);
										tempBW.newLine();
									} else if (tempRecord1.trim().equals(tempRecord2)) {
										currentBitmap.append(record2.substring(8 + 1));
										flag = true;
									} else if (tempRecord1.compareToIgnoreCase(tempRecord2) > 0) {
										tempBW.write(record2);
										tempBW.newLine();
									} else if (tempRecord1.compareToIgnoreCase(tempRecord2) < 0) {
										break;
									} 
								}
								tempBW.close();
								br2.close();
								if(flag) {
									File file1 = new File(subListName.get(j));
									file1.delete();
									File file2 = new File(Constants.BLOCK_PATH + "/temp");
									boolean successful = file2.renameTo(file1);										
								} else {
									File file2 = new File(Constants.BLOCK_PATH + "/temp");
									file2.delete();
								}									
							}
							bw.write(currentBitmap.toString());
							bw.newLine();
							currentBitmap = new StringBuilder();
					}
					br1.close();
				}
				br1 = new BufferedReader(new FileReader(subListName.get(subListName.size() - 1)));
				String record1 = "";
				while ((record1 = br1.readLine()) != null) {
					if (!record1.substring(0, 7).equals("checked")) {
						StringBuilder temp = new StringBuilder();
						temp.append(record1.substring(0, 9));
						for (int k = 1; k <= (subListName.size() - 1) * firstFile; k++)
							temp.append(0);
						temp.append(record1.substring(9));
						bw.write(temp.toString());
						bw.newLine();
						temp = new StringBuilder();
					}
				}
			}
			bw.close();
			System.out.println("Time to Merge : " +(System.currentTimeMillis() - begin));
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public ArrayList<String> oldSortTuple(String tuple, String path) {
		try {
			br = new BufferedReader(new FileReader(path));
			boolean run = true;

			long blockSize = (Constants.TOTAL_MEMORY / (100 * 1000)); // Using 10% memory for reading data
								// from disk
			firstFile = blockSize;
			long begin = System.currentTimeMillis();
			while (run) {
				String record = null;
				//ArrayList<String> subList = new ArrayList<String>();
				ArrayList<String> uniqueList = new ArrayList<>();
				ArrayList<String> sortedList = new ArrayList<>();
				long[][] bitmap = new long[(int) blockSize][(int) (blockSize + 1)];
				long data_count = 0;
				int subListRecord = 0;
				while ((record = br.readLine()) != null) {
					//subList.add(record);
					String empId = record.substring(0, 8);
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
				sortedList = quickSort.executeQuickSort(sortedList);				
				String outputFile = Constants.BLOCK_PATH + "/Block-" + currentBlock;
				BufferedWriter write = new BufferedWriter(new FileWriter(outputFile));
				/*
				 * for (int i = 0; i < uniqueList.size(); i++) { StringBuilder tempBuilder = new
				 * StringBuilder(); tempBuilder.append(bitmap[i][0] + ":"); for (int j = 1; j <=
				 * subList.size(); j++) { tempBuilder.append(bitmap[i][j]); }
				 * write.write(tempBuilder.toString()); tempBuilder = new StringBuilder(); if (i
				 * < uniqueList.size() - 1) write.newLine(); }
				 */
				
				for (int i = 0; i < sortedList.size(); i++) {
					StringBuilder tempBuilder = new StringBuilder();
					int index = uniqueList.indexOf(sortedList.get(i));
					tempBuilder.append(bitmap[index][0] + ":");
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
			System.out.println("Time taken by Phase 1 for " + tuple + " : " + (System.currentTimeMillis() - begin)
					+ "ms (" + (System.currentTimeMillis() - begin) / 1000.0 + "sec)");
			System.out.println("First File " + firstFile);
			System.out.println("Last File " + lastFile);
			System.gc();
			mergeIndex();
		} catch (FileNotFoundException e) {
			System.out.println("The File doesn't Exist : " + path);
			System.exit(1);
		} catch (IOException e) {
			e.printStackTrace();
			System.exit(1);
		}
		return subListName;
	}
	public void oldv1_mergeIndex() {
		try {
			long begin = System.currentTimeMillis();
			BufferedReader br1 = null;
			BufferedReader br2 = null;
			BufferedWriter bw = new BufferedWriter(new FileWriter(Constants.BLOCK_PATH + "/BITMAP_INDEX_T1"));
			if (subListName.size() == 1) {
				br1 = new BufferedReader(new FileReader(subListName.get(0)));
				String record1 = "";
				while ((record1 = br1.readLine()) != null) {
					bw.write(record1);
					bw.newLine();
				}
			} else {
				for (int i = 0; i < subListName.size() - 1; i++) {
					br1 = new BufferedReader(new FileReader(subListName.get(i)));
					System.out.println((System.currentTimeMillis() - begin) +  " " + subListName.get(i));
					String record1 = "";
					while ((record1 = br1.readLine()) != null) {
						if (!record1.substring(0, 7).equals("checked")) {
							StringBuilder currentBitmap = new StringBuilder();
							currentBitmap.append(record1.substring(0, 8 + 1));
							for (int k = 1; k <= i * firstFile; k++)
								currentBitmap.append(0);
							currentBitmap.append(record1.substring(8 + 1));
							for (int j = i + 1; j < subListName.size(); j++) {
								br2 = new BufferedReader(new FileReader(subListName.get(j)));
								BufferedWriter tempBW = new BufferedWriter(
										new FileWriter(Constants.BLOCK_PATH + "/temp"));
								String record2 = "";
								boolean flag = false;
								long size = lastFile;
								
								while ((record2 = br2.readLine()) != null) {
									boolean isOccured = record2.substring(0, 7).equals("checked") ? true : false;
									String tempRecord = record2.substring(0, 7).equals("checked") ? record2.substring(7 + 0,7 + 8) : record2.substring(0, 8); 
									int tempBitIndex = record2.substring(0, 7).equals("checked") ? 7 + 8 - 0 + 1: 8 - 0 + 1;
							//		System.out.println(record1.substring(0, 8) +" -  "  +tempRecord);
								//	System.out.println(isOccured + " " + tempRecord +  " " +tempBitIndex);
									if(flag) {
										tempBW.write(record2);
										tempBW.newLine();
									} else if (record1.substring(0, 8).equals(tempRecord) && isOccured) {
						//				System.out.println("Here 0 ");
										break;
									} else if (record1.substring(0, 8).trim().equals(tempRecord) && !isOccured && !flag) {
										//System.out.println("Here 1 ");
										currentBitmap.append(record2.substring(tempBitIndex));
										tempBW.write("checked" +record2);
										tempBW.newLine();
										flag = true;
										size = record2.substring(tempBitIndex).length();
									} else if (record1.substring(0, 8).compareToIgnoreCase(tempRecord) > 0 && isOccured) {
									//	System.out.println("Here 2 ");
										tempBW.write(record2);
										tempBW.newLine();
										size = record2.substring(tempBitIndex).length();
									} else if (record1.substring(0, 8).compareToIgnoreCase(tempRecord) > 0 && !isOccured) {
										tempBW.write(record2);
										//System.out.println("Here 3 ");
										tempBW.newLine();
										size = record2.substring(tempBitIndex).length();
									} else if (record1.substring(0, 8).compareToIgnoreCase(tempRecord) < 0) {
										//System.out.println("Here 4 ");
										size = record2.substring(tempBitIndex).length();
										break;
									} else {
										size = record2.substring(tempBitIndex).length();
									}
								}
								tempBW.close();
								br2.close();
//								System.out.println(subListName.get(i) + " --------- " + subListName.get(j) + " Flag " + flag);
								if(flag) {
									File file1 = new File(subListName.get(j));
									file1.delete();
									File file2 = new File(Constants.BLOCK_PATH + "/temp");
									boolean successful = file2.renameTo(file1);										
				//					System.out.println("File Edited" +successful);
								} else {
									File file2 = new File(Constants.BLOCK_PATH + "/temp");
									file2.delete();
								}
								// System.out.println(successful);
								if (!flag) {
					//				System.out.println(size + "Here Fla " + flag  + " " +currentBitmap.toString());
									for (int k = 1; k <= size; k++)
										currentBitmap.append(0);
								}
									
							}
							bw.write(currentBitmap.toString());
							bw.newLine();
							currentBitmap = new StringBuilder();
						}
					}
					br1.close();
				}
				br1 = new BufferedReader(new FileReader(subListName.get(subListName.size() - 1)));
				String record1 = "";
				while ((record1 = br1.readLine()) != null) {
					if (!record1.substring(0, 7).equals("checked")) {
						StringBuilder temp = new StringBuilder();
						temp.append(record1.substring(0, 9));
						for (int k = 1; k <= (subListName.size() - 1) * firstFile; k++)
							temp.append(0);
						temp.append(record1.substring(9));
						bw.write(temp.toString());
						bw.newLine();
						temp = new StringBuilder();
					}
				}
			}
			bw.close();
			System.out.println("Time to Merge : " +(System.currentTimeMillis() - begin));
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void old_mergeIndex() {
		try {
			long begin = System.currentTimeMillis();
			BufferedReader br1 = null;
			BufferedReader br2 = null;
			BufferedWriter bw = new BufferedWriter(new FileWriter(Constants.BLOCK_PATH + "/BITMAP_INDEX_T1"));
			if (subListName.size() == 1) {
				br1 = new BufferedReader(new FileReader(subListName.get(0)));
				String record1 = "";
				while ((record1 = br1.readLine()) != null) {
					bw.write(record1);
					bw.newLine();
				}
			} else {
				for (int i = 0; i < subListName.size() - 1; i++) {
					br1 = new BufferedReader(new FileReader(subListName.get(i)));
					System.out.println((System.currentTimeMillis() - begin) +  " " + subListName.get(i));
					String record1 = "";
					while ((record1 = br1.readLine()) != null) {
						if (!record1.equals("*") || !(record1.trim().length() == 1)) {
							StringBuilder temp = new StringBuilder();
							temp.append(record1.substring(0, 9));
							for (int k = 1; k <= i * firstFile; k++)
								temp.append(0);
							temp.append(record1.substring(9));
							for (int j = i + 1; j < subListName.size(); j++) {
								br2 = new BufferedReader(new FileReader(subListName.get(j)));
								BufferedWriter tempBW = new BufferedWriter(
										new FileWriter(Constants.BLOCK_PATH + "/temp"));
								String record2 = "";
								boolean flag = false;
								long size = lastFile;
								
								while ((record2 = br2.readLine()) != null) {
									if (!record2.trim().equals("*") || !(record2.trim().length() == 1)) {
										/*
										 * System.out.println("--->" + record1);
										 * System.out.println(record2.trim().equals("*"));
										 * System.out.println(record2.trim().length() >= 1);
										 */ 
										if (record1.substring(0, 8).equals(record2.substring(0, 8))) {
											temp.append(record2.substring(9));
											tempBW.write("*");
											tempBW.newLine();
											flag = true;
										} else {
											tempBW.write(record2);
											tempBW.newLine();
											size = record2.substring(9).length();
										}
									} else {
										tempBW.write(record2);
										tempBW.newLine();
										// size = record2.substring(9).length();
									}
								}
								tempBW.close();
								br2.close();
								File file1 = new File(subListName.get(j));
								file1.delete();
								File file2 = new File(Constants.BLOCK_PATH + "/temp");
								// System.out.println(file1.getPath());
								// System.out.println(file2.getPath());
								boolean successful = file2.renameTo(file1);
								// System.out.println(successful);
								if (!flag)
									for (int k = 1; k <= size; k++)
										temp.append(0);
							}
							bw.write(temp.toString());
							bw.newLine();
							temp = new StringBuilder();
						}
					}
					br1.close();
				}
				br1 = new BufferedReader(new FileReader(subListName.get(subListName.size() - 1)));
				String record1 = "";
				while ((record1 = br1.readLine()) != null) {
					if (!record1.equals("*") || !(record1.trim().length() == 1)) {
						StringBuilder temp = new StringBuilder();
						temp.append(record1.substring(0, 9));
						for (int k = 1; k <= (subListName.size() - 1) * firstFile; k++)
							temp.append(0);
						temp.append(record1.substring(9));
						bw.write(temp.toString());
						bw.newLine();
						temp = new StringBuilder();
					}
				}
			}
			bw.close();
			System.out.println("Time to Merge : " +(System.currentTimeMillis() - begin));
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
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
		PhaseOne.recordCount = recordCount;
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
