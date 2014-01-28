package com.masterofcode.android.EPubBook;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.io.StringWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.json.JSONArray;
import org.json.JSONException;
import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.XMLReader;

import com.masterofcode.android.EPubBook.SAXHandlers.ContainerHandler;
import com.masterofcode.android.EPubBook.SAXHandlers.OPFHandler;
import com.masterofcode.android.EPubBook.SAXHandlers.TOCHandler;
import com.masterofcode.android.EPubBookRender.EmbeddedMediaElement;
import com.masterofcode.android.magreader.utils.ApplicationUtils;


import android.content.Context;
import android.util.Log;

public class EPubBook {
	final private String		TOPIC_CONTENT_PREFIX = ".content.xhtml";
	final private String		TOPIC_MEDIA_PREFIX = ".media.xhtml";
	final private String		TOPIC_MEDIA_INFO_PREFIX = ".media.json";
	final private String		TOPIC_TITLES_FILE_NAME = "toc.json";
	final private String		TOPIC_ORIGINAL_HIGHLIGHTED_PREFIX = ".hl.xhtml";
	final private String		TOPIC_WITHOUT_MEDIA_HIGHLIGHTED_PREFIX = ".hlproc.xhtml";
	final private String		TOPIC_SEARCHABLE_TEXT_PREFIX = ".searchable.txt";
	
	final private String		MEDIA_PAGE_HTML_PREFIX = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n"+
														 "<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.1//EN\" \"http://www.w3.org/TR/xhtml11/DTD/xhtml11.dtd\">\n"+
														 "<html xmlns=\"http://www.w3.org/1999/xhtml\">\n"+
														 "<head>\n"+
														 "<title>Media</title>"+
														 "<script type=\"text/javasrcipt\">\n"+
														 "function scrollToElement(id) {\n"+
														 "    var elem = document.getElementById(id);\n"+
														 "    var x = 0;\n"+
														 "    var y = 0;\n"+
														 "    while (elem != null) {\n"+
														 "        x += elem.offsetLeft;\n"+
														 "        y += elem.offsetTop;\n"+
														 "        elem = elem.offsetParent;\n"+
														 "    }\n"+
														 "    y-=10;\n"+
														 "    if(y&lt;0)y=0;\n"+
														 "    window.scrollTo(0, y);\n"+
														 "}\n"+
														 "</script>\n"+
														 "<style type='text/css'>\n"+
														 "   body { background-color: #FFFFFF; width: 352px; margin: 0px; margin-top: 10px; }\n"+
														 "   img { width: 340px; }\n"+
														 "	 td.teaser { text-align: center; width: 340px; }\n"+
														 "	 td.title { text-align: center; }\n"+
														 "	 td.description { text-align: justify; }\n"+
														 "</style>\n"+
														 "</head>\n"+
														 "<body>\n"+
														 "<table border='0' cellspacing='0' cellpadding='0' width='100%' style='padding-left: 10px'>\n"+
														 "<tr><td>";
	
	final private String		MEDIA_PAGE_HTML_POSTFIX = "</td></tr></table></body></html>";

	Context		context;
	String		epubFileName;
	String		rootFilePath;
	boolean		isValid = false;
	boolean		isStrictCheck;
	OPFInfo		opfInfo = null;
	String		contentPath = null;
	TOCInfo		tocInfo = null;
	String		pathToBookContentOnExternalStorage = null;
	String		pathToBookOnExternalStorage = null;
	
	private ArrayList<String>		topicTitles = null;
	
	public EPubBook(Context context, String epubFileName, boolean strictCheck)
	{
		this.context = context;
		this.epubFileName = epubFileName;
		this.isStrictCheck = strictCheck;
	}
	
	public void load() throws Exception
	{
		checkMimeType();
		loadContainerInfo();
		parseOPF();
		loadTOC(contentPath + opfInfo.getTOCFileName());
    	isValid = true;

//		Log.i("-----!-----", rootFilePath);
	}

	public String getBookName()
	{
		if(epubFileName==null) return null;  // not inited
		
		int			pos = epubFileName.lastIndexOf(File.separator);
		
		if(pos!=-1)
		{
			return epubFileName.substring(pos + 1);
		}
		
		return null;
	}
	
	private void loadContainerInfo() throws Exception
	{
		ZipFile		zipFile = new ZipFile( epubFileName );
		ZipEntry	entry = zipFile.getEntry(Constants.EPUB_CONTAINER_FILENAME);
		InputStream	is = zipFile.getInputStream(entry);
		InputSource isou = new InputSource(is); 
		
		SAXParserFactory factory = SAXParserFactory.newInstance();
        SAXParser parser;

        parser = factory.newSAXParser();
        XMLReader xmlreader = parser.getXMLReader();
        ContainerHandler epbch = new ContainerHandler();
        xmlreader.setContentHandler(epbch);
        xmlreader.parse(isou);

        String	rFileMediaType = epbch.getRootFileMediaType();
        String	rFilePath = epbch.getRootFilePath();
        
        if(rFileMediaType==null || rFilePath==null) throw new Exception("EPub doesn't have Container MediaType or FilePath");
        
        if (rFileMediaType.equals(Constants.EPUB_CONTAINER_MEDIATYPE))
        {	
        	rootFilePath = rFilePath;
        	
        	int			idx = rFilePath.lastIndexOf("/");

        	if(idx == -1)
        	{
        		contentPath = rFilePath;
        	} else {
        		contentPath = rFilePath.substring(0, idx+1); // with trailing / char
        	}
        	
//        	Log.i("!!!!!", contentPath);
        } else {
        	 throw new Exception("EPub has invalid Container MediaType");
        }
	}
	
