package com.example.demo;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.File;
import java.io.IOException;
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
@Rollback(true)
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
		dataset.setContent(file.getName());
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
		repo.deleteFromId("42");

		Dataset data = repo.findByDatasetId(42);

		assertThat(data).isNull();
	}

	@Test
	public void testFindAllDataset() {
		assertThat(repo.findAll().size()).isEqualTo(2);
	}

	@Test
	public void testFindByUsername() {
		assertThat(repo.findByUsername("testcomp1@domain.com").size()).isEqualTo(1);
	}

	@Test
	public void testGetFilename() {
		assertThat(repo.getFilename(42)).isEqualTo("atestdataset.csv");
	}

	@Test
	public void testFindByUsernameAndComp() {
		assertThat(repo.findByUsernameAndComp("demo1@domain.com", "testcomp1@domain.com").size()).isEqualTo(2);
	}

	@Test
	public void testDeleteFromUsername() {
		repo.deleteFromUsername("demo1@domain.com");

		assertThat(repo.findByUsername("demo1@domain.com")).isEmpty();
	}

	@Test
	public void testGetName() throws IOException {
		File file = new File("/Users/joannatyan/Downloads/atestdataset.csv");
		Dataset dataset = new Dataset();

		dataset.setName(file.getName());

		byte[] bytes = Files.readAllBytes(file.toPath());
		dataset.setContent(file.getName());
		long fileSize = bytes.length;
		dataset.setSize(fileSize);

		dataset.setUploadTime(new Date());

		dataset.setUsername("10");

		assertThat(dataset.getName()).isEqualTo("atestdataset.csv");

	}

	@Test
	public void testGetUploadTime() throws IOException {
		File file = new File("/Users/joannatyan/Downloads/atestdataset.csv");
		Dataset dataset = new Dataset();

		dataset.setName(file.getName());

		byte[] bytes = Files.readAllBytes(file.toPath());
		dataset.setContent(file.getName());
		long fileSize = bytes.length;
		dataset.setSize(fileSize);

		Date time = new Date();
		dataset.setUploadTime(time);

		dataset.setUsername("10");

		assertThat(dataset.getUploadTime()).isEqualTo(time);
	}

	@Test
	public void testSetDataset() {
		Dataset dataset = new Dataset(1, "test", 12);

		dataset.setDatasetID(3);

		assertThat(dataset.getDatasetID()).isEqualTo(3);
	}

	@Test
	public void testGetUsername() {
		Dataset dataset = new Dataset();

		dataset.setUsername("hello@domain.com");

		assertThat(dataset.getUsername()).isEqualTo("hello@domain.com");
	}

	@Test
	public void testGetContent() throws IOException {
		File file = new File("/Users/joannatyan/Downloads/atestdataset.csv");
		Dataset dataset = new Dataset();

		dataset.setName(file.getName());

		byte[] bytes = Files.readAllBytes(file.toPath());
		dataset.setContent(file.getName());

		assertThat(dataset.getContent()).isNotEmpty();
	}
}
