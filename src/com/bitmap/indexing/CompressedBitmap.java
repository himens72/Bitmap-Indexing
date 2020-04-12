package com.bitmap.indexing;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class CompressedBitmap {
	BufferedReader br;

	public void generateBitmap(String filename1, String name, int endIndex) {
		// TODO Auto-generated method stub
		try {
			br = new BufferedReader(new FileReader(filename1));
			BufferedWriter write = new BufferedWriter(new FileWriter(Constants.COMPRESSED_PATH + name));
			String record = "";
			long begin = System.currentTimeMillis();
			while ((record = br.readLine()) != null) {
				String id = record.trim().substring(0, endIndex + 1);
				String bits = record.trim().substring(endIndex + 1);
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
			long end = (System.currentTimeMillis() - begin);
			System.out.println("Time Taken to Create Compressed Bitmap of " + name + " : "
					+ end + " ms (approx " + (end / 1000) +"sec )" );
			write.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		CompressedBitmap bitmap = new CompressedBitmap();
		bitmap.generateBitmap("./T1_EMP/7-Block-0_1", "./T2_EMP/7-Block-0_1", 8);
	}
}
