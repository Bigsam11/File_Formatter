package com.ubagroup.model;

import java.sql.Blob;
import java.util.Arrays;
import java.util.Date;

public class File {

	private String file_location;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getFile_name() {
		return file_name;
	}

	public void setFile_name(String file_name) {
		this.file_name = file_name;
	}

	public Blob getFile_content() {
		return file_content;
	}

	public void setFile_content(Blob file_content) {
		this.file_content = file_content;
	}

	public String getInputter_name() {
		return inputter_name;
	}

	public void setInputter_name(String inputter_name) {
		this.inputter_name = inputter_name;
	}

	private Long id;
	private String file_name;
	private Blob file_content;
	private String inputter_name;

	public Date getInputted_date() {
		return inputted_date;
	}

	@Override
	public String toString() {
		return "File{" +
				"id=" + id +
				", file_name='" + file_name + '\'' +
				", file_content=" + file_content +
				", inputter_name='" + inputter_name + '\'' +
				", inputted_date=" + inputted_date +
				'}';
	}

	public void setInputted_date(Date inputted_date) {
		this.inputted_date = inputted_date;
	}

	private Date inputted_date;

	public String getFile_location() {
		return file_location;
	}

	public void setFile_location(String file_location) {
		this.file_location = file_location;
	}
}