	private void checkMimeType() throws Exception
	{
		ZipFile			zipFile = new ZipFile(epubFileName);
		ZipEntry		entry = zipFile.getEntry(Constants.EPUB_MIMETYPE_FILENAME);
		InputStream		is = zipFile.getInputStream(entry);
		StringBuffer 	out = new StringBuffer();

		byte[] b = new byte[1024];
	    for (int n; (n = is.read(b)) != -1;)
	    {
	        out.append(new String(b, 0, n));
	    }
	    is.close();
		String mType = out.toString();
		
		if (!mType.equals(Constants.EPUB_MIMETYPE)) throw new Exception("EPub has invalid MimeType");
	}

	private void parseOPF() throws Exception
	{
		ZipFile				zipFile = new ZipFile(epubFileName);
		ZipEntry			zipEntry = zipFile.getEntry(rootFilePath);
		InputStream			zipStream = zipFile.getInputStream(zipEntry);
		InputSource			zipInputSource = new InputSource(zipStream); 
		SAXParserFactory 	factory = SAXParserFactory.newInstance();
        SAXParser 			parser;

        parser = factory.newSAXParser();
        XMLReader xmlreader = parser.getXMLReader();
        OPFHandler opfHandler = new OPFHandler(isStrictCheck);
        xmlreader.setContentHandler(opfHandler);
        xmlreader.parse(zipInputSource);
        
		if (isStrictCheck) opfHandler.getOpfInfo().strictCheck();
		opfInfo = opfHandler.getOpfInfo();
	}

	private void loadTOC(String tocFileName) throws Exception
	{
		ZipFile				zipFile = new ZipFile(epubFileName);
		ZipEntry			zipEntry = zipFile.getEntry(tocFileName);
		InputStream			zipStream = zipFile.getInputStream(zipEntry);
		InputSource			zipInputSource = new InputSource(zipStream); 
		SAXParserFactory 	factory = SAXParserFactory.newInstance();
        SAXParser 			parser;

        parser = factory.newSAXParser();
        XMLReader xmlreader = parser.getXMLReader();
        TOCHandler tocHandler = new TOCHandler();
        xmlreader.setContentHandler(tocHandler);
        xmlreader.parse(zipInputSource);

        tocInfo = tocHandler.getTocInfo();
	}

	public OPFInfo getOpfInfo()
	{
		return opfInfo;
	}
	
