package com.example.plistparsertesting;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;

import com.longevitysoft.android.xml.plist.PListXMLHandler;
import com.longevitysoft.android.xml.plist.PListXMLParser;
import com.longevitysoft.android.xml.plist.domain.Array;
import com.longevitysoft.android.xml.plist.domain.Dict;
import com.longevitysoft.android.xml.plist.domain.IPListSimpleObject;
import com.longevitysoft.android.xml.plist.domain.PList;

public class MainActivity extends Activity {
	
	/*
	 * <?xml version="1.0" encoding="UTF-8"?>
		<!DOCTYPE plist PUBLIC "-//Apple Computer//DTD PLIST 1.0//EN" "http://www.apple.com/DTDs/PropertyList-1.0.dtd">
		<plist version="1.0">
		<dict>
			<key>workflow_answers</key>
			<array>
				<dict>
					<key>answer_id</key>
					<integer>1</integer>
					<key>image_url</key>
					<string>http://www.galaxyzoo.org/images/buttons/1_button.gif</string>
					<key>next_workflow_task_id</key>
					<integer>2</integer>
					<key>value</key>
					<string>Smooth</string>
					<key>workflow_answer_id</key>
					<integer>1</integer>
					<key>workflow_task_id</key>
					<integer>1</integer>
				</dict>
				<dict>
					<key>answer_id</key>
					<integer>2</integer>
					<key>image_url</key>
					<string>http://www.galaxyzoo.org/images/buttons/2_button.gif</string>
					<key>next_workflow_task_id</key>
					<integer>30</integer>
					<key>value</key>
					<string>Features or disk</string>
					<key>workflow_answer_id</key>
					<integer>2</integer>
					<key>workflow_task_id</key>
					<integer>1</integer>
				</dict>
				<dict>
					<key>answer_id</key>
					<integer>3</integer>
					<key>image_url</key>
					<string>http://www.galaxyzoo.org/images/buttons/3_button.gif</string>
					<key>next_workflow_task_id</key>
					<integer>0</integer>
					<key>value</key>
					<string>Star or artifact</string>
					<key>workflow_answer_id</key>
					<integer>3</integer>
					<key>workflow_task_id</key>
					<integer>1</integer>
				</dict>
				<dict>
					<key>answer_id</key>
					<integer>16</integer>
					<key>image_url</key>
					<string>http://www.galaxyzoo.org/images/buttons/16_button.gif</string>
					<key>next_workflow_task_id</key>
					<integer>3</integer>
					<key>value</key>
					<string>Completely round</string>
					<key>workflow_answer_id</key>
					<integer>4</integer>
					<key>workflow_task_id</key>
					<integer>2</integer>
				</dict>
				<dict>
					<key>answer_id</key>
					<integer>20</integer>
					<key>image_url</key>
					<string>http://www.galaxyzoo.org/images/buttons/20_button.gif</string>
					<key>next_workflow_task_id</key>
					<integer>0</integer>
					<key>value</key>
					<string>Lens or arc</string>
					<key>workflow_answer_id</key>
					<integer>10</integer>
					<key>workflow_task_id</key>
					<integer>4</integer>
				</dict>
				<dict>
					<key>answer_id</key>
					<integer>21</integer>
					<key>image_url</key>
					<string>http://www.galaxyzoo.org/images/buttons/21_button.gif</string>
					<key>next_workflow_task_id</key>
					<integer>0</integer>
					<key>value</key>
					<string>Disturbed</string>
					<key>workflow_answer_id</key>
					<integer>11</integer>
					<key>workflow_task_id</key>
					<integer>4</integer>
				</dict>
			</array>
			<key>workflow_tasks</key>
			<array>
				<dict>
					<key>name</key>
					<string>Is the galaxy simply smooth and rounded, with no sign of a disk?</string>
					<key>parent_id</key>
					<integer>-1</integer>
					<key>task_id</key>
					<integer>1</integer>
					<key>workflow_answers</key>
					<array>
						<integer>1</integer>
						<integer>2</integer>
						<integer>3</integer>
					</array>
					<key>workflow_task_id</key>
					<integer>1</integer>
				</dict>
				<dict>
					<key>name</key>
					<string>How rounded is it?</string>
					<key>parent_id</key>
					<integer>1</integer>
					<key>task_id</key>
					<integer>7</integer>
					<key>workflow_answers</key>
					<array>
						<integer>4</integer>
						<integer>5</integer>
						<integer>6</integer>
					</array>
					<key>workflow_task_id</key>
					<integer>2</integer>
				</dict>
				<dict>
					<key>name</key>
					<string>Is there anything odd?</string>
					<key>parent_id</key>
					<integer>2</integer>
					<key>task_id</key>
					<integer>6</integer>
					<key>workflow_answers</key>
					<array>
						<integer>7</integer>
						<integer>8</integer>
					</array>
					<key>workflow_task_id</key>
					<integer>3</integer>
				</dict>
			</array>
		</dict>
		</plist>

	 * 
	 * 
	 * 
	 */
	private final String TAG = "PListPaserTesting";

