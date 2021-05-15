<<<<<<< Updated upstream
package com.example.demo; 
=======
package com.example.demo;
>>>>>>> Stashed changes

import java.util.stream.Collectors;
import java.util.*;
import java.io.File;
import java.io.IOException;

public class DataClean {
    private double[][] inputData;

    public DataClean(int m, int n, double t) { // create your own inputData matrix by editiing this constructor
        this.inputData = new double[m][n];
        
        for (int i=0; i<m; i++) {
            for (int j=0; j<n; j++) {
                inputData[i][j] = (i+j)/t;
            }
        }
    }

    public DataClean(File file) { // load data from FileHandler class
        FileHandler fh = new FileHandler();
        try {
            this.inputData = fh.convertToData(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public double[][] getInputdata() {
        return this.inputData;
    }

    public void editCellVal(double[][] dataSet, int i, int j, double n) {
        dataSet[i][j] = n;
    }

    public double[][] deleteRow(double[][] dataSet, int delete_row_index) {
        double[][] newData = new double[dataSet.length-1][dataSet[0].length];
        int n=0;
        if (delete_row_index==dataSet.length-1) {
            for (int i=0;i<dataSet.length-1; i++) {
                for (int j=0;j<dataSet[0].length;j++) {
                    if (delete_row_index==i) {
                        i++;
                    }
                    newData[n][j]=dataSet[i][j];
                }
                n++;
            }
        }
        else {
            for (int i=0;i<dataSet.length; i++) {
                for (int j=0;j<dataSet[0].length;j++) {
                    if(delete_row_index==i) {
                        i++;
                    } 
                    newData[n][j]=dataSet[i][j];
                }
                n++;
            }
        }
        return newData;
    }

    public double[][] deleteColumn(double[][] dataSet, int delete_col_index) {
        double[][] newData = new double[dataSet.length][dataSet[0].length-1];
        for (int i=0; i < dataSet.length; i++) {
            for (int j=0, k=0; j < dataSet[0].length-1 && k < dataSet[0].length; j++) {
                if (j != delete_col_index) {
                    newData[i][k++] = dataSet[i][j];
                }
            }
        }
        return newData;
    }

    // helper function to convert 2d array to array list
    public static ArrayList<Double> twoDArrToArrList(Double[][] dataSet) {
        List<Double> collection = Arrays.stream(dataSet).flatMap(Arrays::stream).collect(Collectors.toList());

        ArrayList<Double> arrList = new ArrayList<Double>(collection);
        return arrList;
    }

    // returns a unique list of values in a possibly large sparse matrix
    public List<Double> unique(Double[][] dataSet) {
        ArrayList<Double> uniqueList = DataClean.twoDArrToArrList(dataSet);
        List<Double> uniqueNumbers = uniqueList.stream().distinct().collect(Collectors.toList());

        return uniqueNumbers;
    }

    // Normalization: rescale values into range in [0,1]
    public Double[][] normalize(Double[][] dataSet) {
        Double maxVal = Collections.max(DataClean.twoDArrToArrList(dataSet));
        Double minVal = Collections.min(DataClean.twoDArrToArrList(dataSet));

        Double[][] newData = new Double[dataSet.length][dataSet[0].length];
        for (int i = 0; i < dataSet.length; i++) {
            for (int j = 0; j < dataSet[0].length; j++) {
                newData[i][j] = (dataSet[i][j] - minVal) / (maxVal - minVal);
            }
        }
        return newData;
    }

    //Standardization: rescale to mean-zero and unit standard deviation 
   public double[][] standardize(double[][] dataSet) {
       double sum=0.0;
       for (int i=0;i<dataSet.length;i++){
           for (int j=0; j<dataSet[0].length;j++){
               sum += dataSet[i][j];
           }
       }
        double mean = sum/(dataSet.length*dataSet[0].length);
        double std = 0.0;
        //compute standard deviation
        for (double[] i: dataSet) {
            for (double ij: i) {
                std += Math.pow(ij-mean,2.0); 
            }
        }
        std = Math.sqrt(std / (dataSet.length * dataSet[0].length));

        
        double[][] newData = new double[dataSet.length][dataSet[0].length];
        for (int i=0; i<dataSet.length; i++) {
            for (int j=0; j<dataSet[0].length; j++) {
                newData[i][j]= (dataSet[i][j]-mean)/std;
            }
        }
        return newData;
    }

    //@Override
    public void toDense(double[][] dataSet) {
        int count = 0;
        for (int i = 0; i < dataSet.length; i++) {
            for (int j = 0; j < dataSet[0].length; j++) {
                if (dataSet[i][j] == 0) {
                    ++count;
                }
            }
        }

        double[] averageOnRows = average(dataSet);

        // define sparse matrix as having at least more than half elements as zero
        if (count >= dataSet.length * dataSet[0].length * 0.5) {
            for (int i = 0; i < dataSet.length; i++) {
                for (int j = 0; j < dataSet[0].length; j++) {
                    if (dataSet[i][j] == 0) {
                        dataSet[i][j] = averageOnRows[i];
                    }
                }
            }
        }

    }

    //@Override
    public double[] average(double[][] dataSet) {
        double[] averageOnRows = new double[dataSet.length];
        for (int i = 0; i < dataSet.length; i++) {
            double sumRow = 0.0;
            for (int j = 0; j < dataSet[0].length; j++) {
                sumRow = sumRow + dataSet[i][j];
            }
            averageOnRows[i] = sumRow / dataSet[0].length;
        }

        return averageOnRows;
    }

}
