package com.masterofcode.android.magreader.utils;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;

import org.kroz.activerecord.ActiveRecordBase;
import org.kroz.activerecord.ActiveRecordException;

import com.masterofcode.android.magreader.MainLibraryActivity;
import com.masterofcode.android.magreader.application.JtjApplication;
import com.masterofcode.android.magreader.db.entity.IssueItem;
import com.masterofcode.android.magreader.db.entity.LibraryItem;
import com.masterofcode.android.magreader.db.entity.PurchaseItem;
import com.masterofcode.android.magreader.inapp.Consts;
import com.masterofcode.android.magreader.library.LibraryManager;
import com.masterofcode.android.magreader.utils.constants.Constants;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.res.AssetManager;
import android.os.AsyncTask;
import android.text.TextUtils;
import android.util.Log;

public class DownloadManager {

	private static final int DIALOG_DOWNLOAD_PROGRESS = 1;
	private Context context;
	private ProgressDialog mProgressDialog;
	private ActiveRecordBase _db;
	private ActiveRecordBase _db_purchase;
	private List<IssueItem> mIssues;
	private List<PurchaseItem> mPurchases;
	
	public DownloadManager(Context context){
		this.context = context;
	}
	public void startDownloadCover(String coverUrl) {
		//downloadCover(coverUrl, getCoverFileNameFromUrl(coverUrl));
        new DownloadCoverDialogAsync().execute(coverUrl); 
       
    }
	
	public void startDownloadIssue(String url) {
        new DownloadIssueAsync().execute(url); 
    }
	
	public void startCopyFromResources(String path){
		new DownloadIssueFromResAsync().execute(path);
	}
	
	//for downloading purchased early issues(if app run on new device)
	public void startDownloadWithoutUI(String url, String coverUrl) {
		downloadCover(coverUrl, getCoverFileNameFromUrl(coverUrl));
 //       new DownloadFileWithoutDialogAsync().execute(url); 
    }
	
    protected Dialog onCreateDialog(int id) {
        switch (id) {
            case DIALOG_DOWNLOAD_PROGRESS:
                mProgressDialog = new ProgressDialog(context);
                mProgressDialog.setMessage("Downloading file..");
                mProgressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                mProgressDialog.setCancelable(false);
                mProgressDialog.show();
                return mProgressDialog;
            default:
                return null;
        }
    }
    
    private void downloadCover(final String coverURL, final String coverFileName){
    	new Thread(new Runnable() {
            public void run() {
            	int count;
            	try {
                    URL url = new URL(coverURL);
                    URLConnection conexion = url.openConnection();
                    conexion.connect();

                    int lenghtOfFile = conexion.getContentLength();
                    Log.d("ANDRO_ASYNC", "Lenght of file: " + lenghtOfFile);
                    
                    File dir = context.getDir("library", Context.MODE_PRIVATE);
                    File newfile = new File(dir.getAbsolutePath() + File.separator + coverFileName);
                    
                    InputStream input = new BufferedInputStream(url.openStream());
                    OutputStream output = new FileOutputStream(newfile.getAbsolutePath());

                    byte data[] = new byte[1024];

                    while ((count = input.read(data)) != -1) {
                        output.write(data, 0, count);
                    }
                    output.flush();
                    output.close();
                    input.close();
                } catch (Exception e) {
                	e.printStackTrace();
                }
            }
        }).start();
    }
    
    class DownloadIssueFromResAsync extends AsyncTask<String, String, String> {
    	
    	@Override
        protected void onPreExecute() {
            super.onPreExecute();
            onCreateDialog(DIALOG_DOWNLOAD_PROGRESS);
        }
    	
