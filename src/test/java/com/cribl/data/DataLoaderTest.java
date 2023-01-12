package com.cribl.data;

import org.junit.Test;

import com.cribl.app.engine.WordLookupEnginerTest;
import com.cribl.app.engine.WordLookupFrequencyAwareEngine;
import com.cribl.data.LookupEngineDataLoader;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.IOException;
import java.util.List;
import java.util.SortedMap;

public class DataLoaderTest{
    
	/**
	 * This test loads the full DB so it can potentially run a little slower. Though I've only noticed it during debug
	 * Here we test the the Loader can load the full DB and then project out a small matching list.
	 */
    @Test
    public void testLoad(){
    	// Setup
        LookupEngineDataLoader dl = new LookupEngineDataLoader();
        String [] expectedCompletions =  {"axman", "axmen", "axminster"};
        
        // Verify
        List<String> completions = dl.getEngine().getCompletions("axm");
        
        assertTrue(completions.size() == expectedCompletions.length);
        for(int i = 0; i < completions.size(); i++) {
        	assertEquals(expectedCompletions[i], completions.get(i));
        }
    }
}