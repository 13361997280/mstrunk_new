package com.qbao.search.logging.support;

import java.util.Map;

import com.qbao.search.util.CommonUtil;


public class PrintLogger
	extends AbstractESLogger
{

	StringBuilder sb = new StringBuilder();

	public PrintLogger(String string)
	{
		super(string);
	}

	protected void internalTrace(String msg)
	{
		sb.append("[TRACE]").append(msg);
		sb.append("\n");
	}

	protected void internalTrace(String msg, Throwable cause)
	{
		internalTrace(msg);
		String string = CommonUtil.toString(cause);
		sb.append(string);
		sb.append("\n");
	}

	protected void internalDebug(String msg)
	{
		sb.append("[DEBUG]").append(msg);
		sb.append("\n");
	}

	protected void internalDebug(String msg, Throwable cause)
	{
		internalDebug(msg);
		String string = CommonUtil.toString(cause);
		sb.append(string);
		sb.append("\n");
	}

	protected void internalInfo(String msg)
	{
		sb.append("[INFO]").append(msg);
		sb.append("\n");
	}


	protected void internalInfo(String msg, Throwable cause)
	{
		internalInfo(msg);
		String string = CommonUtil.toString(cause);
		sb.append(string);
		sb.append("\n");
	}

	protected void internalWarn(String msg)
	{
		sb.append("[WARN]").append(msg);
		sb.append("\n");
	}

	protected void internalWarn(String msg, Throwable cause)
	{
		internalWarn(msg);
		String string = CommonUtil.toString(cause);
		sb.append(string);
		sb.append("\n");
	}

	protected void internalError(String msg)
	{
		sb.append("[ERROR]").append(msg);
		sb.append("\n");
	}

	protected void internalError(String msg, Throwable cause)
	{
		internalError(msg);
		String string = CommonUtil.toString(cause);
		sb.append(string);
		sb.append("\n");
	}

	public String getName()
	{
		return null;
	}

	public boolean isTraceEnabled()
	{
		return true;
	}

	public boolean isDebugEnabled()
	{
		return true;
	}

	public boolean isInfoEnabled()
	{
		return true;
	}

	public boolean isWarnEnabled()
	{
		return true;
	}

	public boolean isErrorEnabled()
	{
		return true;
	}


	@Override
	public String toString()
	{
		return sb.toString();
	}

	@Override
	protected void internalInfo(Map<String, String> msg) {
		// TODO Auto-generated method stub
		
	}

}
