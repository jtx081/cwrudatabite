package com.example.demo;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class FileHandler {

	Object file;

	public boolean checkFileType(File file) {
		String fileName = file.getName();

		String extension = getExtension(fileName);
		if (extension.equals(".CSV") == false && extension.equals(".TSV") == false && extension.equals(".txt") == false
				&& extension.equals(".dat") == false && extension.equals(".sql") == false
				&& extension.equals(".xml") == false) {
			System.out.println("Error. Incorrect file format.");
			return false;
		}
		else return true; 
	}

	public double[][] convertToFile(File file) throws IOException{
		int setSize = getFileLength(file);
		double[][] dataSet = new double[setSize][];
		if (checkFileType(file)== true){
			Scanner sc = new Scanner(file);
			String line;
			int index = 0;
			while(sc.hasNextLine()){
				line = sc.nextLine();
				double[] val = stringToArray(line);
				dataSet[index] = val;
				index++;
			}
			sc.close();
		}
		return dataSet;
	}
	
	public Double[][] convertToData(Object dataSet, File file) {
        String fileType = FileHandler.getFileType(file);
        String fileName = file.getName();
        
        String thisLine;
        FileInputStream fis = new FileInputStream(fileName);
        DataInputStream myInput = new DataInputStream(fis);

        switch (fileType) {
            case "CSV":
                List<String[]> clines = new ArrayList<String[]>();
                while ((thisLine = myInput.readLine()) != null) {
                     clines.add(thisLine.split(";"));
                }
                
                // convert our list to a String array.
                String[][] array = new String[clines.size()][0];
                clines.toArray(array);
                
                Double[][] resultArray = new Double[clines.size()][0];
                for (int i=0; i<array.length; i++) {
                	for (int j=0; j<array[0].length; j++) {
                		resultArray[i][j] = Double.parseDouble(array[i][j]);
                	}
                }
                return resultArray;

            case "TSV":
                List<String[]> tlines = new ArrayList<String[]>();
                while ((thisLine = myInput.readLine()) != null) {
                     tlines.add(thisLine.split("\t"));
                }
                
                // convert our list to a String array.
                String[][] tarray = new String[tlines.size()][0];
                tlines.toArray(tarray);
                
                Double[][] tresultArray = new Double[tlines.size()][0];
                for (int i=0; i<tarray.length; i++) {
                	for (int j=0; j<tarray[0].length; j++) {
                		tresultArray[i][j] = Double.parseDouble(tarray[i][j]);
                	}
                }
                return tresultArray;
               
        }

	}

	public double[] stringToArray(String line){
		int length = line.length();
		int index = 1;
		double[] returnValue = new double[length];
		while(index <= length){
			returnValue[index-1] = line.charAt(index-1)-48;
			index++;
		}
		return returnValue;
	}

	public int getFileLength(File file) throws IOException{
		Scanner sc = new Scanner(file);
		int length = 0;
		while (sc.hasNextLine()){
			length ++;
			sc.nextLine();
		}
		sc.close();
		return length;
	}

	private String getExtension(String name) {
		int position = getExtensionPosition(name);
		return name.substring(position, name.length());
	}

	private int getExtensionPosition(String name) {
		int returnVal = 0;
		for (int i = 0; i < name.length(); i++) {
			if (name.charAt(i) == '.') {
				returnVal = i;
			}
		}
		return returnVal;
	}
}