package com.cribl.app.api;

import java.util.List;

/**
 * API For word auto completion engine
 * 
 * All Operations run in O(k) where k = length of the word
 * All results are case-insensitive
 * ß
 * @author cody
 *
 */
public interface WordLookup {
	/**
	 * Add the word to the underlying dictionary
	 * 
	 * @param newWord to be added
	 */
	void addWord(String newWord);

	/**
	 * Increments an associated counter value for the given word. This allows us to
	 * rank results returned from {@link com.cribl.app.api.WordLookup#getCompletions(String)}.
	 * 
	 * 
	 * @param chosenWord increments the value associated with this key
	 * 
	 */
	void incrementWordHit(String chosenWord);

	/**
	 * Gets the case-insensitive matching words in the dictionary for the given prefix.
	 * Ex. Dict = ["box", "boy", "book"]
	 *     getCompletions("bo") -> ["box", "boy"]
	 *     
	 * NOTE!!
	 * 
	 * Completion results are limited to a maximum of 20 matches.
	 * Completion results are ordered by the cardinality of the number of times 
	 * 	{@link com.cribl.app.api.WordLookup#incrementWordHit(String)} has been called for a given key.
	 * The more frequently a particular key is hit, the higher it's ranking in the search result
	 * 
	 * Keys of equal hit values will be sorted alphabetically.
	 * 
	 * @param prefix word to match
	 * @return
	 */
	List<String> getCompletions(String prefix);
}