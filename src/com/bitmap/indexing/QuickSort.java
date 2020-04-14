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

import java.util.ArrayList;

public class QuickSort {
	int partition(ArrayList<String> list, int low, int high, int startIndex, int endIndex) {
		String pivot = list.get(high);
		int i = (low - 1);
		for (int j = low; j < high; j++) {
			if (list.get(j).substring(0, endIndex - startIndex)
					.compareToIgnoreCase(pivot.substring(0, endIndex - startIndex)) < 0) {
				i++;
				String temp = list.get(i);
				list.set(i, list.get(j));
				list.set(j, temp);
			}
		}
		String temp = list.get(i + 1);
		list.set(i + 1, list.get(high));
		list.set(high, temp);
		return i + 1;
	}

	void sort(ArrayList<String> list, int low, int high, int startIndex, int endIndex) {
		if (low < high) {
			int pi = partition(list, low, high, startIndex, endIndex);
			sort(list, low, pi - 1, startIndex, endIndex);
			sort(list, pi + 1, high, startIndex, endIndex);
		}
	}

	public ArrayList<String> executeQuickSort(ArrayList<String> list, int startIndex, int endIndex) {
		int n = list.size();
		sort(list, 0, n - 1, startIndex, endIndex);
		return list;
	}
}