	private ArrayList<String> topicSearchableText(int topicIndex)
	{
		String					topicSearchableTextFile = getTopicSearchableTextPathByIndex(topicIndex);
		
		if(topicSearchableTextFile!=null)
		{

			FileReader fileReader;
			try {
				fileReader = new FileReader(topicSearchableTextFile);
		        BufferedReader bufferedReader = new BufferedReader(fileReader);
		        String line = null;
				ArrayList<String>		result = new ArrayList<String>();

				while ((line = bufferedReader.readLine()) != null)
				{
		            result.add(line);
		        }
		        bufferedReader.close();
		        
		        return result;
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		return null;
	}
	
	/*private boolean isTopicContainsKeyword(int topicIndex, String keyword)
	{
		ArrayList<String>		topicsSearchableText = topicSearchableText(topicIndex);
		
		if(topicsSearchableText!=null)
		{
			for(int i=0; i<topicsSearchableText.size(); i++)
			{
				if(topicsSearchableText.get(i).toUpperCase().contains(keyword.toUpperCase()))
				{
					return true;
				}
			}
		}
		
		return false;
	}*/
	
	private boolean isTopicContainsKeyword(int topicIndex, String keyword)
	{
		ArrayList<String>		topicsSearchableText = topicSearchableText(topicIndex);
		String newKeyword = keyword.replaceAll("[ ]+", "[ ]+");
		Pattern p = Pattern.compile(newKeyword, Pattern.CASE_INSENSITIVE);

		 if(topicsSearchableText!=null)
	       {
	           for(int i=0; i<topicsSearchableText.size(); i++)
	           {
	        	   String st = topicsSearchableText.get(i);
	        	   Matcher m = p.matcher(topicsSearchableText.get(i));
	               if(m.find())
	               {
	                   return true;
	               }
	           }
		}
		
		return false;
	}
	
	public ArrayList<String> getTopicTitles()
	{
		return topicTitles;
	}
	
	public void loatTopicTitles()
	{
		String			topicTitlesJSONString = getTopicTitlesJSON();
		
		if(topicTitlesJSONString!=null)
		{
			try {
				JSONArray		topicTitlesJSON = new JSONArray(topicTitlesJSONString);
				
				topicTitles = new ArrayList<String>();
				
				for(int i=0; i<topicTitlesJSON.length(); i++)
				{
					topicTitles.add(topicTitlesJSON.getString(i));
				}
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
		
	}

	public String getTopicTitle(int index)
	{
		if(topicTitles==null)
		{
			loatTopicTitles();
		}
		
		if(topicTitles!=null)
		{
			if(index>=0 && index<topicTitles.size()) return topicTitles.get(index);
		}
		
		return "#"+index;
	}
	
	public EPubBookSearchResult isContainsKeyword(String keyword)
	{
		ArrayList<Integer>		topicsWithKeyword = null;
		EPubBookSearchResult	result = new EPubBookSearchResult();
		
		result.setKeywordFinded(false);
		
		for(int i=0; i<getTopicsCount(); i++)
		{
			if(isTopicContainsKeyword(i, keyword))
			{
				if(topicsWithKeyword==null)
				{
					topicsWithKeyword = new ArrayList<Integer>();
					result.setKeywordFinded(true);
				}
				topicsWithKeyword.add(Integer.valueOf(i));
			}
		}
		
		if(result.isKeywordFinded())
		{
			result.setTopicsWithKeyword(topicsWithKeyword);
			
			ArrayList<String>		topicsTitlesWithKeywords = new ArrayList<String>();
			for(int i=0; i<topicsWithKeyword.size(); i++)
			{
				topicsTitlesWithKeywords.add(getTopicTitle(topicsWithKeyword.get(i).intValue()));
			}
			result.setTopicsTitles(topicsTitlesWithKeywords);
		}
		return result;
	}

	public String getSpineItemContentByIndex(int currentSpineItemIndex) throws Exception {
		OPFInfoManifestItem 	item = opfInfo.getSpine().getReferencedManifestItemByIndex(opfInfo.getManifest(), currentSpineItemIndex);
		
		ZipFile				zipFile = new ZipFile(epubFileName);
		ZipEntry			zipEntry = zipFile.getEntry(contentPath + item.getHref());
		InputStream			zipStream = zipFile.getInputStream(zipEntry);
		Writer 				writer = new StringWriter();
        
        char[] buffer = new char[1024];
        Reader reader = new BufferedReader(new InputStreamReader(zipStream, "UTF-8"));
        int n;
        while ((n = reader.read(buffer)) != -1) {
        	writer.write(buffer, 0, n);
        }
		Log.i("---------", item.getHref());
		return writer.toString();
	}
	
	public static void deleteContent(Context context, String epubPath)
	{
		String		epubsPath = context.getExternalFilesDir(Constants.EPUBS_EXTRACTED_EXTERNAL_STORAGE_DIR).getAbsolutePath();
		int			i = epubPath.lastIndexOf(File.separator);
		String		curName = epubPath;
		
		if(i > 0) {
			curName = epubPath.substring(i+1);
		}
		
		String pathToBook = epubsPath + File.separator + curName;
		ApplicationUtils.rmDir(pathToBook);
	}
	
	public boolean extractContentToExternalStorage(EPubBookProcessingListener ePubBookProcessingListener)
	{
		Log.i("------------", getPathToBookContentOnExternalStorage());
		int		BUFFER = 2048;
		String	pathToBook = getPathToBookOnExternalStorage() + File.separator;
		
		try {
			ZipFile				zipFile = new ZipFile(epubFileName);
			
			long				totalSize = 0, currentExtractedSize = 0, oldPercents = 0;
			
			// calc total size
			 for (Enumeration<? extends ZipEntry> e = zipFile.entries(); e.hasMoreElements();)
			 {
				 ZipEntry entry = (ZipEntry) e.nextElement();
				 
				 if(!entry.isDirectory())
				 {
					 if(entry.getName().startsWith(contentPath))
					 {
						 totalSize += entry.getSize();
					 }
				 }
			 }


			 for (Enumeration<? extends ZipEntry> e = zipFile.entries(); e.hasMoreElements();)
			 {
				 ZipEntry entry = (ZipEntry) e.nextElement();
				 
				 if(!entry.isDirectory())
				 {
					 // workaround for zip archives with files entries before dir entries
					 // extract path and create dir
					 String		outputFilePath = pathToBook+entry.getName();
					 
					 File		parent = new File(outputFilePath).getParentFile();

					 try{
						 parent.mkdirs();
					 } catch (Exception e1)
					 {
					 }
					 
					 if(entry.getName().startsWith(contentPath))
					 {
						 //Log.i("----------->", entry.getName());
						 
						 int count;
						 
						 byte data[] = new byte[BUFFER];
				            // write the files to the disk
						 	InputStream zis = zipFile.getInputStream(entry);
				            FileOutputStream fos = new FileOutputStream(outputFilePath);
				            BufferedOutputStream dest = new BufferedOutputStream(fos, BUFFER);
				            while ((count = zis.read(data, 0, BUFFER)) != -1) {
				                dest.write(data, 0, count);

				                currentExtractedSize+=count;
				                float 		percents = (float) (100.0 * currentExtractedSize / totalSize);
				                long		percentNew = Math.round(percents);
				                
				                if(percentNew!=oldPercents)
				                {
				                	ePubBookProcessingListener.onProgress(percentNew);
				                	oldPercents = percentNew;
				                }
				            }
				            dest.flush();
				            dest.close();
					        zis.close();
					 }
				 } else {
					 if(entry.getName().startsWith(contentPath))
					 {
						 File file = new File(pathToBook + entry.getName());
						 file.mkdirs();
					 }
				 }
			 }
			 createExtractedMark();
			 return true;
		} catch (Exception e) {
			e.printStackTrace();
		}

		return false;
	}

	public String getPathToBookContentOnExternalStorage() {
		if (pathToBookContentOnExternalStorage==null)
		{
			pathToBookContentOnExternalStorage = getPathToBookOnExternalStorage() + File.separator + contentPath;
		}
		return pathToBookContentOnExternalStorage;
	}
	
	public String getPathToBookOnExternalStorage() {
		if (pathToBookOnExternalStorage==null)
		{
			String		epubsPath = context.getExternalFilesDir(Constants.EPUBS_EXTRACTED_EXTERNAL_STORAGE_DIR).getAbsolutePath();
			int			i = epubFileName.lastIndexOf(File.separator);
			String		curName = epubFileName;
			
			if(i > 0) {
				curName = epubFileName.substring(i+1);
			}
			
			pathToBookOnExternalStorage = epubsPath + File.separator + curName;
		}
		return pathToBookOnExternalStorage;
		
	}

	private String extractedMarkFileName()
	{
		return getPathToBookOnExternalStorage()+File.separator+"extracted";
	}
	
	private String processedMediaMarkFileName()
	{
		return getPathToBookOnExternalStorage()+File.separator+"mprocessed";
	}
	
	public void createExtractedMark() throws Exception
	{
		File		file = new File(extractedMarkFileName());
		
		file.createNewFile();
	}
	
	public boolean isNeedExtract()
	{
		File		file = new File(extractedMarkFileName());
		
		if(file.canRead()) return false;
		return true;
	}

	public String getTopicOriginalURLByIndex(int currentSpineItemIndex)
	{
		String		path = getTopicOriginalPathByIndex(currentSpineItemIndex);
		if(path==null)return null;
		return "file://" + path;
	}

	public String getTopicOriginalPathByIndex(int currentSpineItemIndex)
	{
		OPFInfoManifestItem 	item = opfInfo.getSpine().getReferencedManifestItemByIndex(opfInfo.getManifest(), currentSpineItemIndex);
		
		if(item==null)return null;
		return getPathToBookContentOnExternalStorage() + item.getHref();
	}

	public String getTopicContentURLByIndex(int currentSpineItemIndex)
	{
		String		path = getTopicContentPathByIndex(currentSpineItemIndex);
		if(path==null)return null;
		return "file://" + path;
	}

	public String getTopicContentPathByIndex(int currentSpineItemIndex)
	{
		OPFInfoManifestItem 	item = opfInfo.getSpine().getReferencedManifestItemByIndex(opfInfo.getManifest(), currentSpineItemIndex);
		
		if(item==null)return null;
		return getPathToBookContentOnExternalStorage() + item.getHref() + TOPIC_CONTENT_PREFIX;
	}

	public String getTopicMediaURLByIndex(int currentSpineItemIndex)
	{
		String		path = getTopicMediaPathByIndex(currentSpineItemIndex);
		if(path==null)return null;
		return "file://" + path;

	}

	public String getContentURL()
	{
		return "file://" + getContentPath();
	}

	public String getContentPath()
	{
		return getPathToBookContentOnExternalStorage();
	}

	public String getTopicTitlesFilePath()
	{
		return getPathToBookOnExternalStorage() + File.separator + TOPIC_TITLES_FILE_NAME;
	}

	public String getTopicMediaPathByIndex(int currentSpineItemIndex)
	{
		OPFInfoManifestItem 	item = opfInfo.getSpine().getReferencedManifestItemByIndex(opfInfo.getManifest(), currentSpineItemIndex);
		
		if(item==null)return null;
		return getPathToBookContentOnExternalStorage() + item.getHref() + TOPIC_MEDIA_PREFIX;
	}

	public String getTopicMediaInfoPathByIndex(int currentSpineItemIndex)
	{
		OPFInfoManifestItem 	item = opfInfo.getSpine().getReferencedManifestItemByIndex(opfInfo.getManifest(), currentSpineItemIndex);
		
		if(item==null)return null;
		return getPathToBookContentOnExternalStorage() + item.getHref() + TOPIC_MEDIA_INFO_PREFIX;
	}

	public String getTopicSearchableTextPathByIndex(int currentSpineItemIndex)
	{
		String				result = getTopicOriginalPathByIndex(currentSpineItemIndex);
		
		if(result==null)return null;
		return result + TOPIC_SEARCHABLE_TEXT_PREFIX;
	}

	public String getTopicOriginalHighlightedPathByIndex(int currentSpineItemIndex)
	{
		String				result = getTopicOriginalPathByIndex(currentSpineItemIndex);
		
		if(result==null)return null;
		return result + TOPIC_ORIGINAL_HIGHLIGHTED_PREFIX;
	}

	public String getTopicWithoutMediaHighlightedPathByIndex(int currentSpineItemIndex)
	{
		String				result = getTopicOriginalPathByIndex(currentSpineItemIndex);
		
		if(result==null)return null;
		return result + TOPIC_WITHOUT_MEDIA_HIGHLIGHTED_PREFIX;
	}

	public String getTopicOriginalHighlightedURLByIndex(int currentSpineItemIndex)
	{
		String				result = getTopicOriginalHighlightedPathByIndex(currentSpineItemIndex);
		
		if(result==null)return null;
		return "file://" + result;
	}

	public String getTopicWithoutMediaHighlightedURLByIndex(int currentSpineItemIndex)
	{
		String				result = getTopicWithoutMediaHighlightedPathByIndex(currentSpineItemIndex);
		
		if(result==null)return null;
		return "file://" + result;
	}

	public boolean getTopicFullscreenFlag(int index)
	{
		return opfInfo.getSpine().getTopicFullscreenFlag(index);
	}
	
	public String getTopicMediaInfoJSONByIndex(int index)
	{
		String			filePath = getTopicMediaInfoPathByIndex(index);
		File           	file = new File(filePath);
    
        if(!file.canRead()) return null;
		
		try {
			BufferedReader r;
			r = new BufferedReader(new InputStreamReader(new FileInputStream(file)));
			StringBuilder		total = new StringBuilder();
			String 				line;
			while ((line = r.readLine()) != null) {
			    total.append(line);
			}
			return total.toString();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public String getTopicTitlesJSON()
	{
		String			filePath = getTopicTitlesFilePath();
		File           	file = new File(filePath);
    
        if(!file.canRead()) return null;
		
		try {
			BufferedReader r;
			r = new BufferedReader(new InputStreamReader(new FileInputStream(file)));
			StringBuilder		total = new StringBuilder();
			String 				line;
			while ((line = r.readLine()) != null) {
			    total.append(line);
			}
			return total.toString();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public int getTopicsCount()
	{
		return opfInfo.getSpine().itemsSize();
	}

	public String getTitle()
	{
		if(opfInfo!=null)
		{
			OPFInfoMetadata  metadata = opfInfo.getMetadata();
			
			if(metadata != null)
			{
				return metadata.getTitle();
			}
		}
		return null;
	}

	public boolean isNeedProcessingMedia()
	{
		File		file = new File(processedMediaMarkFileName());
		if(file.canRead()) return false;
		return true;
	}

	private void createProcessedMediaMark() throws Exception
	{
		File		file = new File(processedMediaMarkFileName());
		file.createNewFile();
	}

	private Node findNodeWithNameAndClass(Node parent, String name, String nodeClass)
	{
		NamedNodeMap	attrs = parent.getAttributes();
		
		if(parent.getNodeName().equalsIgnoreCase(name))
		{
			if(nodeClass != null)
			{
				if(attrs != null)
				{
					Node			attrClass = attrs.getNamedItem("class");
					
					if(attrClass != null)
					{
						if(attrClass.getTextContent().equalsIgnoreCase(nodeClass)) return parent;
						
					}
				}
			} else {
				return parent;
			}
		}
		
		
		NodeList		childs = parent.getChildNodes();
		
		if(childs != null)
		{
			for( int i=0; i<childs.getLength(); i++)
			{
				Node		result = findNodeWithNameAndClass(childs.item(i), name, nodeClass);
				
				if(result!=null) return result;
			}
		}
		
		return null;
	}
	
	@SuppressWarnings("unused")
	private static String convertNodeToXML(Node node)
	{
		try {
			Transformer transformer = TransformerFactory.newInstance().newTransformer();
			Properties outFormat = new Properties();
	        outFormat.setProperty( OutputKeys.OMIT_XML_DECLARATION, "yes" );
            outFormat.setProperty( OutputKeys.METHOD, "xml" );
	        transformer.setOutputProperties( outFormat );
	        
		    StringWriter sw = new StringWriter();
		    transformer.transform(new DOMSource(node), new StreamResult(sw));
		    return sw.toString();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	// also keep text. for <node>text1<child>text2</child>text3</node>
	// it's returns text1<child>text2</child>text3
	private static String convertNodeToXMLOnlyChilds(Node node)
	{
		String			 result = "";
		NodeList		 childs = node.getChildNodes();
		if(childs==null) return node.getTextContent();
		try {
			for (int i=0; i<childs.getLength(); i++)
			{
				Transformer transformer = TransformerFactory.newInstance().newTransformer();
				Properties outFormat = new Properties();
		        outFormat.setProperty( OutputKeys.OMIT_XML_DECLARATION, "yes" );
	            outFormat.setProperty( OutputKeys.METHOD, "xml" );
		        transformer.setOutputProperties( outFormat );
		        
			    StringWriter sw = new StringWriter();
			    transformer.transform(new DOMSource(childs.item(i)), new StreamResult(sw));
			    
			    result += sw.toString();
			}
			
		    return result;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	private static void clearNodeChilds(Node node)
	{
		NodeList		 childs = node.getChildNodes();
		if(childs==null) return;
		for (int i=0; i<childs.getLength(); i++)
		{
		    node.removeChild(childs.item(i));
		}
	}

	private String getChildNodeText(Node parent, String tag, String tagClass)
	{
		Node			node = findNodeWithNameAndClass(parent, tag, tagClass);	
		
		if(node!=null)
		{
			return node.getTextContent();
		}
		return null;
	}
	
	private String getEmbeddedMediaNodeTitle(Node parent)
	{
		return getChildNodeText(parent, "div", "embedded_media_title");	
	}
	
	private String getEmbeddedMediaNodeDescription(Node parent)
	{
		return getChildNodeText(parent, "div", "embedded_media_description");	
	}
	
	private void extractMediaFromTopicFile(int index, String originalPath, String contentPath, String mediaPath, String mediaInfoPath, JSONArray topicTitles, String searchableTextPath)
	{
		int									mediaCount = 0;
		ArrayList<EmbeddedMediaElement>		media = new ArrayList<EmbeddedMediaElement>();
		JSONArray							jsonMediaIds = new JSONArray();
		String								topicTitle = null;
		
		Log.i("---", "processed="+originalPath);
		try {
			InputStream				source = new FileInputStream(originalPath);
			DocumentBuilderFactory 	dbFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder builder = dbFactory.newDocumentBuilder();
			Document dom = builder.parse(source);

			
			// processing topic head
			NodeList		headList = dom.getElementsByTagName("head");
			for(int i=0; i<headList.getLength(); i++)
			{
				Node			node = headList.item(i);
				Node			mTitleNode = findNodeWithNameAndClass(node, "title", null);
				
				if(mTitleNode!=null)
				{
					topicTitle = mTitleNode.getTextContent();
				}
			}
			
			if(topicTitle==null)	// no title found
			{
				topicTitle = String.format("Topic #%d", index);
			}
			
			topicTitles.put(topicTitle);
			
			// processing topic body
			NodeList		list = dom.getElementsByTagName("div");
			for(int i=0; i<list.getLength(); i++)
			{
				Node			node = list.item(i);
				NamedNodeMap	attrs = node.getAttributes();
				Node			attrClass = attrs.getNamedItem("class");
				Node			idClass = attrs.getNamedItem("id");
				
				if(attrClass != null  && idClass!=null)
				{
					String		classValue = attrClass.getTextContent();
					String		idValue = idClass.getTextContent();
					
					if(classValue.equalsIgnoreCase("embedded_media"))
					{
						mediaCount++;
						
						// parsing media node
						EmbeddedMediaElement		mElement = new EmbeddedMediaElement();
						
						mElement.setTitle(getEmbeddedMediaNodeTitle(node));
						mElement.setDescription(getEmbeddedMediaNodeDescription(node));

						Node						mTeaser = findNodeWithNameAndClass(node, "div", "embedded_media_teaser");	
						if(mTeaser!=null)
						{
							String					mTeaserContent =	convertNodeToXMLOnlyChilds(mTeaser);
							
							if(mTeaserContent != null)
							{
								mElement.setTeaser(mTeaserContent);
							}
						}
						
						// removing media
						String		newEmbeddedId = idValue.replace("embedded_media_", "embedded_");
						mElement.setId(newEmbeddedId);
						
						// get media IMAGE
						Node		imageNode = findNodeWithNameAndClass(node, "div", "embedded_media_image");
						
						if(imageNode!=null)
						{
							Node		imageElementNode = findNodeWithNameAndClass(imageNode, "img", null);
							
							if(imageElementNode != null)
							{
								NamedNodeMap	imgAttrs = imageElementNode.getAttributes();
								
								if(imgAttrs != null)
								{
									Node			imgAttrSrc = imgAttrs.getNamedItem("src");
									
									if(imgAttrSrc != null)
									{
										mElement.setImageURL(getContentURL() + imgAttrSrc.getTextContent());
									}
								}
								
							}
						} else {			// get media VIDEO, etc.
							Node		externalContentNode = findNodeWithNameAndClass(node, "div", "embedded_media_external");
							
							if(externalContentNode != null)
							{
								mElement.setExternalContent(convertNodeToXMLOnlyChilds(externalContentNode));
							}
						}
						
						jsonMediaIds.put(mElement.getJSONObject());

						clearNodeChilds(node);
						org.w3c.dom.Element el = (org.w3c.dom.Element) node;
						el.setAttribute("id", newEmbeddedId);
						
						if(mElement.getType() != EmbeddedMediaElement.MEDIA_TYPE_NONE)media.add(mElement);
					} else if (classValue.equalsIgnoreCase("embedded_link")) {
						dom.renameNode(node, null, "a");
						
						String		newMediaId = idValue.replace("embedded_link_", "embedded_");

						org.w3c.dom.Element el = (org.w3c.dom.Element) node;
						el.removeAttribute("class");
						el.removeAttribute("id");
						el.setAttribute("href", "http://embedded_select/"+newMediaId);
					}
				}
			}

			if(mediaCount > 0)		// remove anyway, even if wrong embedded_media (without embedded_media_image or embedded_media_external)
			{
				Transformer transformer = TransformerFactory.newInstance().newTransformer();
				Result output = new StreamResult(new File(contentPath));
				Source input = new DOMSource(dom);
				Properties outFormat = new Properties();
//	            outFormat.setProperty( OutputKeys.INDENT, "yes" );
	            outFormat.setProperty( OutputKeys.METHOD, "xml" );
	            outFormat.setProperty( OutputKeys.OMIT_XML_DECLARATION, "no" );
	            outFormat.setProperty( OutputKeys.VERSION, "1.0" );
	            outFormat.setProperty( OutputKeys.ENCODING, "UTF-8" );
	            transformer.setOutputProperties( outFormat );
				transformer.transform(input, output);
				
				//============== write media ================
				String			html = "";
				
				for(int i=0; i<media.size(); i++)
				{
					html += media.get(i).HTMLRepresentation();
				}
				
				html = MEDIA_PAGE_HTML_PREFIX + html + MEDIA_PAGE_HTML_POSTFIX;
				
				
				OutputStreamWriter out = new OutputStreamWriter(new FileOutputStream(mediaPath));
				out.write(html);
				out.close();
				
				// write json with embedded elements
				String		json = jsonMediaIds.toString();
				OutputStreamWriter outMediaInfo = new OutputStreamWriter(new FileOutputStream(mediaInfoPath));
				outMediaInfo.write(json);
				outMediaInfo.close();
				
				// check if this are media external
				boolean	isExternalMediaPresent = false;
				for(int i=0; i<media.size(); i++)
				{
					if(media.get(i).getType() == EmbeddedMediaElement.MEDIA_TYPE_EXTERNAL)
					{
						isExternalMediaPresent = true;
						break;
					}
				}
				if(isExternalMediaPresent)
				{
					modifyOriginalTopicContentExternalMedia(originalPath);
				}
			}
			
			// search prepare
			extractSearchableTextContent(dom, searchableTextPath);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		
	}

	// replace embedded_media_external by teaser image in original topc
	private void modifyOriginalTopicContentExternalMedia(String originalPath)
	{
		try {
			InputStream				source = new FileInputStream(originalPath);
			DocumentBuilderFactory 	dbFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder builder = dbFactory.newDocumentBuilder();
			Document dom = builder.parse(source);

			// processing topic body
			NodeList		list = dom.getElementsByTagName("div");
			for(int i=0; i<list.getLength(); i++)
			{
				Node			node = list.item(i);
				NamedNodeMap	attrs = node.getAttributes();
				Node			attrClass = attrs.getNamedItem("class");
				Node			idClass = attrs.getNamedItem("id");
				
				if(attrClass != null  && idClass!=null)
				{
					String		classValue = attrClass.getTextContent();
					String		idValue = idClass.getTextContent();
					
					if(classValue.equalsIgnoreCase("embedded_media"))
					{
						// parsing media node
						EmbeddedMediaElement		mElement = new EmbeddedMediaElement();
						
						mElement.setTitle(getEmbeddedMediaNodeTitle(node));
						mElement.setDescription(getEmbeddedMediaNodeDescription(node));

						Node						mTeaser = null;
						
						mTeaser = findNodeWithNameAndClass(node, "div", "embedded_media_teaser");
						Node		mTeaserCopy = null;
						if(mTeaser!=null)
						{
							String					mTeaserContent =	convertNodeToXMLOnlyChilds(mTeaser);
							
							if(mTeaserContent != null)
							{
								mElement.setTeaser(mTeaserContent);
							}
							mTeaserCopy = mTeaser.cloneNode(true);
						}
						
						Node		externalContentNode = findNodeWithNameAndClass(node, "div", "embedded_media_external");
						if(externalContentNode != null)
						{
							mElement.setExternalContent(convertNodeToXMLOnlyChilds(externalContentNode));

							String		newMediaId = idValue.replace("embedded_media_", "embedded_");

							clearNodeChilds(node);
							
							// anchor
							org.w3c.dom.Element		newAnchor = dom.createElement("a");
							newAnchor.setAttribute("href", "http://embedded_external/"+newMediaId);
							
							// add childs of teaser to anchor
							if(mTeaserCopy!=null)
							{
								NodeList		childs = mTeaserCopy.getChildNodes();
								
								if(childs!=null)
								{
									for(int j=0; j<childs.getLength(); j++)
									{
										Node		child = childs.item(j);
										newAnchor.appendChild(child);
									}
								}
							}
							
							node.appendChild(newAnchor);
							
							org.w3c.dom.Element el = (org.w3c.dom.Element) node;
							el.removeAttribute("class");
							el.removeAttribute("id");
						}
					}
				}
			}

			source.close();
			
			// write content
			Transformer transformer = TransformerFactory.newInstance().newTransformer();
			Result output = new StreamResult(new File(originalPath));
			Source input = new DOMSource(dom);
			Properties outFormat = new Properties();
            outFormat.setProperty( OutputKeys.METHOD, "xml" );
            outFormat.setProperty( OutputKeys.OMIT_XML_DECLARATION, "no" );
            outFormat.setProperty( OutputKeys.VERSION, "1.0" );
            outFormat.setProperty( OutputKeys.ENCODING, "UTF-8" );
            transformer.setOutputProperties( outFormat );
			transformer.transform(input, output);
			
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private void extractTextFromNode(Node parent, ArrayList<String> result)
	{
		NodeList		childs = parent.getChildNodes();
		
		if(childs != null)
		{
			if(childs.getLength()==0)
			{
				result.add(parent.getTextContent());
			} else {
				for( int i=0; i<childs.getLength(); i++)
				{
					extractTextFromNode(childs.item(i), result);
				}
			}
		} else {
			result.add(parent.getTextContent());
		}
	}

	// prepare searchable text line before write to a file
	private String processSearchableTextLine(String line)
	{
		String		result = line;
		
		// replace new lines by spaces
		result = result.replace("\n", " ");

		// replace tab by spaces
		result = result.replace("\t", " ");

		// drop out empty lines
		String		trimmed = result;
		trimmed = trimmed.trim();
		if(trimmed.isEmpty()) return null;
		
		if(com.masterofcode.android.magreader.utils.constants.Constants.SEARCH_IN_LIBRARY_IS_SEARCHABLE_TEXT_TRIMMED)
			result = trimmed;
		return result;
	}
	
	// extract from topic in-tag text and save it to file line-by-line
	// this behavior prevent search that may break tag nesting
	private void extractSearchableTextContent(Document dom, String searchableTextPath)
	{
		ArrayList<String>		searchableText = new ArrayList<String>();
		
		NodeList		bodyList = dom.getElementsByTagName("body");
		for(int i=0; i<bodyList.getLength(); i++)
		{
			Node			node = bodyList.item(i);
			
			extractTextFromNode(node, searchableText);
		}
		
		OutputStreamWriter out;
		try {
			out = new OutputStreamWriter(new FileOutputStream(searchableTextPath));
			for(int i=0; i<searchableText.size(); i++)
			{
				String		resultLine = processSearchableTextLine(searchableText.get(i));
				
				if(resultLine!=null)out.write(resultLine+"\n");
			}
			out.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void processMedia(EPubBookProcessingListener processingListener)
	{
		int			count = getTopicsCount(); 
		JSONArray	topicTitles = new JSONArray();

		for(int i=0; i<count; i++)
		{
			String		documentPath = getTopicOriginalPathByIndex(i);
			String		contentPath = getTopicContentPathByIndex(i);
			String		mediaPath = getTopicMediaPathByIndex(i);
			String		mediaInfoPath = getTopicMediaInfoPathByIndex(i);
			String		searchableTextPath = getTopicSearchableTextPathByIndex(i);

			extractMediaFromTopicFile(i, documentPath, contentPath, mediaPath, mediaInfoPath, topicTitles, searchableTextPath);
			
			processingListener.onProgress( Math.round(((float)(i+1)*100) / count ));
		}

		try {
			saveTopicTitles(topicTitles);
			createProcessedMediaMark();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private void saveTopicTitles(JSONArray topicTitles) throws Exception
	{
		String		json = topicTitles.toString();
		OutputStreamWriter outMediaInfo = new OutputStreamWriter(new FileOutputStream(getTopicTitlesFilePath()));
		outMediaInfo.write(json);
		outMediaInfo.close();
	}

	private String highlightString(String text, String keyword)
	{
		String newKeyword = keyword.replaceAll("[ ]+", "[ ]+");
		return text.replaceAll("(?i)(?m)("+newKeyword+")(?=[^>]*?<)","<span style='background-color: #FFFF00;'>$1</span>");
	}
	
	private void hightlightFile(String sourceFilePath, String destinationFilePath, String keyword)
	{
		try {
			File file = new File(sourceFilePath);
			if(file.canRead())
			{
				InputStream in = new FileInputStream(file);
				byte[] b  = new byte[(int) file.length()];
				int len = b.length;
				int total = 0;

				while (total < len) {
				  int result = in.read(b, total, len - total);
				  if (result == -1) {
				    break;
				  }
				  total += result;
				}

				String result = new String(b, "UTF-8" );
		        
		        result = highlightString(result, keyword);

				OutputStreamWriter outStream = new OutputStreamWriter(new FileOutputStream(destinationFilePath));
				outStream.write(result);
				outStream.close();
				
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private void hightlightTopic(int index, String keyword)
	{
		if(index<0 || index>=getTopicsCount()) return;

		String		highlightedOriginalFilePath = getTopicOriginalHighlightedPathByIndex(index);
		String		sourceOriginalFilePath = getTopicOriginalPathByIndex(index);

		if(highlightedOriginalFilePath!=null && sourceOriginalFilePath!=null)
		{
			hightlightFile(sourceOriginalFilePath, highlightedOriginalFilePath, keyword);
		}
		
		String		highlightedWithoutMediaFilePath = getTopicWithoutMediaHighlightedPathByIndex(index);
		String		sourceWithoutMediaFilePath = getTopicContentPathByIndex(index);

		if(highlightedWithoutMediaFilePath!=null && sourceWithoutMediaFilePath!=null)
		{
			hightlightFile(sourceWithoutMediaFilePath, highlightedWithoutMediaFilePath, keyword);
		}
	}
	
	public void hightlightTopics(ArrayList<Integer> topicsForHighlight, String keyword, EPubBookProcessingListener processingListener)
	{
		for(int i=0; i<topicsForHighlight.size(); i++)
		{
			hightlightTopic(topicsForHighlight.get(i).intValue(), keyword);
			processingListener.onProgress(i+1);
		}
	}
}
