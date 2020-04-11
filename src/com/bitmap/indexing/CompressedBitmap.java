package com.bitmap.indexing;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

public class CompressedBitmap {
	BufferedReader br;

	/*
	 * public void generateBitmap(String filename1, String name) { try { br = new
	 * BufferedReader(new FileReader(filename1)); BufferedWriter write = new
	 * BufferedWriter(new FileWriter(Constants.COMPRESSED_PATH + name)); String
	 * record = ""; long begin = System.currentTimeMillis(); long blockSize =
	 * ((Constants.TOTAL_MEMORY * 5) / (100 * 1000)); while (true) {
	 * ArrayList<String> subList = new ArrayList<String>(); long recordCount = 0;
	 * while ((record = br.readLine()) != null) { subList.add(record);
	 * recordCount++; if (recordCount == blockSize) { recordCount = 0; break; } }
	 * for (int index = 0; index < subList.size(); index++) { String id =
	 * subList.get(index).trim().substring(0, 8 + 1); String bits =
	 * subList.get(index).trim().substring(8 + 1); int i = 0; int j = 0;
	 * StringBuilder builder = new StringBuilder(); for (int k = 0; k <
	 * bits.length(); k++) { if (bits.substring(k, k + 1).equals("1")) { j = (int)
	 * (Math.log(i + 1) / Math.log(2) + 1e-10); for (int m = 0; m < j - 1; m++) {
	 * builder.append(1); } builder.append(0);
	 * builder.append(Integer.toBinaryString(i)); i = 0; j = 0; } else { i++; } }
	 * write.write(id + builder.toString()); write.newLine(); builder = new
	 * StringBuilder(); } if (record == null) { break; } }
	 * System.out.println("Time Taken to Create Compressed Bitmap : " +
	 * ((System.currentTimeMillis() - begin) / 1000) + "sec"); write.close(); }
	 * catch (FileNotFoundException e) { // TODO Auto-generated catch block
	 * e.printStackTrace(); } catch (IOException e) { // TODO Auto-generated catch
	 * block e.printStackTrace(); } }
	 */

	public void generateBitmap(String filename1, String name) {
		// TODO Auto-generated method stub
		try {
			br = new BufferedReader(new FileReader(filename1));
			BufferedWriter write = new BufferedWriter(new FileWriter(Constants.COMPRESSED_PATH + name));
			String record = "";
			long begin = System.currentTimeMillis();
			while ((record = br.readLine()) != null) {
				String id = record.trim().substring(0, 8 + 1);
				String bits = record.trim().substring(8 + 1);
				int i = 0;
				int j = 0;
				StringBuilder builder = new StringBuilder();
				for (int k = 0; k < bits.length(); k++) {
					if (bits.substring(k, k + 1).equals("1")) {
						j = (int) (Math.log(i + 1) / Math.log(2) + 1e-10);
						for (int m = 0; m < j - 1; m++) {
							builder.append(1);
						}
						builder.append(0);
						builder.append(Integer.toBinaryString(i));
						i = 0;
						j = 0;
					} else {
						i++;
					}
				}
				write.write(id + builder.toString());
				write.newLine();
				builder = new StringBuilder();
			}
			System.out.println("Time Taken to Create Compressed Bitmap : "
					+ ((System.currentTimeMillis() - begin) / 1000) + "sec");
			write.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		CompressedBitmap bitmap = new CompressedBitmap();
		bitmap.generateBitmap(Constants.COMPRESSED_PATH + "6-Block-0_1", Constants.COMPRESSED_PATH + "demo");
	}
}
