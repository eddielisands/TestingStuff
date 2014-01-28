package com.masterofcode.android.EPubBookRender;

import java.util.ArrayList;

import android.app.ActionBar;
import android.app.Activity;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.masterofcode.android.EPubBook.EPubBook;
import com.masterofcode.android.EPubBook.OPFInfoManifest;
import com.masterofcode.android.EPubBook.OPFInfoManifestItem;
import com.masterofcode.android.EPubBook.OPFInfoSpine;
import com.masterofcode.android.EPubBook.OPFInfoSpineItem;
import com.sandsmedia.apps.mobile.android.epub.lib.R;

public class EPubBookRender {
	final static public	String					FULLSCREEN_IMAGE_CONTENT_KEY = "image_content";
	final static public	String					FULLSCREEN_BASE_URL_KEY = "base_url";
	final static public	String					FULLSCREEN_IMAGE_URL_KEY = "image_url";
	final static public	String					FULLSCREEN_CONTENT_TYPE_KEY = "content_type";
	final static public	String					FULLSCREEN_EXTERNAL_CONTENT_KEY = "external_content";

	final static public int						TOC_EVENT_SHOW = 0;
	final static public int						TOC_EVENT_HIDE = 1;

	final static private int					TOC_IMAGE_PADDING_LEFT = 6;
	final static private int					TOC_IMAGE_PADDING_TOP = 6;
	final static private int					TOC_IMAGE_PADDING_RIGHT = 6;
	final static private int					TOC_IMAGE_PADDING_BOTTOM = 6;

	private	double								MIN_GESTURE_LENGTH = 70.0f;
	private int									RENDERING_TOPICS_COUNT = 5;
	private	EPubBook							book = null;
	private int									currentTopicIndex = 0;
	private ViewGroup							renderContainer;
	private Activity							context;
	private ArrayList<EPubBookTopicContainer>	topicContainers;
	private int									centerIndex;
	private float								msx, msy;
	private ViewGroup							renderController;
	private ViewGroup							renderView;
	
	private boolean								deviceOrientationIsLandscape = false;
	// toc
	private ArrayList<TOCImageView>				tocImages;
    private LinearLayout						tocRootLayout;
    private HorizontalScrollView				tocScrollView;
    private LinearLayout						tocContainer;
    private boolean								tocIsShowed = false;
    
    private Handler								topicChangeHandler = null;
    
    private int									fontChangeFactor = -1;
    
    private boolean								isHighlightMode = false;
    private ArrayList<Integer>					highlightingTopics = null;
    
    private TextView							paginationInfoTextView = null;
    private ProgressBar							paginationProgressBar = null;
    
    private ActionBar 							actionBar;
    
    private	Handler								tocEventsHandler;

	public EPubBookRender(EPubBook book, Activity context, final ViewGroup renderContainer)
	{
		super();
		this.book = book;
		this.context = context;
		this.renderContainer = renderContainer;
		
		if(context.getResources().getConfiguration().orientation==Configuration.ORIENTATION_LANDSCAPE)
		{
			deviceOrientationIsLandscape = true;
		}

		paginationInfoTextView = (TextView) context.findViewById(R.id.topic_page_indicator);
		paginationProgressBar= (ProgressBar) context.findViewById(R.id.topic_pagination_progress_bar);
				
		centerIndex = (int) Math.round(Math.ceil((double)RENDERING_TOPICS_COUNT / 2)) - 1;
		topicContainers = new ArrayList<EPubBookTopicContainer>(RENDERING_TOPICS_COUNT); 
		for (int i=0; i<RENDERING_TOPICS_COUNT; i++)topicContainers.add(null);
	}

	public void setHighlightMode(boolean mode)
	{
		isHighlightMode = mode;
	}
	
	public void setHighlightningTopics(ArrayList<Integer> topics)
	{
		highlightingTopics = topics;
	}
	
	public void setTopicChangeHandler(Handler topicChangeHandler)
	{
		this.topicChangeHandler = topicChangeHandler;
	}
	
