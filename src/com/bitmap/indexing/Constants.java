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

public class Constants {

	public static final int TUPLE_SIZE = 100;
	public static final int BLOCK_SIZE = (4096 / TUPLE_SIZE) * TUPLE_SIZE;
	public static final int TOTAL_MEMORY = ((int) (Runtime.getRuntime().totalMemory()));
	public static final int MEMORY_SIZE = (TOTAL_MEMORY / BLOCK_SIZE) * BLOCK_SIZE;
	public static final int MAX_RECORD = 40;
	public static final String INPUT_PATH = "./inputfiles/";
	public static final String T1_BLOCK_PATH = "./T1_BLOCK/";
	public static final String T2_BLOCK_PATH = "./T2_BLOCK/";
	public static final String T1_EMP = "./T1_EMP/";
	public static final String T2_EMP = "./T2_EMP/";
	public static final String T1_GEN = "./T1_GEN/";
	public static final String T1_T2 = "./T1_T2/";
	public static final String T2_GEN = "./T2_GEN/";
	public static final String T1_DEPT = "./T1_DEPT/";
	public static final String T2_DEPT = "./T2_DEPT/";
	public static final String COMPRESSED_PATH = "./COMPRESSED_BITMAP/";
	public static final String OUTPUT_PATH = "./outputfiles/";
	public static final String INPUT_FILE1 = "sample7.txt";
	public static final String INPUT_FILE2 = "sample8.txt";
	public static final String T1 = "T1";
	public static final String T2 = "T2";
}
