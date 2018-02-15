package unix;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;

public class JavaShell {

	private static JavaShell instance;

	public static final JavaShell getInstance() {
		if (instance == null) {
			synchronized (JavaShell.class) {
				if (instance == null) {
					instance = new JavaShell();
				}
			}
		}
		return instance;
	}

	public JavaShell() {
		super();
	}

	public String executeShell(String shellCommand) {
		String success = "失败";
		StringBuffer stringBuffer = new StringBuffer();
		BufferedReader bufferedReader = null;
		try {
			// logger.info("+++java.shell   ： 开始执行Shell命令 ");
			Process pid = null;
			String[] cmd = { "/bin/sh", "-c", shellCommand };

			// 执行Shell命令
			pid = Runtime.getRuntime().exec(cmd);
			if (pid != null) {
				// logger.info("+++java.shell 进程号：" + pid.toString());
				// bufferedReader用于读取Shell的输出内容
				bufferedReader = new BufferedReader(new InputStreamReader(
						pid.getInputStream()));
				pid.waitFor();
			} else {
				// logger.info("+++java.shell 没有进程pid");
			}

			stringBuffer.append("Shell命令执行完毕\r\n执行结果为：\r\n");
			String line = null;
			// 读取Shell的输出内容，并添加到stringBuffer中
			while (bufferedReader != null
					&& (line = bufferedReader.readLine()) != null) {
				stringBuffer.append(line).append("\r\n");
			}

		} catch (Exception ioe) {
			// logger.info(stringBuffer.toString() + "执行Shell命令时发生异常：\r\n");
		} finally {
			if (bufferedReader != null) {
				OutputStreamWriter outputStreamWriter = null;
				try {
					bufferedReader.close();
					// 将Shell的执行情况输出到日志文件中
					OutputStream outputStream = new ByteArrayOutputStream();
					outputStreamWriter = new OutputStreamWriter(outputStream,
							"UTF-8");
					outputStreamWriter.write(stringBuffer.toString());
				} catch (Exception e) {
					e.printStackTrace();
				} finally {
					try {
						outputStreamWriter.close();
					} catch (IOException e) {
						// logger.info(e);
					}
				}
			}
			success = "成功";
		}

		return "执行状态：" + success + "\r\n" + stringBuffer.toString();
	}

}