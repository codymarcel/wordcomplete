package com.cribl.app.engine;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

import org.junit.Test;

import com.cribl.app.api.WordLookup;
import com.cribl.data.LookupEngineDataLoader;


/**
 * 
 * @author Cody Marcel
*/
public class WordLookupEnginerTest {

	@Test
	public void incrementWordHit() {
		// Setup
		// Bypass interface to get test methods
		WordLookupFrequencyAwareEngine engine = new WordLookupFrequencyAwareEngine();
		String[] words = { "box", "boxes", "boards", "a", "ax", "book", "books" };
		
		for (String word : words) {
			engine.addWord(word);
		}

		// Act
		engine.incrementWordHit("a");
		engine.incrementWordHit("ax");
		engine.incrementWordHit("a");
		engine.incrementWordHit("a");
		List<String> completions = engine.getCompletions("a");

		// Verify
		int num = engine.getValue("a");
		assertTrue(completions.size() == 2);
		assertTrue("Count a Failed expected 3 got " + num,
				num == 3);
		num = engine.getValue("ax");
		assertTrue("Count ax Failed expected 1 got " + num,
				num == 1);
	}
	
	/**
	 * Ensure that keys of equal value rankings return in alphabetical order
	 */
	@Test
	public void getCompletions_EnsureOrderingDefaultAplpha() {
		// Setup
		WordLookup engine = new WordLookupFrequencyAwareEngine();
		String[] words = { "abstract", "awesome", "b",  "a", "ax"};
		String[] expectedOrder = { "a", "abstract", "awesome", "ax"};
		
		for (String word : words) {
			engine.addWord(word);
		}

		// Act
		// Increment each value once to give them equal weight. While initial values work we implicitly also test increment
		for(String word : words) {
			engine.incrementWordHit(word);
		}
		
		List<String> completions = engine.getCompletions("a");

		// Verify
		assertTrue(completions.size() == 4);
		
		for(int i = 0; i < expectedOrder.length; i++) {
			assertEquals(expectedOrder[i], completions.get(i));
		}
	}

	@Test
	public void getCompletions_EnsureOrderingMixedWeights(){
		// Setup
		WordLookup engine = new WordLookupFrequencyAwareEngine();
		Pair[] expectedCompletionsOrder = {new Pair("box", 5),
				new Pair("boxes", 4),
				new Pair("book", 3),
				new Pair("boat", 1),
				new Pair("boats", 1),
				};
		
		// Add extra words not included in result to ensure they have no effect
		engine.addWord("a");
		engine.addWord("ba");
		
		// Act
		// Increment the values to the expected outcome as defined by the Pairs order
		for (Pair pair : expectedCompletionsOrder) {
			engine.addWord(pair.name);
			for(int i = 0; i < pair.value; i++) {
				engine.incrementWordHit(pair.name);
			}
		}
		
		List<String> completions = engine.getCompletions("bo");

		// Verify
		assertTrue(completions.size() == 5);
		
		for(int i = 0; i < expectedCompletionsOrder.length; i++) {
			assertEquals(expectedCompletionsOrder[i].name, completions.get(i));
		}
		
	}
	
	@Test
	public void getCompletions_LimitEnforced() {
    	// Setup Load full DB
        LookupEngineDataLoader dl = new LookupEngineDataLoader();
        // Verify
        // 39 values for "a" in data set
        List<String> completions = dl.getEngine().getCompletions("a");
        assertEquals(completions.size(), WordLookupFrequencyAwareEngine.COMPLETION_MAX);
	}

	@Test
	public void getCompletions_SimleListAdd() {

		// Setup
		WordLookup engine = new WordLookupFrequencyAwareEngine();
		String[] words = { "box", "boxes", "a", "ax", "book", "books" };
		String[] expectedCompletions = {"box", "boxes"};

		for (String word : words) {
			engine.addWord(word);
		}

		// Act
		List<String> completions = engine.getCompletions("box");

		// Verify
		for(int i = 0; i < completions.size(); i++) {
			String val = completions.get(i);
			assertNotNull(val);
			assertTrue(expectedCompletions[i].equals(val));
		}
	}
	
	class Pair{
		String name;
		Integer value;
		
		Pair(String name, Integer value){
			this.name = name;
			this.value = value;
		}
	}
}
