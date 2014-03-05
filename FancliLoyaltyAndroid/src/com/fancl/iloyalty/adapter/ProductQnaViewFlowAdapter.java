package com.fancl.iloyalty.adapter;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.fancl.iloyalty.R;
import com.fancl.iloyalty.exception.FanclException;
import com.fancl.iloyalty.factory.CustomServiceFactory;
import com.fancl.iloyalty.factory.GeneralServiceFactory;
import com.fancl.iloyalty.pojo.ProductAnswer;
import com.fancl.iloyalty.pojo.ProductQuestion;
import com.fancl.iloyalty.service.LocaleService;
import com.fancl.iloyalty.service.impl.LocaleServiceImpl.LANGUAGE_TYPE;
import com.fancl.iloyalty.util.LogController;

public class ProductQnaViewFlowAdapter extends BaseAdapter{
	private Context mContext;
	private LayoutInflater mInflater;
	private List<ProductQuestion> questionList;
	private List<ProductAnswer> answerList;
	String[] arrayAns;
	String[] arrayAnsCode;
	
	private LocaleService localeService;
	
	private int positionZero = 0;

	
	public ProductQnaViewFlowAdapter(Context context, List<ProductQuestion> list) {
		mContext = context;
		questionList = list;
		arrayAns = new String[questionList.size()];
		arrayAnsCode = new String[questionList.size()];
		for (int i = 0; i < questionList.size(); i++) {
			arrayAns[i]="-1";
			arrayAnsCode[i]="-1";
		}

		mInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		
		localeService = GeneralServiceFactory.getLocaleService();
	}
	
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return questionList.size();
	}
	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return 0;
	}
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		if (convertView == null) {
			convertView = mInflater.inflate(R.layout.product_qna_detail, null);
		}

		TextView stepText = (TextView) convertView.findViewById(R.id.qna_step);
		stepText.setText(R.string.qna_step);
		stepText.append(" "+(position+1));
		if(localeService.getCurrentLanguageType().equals(LANGUAGE_TYPE.EN)){
			
		}else{
			String tmp = mContext.getString(R.string.qna_step_2);
			stepText.append(" "+tmp);
		}
		
		TextView questionText = (TextView) convertView.findViewById(R.id.qna_question);
		questionText.setText(localeService.textByLangaugeChooser(mContext, questionList.get(position).getQuestionEn(), questionList.get(position).getQuestionZh(), questionList.get(position).getQuestionSc()));
		
		
		LinearLayout qnaLayout = (LinearLayout) convertView.findViewById(R.id.qna_detail_layout);
		RadioGroup answersGroup = new RadioGroup(mContext);
		answersGroup.setOrientation(RadioGroup.VERTICAL);

		try {
			answerList = CustomServiceFactory.getProductService().getQnaAnswerWithQuestionId(questionList.get(position).getObjectId());
		} catch (FanclException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		LogController.log("answerlist:"+answerList.size()+", question:"+questionList.get(position).getQuestionEn());
		
		if(position == 0){
			positionZero++;
		}
		
		LogController.log("postion question:"+position);
		
		if(positionZero<=1 || (position==0 && positionZero>2) || position == questionList.size()-1){

			for (int j = 0; j < answerList.size(); j++) {
				LogController.log("add answer"+",question:"+questionList.get(position).getQuestionEn());
				int answerIndex = position*10+j+1;
				this.addAnswer(answersGroup, localeService.textByLangaugeChooser(mContext, answerList.get(j).getAnswerEn(), answerList.get(j).getAnswerZh(), answerList.get(j).getAnswerSc()), answerIndex);
			}
			LinearLayout.LayoutParams answersGroupLayoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
			answersGroupLayoutParams.setMargins(20, 0, 10, 10);
			qnaLayout.addView(answersGroup, answersGroupLayoutParams);
		}

		
		return convertView;
	}

	private void addAnswer(RadioGroup group, String answerStr, int answerId) {
		LogController.log("create answer button");
		RadioButton answerButton = new RadioButton(mContext);
		answerButton.setId(answerId);
		answerButton.setTextColor(mContext.getResources().getColor(R.color.Fancl_Grey));
		answerButton.setText(answerStr);
		answerButton.setButtonDrawable(R.drawable.radiobutton);
		group.addView(answerButton);

		answerButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				RadioButton tmpButton = (RadioButton) v;
				LogController.log("click qna ans:"+tmpButton.getId());
				int questionIndex = tmpButton.getId()/10;
				for (int i = 0; i < questionList.size(); i++) {
					if (i == questionIndex) {
						List<ProductAnswer> tmpAnswerList = null;
						try {
							tmpAnswerList = CustomServiceFactory.getProductService().getQnaAnswerWithQuestionId(questionList.get(i).getObjectId());
						} catch (FanclException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						arrayAns[i] = tmpAnswerList.get(tmpButton.getId()-10*questionIndex-1).getObjectId();
						arrayAnsCode[i] = tmpAnswerList.get(tmpButton.getId()-10*questionIndex-1).getAnswerCode();
//						arrayAns[i] = tmpButton.getId()-10*questionIndex-1;
					}
				}
				for (int j = 0; j < questionList.size(); j++) {
					LogController.log("array qna ans"+j + ":"+ arrayAns[j]);
				}
				
			}
		});
	}
	
	public String[] arrayAnsQna() {
		// TODO Auto-generated method stub
		return arrayAns;
	}
	
	public String[] arrayAnsCodeQna() {
		// TODO Auto-generated method stub
		return arrayAnsCode;
	}

}
