package cwru.databite.databite.Implementation;

import java.io.File;
import java.io.IOException;
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

	public double[][] convertToData(File file) throws IOException{
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
	
	public void convertToFile(Double[][] dataSet, String fileName) throws IOException{
		String fileType = getExtension(fileName);
		ArrayList<Double> inputData = DataClean.twoDArrToArrList(dataSet);
		ArrayList<String> strList = new ArrayList<String>();
		for (Double d : inputData) {
			strList.add(d.toString());
		}

		try {
			BufferedWriter writer = Files.newBufferedWriter(Paths.get(fileName));
			if (fileType==".csv") {
				for (int i=0; i<dataSet.length; i++) {
					ArrayList<String> iList = (ArrayList<String>) strList.subList((i-1)*dataSet[0].length,(i-1)*dataSet[0].length+dataSet[0].length-1);
					writer.write(String.join(",", iList));
					writer.newLine();
				}
				writer.close();
				System.out.println("CSV data entered!");	
			}
			else if (fileType==".tsv") {
				for (int i=0; i<dataSet.length; i++) {
					ArrayList<String> iList = (ArrayList<String>) strList.subList((i-1)*dataSet[0].length,(i-1)*dataSet[0].length+dataSet[0].length-1);
					writer.write(String.join("\t", iList));
					writer.newLine();
				}
				writer.close();
				System.out.println("TSV data entered!");
			}
			else if (fileType==".txt") {
				for (int i=0; i<dataSet.length; i++) {
					ArrayList<String> iList = (ArrayList<String>) strList.subList((i-1)*dataSet[0].length,(i-1)*dataSet[0].length+dataSet[0].length-1);
					
					writer.write(String.join("\s", iList));
					writer.newLine();
				}
				writer.close();
				System.out.println("txt data entered!");	
			}
		}

		catch (IOException e) {
			e.printStackTrace();
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