    	@Override
        protected String doInBackground(String... aurl) {
    		
    		final int BUFFER_SIZE = 1024 * 256;                                                                                                     
            
            try {
            	// copy epub
            	File dir = context.getDir("library", Context.MODE_PRIVATE);
                File newfile = new File(dir.getAbsolutePath() + File.separator + aurl[0]);
            	
            	//String				destinationFilePath = libraryPath(context) + File.separator + magazineForCopying;                                                                
            	//File                destinationFile = new File(destinationFilePath);                                                                 
            	FileOutputStream 	destinationStream = new FileOutputStream(newfile);                                                                          
            	AssetManager        aManager = context.getAssets();                                                                         
            	InputStream 		sourceStream = aManager.open(aurl[0]);
            	long				totalLength = 0, copied = 0;
                                                                                                                                    
            	byte[] buffer = new byte[BUFFER_SIZE];                                                                                      
            	int len = 0;                                                                                                                

            	// determine length
            	while ( (len = sourceStream.read(buffer)) > 0 ) {
            		totalLength += len;
            	}
            	
            	// copying
            	sourceStream.close();
            	sourceStream = aManager.open(aurl[0]);
            	
            	long total = 0;
            	
            	while ( (len = sourceStream.read(buffer)) > 0 ) {
            		total +=len;
            		publishProgress(""+(int)((total*100)/totalLength));
            		destinationStream.write(buffer, 0, len);
            	}                                                                                                                           
                                                                                                                                    
            	sourceStream.close();
            	destinationStream.close();
            	LibraryManager.GetInstance().createMagazineCopiedMark(context, aurl[0]);
            	//createMagazineCopiedMark(context, aurl[0]);
            	
            } catch (Exception e) {                                                                                                 
                e.printStackTrace();                                                                                            
            }
            return null;
    		
    	}
    	
    	protected void onProgressUpdate(String... progress) {
            //Log.d("ANDRO_ASYNC",progress[0]);
            mProgressDialog.setProgress(Integer.parseInt(progress[0]));
       }

       @Override
       protected void onPostExecute(String unused) {
           mProgressDialog.dismiss();
       }
    	
    }
    
    class DownloadIssueAsync extends AsyncTask<String, String, String> {
        
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            onCreateDialog(DIALOG_DOWNLOAD_PROGRESS);
        }

		@Override
        protected String doInBackground(String... aurl) {
            int count;
            if(ApplicationUtils.checkIssueIsAvail()){
            try {
                URL url = new URL(aurl[0]);
                URLConnection conexion = url.openConnection();
                conexion.connect();

                int lenghtOfFile = conexion.getContentLength();
                Log.d("ANDRO_ASYNC", "Lenght of file: " + lenghtOfFile);
                
                String filename = ApplicationUtils.getEpubFileNameFromUrl(url);
                
                File dir = context.getDir("library", Context.MODE_PRIVATE);
                File newfile = new File(dir.getAbsolutePath() + File.separator + filename);
                
                InputStream input = new BufferedInputStream(url.openStream());
                OutputStream output = new FileOutputStream(newfile.getAbsolutePath());

                byte data[] = new byte[1024];

                long total = 0;

                while ((count = input.read(data)) != -1) {
                    total += count;
                    publishProgress(""+(int)((total*100)/lenghtOfFile));
                    output.write(data, 0, count);
                }
                output.flush();
                output.close();
                input.close();
            } catch (Exception e) {
            	e.printStackTrace();
            }
            } else{
            	ApplicationUtils.createNoInternetDialog(context).show();
            }
            return null;

        }
        protected void onProgressUpdate(String... progress) {
             //Log.d("ANDRO_ASYNC",progress[0]);
             mProgressDialog.setProgress(Integer.parseInt(progress[0]));
        }

