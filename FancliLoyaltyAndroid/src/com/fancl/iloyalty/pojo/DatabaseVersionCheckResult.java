package com.fancl.iloyalty.pojo;

public class DatabaseVersionCheckResult {
	
	/**
	 * Need to add/remove parameters to fit the parameters return from Database checking API.
	 * After add/remove parameter, need to re-generate the constructor and get set method
	 */
	
	private String issue;
	private String version;
	private String dbLink;
	
	public DatabaseVersionCheckResult(String issue, String version, String dbLink)
	{
		this.issue = issue;
		this.version = version;
		this.dbLink = dbLink;
	}

	public String getIssue() {
		return issue;
	}

	public void setIssue(String issue) {
		this.issue = issue;
	}

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	public String getDbLink() {
		return dbLink;
	}

	public void setDbLink(String dbLink) {
		this.dbLink = dbLink;
	}
	
	
}
