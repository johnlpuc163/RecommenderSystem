package com.prediction.convert;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;

public class MovieDataConvert {

	public static void main(String[] args) throws Exception {
		// TODO Auto-generated method stub
		BufferedReader br = new BufferedReader(new FileReader("data/10mdata.dat"));
		BufferedWriter bw = new BufferedWriter(new FileWriter("data/movies10m.csv"));
		
		String line;
		while((line = br.readLine()) != null){
			String[] values = line.split("::");
			bw.write(values[0] + "," + values[1] +"," + values[2] + "\n");			
		}
		
		br.close();
		bw.close();
	}

}
