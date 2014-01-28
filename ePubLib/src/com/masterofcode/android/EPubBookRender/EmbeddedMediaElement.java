package com.masterofcode.android.EPubBookRender;

import org.json.JSONException;
import org.json.JSONObject;

public class EmbeddedMediaElement {
	final static public int		MEDIA_TYPE_NONE = 0;
	final static public int		MEDIA_TYPE_IMAGE = 1;
	final static public int		MEDIA_TYPE_VIDEO = 2;
	final static public int		MEDIA_TYPE_EXTERNAL = 3;

	final private String	HTML_PREFIX = "<table border='0' cellspacing='0' cellpadding='0' width='100%'>\n"+
										  "\n";
	final private String	HTML_POSTFIX = "</table><br/>";

	private	String			id;
	private	String			title;
	private	String			description;
	private	String			teaser;
	private	int				type = MEDIA_TYPE_NONE;
	private	String			imageContent;
	private	String			imageURL;
	private	String			externalContent;
	
	private int				topOffset = -1;

	public String getId()
	{
		return id;
	}

	public void setId(String id)
	{
		this.id = id;
	}

	public String getTitle()
	{
		return title;
	}
	
	public String getDescription()
	{
		return description;
	}
	
	public String getTeaser()
	{
		return teaser;
	}
	
	public int getType()
	{
		return type;
	}
	
	public String getImageContent()
	{
		return imageContent;
	}
	
	public void setTitle(String title)
	{
		this.title = title;
	}
	
	public void setDescription(String description)
	{
		this.description = description;
	}
	
	public void setTeaser(String teaser)
	{
		this.teaser = teaser;
	}
	
	public void setType(int type)
	{
		this.type = type;
	}
	
	public void setImageContent(String imageContent)
	{
		this.imageContent = imageContent;
		type = MEDIA_TYPE_IMAGE;
	}


	public String HTMLRepresentation()
	{	String		result = "";

		if(id!=null)
		{
			result += "<tr><td><div id='" + id + "'></div></td></tr>";
		}

		if(teaser!=null)
		{
			result += "<tr><td class='teaser'><a href=\"http://embedded_select/"+id+"\">" + teaser + "</a></td></tr>";
		}
		
		if(title!=null)
		{
			result += "<tr><td class='title'>" + title + "</td></tr>";
		}

		if(description!=null)
		{
			result += "<tr><td class='description'>" + description + "</td></tr>";
		}

		return HTML_PREFIX + result + HTML_POSTFIX;
	}

	public JSONObject getJSONObject()
	{
		JSONObject			result = new JSONObject();
		
		try {
			result.put("id", id);
			result.put("title", title);
			result.put("description", description);
			result.put("type", type);
			if(type==MEDIA_TYPE_IMAGE)
			{
				result.put("image_url", imageURL);
			}
			if(type==MEDIA_TYPE_EXTERNAL)
			{
				result.put("external_content", externalContent);
			}
			return result;
		} catch (JSONException e) {
			e.printStackTrace();
		}		
		return null;
	}

	public EmbeddedMediaElement(JSONObject jsonObject)
	{
		try {
			id = jsonObject.getString("id");
			
			
			if(jsonObject.has("title"))title = jsonObject.getString("title");
			if(jsonObject.has("description"))description = jsonObject.getString("description");
			type = jsonObject.getInt("type");
			
			if(type==MEDIA_TYPE_IMAGE)
			{
				if(jsonObject.has("image_url"))
				{
					imageURL = jsonObject.getString("image_url");
				} else {
					imageURL = "";		// type is image, but content empty
				}
			} else
				if(type==MEDIA_TYPE_EXTERNAL)
				{
					if(jsonObject.has("external_content"))
					{
						externalContent = jsonObject.getString("external_content");
					}
				}
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

	public EmbeddedMediaElement()
	{
	}

	public void setTopOffset(int topOffset)
	{
		this.topOffset = topOffset;
	}

	public int getTopOffset()
	{
		return topOffset;
	}

	public void setImageURL(String imageURL)
	{
		this.imageURL = imageURL;
		type = MEDIA_TYPE_IMAGE;
	}

	public String getImageURL()
	{
		return imageURL;
	}

	public void setExternalContent(String externalContent)
	{
		this.externalContent = externalContent;
		type = MEDIA_TYPE_EXTERNAL;
	}

	public String getExternalContent()
	{
		return externalContent;
	}
}
