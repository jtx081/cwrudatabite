package com.example.demo;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name="dataset")
public class Dataset {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int datasetID;
	
	@Column(length = 512, nullable=false)
	private String name;
	private long size;
	
	@Column(name="upload_time")
	private Date uploadTime;
	
	private byte[] content;
	
		
	private String username;
//	@ManyToOne
//	@JoinColumn(name="username", nullable=false)
//	private String username;
	
	public Dataset() {
		
	}
	
	public Dataset(int datasetID, String name, long size) {
		this.datasetID = datasetID;
		this.name = name;
		this.size = size;
		
	}
	
	public int getDatasetID() {
		return datasetID;
	}

	public void setDatasetID(int datasetID) {
		this.datasetID = datasetID;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public long getSize() {
		return size;
	}

	public void setSize(long size) {
		this.size = size;
	}

	public Date getUploadTime() {
		return uploadTime;
	}

	public void setUploadTime(Date uploadTime) {
		this.uploadTime = uploadTime;
	}

	public byte[] getContent() {
		return content;
	}

	public void setContent(byte[] content) {
		this.content = content;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}
	
	
	
	
}
