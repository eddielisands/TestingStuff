package com.fancl.iloyalty.pojo;

public class ProductQuestion {
	private String objectId;
	private String questionZh;
	private String questionSc;
	private String questionEn;
	
	public ProductQuestion(String objectId, String questionZh, String questionSc,
			String questionEn)
	{
		this.objectId = objectId;
		this.questionZh = questionZh;
		this.questionSc = questionSc;
		this.questionEn = questionEn;
	}

	public String getObjectId() {
		return objectId;
	}

	public void setObjectId(String objectId) {
		this.objectId = objectId;
	}

	public String getQuestionZh() {
		return questionZh;
	}

	public void setQuestionZh(String questionZh) {
		this.questionZh = questionZh;
	}

	public String getQuestionSc() {
		return questionSc;
	}

	public void setQuestionSc(String questionSc) {
		this.questionSc = questionSc;
	}

	public String getQuestionEn() {
		return questionEn;
	}

	public void setQuestionEn(String questionEn) {
		this.questionEn = questionEn;
	}

	@Override
	public String toString() {
		return "ProductQuestion [questionZh=" + questionZh + ", questionSc=" + questionSc + ", questionEn=" + questionEn + ", objectId=" + objectId + "]";
	}
	
	
}
