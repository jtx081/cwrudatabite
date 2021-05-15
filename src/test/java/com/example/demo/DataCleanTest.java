package com.example.demo;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;



import org.junit.jupiter.api.Test;


public class DataCleanTest {
    protected DataClean dc = new DataClean(3,3,2.0);

    @Test
    void testGetInputData() {
        double[][] data = dc.getInputdata();

        double[][] toy = new double[3][3];
        
        for (int i=0; i<3; i++) {
            for (int j=0; j<3; j++) {
                toy[i][j] = (i+j)/2.0;
                
            }
            assertArrayEquals(toy[i],data[i]);
        }
        //assertSame(data, toy);
    }

    @Test
    void testEditCellVal() {
        double[][] data = dc.getInputdata();
        dc.editCellVal(data, 1, 1, 1.0);
        assertEquals(data[1][1], 1.0);
    }

    @Test 
    void testDeleteRow() {
        DataClean dClean = new DataClean(3,2,1.0);
        double[][] result = dClean.deleteRow(dClean.getInputdata(), 1);

        assertEquals(2, result.length);
        assertEquals(2, result[0].length);        
    }

    @Test
    void testDeleteColumn() {
        DataClean dClean = new DataClean(3,2,1.0); 

        double[][] result = dClean.deleteColumn(dClean.getInputdata(), 1);
        assertEquals(3, result.length);
        assertEquals(1, result[0].length);   
    }

    @Test
    void testAverage() {
        DataClean dClean = new DataClean(2,2,2.0);
        double[][] inputData = dClean.getInputdata();
        double[] avg = dClean.average(inputData);

        double[] prd = new double[2];
        for (int i=0; i<2; i++) {
            double count=0.0;
            for (int j=0; j<2; j++) {
                count += inputData[i][j];
            }
            prd[i]=count/2;
        }
        assertArrayEquals(avg, prd);
    }

    @Test
    void testToDense() {
        DataClean dClean = new DataClean(5,5,2.0); 
        double[][] inputData = dClean.getInputdata();
        dClean.toDense(inputData);

        int count=0;
        for (int i=0;i<inputData.length;i++) {
            for (int j=0;j<inputData[0].length;j++) {
                if (inputData[i][j]==0) {
                    count++;
                }
            }
        }
        assertTrue(count<inputData.length*inputData[0].length);
    }

    


}
