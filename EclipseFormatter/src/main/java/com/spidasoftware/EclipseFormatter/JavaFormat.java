/**
 * The JavaFormat class that will format java source code
 * 
 * July of 2013
 * spidasoftware
 * @author Nick Joodi
 */

package com.spidasoftware.EclipseFormatter;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;
import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.ConsoleAppender;
import org.apache.log4j.Level;
import org.apache.log4j.Appender;
import org.apache.log4j.PatternLayout;

import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.core.ToolFactory;
import org.eclipse.jdt.core.formatter.CodeFormatter;
import org.eclipse.jdt.core.formatter.DefaultCodeFormatterConstants;
import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.text.Document;
import org.eclipse.jface.text.IDocument;
import org.eclipse.text.edits.MalformedTreeException;
import org.eclipse.text.edits.TextEdit;

public class JavaFormat {
	private boolean correctlyFormatted;
	private static Logger log = Logger.getRootLogger();

	/*
	 * A no argument constructor that will set the correctlyFormatted field to false
	 */
	public JavaFormat() {
		ConsoleAppender console = new ConsoleAppender();
		String PATTERN = "%m%n";
		console.setLayout(new PatternLayout(PATTERN));
		console.setThreshold(Level.DEBUG);
		console.activateOptions();
		correctlyFormatted = false;
	}

	/**
	 * A two-argument method that will take two strings, a filename and its respective code
	 * and format the code
	 * 
	 * @param filename
	 *            The name of that file
	 * @param code
	 *			  The string of code that will be formatted
	 */
	public void format(String fileName, String code) {
		CodeFormatter cf = initializeFormatter();
		TextEdit te = cf.format(CodeFormatter.K_UNKNOWN, code, 0, code.length(), 0,
				System.getProperty("line.separator"));
		IDocument dc = new Document(code.toString());
		if (te == null || code.length() == 0) {
			log.info("!!! Could not format " + fileName + " !!!");
		} else {
			try {
				te.apply(dc);

				PrintWriter out = new PrintWriter(new FileWriter(fileName));
				out.println(dc.get());
				out.close();

				log.info("*** Java standard formatting conventions have been applied to " + fileName + " ***");
			} catch (MalformedTreeException e) {
				log.error(e, e);
			} catch (BadLocationException e) {
				log.error(e, e);
			} catch (IOException e) {
				log.error(e, e);
			}
			correctlyFormatted = true;
		}
	}

	/**
	 * A method that will return whether or not code has been formatted
	 * 
	 * @return isFormatted		 
	 */
	public boolean isFormatted() {
		return correctlyFormatted;
	}

	/**
	 * A static method that will prepare the JavaFormat object for formatting
	 * the respective code that was passed in to the format method 		 
	 */
	@SuppressWarnings({ "unchecked", "deprecation" })
	private static CodeFormatter initializeFormatter() {
		@SuppressWarnings("rawtypes")
		Map options = DefaultCodeFormatterConstants.getEclipseDefaultSettings();
		options.put(JavaCore.COMPILER_COMPLIANCE, JavaCore.VERSION_1_6);
		options.put(JavaCore.COMPILER_CODEGEN_TARGET_PLATFORM, JavaCore.VERSION_1_6);
		options.put(JavaCore.COMPILER_SOURCE, JavaCore.VERSION_1_6);
		//options.put(DefaultCodeFormatterConstants.FORMATTER_ALIGNMENT_FOR_SELECTOR_IN_METHOD_INVOCATION,
		//		DefaultCodeFormatterConstants.createAlignmentValue(false, DefaultCodeFormatterConstants.WRAP_NO_SPLIT, DefaultCodeFormatterConstants.INDENT_DEFAULT));

		options.put(DefaultCodeFormatterConstants.FORMATTER_LINE_SPLIT, "120");
		//	System.out.println(options.toString());
		final CodeFormatter codeFormatter = ToolFactory.createCodeFormatter(options);
		return codeFormatter;

	}
}

