package com.fancl.iloyalty.parser;

import java.util.ArrayList;
import java.util.List;

import com.fancl.iloyalty.exception.FanclException;
import com.fancl.iloyalty.pojo.Promotion;
import com.fancl.iloyalty.pojo.PromotionAnswer;
import com.fancl.iloyalty.pojo.PromotionQuestion;
import com.fancl.iloyalty.responseimpl.PromotionCountResult;
import com.fancl.iloyalty.util.DataUtil;
import com.fancl.iloyalty.util.LogController;
import com.longevitysoft.android.xml.plist.domain.Array;
import com.longevitysoft.android.xml.plist.domain.Date;
import com.longevitysoft.android.xml.plist.domain.Dict;
import com.longevitysoft.android.xml.plist.domain.PList;

public class PromotionParser {
	
	public List<Promotion> parsePromotionList(PList plist) throws FanclException
	{
		int status = ((Dict) plist.getRootElement()).getConfigurationInteger("status").getValue();
		if (status == 0) {
			List<Promotion> promotionList = new ArrayList<Promotion>();
			Array itemArray = ((Dict) plist.getRootElement()).getConfigurationArray("item");
			LogController.log("itemArray.size() : " + itemArray.size());
			
			for (int i = 0; i < itemArray.size(); i++)
			{
				String id = ((Dict) itemArray.get(i)).getConfiguration("id").getValue();;
				String code = ((Dict) itemArray.get(i)).getConfiguration("code").getValue();
				String thumbnail = DataUtil.convertImageName(((Dict) itemArray.get(i)).getConfiguration("thumbnail").getValue());
				String image = DataUtil.convertImageName(((Dict) itemArray.get(i)).getConfiguration("image").getValue());
				String titleZh = ((Dict) itemArray.get(i)).getConfiguration("titleZh").getValue();
				String titleSc = ((Dict) itemArray.get(i)).getConfiguration("titleSc").getValue();
				String titleEn = ((Dict) itemArray.get(i)).getConfiguration("titleEn").getValue();
				String shortDescriptionZh = ((Dict) itemArray.get(i)).getConfiguration("shortDescriptionZh").getValue();
				String shortDescriptionSc = ((Dict) itemArray.get(i)).getConfiguration("shortDescriptionSc").getValue();
				String shortDescriptionEn = ((Dict) itemArray.get(i)).getConfiguration("shortDescriptionEn").getValue();
				String descriptionZh = ((Dict) itemArray.get(i)).getConfiguration("descriptionZh").getValue();
				String descriptionSc = ((Dict) itemArray.get(i)).getConfiguration("descriptionSc").getValue();
				String descriptionEn = ((Dict) itemArray.get(i)).getConfiguration("descriptionEn").getValue();
				Date promotionStartDatetimeObj = ((Dict) itemArray.get(i)).getConfigurationObject("promotionStartDatetime");
				java.util.Date tmpPromotionStartDatetime = promotionStartDatetimeObj.getValue();
				String promotionStartDatetime = DataUtil.convertDateToString(tmpPromotionStartDatetime);
				Date promotionEndDatetimeObj = ((Dict) itemArray.get(i)).getConfigurationObject("promotionEndDatetime");
				java.util.Date tmpPromotionEndDatetime = promotionEndDatetimeObj.getValue();
				String promotionEndDatetime = DataUtil.convertDateToString(tmpPromotionEndDatetime);
				String isNew = ((Dict) itemArray.get(i)).getConfiguration("isNew").getValue();
				String isPublic = ((Dict) itemArray.get(i)).getConfiguration("isPublic").getValue();
				String promotionType = ((Dict) itemArray.get(i)).getConfiguration("promotionType").getValue();
				String couponSerialNumber = ((Dict) itemArray.get(i)).getConfiguration("couponSerialNumber").getValue();
				String couponStatus = ((Dict) itemArray.get(i)).getConfiguration("couponStatus").getValue();
				String couponStatusCode = ((Dict) itemArray.get(i)).getConfiguration("couponStatusCode").getValue();
				String isParticipated = ((Dict) itemArray.get(i)).getConfiguration("isParticipated").getValue();
				String isLuckyDraw = ((Dict) itemArray.get(i)).getConfiguration("isLuckyDraw").getValue();
				String luckyDrawType = ((Dict) itemArray.get(i)).getConfiguration("luckyDrawType").getValue();
				String gp = ((Dict) itemArray.get(i)).getConfiguration("gp").getValue();

				// 20 not used, 30 suspended, 90 redeemed, 00 others
				
				String publishStatus = "";
				String createDatetime = "";
				
				Promotion promotion = new Promotion(id, code, thumbnail, image, titleZh, titleSc, titleEn, shortDescriptionZh, shortDescriptionSc, shortDescriptionEn, descriptionZh, descriptionSc, descriptionEn, promotionStartDatetime, promotionEndDatetime, publishStatus, promotionType, isLuckyDraw, luckyDrawType, isNew, isPublic, gp, createDatetime, isParticipated, couponSerialNumber, isNew);
				promotion.setCouponStatus(couponStatus);
				promotion.setCouponStatusCode(couponStatusCode);
				promotionList.add(promotion);
				LogController.log(promotion.toString());

			}
			
			return promotionList;
		}
		return null;
	}
	