        @Override
        protected void onPostExecute(String unused) {
            mProgressDialog.dismiss();
        }
    }
    
    class DownloadCoverDialogAsync extends AsyncTask<String, String, String> {
        
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            onCreateDialog(DIALOG_DOWNLOAD_PROGRESS);
        }

		@Override
        protected String doInBackground(String... aurl) {
            int count;

            try {
                URL url = new URL(aurl[0]);
                URLConnection conexion = url.openConnection();
                conexion.connect();

                int lenghtOfFile = conexion.getContentLength();
                Log.d("ANDRO_ASYNC", "Lenght of file: " + lenghtOfFile);
                
                File dir = context.getDir("library", Context.MODE_PRIVATE);
                File newfile = new File(dir.getAbsolutePath() + File.separator + getCoverFileNameFromUrl(aurl[0]));
                
                InputStream input = new BufferedInputStream(url.openStream());
                OutputStream output = new FileOutputStream(newfile.getAbsolutePath());

                byte data[] = new byte[1024];

                long total = 0;
                
                while ((count = input.read(data)) != -1) {
                	total += count;
                    publishProgress(""+(int)((total*100)/lenghtOfFile));
                    output.write(data, 0, count);
                }
                output.flush();
                output.close();
                input.close();
            } catch (Exception e) {
            	e.printStackTrace();
            }
            return null;

        }
		protected void onProgressUpdate(String... progress) {
            //Log.d("ANDRO_ASYNC",progress[0]);
            mProgressDialog.setProgress(Integer.parseInt(progress[0]));
       }

       @Override
       protected void onPostExecute(String unused) {
           mProgressDialog.dismiss();
       }
    }
    
    public void downloadCoversPurchasedIssues(){
    	try{
    		_db = JtjApplication.getInstance().getDatabase();
    		_db_purchase = JtjApplication.getInstance().getPurchaseDatabase();
    		_db_purchase.open();
    		mPurchases = _db_purchase.findAll(PurchaseItem.class);
    		ArrayList<String> mPurchasedIdList = new ArrayList<String>();
    		List<LibraryItem> issuesInLibrary = LibraryManager.GetInstance().queryLibraryItems();
    		for (PurchaseItem mPurchaseItem : mPurchases){
    			mPurchasedIdList.add(mPurchaseItem.product_id);
    		}
    		if (Consts.DEBUG) {
                Log.d("downlcover", "mPurchasedIdList.size(): " + mPurchasedIdList.size());
            }
    		_db_purchase.close();
    		_db.open();
    		mIssues = _db.find(IssueItem.class, false, "_id>?", new String[] { String.valueOf("0") }, null, null, "PUBLICATION_DATE DESC", null);
    		if (mPurchasedIdList != null)
    			for (String purchasedId : mPurchasedIdList){
    				for (IssueItem issue : mIssues){
    					if (issue.googlecheckoutid.equals(purchasedId)){
    						if (Consts.DEBUG) {
    			                Log.d("downlcover", "equals: " + issue.googlecheckoutid + " --- " + issue.downloadUrl);
    			            }
    						boolean isInLibrary = false;
    						for(LibraryItem li : issuesInLibrary){
    							if (Consts.DEBUG) {
        			                Log.d("downlcover", "li.magazine_url: " + li.magazine_url + " --- " + issue.downloadUrl);
        			            }
    							if(!TextUtils.isEmpty(li.magazine_url) && li.magazine_url.equals(issue.downloadUrl)){
    								isInLibrary = true;
    								if (Consts.DEBUG) {
    	    			                Log.d("downlcover", "inLibrary: " + issue.downloadUrl);
    	    			            }
    								break;
    							}
    						}
    						if(!isInLibrary){
    							if (Consts.DEBUG) {
	    			                Log.d("downlcover", " add to Library: " + issue.downloadUrl);
	    			            }
	    						downloadCover(issue.detailCoverUrl, getCoverFileNameFromUrl(issue.detailCoverUrl));
	    						String destinationFilePath = LibraryManager.GetInstance().libraryPath(context) + File.separator + ApplicationUtils.getEpubFileNameFromUrl(new URL(issue.downloadUrl));
	    						boolean isDownloaded = false;
	    						String coverDestinationFilePath = LibraryManager.GetInstance().libraryPath(context) + File.separator + getCoverFileNameFromUrl(issue.detailCoverUrl);
	    						//LibraryManager.GetInstance().addNewMagazine(context, destinationFilePath, coverDestinationFilePath, ApplicationUtils.getPublicationDate(issue.publicationDate), issue.downloadUrl, isDownloaded, Constants.MAGAZINE_TYPE_NORMAL);
	    						LibraryManager.GetInstance().addNewMagazine(context, destinationFilePath, coverDestinationFilePath, issue.title, issue.downloadUrl, isDownloaded, Constants.MAGAZINE_TYPE_NORMAL, issue.id, issue.googlecheckoutid);
    						}
    					}
    				}
    			}
    	} catch (ActiveRecordException exc) {
    		// TODO Auto-generated catch block
    		exc.printStackTrace();
    	} catch (Exception e) {
        	e.printStackTrace();
        }
    }
    
    public static String getCoverFileNameFromUrl(String epubURL){
		String filename = null;
		try {
			URL epubUrl = new URL(epubURL);
			String epubFilename = epubUrl.getFile().substring(epubUrl.getFile().lastIndexOf("/") + 1, epubUrl.getFile().length());
	    	if (epubFilename.contains(".")){
	    		filename = epubFilename.substring(0, epubFilename.lastIndexOf("."))+ ".cover.png";
	    	} else {
	    		filename = epubFilename + ".cover.png";
	    	}
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return filename;
	}
}