	private void callTopicChangeHandlerWithCurrentTopicIndex()
	{
		if(topicChangeHandler!=null)
		{
			topicChangeHandler.sendMessage(topicChangeHandler.obtainMessage(0, currentTopicIndex, 0));
		}
	}

	public void changeFontWithFactor(int factor)  // factor is absolute, not relative
	{
		if(factor==fontChangeFactor)return;
		fontChangeFactor=factor;
		
		// TODO:
		// change font in current topic
		EPubBookTopicContainer		currentContainer = topicContainers.get(centerIndex);
		if(currentContainer!=null)
		{
			currentContainer.changeFontWithFactor(fontChangeFactor);
		}
		
		// change font in neighbours topic
		for (int i=0; i<RENDERING_TOPICS_COUNT; i++)
        {
        	if(i!=centerIndex)
        	{
            	if(isSpineItemValid(i-centerIndex+currentTopicIndex))
            	{
            		EPubBookTopicContainer			topic = topicContainers.get(i);
            		
            		if(topic!=null)
            		{
        				topic.changeFontWithFactor(fontChangeFactor);
            		}
            	}
        	}
        }
	}
	
	public void setFontChangeFactor(int factor)
	{
		fontChangeFactor = factor;
	}
	
	public void setRenderController(ViewGroup renderController)
	{
		this.renderController = renderController;
		
		LinearLayout		tocCallLayout = (LinearLayout) this.renderController.findViewById(R.id.toc_call_linearlayout);
		
		tocCallLayout.setOnClickListener(new View.OnClickListener()
		{
			public void onClick(View v)
			{
				onRenderControllerClick(v);
			}
		});
	}

	public boolean isTOCShow()
	{
		return tocIsShowed;
	}
	
	protected void addTOCImages(LinearLayout tocContainerParam)
	{
		OPFInfoSpine		spine = book.getOpfInfo().getSpine();
		OPFInfoManifest		manifest = book.getOpfInfo().getManifest();
		
		for(int i=0; i<spine.itemsSize(); i++)
		{
			OPFInfoSpineItem	spineItem = spine.getItemWithIndex(i);
			TOCImageView		tocImage = new TOCImageView(context);
			Uri					imageUri;
			String				landscapeShotRef = spineItem.getLanscapeShotRef(); 
			String				portraitShotRef = spineItem.getPortraitShotRef();
			String				curShotRef = portraitShotRef;
			
			if(deviceOrientationIsLandscape)
			{
				curShotRef = landscapeShotRef;
			}
			
			tocImage.setTopicId(i);
			
			if(curShotRef!=null)
			{
				OPFInfoManifestItem		mItem = manifest.getItemById(curShotRef);
				
				if(mItem != null)
				{
					String			curShotPath = mItem.getHref();
					
					if(curShotPath!=null)
					{
						imageUri = Uri.parse(book.getContentURL() + curShotPath);
						tocImage.setImageURI(imageUri);
						
						tocImage.setPadding(TOC_IMAGE_PADDING_LEFT, TOC_IMAGE_PADDING_TOP, TOC_IMAGE_PADDING_RIGHT, TOC_IMAGE_PADDING_BOTTOM);

			        	LinearLayout.LayoutParams lparams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
			            if(i>0)lparams.setMargins(0, 0, 0, 0);

			            tocContainerParam.addView(tocImage, lparams);
			        	
			        	tocImages.add(tocImage);
			        	
			        	tocImage.setOnClickListener(new OnClickListener() {
							public void onClick(View v) {
								TOCImageView	clickedTOCImage = (TOCImageView) v;
								
								if(clickedTOCImage.isSelected()) return;
								
								unselectTocImages();
								selectTocImage(clickedTOCImage);
								scrollToSelectedTOCImage();
								goToTopic(clickedTOCImage.getTopicId());
							}
						});
						if(currentTopicIndex==i) selectTocImage(tocImage);
					}
				}
			}
		}
	}
	