	public List<PromotionQuestion> parsePromotionQuestionAnswerList(PList plist) throws FanclException {
		int status = ((Dict) plist.getRootElement()).getConfigurationInteger("status").getValue();
		if (status == 0) {
			List<PromotionQuestion> promotionQuestionList = new ArrayList<PromotionQuestion>();
			Array promotionQuestionitemArray = ((Dict) plist.getRootElement()).getConfigurationArray("item");
			LogController.log("promotionAnsweritemArray.size() : " + promotionQuestionitemArray.size());
			
			for (int i = 0; i < promotionQuestionitemArray.size(); i++) {
				String promotionId = "";
				String questionZh = ((Dict) promotionQuestionitemArray.get(i)).getConfiguration("questionZh").getValue();
				String questionSc = ((Dict) promotionQuestionitemArray.get(i)).getConfiguration("questionSc").getValue();
				String questionEn = ((Dict) promotionQuestionitemArray.get(i)).getConfiguration("questionEn").getValue();
				
				List<PromotionAnswer> promotionAnswerList = new ArrayList<PromotionAnswer>();
				Array promotionAnsweritemArray = ((Dict) promotionQuestionitemArray.get(i)).getConfigurationArray("answer");
				LogController.log("promotionAnsweritemArray.size() : " + promotionAnsweritemArray.size());
				
				for (int j = 0; j < promotionAnsweritemArray.size(); j++) {
					String key = ((Dict) promotionAnsweritemArray.get(j)).getConfiguration("key").getValue();
					String answerZh = ((Dict) promotionAnsweritemArray.get(j)).getConfiguration("answerZh").getValue();
					String answerSc = ((Dict) promotionAnsweritemArray.get(j)).getConfiguration("answerSc").getValue();
					String answerEn = ((Dict) promotionAnsweritemArray.get(j)).getConfiguration("answerEn").getValue();

					PromotionAnswer promotionAnswer = new PromotionAnswer(key, answerZh, answerSc, answerEn);
					promotionAnswerList.add(promotionAnswer);
				}
				
				PromotionQuestion promotionQuestion = new PromotionQuestion(promotionId, questionZh, 
						questionSc, questionEn, promotionAnswerList);
				
				promotionQuestionList.add(promotionQuestion);
			}
			
			return promotionQuestionList;
		}
		return null;
	}
	
	public Object parsePromotionCountResult(PList plist) throws FanclException {
		int status = ((Dict) plist.getRootElement()).getConfigurationInteger("status").getValue();
		if (status == 0) {
			PromotionCountResult promotionCountResult = new PromotionCountResult();
			promotionCountResult.setStatus(status);
			String countType = ((Dict) plist.getRootElement()).getConfiguration("countType").getValue();
			if (countType != null) {
				promotionCountResult.setCountType(countType);
			}
			String count = ((Dict) plist.getRootElement()).getConfiguration("count").getValue();
			if (count != null) {
				promotionCountResult.setCount(count);
			}
			String unreadCount = ((Dict) plist.getRootElement()).getConfiguration("unreadCount").getValue();
			if (unreadCount != null) {
				promotionCountResult.setUnreadCount(unreadCount);
			}
			
			return promotionCountResult;
		}
		else {
			FanclResultParser fanclResultParser = new FanclResultParser();
			return fanclResultParser.parseGeneralResult(plist);
		}
	}
	
	
}
