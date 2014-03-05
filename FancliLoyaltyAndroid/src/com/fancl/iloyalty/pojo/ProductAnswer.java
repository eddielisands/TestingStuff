package com.fancl.iloyalty.pojo;

public class ProductAnswer {
	private String objectId;
	private String questionId;
	private String answerCode;
	private String answerZh;
	private String answerSc;
	private String answerEn;
	
	public ProductAnswer(String objectId, String questionId, String answerCode, String answerZh,
			String answerSc, String answerEn)
	{
		this.objectId = objectId;
		this.questionId = questionId;
		this.answerCode = answerCode;
		this.answerZh = answerZh;
		this.answerSc = answerSc;
		this.answerEn = answerEn;
	}

	public String getObjectId() {
		return objectId;
	}

	public void setObjectId(String objectId) {
		this.objectId = objectId;
	}

	public String getQuestionId() {
		return questionId;
	}

	public void setQuestionId(String questionId) {
		this.questionId = questionId;
	}

	public String getAnswerCode() {
		return answerCode;
	}

	public void setAnswerCode(String answerCode) {
		this.answerCode = answerCode;
	}

	public String getAnswerZh() {
		return answerZh;
	}

	public void setAnswerZh(String answerZh) {
		this.answerZh = answerZh;
	}

	public String getAnswerSc() {
		return answerSc;
	}

	public void setAnswerSc(String answerSc) {
		this.answerSc = answerSc;
	}

	public String getAnswerEn() {
		return answerEn;
	}

	public void setAnswerEn(String answerEn) {
		this.answerEn = answerEn;
	}

	@Override
	public String toString() {
		return "ProductAnswer [questionId=" + questionId + ", answerCode=" + answerCode + ", answerZh=" + answerZh + ", answerSc=" + answerSc + ", answerEn=" + answerEn + ", objectId=" + objectId + "]";
	}
	
	
}
