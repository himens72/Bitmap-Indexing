package com.bitmap.indexing;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class sample {
	static String fileName1 = Constants.INPUT_PATH + Constants.INPUT_FILE1;
	static String fileName2 = Constants.INPUT_PATH + Constants.INPUT_FILE2;

	public static void main(String[] args) throws IOException {
		BufferedReader br1 = new BufferedReader(new FileReader(fileName1));
		BufferedReader br2 = new BufferedReader(new FileReader(fileName2));
		long limit = 20000;
		BufferedWriter write1 = new BufferedWriter(new FileWriter(Constants.INPUT_PATH + "sample7.txt"));
		BufferedWriter write2 = new BufferedWriter(new FileWriter(Constants.INPUT_PATH + "sample8.txt"));
		String record = "";
		long i = 0;
		while ((record = br1.readLine()) != null) {
			write1.write(record);
			write1.newLine();
			i++;
			if(i == limit) {
				break;
			}
		}
		i = 0;
		write1.close();
		while ((record = br2.readLine()) != null) {
			write2.write(record);
			write2.newLine();
			i++;
			if(i == limit) {
				break;
			}
		}
		write2.close();
	}
}
