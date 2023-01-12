package com.cribl.data;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.nio.file.Files;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.collections4.Trie;
import org.apache.commons.collections4.trie.PatriciaTrie;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;

import com.cribl.app.api.WordLookup;
import com.cribl.app.engine.WordLookupFrequencyAwareEngine;
import com.google.common.annotations.VisibleForTesting;

/**
 * Loads data in from a resource directory on the classpath. If you get exception due to not being able to find
 * the file, look at the project configuration and make sure the resources directory is on top of your build path.
 * Loads in a standard dictionary file is csv format.
 * 
 * This class essentially acts as a decorator of a dictionary of data applied to the WordLook engine.
 * 
 * @author cody
 *
 */
public class LookupEngineDataLoader {
	private static final Logger LOGGER = Logger.getLogger(LookupEngineDataLoader.class);
	private static final WordLookup engine;
	
	private static final String FILE_NAME = "/Dictionary.csv";
	
	// Load once for all instances on a server
	// Spring constructor injection is a better way of managing dependencies, 
	// but trying to keep the app simple
	static {
		engine = new WordLookupFrequencyAwareEngine();
		load();
	}
	
	public WordLookup getEngine() {
		return engine;
	}

	private static void load() {
		LOGGER.log(Level.DEBUG, "Initializing Dicionary");
		InputStream res = LookupEngineDataLoader.class.getResourceAsStream(FILE_NAME);
		
		if(res == null) {
			// Cannot make any progress return gracefully
			LOGGER.log(Level.FATAL, "Could not open [" + FILE_NAME + "]");
			return;
		}

		BufferedReader reader;
		try {
			reader = new BufferedReader(new InputStreamReader(res));
			String line = reader.readLine(); // Advance past the header row
			while ((line = reader.readLine()) != null) {
				String []row = line.split(",");
				engine.addWord(row[0]);
			}
			reader.close();
		} catch(IOException e) {
			LOGGER.log(Level.FATAL, "Could not open [" + FILE_NAME + "] : " 
					+ Arrays.deepToString(e.getStackTrace()));
		}
		// TODO Create Enum for load state so we don't log magic strings
		LOGGER.log(Level.DEBUG, "Initializing Dicionary COMPLETE");
		return;
	}
}
