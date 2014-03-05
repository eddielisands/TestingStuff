package com.fancl.iloyalty.activity.product;

import java.util.Arrays;
import java.util.List;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.RelativeLayout;

import com.fancl.iloyalty.R;
import com.fancl.iloyalty.activity.MainNoTabActivity;
import com.fancl.iloyalty.adapter.ProductQnaViewFlowAdapter;
import com.fancl.iloyalty.exception.FanclException;
import com.fancl.iloyalty.factory.CustomServiceFactory;
import com.fancl.iloyalty.item.ViewFlow;
import com.fancl.iloyalty.pojo.ProductQuestion;
import com.fancl.iloyalty.util.LogController;

public class ProductQandAActivity extends MainNoTabActivity {
	
	private View qnaLayout;
	private ViewFlow qnaViewFlow;
	private ProductQnaViewFlowAdapter productQnaViewFlowAdapter;
	private List<ProductQuestion> productQuestionList;
	private RelativeLayout rightBtnLayout;
	private RelativeLayout leftBtnLayout;

	// WRT <<iloyalty_flow_V1.7_20120713.pdf>> screen 6.1.8, 6.1.9
	
	/** Called when the activity is first created. */
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        navigationBarDoneBtn.setVisibility(View.VISIBLE);
        navigationBarLeftBtn.setVisibility(View.VISIBLE);
        
        headerTitleTextView.setText(this.getResources().getString(
				R.string.qna_bar_title));
        
		this.setupSpaceLayout();
        
    }

	private void setupSpaceLayout() {
		// TODO Auto-generated method stub
		
		qnaLayout = (RelativeLayout) this.getLayoutInflater().inflate(
				R.layout.product_info_qna_page, null);
		
		spaceLayout.addView(qnaLayout, new LayoutParams(
				LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
		
		qnaViewFlow = (ViewFlow) findViewById(R.id.qna_detail_view_pager);
		
		try {
			productQuestionList = CustomServiceFactory.getProductService().getQnaProductQuestion();
		} catch (FanclException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		productQnaViewFlowAdapter = new ProductQnaViewFlowAdapter(this, productQuestionList);
		qnaViewFlow.setAdapter(productQnaViewFlowAdapter);
		LogController.log("productQuestionList.size():"+productQuestionList.size());
		qnaViewFlow.setIsQnaQuestionPage();
		
		leftBtnLayout = (RelativeLayout) findViewById(R.id.qna_left_btn);
		leftBtnLayout.setVisibility(View.GONE);

		rightBtnLayout = (RelativeLayout) findViewById(R.id.qna_right_btn);
		
		
		
		leftBtnLayout.setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View v) {
				LogController.log("left btn");
				qnaViewFlow.leftBtnAction();
				rightBtnLayout.setVisibility(View.VISIBLE);
				if(qnaViewFlow.getSelectedItemPosition() == 0){
					leftBtnLayout.setVisibility(View.GONE);
				}
				
				

			}
		});
		
		rightBtnLayout.setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View v) {
				LogController.log("right btn");
				qnaViewFlow.rightBtnAction();
				leftBtnLayout.setVisibility(View.VISIBLE);
				if(qnaViewFlow.getSelectedItemPosition() == (productQuestionList.size()-1)){
					rightBtnLayout.setVisibility(View.GONE);
				}

			}
		});
		
		navigationBarDoneBtn.setOnClickListener(new View.OnClickListener() {
			
			@SuppressWarnings("deprecation")
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				LogController.log("complete qna");
				

				String[] arrayAns = productQnaViewFlowAdapter.arrayAnsQna();
				String[] arrayAnsCode = productQnaViewFlowAdapter.arrayAnsCodeQna();
				List<String> listAns = Arrays.asList(arrayAns);
				List<String> listAnsCode = Arrays.asList(arrayAnsCode);
				
				Boolean notAnswerAll = false;
				
				for (int i = 0; i < arrayAns.length; i++) {
					if(arrayAns[i] == "-1")
						notAnswerAll = true;
				}
				
				if(notAnswerAll){
					AlertDialog alertDialog = new AlertDialog.Builder(
							ProductQandAActivity.this).create();
					alertDialog.setMessage(getResources().getString(R.string.promotion_detail_answer_all_questions));
					alertDialog.setButton(getResources().getString(R.string.ok_btn_title), new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int which) {
							// Write your code here to execute after dialog closed

						}
					});

					alertDialog.show();
					return;
				}
				
				CustomServiceFactory.getProductService().saveUserQnaAnwserId(listAns, listAnsCode);
				
//				String qnaAnswerString = "0";
//				if (arrayAns != null) {
//					for (int i = 0; i < arrayAns.length; i++) {
//						qnaAnswerString = qnaAnswerString + "," + arrayAns[i];
//					}
//				}
//	
//				SharedPreferences settings = getSharedPreferences (Constants.SHARED_PREFERENCE_APPLICATION_KEY, 0);
//			    SharedPreferences.Editor editor = settings.edit();
//			    editor.remove(Constants.QNA_ANSWER_ID);
//			    editor.putString(Constants.QNA_ANSWER_ID, qnaAnswerString);
//			    editor.commit();
				
				Intent intent = new Intent();
				setResult(RESULT_OK,intent );  
//				startActivityForResult(intent, Constants.PRODUCT_QNA);
				finish();
			}
		});
		

	}
	

	
}