	public static final String VALID_WORKFLOW_PLIST = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" + "<!DOCTYPE plist PUBLIC \"-//Apple Computer//DTD PLIST 1.0//EN\" \"http://www.apple.com/DTDs/PropertyList-1.0.dtd\">\n" + "<plist version=\"1.0\">\n" + "<dict>\n" + "	<key>workflow_answers</key>\n" + "	<array>\n" + "		<dict>\n" + "			<key>answer_id</key>\n" + "			<integer>1</integer>\n" + "			<key>image_url</key>\n" + "			<string>http://www.galaxyzoo.org/images/buttons/1_button.gif</string>\n" + "			<key>next_workflow_task_id</key>\n" + "			<integer>2</integer>\n" + "			<key>value</key>\n" + "			<string>Smooth</string>\n" + "			<key>workflow_answer_id</key>\n" + "			<integer>1</integer>\n" + "			<key>workflow_task_id</key>\n" + "			<integer>1</integer>\n" + "		</dict>\n" + "		<dict>\n" + "			<key>answer_id</key>\n" + "			<integer>2</integer>\n" + "			<key>image_url</key>\n" + "			<string>http://www.galaxyzoo.org/images/buttons/2_button.gif</string>\n" + "			<key>next_workflow_task_id</key>\n" + "			<integer>30</integer>\n" + "			<key>value</key>\n" + "			<string>Features or disk</string>\n" + "			<key>workflow_answer_id</key>\n" + "			<integer>2</integer>\n" + "			<key>workflow_task_id</key>\n" + "			<integer>1</integer>\n" + "		</dict>\n" + "		<dict>\n" + "			<key>answer_id</key>\n" + "			<integer>3</integer>\n" + "			<key>image_url</key>\n" + "			<string>http://www.galaxyzoo.org/images/buttons/3_button.gif</string>\n" + "			<key>next_workflow_task_id</key>\n" + "			<integer>0</integer>\n" + "			<key>value</key>\n" + "			<string>Star or artifact</string>\n" + "			<key>workflow_answer_id</key>\n" + "			<integer>3</integer>\n" + "			<key>workflow_task_id</key>\n" + "			<integer>1</integer>\n" + "		</dict>\n" + "		<dict>\n" + "			<key>answer_id</key>\n" + "			<integer>16</integer>\n" + "			<key>image_url</key>\n" + "			<string>http://www.galaxyzoo.org/images/buttons/16_button.gif</string>\n" + "			<key>next_workflow_task_id</key>\n" + "			<integer>3</integer>\n" + "			<key>value</key>\n" + "			<string>Completely round</string>\n" + "			<key>workflow_answer_id</key>\n" + "			<integer>4</integer>\n" + "			<key>workflow_task_id</key>\n" + "			<integer>2</integer>\n" + "		</dict>\n" + "		<dict>\n" + "			<key>answer_id</key>\n" + "			<integer>20</integer>\n" + "			<key>image_url</key>\n" + "			<string>http://www.galaxyzoo.org/images/buttons/20_button.gif</string>\n" + "			<key>next_workflow_task_id</key>\n" + "			<integer>0</integer>\n" + "			<key>value</key>\n" + "			<string>Lens or arc</string>\n" + "			<key>workflow_answer_id</key>\n" + "			<integer>10</integer>\n" + "			<key>workflow_task_id</key>\n" + "			<integer>4</integer>\n" + "		</dict>\n" + "		<dict>\n" + "			<key>answer_id</key>\n" + "			<integer>21</integer>\n" + "			<key>image_url</key>\n" + "			<string>http://www.galaxyzoo.org/images/buttons/21_button.gif</string>\n" + "			<key>next_workflow_task_id</key>\n" + "			<integer>0</integer>\n" + "			<key>value</key>\n" + "			<string>Disturbed</string>\n" + "			<key>workflow_answer_id</key>\n" + "			<integer>11</integer>\n" + "			<key>workflow_task_id</key>\n" + "			<integer>4</integer>\n" + "		</dict>\n" + "	</array>\n" + "	<key>workflow_tasks</key>\n" + "	<array>\n" + "		<dict>\n" + "			<key>name</key>\n" + "			<string>Is the galaxy simply smooth and rounded, with no sign of a disk?</string>\n" + "			<key>parent_id</key>\n" + "			<integer>-1</integer>\n" + "			<key>task_id</key>\n" + "			<integer>1</integer>\n" + "			<key>workflow_answers</key>\n" + "			<array>\n" + "				<integer>1</integer>\n" + "				<integer>2</integer>\n" + "				<integer>3</integer>\n" + "			</array>\n" + "			<key>workflow_task_id</key>\n" + "			<integer>1</integer>\n" + "		</dict>\n" + "		<dict>\n" + "			<key>name</key>\n" + "			<string>How rounded is it?</string>\n" + "			<key>parent_id</key>\n" + "			<integer>1</integer>\n" + "			<key>task_id</key>\n" + "			<integer>7</integer>\n" + "			<key>workflow_answers</key>\n" + "			<array>\n" + "				<integer>4</integer>\n" + "				<integer>5</integer>\n" + "				<integer>6</integer>\n" + "			</array>\n" + "			<key>workflow_task_id</key>\n" + "			<integer>2</integer>\n" + "		</dict>\n" + "		<dict>\n" + "			<key>name</key>\n" + "			<string>Is there anything odd?</string>\n" + "			<key>parent_id</key>\n" + "			<integer>2</integer>\n" + "			<key>task_id</key>\n" + "			<integer>6</integer>\n" + "			<key>workflow_answers</key>\n" + "			<array>\n" + "				<integer>7</integer>\n" + "				<integer>8</integer>\n" + "			</array>\n" + "			<key>workflow_task_id</key>\n" + "			<integer>3</integer>\n" + "		</dict>\n" + "	</array>\n" + "</dict>\n" + "</plist>\n" + "";

