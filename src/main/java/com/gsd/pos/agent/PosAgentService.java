package com.gsd.pos.agent;

import java.io.File;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import org.apache.log4j.Logger;
import com.gsd.pos.agent.server.MyServer;

/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */
/**
 * Sample service implementation for use with Windows Procrun.
 * <p>
 * Use the main() method for running as a Java (external) service. Use the
 * start() and stop() methods for running as a jvm (in-process) service
 */
public class PosAgentService implements Runnable {
	private static final Logger logger = Logger.getLogger(PosAgentService.class
			.getName());
	private static final long MS_PER_SEC = 1000L; // Milliseconds in a second
	private static volatile Thread thrd; // start and stop are called from
											// different threads
	private final File stopFile;

	/**
	 * 
	 * @param wait
	 *            seconds to wait in loop
	 * @param filename
	 *            optional filename - if non-null, run loop will stop when it
	 *            disappears
	 * @throws IOException
	 */
	private PosAgentService(File file) {
		stopFile = file;
	}

	private static File tmpFile(String filename) {
		return new File(System.getProperty("java.io.tmpdir"),
				filename != null ? filename : "PosAgentService.tmp");
	}

	private static void usage() {
		System.err.println("Must supply the argument 'start' or 'stop'");
	}

	/**
	 * Helper method for process args with defaults.
	 * 
	 * @param args
	 *            array of string arguments, may be empty
	 * @param argnum
	 *            which argument to extract
	 * @return the argument or null
	 */
	private static String getArg(String[] args, int argnum) {
		if (args.length > argnum) {
			return args[argnum];
		} else {
			return null;
		}
	}

	private static void logSystemEnvironment() {
	}

	/**
	 * Common entry point for start and stop service functions. To allow for use
	 * with Java mode, a temporary file is created by the start service, and a
	 * deleted by the stop service.
	 * 
	 * @param args
	 *            [start [pause time] | stop]
	 * @throws IOException
	 *             if there are problems creating or deleting the temporary file
	 */
	public static void main(String[] args) throws IOException {
		final int argc = args.length;
		logger.debug("PosAgentService called with " + argc
				+ " arguments from thread: " + Thread.currentThread());
		for (int i = 0; i < argc; i++) {
			logger.debug("[" + i + "] " + args[i]);
		}
		String mode = getArg(args, 0);
		if ("start".equals(mode)) {
			File f = tmpFile(getArg(args, 2));
			logger.debug("Creating file: " + f.getPath());
			f.createNewFile();
			startThread(f);
		} else if ("stop".equals(mode)) {
			final File tmpFile = tmpFile(getArg(args, 1));
			logger.debug("Deleting file: " + tmpFile.getPath());
			tmpFile.delete();
		} else {
			usage();
		}
	}

	/**
	 * Start the jvm version of the service, and waits for it to complete.
	 * 
	 * @param args
	 *            optional, arg[0] = timeout (seconds)
	 */
	public static void start(String[] args) {
		startThread(null);
		while (thrd.isAlive()) {
			try {
				thrd.join();
			} catch (InterruptedException ie) {
				// Ignored
			}
		}
	}

	private static void startThread(File file) {
		thrd = new Thread(new PosAgentService(file));
		thrd.start();
	}

	/**
	 * Stop the JVM version of the service.
	 * 
	 * @param args
	 *            ignored
	 */
	public static void stop(String[] args) {
		if (thrd != null) {
			logger.debug("Interrupting the thread");
			thrd.interrupt();
		} else {
			logger.debug("No thread to interrupt");
		}
	}

	public void run() {
		logger.debug("Started thread in " + System.getProperty("user.dir"));
		logSystemEnvironment();
		try {
			new MyServer().start();
		} catch (Exception e) {
			e.printStackTrace();
			logger.warn(e);
			logger.debug("Exiting");
		}

	}

	protected void finalize() {
		logger.debug("Finalize called from thread " + Thread.currentThread());
	}
}