package com.example.demo;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.util.Date;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.annotation.Rollback;

@DataJpaTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
@Rollback(false)
public class DatasetRepositoryTest {
	@Autowired
	private DatasetRepository repo;
	
	@Autowired
	private TestEntityManager entityManager;
	
	@Test
	public void testInsertDataset() throws IOException {
		File file = new File("/Users/joannatyan/Downloads/atestdataset.csv");
		Dataset dataset = new Dataset();
		
		dataset.setName(file.getName());
		
		byte[] bytes = Files.readAllBytes(file.toPath());
		dataset.setContent(bytes);
		long fileSize = bytes.length;
		dataset.setSize(fileSize);
		
		dataset.setUploadTime(new Date());
		
		dataset.setUsername("10");
		
		System.out.println(file.getName());
		System.out.println(bytes);
		
		Dataset savedDataset = repo.save(dataset);
		
		Dataset existDataset = entityManager.find(Dataset.class, savedDataset.getDatasetID());
		
		assertThat(existDataset.getSize()).isEqualTo(fileSize);
		
	}
	
	@Test
	public void testDeleteDataset() {
		
		
		repo.deleteFromId("15");
		
		Dataset data = repo.findByDatasetId(15);
		
		assertThat(data).isNull();
	}
	
	@Test
	public void testGetFile() throws IOException {
		String fileName = repo.getFilename(31);
		byte[] bytes = repo.getFile(31);
		
//		File newFile = new File(fileName);
//		newFile.createNewFile();
//		
//		OutputStream os = new FileOutputStream(newFile);
//		os.write(bytes);
//		os.close();
//		FileInputStream fis = new FileInputStream(newFile);
//		
//		fis.read(bytes);
//		fis.close();
//		String s = new String(bytes);
//		
//		System.out.println(s);
		
		
		
		assertThat(bytes).isNotNull();
		
		
	}
}
