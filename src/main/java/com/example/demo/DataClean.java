package cwru.databite.databite.Implementation;

import cwru.databite.databite.Interface.IDataClean;

import java.util.stream.Collectors;
import java.util.*;


public class DataClean implements IDataClean {
    Double[][] inputData;

    public DataClean(Double[][] inputData) {
        this.inputData = inputData;
    }
    
    public Double[][] getInputdata() {
        return this.inputData;
    }

    public void editCellVal(Double[][] dataSet, int i, int j, double n) {
        dataSet[i][j] = n;
    }

    public Double[][] deleteRow(Double[][] dataSet, int delete_row_index) {
        Double[][] newData = new Double[dataSet.length - 1][dataSet[0].length];
        for (int i=0,j=0; i < dataSet.length-1 && j<dataSet.length; i++) {
            for (int k = 0; k < dataSet[0].length; k++) {
                if (i != delete_row_index) {
                    newData[j++][k] = dataSet[i][j];
                }
            }
        }
        return newData;
    }

    public Double[][] deleteColumn(Double[][] dataSet, int delete_col_index) {
        Double[][] newData = new Double[dataSet.length][dataSet[0].length-1];
        for (int i=0; i < dataSet.length; i++) {
            for (int j=0, k=0; j < dataSet[0].length-1 && k < dataSet[0].length; j++) {
                if (j != delete_col_index) {
                    newData[i][k++] = dataSet[i][j];
                }
            }
        }
        return newData;
    }

    //helper function to convert 2d array to array list
    public static ArrayList<Double> twoDArrToArrList(Double[][] dataSet) {
        List<Double> collection = Arrays.stream(dataSet)  
            .flatMap(Arrays::stream)
            .collect(Collectors.toList());

        ArrayList<Double> arrList = new ArrayList<Double>(collection);   
        return arrList; 
    }

    //returns a unique list of values in a possibly large sparse matrix
    public List<Double> unique(Double[][] dataSet) {
        ArrayList<Double> uniqueList = DataClean.twoDArrToArrList(dataSet);
        List<Double> uniqueNumbers = uniqueList.stream().distinct().collect(Collectors.toList());

        return uniqueNumbers;
    }

    //Normalization: rescale values into range in [0,1]
    public Double[][] normalize(Double[][] dataSet) {
        Double maxVal = Collections.max(DataClean.twoDArrToArrList(dataSet));
        Double minVal = Collections.min(DataClean.twoDArrToArrList(dataSet)); 
        
        Double[][] newData = new Double[dataSet.length][dataSet[0].length];
        for (int i=0; i<dataSet.length; i++) {
            for (int j=0; j<dataSet[0].length; j++) {
                newData[i][j]= (dataSet[i][j]-minVal)/(maxVal-minVal);
            }
        }
        return newData;
    }



    //Standardization: rescale to mean-zero and unit standard deviation 
   public Double[][] standardize(Double[][] dataSet) {
       Double sum=0.0;
       for (int i=0;i<dataSet.length;i++){
           for (int j=0; j<dataSet[0].length;j++){
               sum += dataSet[i][j];
           }
       }
        Double mean = sum/(dataSet.length*dataSet[0].length);
        Double std = 0.0;
        //compute standard deviation
        for (Double[] i: dataSet) {
            for (Double ij: i) {
                std += Math.pow(ij-mean,2.0); 
            }
        }
        std = Math.sqrt(std/ (dataSet.length * dataSet[0].length));

        
        Double[][] newData = new Double[dataSet.length][dataSet[0].length];
        for (int i=0; i<dataSet.length; i++) {
            for (int j=0; j<dataSet[0].length; j++) {
                newData[i][j]= (dataSet[i][j]-mean)/std;
            }
        }
        return newData;
    } 

    @Override
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

    @Override
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