	private void scrollToTOCImage(TOCImageView tocImage)
	{
		if(tocImage == null)return;
		int				svCenter = tocScrollView.getWidth() / 2;
		int				x = tocImage.getLeft() - svCenter + tocImage.getWidth() / 2;
		
		if(x < 0) x = 0;
		tocScrollView.smoothScrollTo(x, 0);
	}

	private void scrollToSelectedTOCImage()
	{
		TOCImageView		selectedTOCImage = selectedTOCImage();
		
		if(selectedTOCImage != null) scrollToTOCImage(selectedTOCImage);
	}

	private TOCImageView selectedTOCImage()
	{
		if(tocImages==null) return null;
		for(int i=0; i<tocImages.size(); i++)
		{
			TOCImageView	curTOCImage = tocImages.get(i);
			
			if (curTOCImage.isSelected()) return curTOCImage;
		}
		return null;
	}
	
	private TOCImageView TOCImageWithTopicId(int topicId)
	{
		if(tocImages==null) return null;
		for(int i=0; i<tocImages.size(); i++)
		{
			TOCImageView	curTOCImage = tocImages.get(i);
			
			if (curTOCImage.getTopicId()==topicId) return curTOCImage;
		}
		return null;
	}

	private void unselectTocImage(TOCImageView tocImage)
	{
		tocImage.setBackgroundResource(0);
		tocImage.setSelected(false);
	}
	
	private void selectTocImage(TOCImageView tocImage)
	{
		if(tocImage==null) return;				// invalid magazine (without TOC)
		tocImage.setBackgroundResource(R.drawable.epub_toc_selection_bg);
		tocImage.setSelected(true);
	}

	private void unselectTocImages()
	{
		if(tocImages==null) return;
		for(int i=0; i<tocImages.size(); i++)
		{
			TOCImageView	curTOCImage = tocImages.get(i);
			
			if (curTOCImage.isSelected())
			{
				unselectTocImage(curTOCImage);
				break;
			}
		}
	}

	private void fixTOCselection() // prev/next topic
	{
		if(!tocIsShowed)
		{
			if(tocRootLayout!=null)
			{
				TOCImageView	curTOCImage = TOCImageWithTopicId(currentTopicIndex);

				unselectTocImages();
				selectTocImage(curTOCImage);
				scrollToSelectedTOCImage();
			}
		} else {
			TOCImageView curSelectedTOCImage = selectedTOCImage();
			if(curSelectedTOCImage!=null)
			{
				int curSelectedTOCIndex = curSelectedTOCImage.getTopicId();
				
				if(currentTopicIndex!=curSelectedTOCIndex)
				{
					TOCImageView		newSelectionImage = TOCImageWithTopicId(currentTopicIndex);
					
					unselectTocImage(curSelectedTOCImage);
					selectTocImage(newSelectionImage);
					scrollToTOCImage(newSelectionImage);
				}
			}
		}
	}
	
	private void goToTopic(int newTopicId)
	{
//		Log.d("----", "go to topic="+newTopicId);
		
		// simple prev/next case
		if(Math.abs(currentTopicIndex-newTopicId)==1)
		{
			if(newTopicId > currentTopicIndex)
			{
				loadNextTopic();
			} else {
				loadPrevTopic();
			}
		} else {
			EPubBookTopicContainer		existingContainer = findRenderedTopic(newTopicId);
			EPubBookTopicContainer		oldCenter = centerTopic();
			int							existingIndex = renderedTopicIndex(existingContainer);
			
			
			currentTopicIndex = newTopicId;
			
			// set existing as center, swap it with center
			if(existingContainer!=null && existingIndex!=-1)
			{
				topicContainers.set(centerIndex, existingContainer);
				topicContainers.set(existingIndex, oldCenter);
				oldCenter.getParentContainer().setVisibility(View.GONE);
				existingContainer.getParentContainer().setVisibility(View.VISIBLE);
			} else {		// current center became new center if not found in queue
				oldCenter.setParentScrollHandler(topicScrollHandler);
				oldCenter.setHighlightMode(isTopicHiglighted(newTopicId));
				oldCenter.setSpineIndex(book, newTopicId);
				oldCenter.changeFontWithFactor(fontChangeFactor);
				oldCenter.load();
			}
			
			loadNeighbours();
			callTopicChangeHandlerWithCurrentTopicIndex();
			//updatePaginationInfo();
		}
		updateActionBarTitle();
	}
	
