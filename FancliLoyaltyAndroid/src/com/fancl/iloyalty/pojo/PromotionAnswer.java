package com.fancl.iloyalty.pojo;

public class PromotionAnswer {
	private String key;
	private String answerZh;
	private String answerSc;
	private String answerEn;
	
	public PromotionAnswer(String key, String answerZh, String answerSc,
			String answerEn) {
		this.key = key;
		this.answerZh = answerZh;
		this.answerSc = answerSc;
		this.answerEn = answerEn;
	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
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
		return "PromotionAnswer [key=" + key + ", answerZh=" + answerZh
				+ ", answerSc=" + answerSc + ", answerEn=" + answerEn + "]";
	}
	
	
	
}
