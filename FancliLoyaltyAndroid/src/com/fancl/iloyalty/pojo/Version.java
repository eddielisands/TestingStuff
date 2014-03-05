package com.fancl.iloyalty.pojo;

public class Version {
	private String versionMajor;
	private String versionMinor;
	private String versionRevision;
	private String issue;
	private String createDatetime;
	
	public Version(String versionMajor, String versionMinor,
			String versionRevision, String issue, String createDatetime)
	{
		this.versionMajor = versionMajor;
		this.versionMinor = versionMinor;
		this.versionRevision = versionRevision;
		this.issue = issue;
		this.createDatetime = createDatetime;
	}

	public String getVersionMajor() {
		return versionMajor;
	}

	public void setVersionMajor(String versionMajor) {
		this.versionMajor = versionMajor;
	}

	public String getVersionMinor() {
		return versionMinor;
	}

	public void setVersionMinor(String versionMinor) {
		this.versionMinor = versionMinor;
	}

	public String getVersionRevision() {
		return versionRevision;
	}

	public void setVersionRevision(String versionRevision) {
		this.versionRevision = versionRevision;
	}

	public String getIssue() {
		return issue;
	}

	public void setIssue(String issue) {
		this.issue = issue;
	}

	public String getCreateDatetime() {
		return createDatetime;
	}

	public void setCreateDatetime(String createDatetime) {
		this.createDatetime = createDatetime;
	}

	@Override
	public String toString() {
		return "Version [versionMajor=" + versionMajor + ", versionMinor=" + versionMinor + ", versionRevision=" + versionRevision + ", issue=" + issue + ", createDatetime=" + createDatetime + "]";
	}
	
	
}