	public void goToTopicViaTextTOC(int index)
	{
		if(index==currentTopicIndex) return;   // we are already there
		
		if(tocRootLayout!=null)
		{
			TOCImageView	clickedTOCImage = TOCImageWithTopicId(index);

			unselectTocImages();
			selectTocImage(clickedTOCImage);
			scrollToSelectedTOCImage();
		}
		goToTopic(index);
	}
	
	private EPubBookTopicContainer centerTopic()
	{
		return topicContainers.get(centerIndex);
	}
	
	private int renderedTopicIndex(EPubBookTopicContainer container)
	{
		for(int i=0; i<RENDERING_TOPICS_COUNT; i++)
		{
			EPubBookTopicContainer		currentTopicContainer = topicContainers.get(i);
			
			if(currentTopicContainer == container)
			{
				return i;
			}
		}
		return -1;
	}
	
	private EPubBookTopicContainer findRenderedTopic(int topicId)
	{
		for(int i=0; i<RENDERING_TOPICS_COUNT; i++)
		{
			EPubBookTopicContainer		currentTopicContainer = topicContainers.get(i);
			
			if(currentTopicContainer != null)
			{
				if(currentTopicContainer.getTopicId()>=0)
				{
					if(currentTopicContainer.getTopicId() == topicId)
					{
						if(currentTopicContainer.getContentLoadingStatus()==EPubBookTopicContainer.CONTENT_LOADING_STATUS_LOADINF_FINISHED)
						{
							return currentTopicContainer;
						}
					}
				}
			}
		}
		return null;
	}
	
	public void showTOC()
	{
		if(tocIsShowed)return;
		if(tocEventsHandler!=null)
		{
			tocEventsHandler.sendEmptyMessage(TOC_EVENT_SHOW);
		}
		if(tocRootLayout==null)
		{
			tocImages = new ArrayList<TOCImageView>(); 
				
			View renderTocRoot = context.getLayoutInflater().inflate(R.layout.epub_toc_container, null);
			tocRootLayout = (LinearLayout) renderTocRoot.findViewById(R.id.tocRoot);
			tocScrollView = (HorizontalScrollView) renderTocRoot.findViewById(R.id.tocScrollView);
			tocContainer =  (LinearLayout) renderTocRoot.findViewById(R.id.tocContainer);
			
			tocScrollView.setSmoothScrollingEnabled(true);
			
			RelativeLayout.LayoutParams		params = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.FILL_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
			params.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM, RelativeLayout.TRUE);
			
	        tocRootLayout.setVisibility(View.GONE);
			renderView.addView(tocRootLayout, params);
			
			addTOCImages(tocContainer);

			Button		tocHideButton = (Button) renderTocRoot.findViewById(R.id.tocHideButton);

			tocHideButton.setOnClickListener(new OnClickListener() {
				public void onClick(View v) {
					hideTOC();
				}
			});
		}
		// animation
		Animation anim_in = AnimationUtils.loadAnimation(context, R.anim.epub_toc_animation_in);

        anim_in.setAnimationListener(new AnimationListener() {
			public void onAnimationStart(Animation animation) {
			}
			
			public void onAnimationRepeat(Animation animation) {
			}
			
			public void onAnimationEnd(Animation animation) {
		        scrollToSelectedTOCImage();
			}
		});

