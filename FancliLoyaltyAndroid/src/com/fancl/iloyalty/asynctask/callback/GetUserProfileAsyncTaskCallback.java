package com.fancl.iloyalty.asynctask.callback;

import com.fancl.iloyalty.pojo.User;

public interface GetUserProfileAsyncTaskCallback {
	public void onPostExecuteCallback(User results);
}
