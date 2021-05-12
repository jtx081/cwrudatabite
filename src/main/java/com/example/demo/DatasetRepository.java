package com.example.demo;

import java.io.File;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface DatasetRepository extends JpaRepository<Dataset,Long>{
//	@Query("SELECT new Dataset(d.name, d.size) FROM Dataset d ORDER BY d.uploadTime DESC")
	@Query(value = "SELECT * FROM dataset d ORDER BY d.upload_time DESC", nativeQuery = true)
	List<Dataset> findAll();
	
	@Transactional
	@Modifying
	@Query(value = "DELETE FROM dataset d WHERE d.datasetID= CAST(?1 AS UNSIGNED)", nativeQuery= true)
	void deleteFromId(String datasetID);
	
	@Query(value = "SELECT * FROM dataset d WHERE d.datasetID=?1", nativeQuery=true)
	Dataset findByDatasetId(int datasetID);
	
	@Query(value = "SELECT * FROM dataset d WHERE d.username=?1 ORDER BY d.upload_time DESC", nativeQuery=true)
	List<Dataset> findByUsername(String username);

	@Query(value = "SELECT d.content FROM dataset d WHERE d.datasetID=?1", nativeQuery=true)
	byte[] getFile(int datasetID);
	
	@Query(value = "SELECT d.name FROM dataset d WHERE d.datasetID=?1", nativeQuery=true)
	String getFilename(int datasetID);
	
	@Query(value="SELECT * FROM dataset d WHERE d.username=?1 OR d.username=?2 ORDER BY d.upload_time DESC", nativeQuery=true)
	List<Dataset> findByUsernameAndComp(String username, String companyUsername);
	
	@Transactional
	@Modifying
	@Query(value = "DELETE FROM dataset d WHERE d.username=?1", nativeQuery= true)
	void deleteFromUsername(String username);
	

}