		tocRootLayout.startAnimation(anim_in);
        tocRootLayout.setVisibility(View.VISIBLE);
        tocIsShowed = true;
	}

	protected void hideTOC()
	{
		if(!tocIsShowed)return;
		if(tocRootLayout==null)return;
		
		if(tocEventsHandler!=null)
		{
			tocEventsHandler.sendEmptyMessage(TOC_EVENT_HIDE);
		}
		
		// animation
		Animation anim_out = AnimationUtils.loadAnimation(context, R.anim.epub_toc_animation_out);
        tocRootLayout.startAnimation(anim_out);
        tocRootLayout.setVisibility(View.GONE);

        tocIsShowed = false;
	}

	public float getCurrentTopicContentOffset()
	{
		EPubBookTopicContainer		currentTopicContainer = centerTopic(); 
		return currentTopicContainer.getContentOffset();
	}
	
	public void setCurrentTopicContentOffset(float offset)
	{
		EPubBookTopicContainer		currentTopicContainer = centerTopic();
		currentTopicContainer.setContentOffset(offset);
	}

	protected void onRenderControllerClick(View v)
	{
		if(book==null) return;
		showTOC();
	}

	public ViewGroup getRenderController()
	{
		return renderController;
	}

	public void setRenderView(ViewGroup renderView)
	{
		this.renderView = renderView;
	}

	public ViewGroup getRenderView()
	{
		return renderView;
	}

	public EPubBook getBook()
	{
		return book;
	}

	public void setBook(EPubBook book)
	{
		this.book = book;
	}

	public int getCurrentTopicIndex()
	{
		return currentTopicIndex;
	}

	public String getCurrentTopicFilePath()
	{
		return book.getTopicOriginalURLByIndex(currentTopicIndex);
	}
	
	public void setCurrentTopicIndex(int currentTopicIndex)
	{
		if(isSpineItemValid(currentTopicIndex))
		{
			this.currentTopicIndex = currentTopicIndex;
		}
	}

	public boolean isSpineIsValid()
	{
		if(book==null) return false;
		if(book.getOpfInfo()==null) return false;
		if(book.getOpfInfo().getSpine()==null) return false;
		return true;
	}
	
	public boolean isSpineItemValid(int spineItem)
	{
		if(spineItem<0) return false;
		if(!isSpineIsValid()) return false;
		if(spineItem >= book.getOpfInfo().getSpine().itemsSize()) return false;
		return true;
	}

	public boolean isPreviousSpineItemExist()
	{
		return isSpineItemValid(currentTopicIndex - 1);
	}

	public boolean isNextSpineItemExist()
	{
		return isSpineItemValid(currentTopicIndex + 1);
	}

	public void selectPreviousSpineItem()
	{
		if(isPreviousSpineItemExist())currentTopicIndex--;
	}

	public void selectNextSpineItem()
	{
		if(isNextSpineItemExist())currentTopicIndex++;
	}

	public String getCurrentSpineItemContent() throws Exception
	{
		if(isSpineItemValid(currentTopicIndex))
		{
			return book.getSpineItemContentByIndex(currentTopicIndex);
		}
		return "";
	}

	public String getCurrentSpineItemPath()
	{
		if(isSpineItemValid(currentTopicIndex))
		{
			return book.getTopicOriginalURLByIndex(currentTopicIndex);
		}
		return "";
	}

	private EPubBookTopicContainer createCenterTopic()
	{
		EPubBookTopicContainer			centerTopic = topicContainers.get(centerIndex);
		View							container;
		
		if (centerTopic==null)
		{
			centerTopic = new EPubBookTopicContainer(context, deviceOrientationIsLandscape, topicScrollHandler);
			centerTopic.setTopicHandler(centerTopicHandler);
			centerTopic.setParentScrollHandler(topicScrollHandler);
			
			topicContainers.set(centerIndex, centerTopic);
			renderContainer.addView(centerTopic.getParentContainer());
		}
		container = centerTopic.getParentContainer();

		centerTopic.setHighlightMode(isTopicHiglighted(currentTopicIndex));
		centerTopic.setSpineIndex(book, currentTopicIndex);
		centerTopic.changeFontWithFactor(fontChangeFactor);
		centerTopic.load();
		container.setVisibility(View.VISIBLE);
		updateScrollButtons(true);
    	updateActionBarTitle();
		return centerTopic;
	}

	private boolean isTopicHiglighted(int index)
	{
		if(isHighlightMode && highlightingTopics!=null)
		{
			for(int i=0; i<highlightingTopics.size(); i++)
			{
				if(highlightingTopics.get(i).intValue()==index) return true;
			}
		}
		return false;
	}
	
	public void render()
	{
		if(isHighlightMode && highlightingTopics==null) isHighlightMode = false;
		createCenterTopic();
		//updatePaginationInfo();
	}
	
	private void loadNeighbours()
	{
        for (int i=0; i<RENDERING_TOPICS_COUNT; i++)
        {
        	if(i!=centerIndex)
        	{
            	if(isSpineItemValid(i-centerIndex+currentTopicIndex))
            	{
            		EPubBookTopicContainer			topic = topicContainers.get(i);
            		
            		if(topic==null)
            		{
            			topic = new EPubBookTopicContainer(context, deviceOrientationIsLandscape, topicScrollHandler);
            			topicContainers.set(i, topic);
                		topic.getParentContainer().setVisibility(View.GONE);
                		renderContainer.addView(topic.getParentContainer());
            		}
            		topic.setHighlightMode(isTopicHiglighted(i-centerIndex+currentTopicIndex));
            		topic.setSpineIndex(book, i-centerIndex+currentTopicIndex);
        			topic.setParentScrollHandler(topicScrollHandler);
    				topic.changeFontWithFactor(fontChangeFactor);
            		topic.load();
            	}
        	}
        }
		
//		Log.i("--------", "center finished");
		EPubBookTopicContainer			centerTopic = topicContainers.get(centerIndex);
		setOnTouchListener(centerTopic.getContentWebView());
	}
	
	private void setOnTouchListener(View v)
	{
		v.setOnTouchListener(new View.OnTouchListener() {
			public boolean onTouch(View v, MotionEvent event) {
				return onRenderContainerTouch(v, event);
			}
        });

	}

	@SuppressWarnings("unused")
	private void contentPgDown()
	{
		topicContainers.get(centerIndex).pageDown();
//		updatePaginationInfo();
	}
	
	@SuppressWarnings("unused")
	private void contentPgUp()
	{
		topicContainers.get(centerIndex).pageUp();
//		updatePaginationInfo();
	}
	
	private void updateActionBarTitle()
	{
		EPubBookTopicContainer			current = topicContainers.get(centerIndex);
		String							title = String.format("%s | %s", book.getTitle(), book.getTopicTitle(currentTopicIndex));
		
		actionBar.setTitle(title);
	}
	
	public boolean isPaginationFinished()
	{
		EPubBookTopicContainer			current = topicContainers.get(centerIndex);
		
		if(current!=null)
		{
			if(current.isPaginationFinished())
			{
				return true;
			}
		}
		return false;
	}
	
	private void updatePaginationInfo()
	{
		EPubBookTopicContainer			current = topicContainers.get(centerIndex);
		
		if(current.isPaginationFinished())
		{
			String			info;
		
			if(paginationProgressBar.getVisibility()==View.VISIBLE)
			{
				paginationProgressBar.setVisibility(View.GONE);
			}
			info = String.format("page %d of %d", current.paginationCurrentPage(), current.paginationPageCount());
			paginationInfoTextView.setText(info);
		} else {
			paginationInfoTextView.setText("");
			paginationProgressBar.setVisibility(View.VISIBLE);
		}
		if(topicChangeHandler!=null)topicChangeHandler.sendMessage(topicChangeHandler.obtainMessage(1, 0, 0));
	}
	
	private void updateScrollButtons(boolean newTopic)
	{
		
		//EPubBookTopicContainer			current = topicContainers.get(centerIndex);
		
//		Log.i("----------------", "Update Scroll B: new="+newTopic);
/*		if(newTopic)
		{
			scrollUpButton.setEnabled(false);
			scrollDownButton.setEnabled(false);
		} else {
			scrollUpButton.setEnabled(current.isCanScrollUp());
			scrollDownButton.setEnabled(current.isCanScrollDown());
		}*/
	}
	
	public void loadNextTopic()
	{
//		Log.i("---", "next topic. currentTopicIndex="+currentTopicIndex+",  size="+book.getOpfInfo().getSpine().itemsSize());
		
	    if (!isSpineItemValid(currentTopicIndex+1)) return;
        int     rightMostIndex = RENDERING_TOPICS_COUNT - centerIndex + currentTopicIndex;

	    View		curContainer = topicContainers.get(centerIndex).getParentContainer();

	    currentTopicIndex++;

	    EPubBookTopicContainer       leftMost = topicContainers.remove(0);
        topicContainers.add(leftMost);

	    View		newContainer = topicContainers.get(centerIndex).getParentContainer();

        if(isSpineItemValid(rightMostIndex)) {
        	if(leftMost==null){
        		leftMost = new EPubBookTopicContainer(context, deviceOrientationIsLandscape, topicScrollHandler);
    		    View		container = leftMost.getParentContainer();
    		    renderContainer.addView(container);
    		    container.setVisibility(View.GONE);
    		    topicContainers.set(RENDERING_TOPICS_COUNT-1, leftMost);
        	}
    		leftMost.setHighlightMode(isTopicHiglighted(rightMostIndex));
        	updateScrollButtons(true);
		    leftMost.setSpineIndex(book, rightMostIndex);
			leftMost.changeFontWithFactor(fontChangeFactor);
		    leftMost.load();
        } else  {
        	updateScrollButtons(false);
        }
	    Animation anim_in = AnimationUtils.loadAnimation(context, R.anim.epub_render_animation_next_topic_in);
    	newContainer.setVisibility(View.VISIBLE);
    	newContainer.startAnimation(anim_in);


	    Animation anim_out = AnimationUtils.loadAnimation(context, R.anim.epub_render_animation_next_topic_out);
    	curContainer.startAnimation(anim_out);
    	curContainer.setVisibility(View.GONE);

    	setOnTouchListener(topicContainers.get(centerIndex).getContentWebView());
    	
    	topicContainers.get(centerIndex).setParentScrollHandler(topicScrollHandler);
    	
    	fixTOCselection();
    	callTopicChangeHandlerWithCurrentTopicIndex();
    	
    	updateActionBarTitle();
    	//updatePaginationInfo();
	}
	
	public void loadPrevTopic()
	{
//		Log.i("---", "prev topic. currentTopicIndex="+currentTopicIndex+",  size="+book.getOpfInfo().getSpine().itemsSize());
		
	    if (!isSpineItemValid(currentTopicIndex-1)) return;
	    
	    View		curContainer = topicContainers.get(centerIndex).getParentContainer();

	    currentTopicIndex--;
	    int     leftMostIndex = currentTopicIndex - centerIndex;

	    EPubBookTopicContainer       rightMost = topicContainers.remove(RENDERING_TOPICS_COUNT - 1);
        topicContainers.add(0, rightMost);

	    View		newContainer = topicContainers.get(centerIndex).getParentContainer();

        if(isSpineItemValid(leftMostIndex)) {
        	if(rightMost==null){
        		rightMost = new EPubBookTopicContainer(context, deviceOrientationIsLandscape, topicScrollHandler);
    		    View		container = rightMost.getParentContainer();
    		    renderContainer.addView(container);
    		    container.setVisibility(View.GONE);
    		    topicContainers.set(0, rightMost);
        	}
    		rightMost.setHighlightMode(isTopicHiglighted(leftMostIndex));
        	updateScrollButtons(true);
		    rightMost.setSpineIndex(book, leftMostIndex);
			rightMost.changeFontWithFactor(fontChangeFactor);
		    rightMost.load();
        } else {
        	updateScrollButtons(false);
        }
	    Animation anim_in = AnimationUtils.loadAnimation(context, R.anim.epub_render_animation_prev_topic_in);
    	newContainer.setVisibility(View.VISIBLE);
    	newContainer.startAnimation(anim_in);


	    Animation anim_out = AnimationUtils.loadAnimation(context, R.anim.epub_render_animation_prev_topic_out);
    	curContainer.startAnimation(anim_out);
    	curContainer.setVisibility(View.GONE);

    	setOnTouchListener(topicContainers.get(centerIndex).getContentWebView());
    	topicContainers.get(centerIndex).setParentScrollHandler(topicScrollHandler);
    	
    	fixTOCselection();
    	callTopicChangeHandlerWithCurrentTopicIndex();
    	
    	updateActionBarTitle();
    	//updatePaginationInfo();
	}

	public boolean onRenderContainerTouch(View v, MotionEvent event) {
		// true -  we are consume event
		// false - left for system processing
		
		if(isTOCShow())			// ignoring all, except UP
		{
	    	if(event.getAction()==MotionEvent.ACTION_UP)
	    	{
	    		hideTOC();
	    	}
	    	return true;
		}
/*		if(true)
		{
	    	if(event.getAction()==MotionEvent.ACTION_UP){
	    		Log.i("-------", "touch UP!");
	    	}
			return false;
		}
	*/	
		return false;
/*    	if(event.getAction()==MotionEvent.ACTION_DOWN){
    		msx = event.getX();
    		msy = event.getY();
    	}
    	if(event.getAction()==MotionEvent.ACTION_UP){
    		float mex = event.getX();
    		float mey = event.getY();
    		
    		double width = Math.abs(mex - msx);
    		double height = Math.abs(mey - msy);
    		
    		double length = Math.sqrt(width*width + height*height);
    		
    		if(length<MIN_GESTURE_LENGTH)
   			{
    			if(tocIsShowed)hideTOC();
    			return false;
   			}
    		
    		if (height > width)
    		{
    			if(mey <= msy)
    			{
    				//contentPgDown();
    				return false;
    			} else {
    				//contentPgUp();
    				return false;
    			}
    		} else {
    			if(mex <= msx) {
    				loadNextTopic();
    			} else {
    				loadPrevTopic();
    			}
    		}
    	}
        return(event.getAction() == MotionEvent.ACTION_MOVE);*/
    }
	
    public ActionBar getActionBar()
    {
		return actionBar;
	}

	public void setActionBar(ActionBar actionBar)
	{
		this.actionBar = actionBar;
	}

	public Handler getTocEventsHandler()
	{
		return tocEventsHandler;
	}

	public void setTocEventsHandler(Handler tocEventsHandler)
	{
		this.tocEventsHandler = tocEventsHandler;
	}

	Handler centerTopicHandler = new Handler() {
        public void handleMessage(Message msg) {
        	if(msg.what==EPubBookTopicContainer.CONTENT_PAGE_FINISHED)
        	{
        		topicContainers.get(centerIndex).setTopicHandler(null);
        		loadNeighbours();
        		//setOnTouchListener();
        	}
        }
    };

	Handler topicScrollHandler = new Handler() {
        public void handleMessage(Message msg) {
        	if(msg.what==EPubBookTopicContainer.CONTENT_SCROLLING_FINISHED)
        	{
        		updateScrollButtons(false);
        	}

        	if(msg.what==EPubBookTopicContainer.CONTENT_PAGINATION_FINISHED)
        	{
        		//updatePaginationInfo();
        	}

        	if(msg.what==EPubBookTopicContainer.CONTENT_UPDATE_PAGE_INFO)
        	{
        		//updatePaginationInfo();
        	}
        }
    };
}
