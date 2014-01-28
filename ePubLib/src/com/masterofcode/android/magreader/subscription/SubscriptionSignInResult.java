package com.masterofcode.android.magreader.subscription;

public class SubscriptionSignInResult
{
	boolean			loginSuccessful;
	boolean			networkError;
	String			login;
	String			password;
	
	public boolean isLoginSuccessful()
	{
		return loginSuccessful;
	}
	
	public boolean isNetworkError()
	{
		return networkError;
	}
	
	public void setLoginSuccessful(boolean loginSuccessful)
	{
		this.loginSuccessful = loginSuccessful;
	}
	
	public void setNetworkError(boolean networkError)
	{
		this.networkError = networkError;
	}
	
	public String getLogin()
	{
		return login;
	}

	public String getPassword()
	{
		return password;
	}

	public void setLogin(String login)
	{
		this.login = login;
	}

	public void setPassword(String password)
	{
		this.password = password;
	}

	public SubscriptionSignInResult(boolean loginSuccessful, boolean networkError)
	{
		this.loginSuccessful = loginSuccessful;
		this.networkError = networkError;
		this.login = null;
		this.password = null;
	}
}

