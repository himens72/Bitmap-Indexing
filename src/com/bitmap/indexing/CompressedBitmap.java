package com.bitmap.indexing;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class CompressedBitmap {
	BufferedReader br;

	public void generateBitmap(String filename1, String filename2) {
		// TODO Auto-generated method stub
		try {
			br = new BufferedReader(new FileReader(filename1));
			BufferedWriter write = new BufferedWriter(new FileWriter(Constants.COMPRESSED_PATH + "T1_EMPLOYEE"));
			String record = "";
			while ((record = br.readLine()) != null) {

			}
			
			write = new BufferedWriter(new FileWriter(Constants.COMPRESSED_PATH + "T2_EMPLOYEE"));
			
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
