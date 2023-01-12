package com.cribl.app.engine;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.SortedMap;
import java.util.stream.Collectors;

import org.apache.commons.collections4.Trie;
import org.apache.commons.collections4.trie.PatriciaTrie;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;

import com.cribl.app.api.WordLookup;
import com.google.common.annotations.VisibleForTesting;

/**
 * Main driver for powering the Word lookup API
 * 
 * This implementation uses a Patricia Trie structure giving efficient worst case O(k) where k is the length of the word
 * being acted upon in dictionary. O(k) operations are possible because Patricia Treis store data at each node rather than 
 * just leaf nodes. The Apache Commons version of the Patricia Tries are also compressed giving
 * an efficient use of space.
 * 
 *  The trie nodes will take on a logical structure for efficient lookups of words. This is an example for the word "book"
 *  TreeNode
 *  	key = "book"
 *  	value = 0
 * 
 * @author cody
 *
 */
public class WordLookupFrequencyAwareEngine implements WordLookup {
	
	// Constant for simplicity. Perhaps .properties or config file is better, but went with simplicity since it's only 1 
	public static final int COMPLETION_MAX = 20;
	private static final Logger LOGGER = Logger.getLogger(WordLookupFrequencyAwareEngine.class);
	
	/** 
	 * Primary data structure for the dictionary to be stored. This is implemented using Apache Commons PatriciaTrie
	 */
	private final Trie<String,Integer> searchTree;
	
	public WordLookupFrequencyAwareEngine() {
		this.searchTree = new PatriciaTrie<>();
	}
	
	/**
	 * @see {@link WordLookup}
	 */
	@Override
	public void addWord(String newWord) {
		/** 
		 * Filter out duplicate definitions of a word since we are only concerned with the word itself  
		 */
		newWord = newWord.toLowerCase();
		if(!searchTree.containsKey(newWord)) {
			this.searchTree.put(newWord, 0);
		} else {
			LOGGER.log(Level.DEBUG, "Duplicate Value: " + newWord);
		}
	}

	/**
	 * @see {@link WordLookup}
	 */
	@Override
	public void incrementWordHit(String chosenWord) {
		// Remove case sensitivity
		chosenWord = chosenWord.toLowerCase();
		Integer value = searchTree.get(chosenWord);
		searchTree.put(chosenWord, ++value);
		LOGGER.log(Level.INFO, "Incrimented Key: [" + chosenWord 
				+ "] Value [" + searchTree.get(chosenWord) + "]");
	}

	/**
	 * @see {@link WordLookup}
	 */
	@Override
	public List<String> getCompletions(String prefix) {
		Map<String, Integer> hitCountOrderedCompletions = new LinkedHashMap<>();
		prefix = prefix.toLowerCase();
		SortedMap<String, Integer> prefixMap = searchTree.prefixMap(prefix);
		
		/**
		 * Transform map to be ordered by the number of times an entry was hit.
		 * This operation should take at most O(k) where k = length of the prefix
		 */
		prefixMap.entrySet().stream()
	        .sorted(Map.Entry.<String, Integer>comparingByValue().reversed())
	        .forEachOrdered(x -> hitCountOrderedCompletions.put(x.getKey(), x.getValue()));
		
		
		/**
		 * Limit values streamed through which gives these transformations O(1)
		 * We also only need to return the value portion of the Entry
		 */
		ArrayList<String> nodes = hitCountOrderedCompletions.entrySet().stream()
				.limit(COMPLETION_MAX)
				.map(Map.Entry::getKey)
				.collect(Collectors.toCollection(ArrayList::new));
		return nodes;
	}

	@VisibleForTesting
	Integer getValue(String key) {
		return searchTree.get(key);
	}
	
	@VisibleForTesting
	int trieSize() {
		return searchTree.size();
	}
}
