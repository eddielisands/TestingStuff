package com.fancl.iloyalty.pojo;

import java.util.List;

public class PromotionQuestion {
	private String promotionId;
	private String questionZh;
	private String questionSc;
	private String questionEn;
	private List<PromotionAnswer> promotionAnswerList;
	
	public PromotionQuestion(String promotionId, String questionZh,
			String questionSc, String questionEn,
			List<PromotionAnswer> promotionAnswerList) {
		this.promotionId = promotionId;
		this.questionZh = questionZh;
		this.questionSc = questionSc;
		this.questionEn = questionEn;
		this.promotionAnswerList = promotionAnswerList;
	}

	public String getPromotionId() {
		return promotionId;
	}

	public void setPromotionId(String promotionId) {
		this.promotionId = promotionId;
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

	public List<PromotionAnswer> getPromotionAnswerList() {
		return promotionAnswerList;
	}

	public void setPromotionAnswerList(List<PromotionAnswer> promotionAnswerList) {
		this.promotionAnswerList = promotionAnswerList;
	}

	@Override
	public String toString() {
		return "PromotionQuestion [promotionId=" + promotionId
				+ ", questionZh=" + questionZh + ", questionSc=" + questionSc
				+ ", questionEn=" + questionEn + ", promotionAnswerList="
				+ promotionAnswerList + "]";
	}
	
	
}
