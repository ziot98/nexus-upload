package app.executor;

import app.entity.UploadOption;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.zeroturnaround.exec.InvalidExitValueException;
import org.zeroturnaround.exec.ProcessExecutor;
import org.zeroturnaround.exec.ProcessResult;
import org.zeroturnaround.exec.stream.LogOutputStream;

import javax.swing.*;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.concurrent.TimeoutException;

public class RedirectProcessExecutor {
	private final Logger logger = LoggerFactory.getLogger(this.getClass());
	private static RedirectProcessExecutor instance;
	private boolean isFirst = true;

	public RedirectProcessExecutor() {
		this.instance = this;
	}

	public static RedirectProcessExecutor getInstance() {
		return instance;
	}

	public ProcessResult run(String[] args, JTextArea outputTextArea) throws InterruptedException {

		try {

			ProcessExecutor processExecutor = new ProcessExecutor();

			return processExecutor.command(args).redirectOutput(new LogOutputStream() {

				@Override
				protected void processLine(String arg0) {

					if (isFirst) {
						outputTextArea.setText(null);
						isFirst = false;
					}

					outputTextArea.append(arg0);
					outputTextArea.append("\n");

				}
			}).execute();

		} catch (InterruptedException e) {
			throw e;
		}
		catch (InvalidExitValueException | IOException | TimeoutException e) {

			StringWriter errors = new StringWriter();
			e.printStackTrace(new PrintWriter(errors));
			
			outputTextArea.append("***ERROR***\n");
			outputTextArea.append(errors.toString());

			return new ProcessResult(-1, null);
		}
	}

	public boolean isFirst() {
		return isFirst;
	}

	public void setFirst(boolean first) {
		isFirst = first;
	}
}
