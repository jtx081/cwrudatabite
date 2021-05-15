package com.example.demo;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.File;
import java.io.IOException;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.Rollback;

@DataJpaTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
@Rollback(false)
public class FileHandlerTests {

    private FileHandler fh = new FileHandler();
	
	File txtFile = new File("test.txt");
	File csvFile = new File("test.CSV");
	File tsvFile = new File("test.TSV");
	File datFile = new File("test.dat");
	File sqlFile = new File("test.sql");
	File xmlFile = new File("test.xml");
	
	@Test
	public void testGetExtension() {
		assertEquals(fh.getExtension("test.txt"), ".txt");
		assertEquals(fh.getExtension("test.CSV"), ".CSV");
		assertEquals(fh.getExtension("test.TSV"), ".TSV");
		assertEquals(fh.getExtension("test.dat"), ".dat");
		assertEquals(fh.getExtension("test.sql"), ".sql");
		assertEquals(fh.getExtension("test.xml"), ".xml");
	}
	
	@Test
	public void testCheckFileType() {
		assertTrue(fh.checkFileType(txtFile));
		assertTrue(fh.checkFileType(csvFile));
		assertTrue(fh.checkFileType(tsvFile));
		assertTrue(fh.checkFileType(datFile));
		assertTrue(fh.checkFileType(sqlFile));
		assertTrue(fh.checkFileType(xmlFile));
	}
	
	@Test
	public void testStringToArray() {
		assertEquals(fh.stringToArray("123")[0], 1);
        assertEquals(fh.stringToArray("123")[1], 2);
        assertEquals(fh.stringToArray("123")[2], 3);
	}
	
	@Test
	public void testGetFileLength() {
		try{
			assertEquals(fh.getFileLength(txtFile), 2);
            assertEquals(fh.getFileLength(csvFile), 2);
            assertEquals(fh.getFileLength(tsvFile), 2);
            assertEquals(fh.getFileLength(datFile), 2);
            assertEquals(fh.getFileLength(sqlFile), 2);
            assertEquals(fh.getFileLength(xmlFile), 2);
		}
		catch(IOException e){
			System.out.println("Error");
		}
	}
	
	@Test
	public void testGetExtensionPosition() {
		assertEquals(fh.getExtensionPosition("test.txt"), 4);
	}
	
	@Test
	public void testConvertToData() {
		try{
			assertEquals(fh.convertToData(txtFile)[0], 0);
            assertEquals(fh.convertToData(txtFile)[1], 1);
		}
		catch(IOException e){
			System.out.println("Error");
		}
	}
}