	@SuppressWarnings("unchecked")
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		PListXMLParser parser = new PListXMLParser();
		PListXMLHandler handler = new PListXMLHandler();
		parser.setHandler(handler);
		parser.parse(VALID_WORKFLOW_PLIST);
		PList actualPList = ((PListXMLHandler) parser.getHandler()).getPlist();

		Array workflow_answersaArray = ((Dict) actualPList.getRootElement()).getConfigurationArray("workflow_answers");
		Log.d(TAG, " workflow_answersaArray.size() : " + workflow_answersaArray.size());
		for (int i = 0; i < workflow_answersaArray.size(); i++)
		{

			int answer_id = ((Dict) workflow_answersaArray.get(i)).getConfigurationInteger("answer_id").getValue();
			Log.d(TAG, "answer_id : " + answer_id);

			String image_url = ((Dict) workflow_answersaArray.get(i)).getConfiguration("image_url").getValue();
			Log.d(TAG, "image_url : " + image_url);

			int next_workflow_task_id = ((Dict) workflow_answersaArray.get(i)).getConfigurationInteger("next_workflow_task_id").getValue();
			Log.d(TAG, "next_workflow_task_id : " + next_workflow_task_id);

			String value = ((Dict) workflow_answersaArray.get(i)).getConfiguration("value").getValue();
			Log.d(TAG, "value : " + value);

			int workflow_answer_id = ((Dict) workflow_answersaArray.get(i)).getConfigurationInteger("workflow_answer_id").getValue();
			Log.d(TAG, "workflow_answer_id : " + workflow_answer_id);

			int workflow_task_id = ((Dict) workflow_answersaArray.get(i)).getConfigurationInteger("workflow_task_id").getValue();
			Log.d(TAG, "workflow_task_id : " + workflow_task_id);
		}

		Array workflow_tasksArray = ((Dict) actualPList.getRootElement()).getConfigurationArray("workflow_tasks");
		Log.d(TAG, " workflow_tasksArray.size() : " + workflow_tasksArray.size());
		for (int i = 0; i < workflow_tasksArray.size(); i++)
		{
			String name = ((Dict) workflow_tasksArray.get(i)).getConfiguration("name").getValue();
			Log.d(TAG, "name : " + name);

			int parent_id = ((Dict) workflow_tasksArray.get(i)).getConfigurationInteger("parent_id").getValue();
			Log.d(TAG, "parent_id : " + parent_id);

			int task_id = ((Dict) workflow_tasksArray.get(i)).getConfigurationInteger("task_id").getValue();
			Log.d(TAG, "task_id : " + task_id);

			// inArray
			Array workflow_answers = ((Dict) workflow_tasksArray.get(i)).getConfigurationArray("workflow_answers");
			Log.d(TAG, " workflow_answers.size() : " + workflow_answers.size());
			for (int j = 0; j < workflow_answers.size(); j++)
			{
				int integer = ((IPListSimpleObject<Integer>) workflow_answers.get(j)).getValue();
				Log.d(TAG, "integer : " + integer);
			}

			int workflow_task_id = ((Dict) workflow_tasksArray.get(i)).getConfigurationInteger("workflow_task_id").getValue();
			Log.d(TAG, "workflow_task_id : " + workflow_task_id);
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.activity_main, menu);
		return true;
	}

}
