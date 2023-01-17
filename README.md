# wordcomplete
## Summary
This is the solution to the wordcomplete problem. This implementation uses a PatriciaTrie data struct to efficiently store dictionary word keys to Integer values that represent the number of times a given word has been hit. This hit count is then used to rank the results in the getCompletsions() search method in the API. 

The full English language dictionary provided is about 16MB in size, however we are only storing the word->hitCount rather than the entire dictionary. I didn't calculate actual size of the Trie, however, these numbers can be used as a upper bound on space since the Trie will be a subset of this. Additionally, the PatriciaTrie compresses the data structure to have a minimal memory footprint. This should easily fit memory on any commodity hardware.

The entire implementation is driven from the unit tests. There is not main function to run. The tests are located here:
>/Users/cody/Documents/job/cribl/wordcomplete/src/test/java/com/cribl/app/engine/WordLookupEnginerTest.java
>/Users/cody/Documents/job/cribl/wordcomplete/src/test/java/com/cribl/data/DataLoaderTest.java

Most of the tests use a simple DB created using addWord() to prove correctness of the algorith. 

## Building
From the command line you can simply run `mvn test` and all the tests will run. There isn't a lot of useful outout there though. 

## Requirements
>1. Select a data structure that provides word-level auto-completions, given a specific prefix. The prefix “pi” should, for example, result in a list of words like “pizza”, “pie”, “pineapple,” and so on. The total number of returned words can be limited to N=20.
>2. The data structure has to deal only with words of the English (26-character, case-insensitive) alphabet. Webster’s current dictionary holds 171,476 words (with an average length of 4.7 characters). This should be considered to be the payload of this data structure.
>3. We want to learn “automatically” which words are more likely to be used and which are not that important. Our API will therefore have a feedback mechanism, telling us which (if any) word of our returned auto-completion list has actually been selected by the user. This feedback is supposed to help us rank the returned results, meaning that the more frequently used words will be returned before the less frequently used ones, and if we have more than N words in our list, we ensure that the top-ranked N are returned.
>4. Latency is important! Our new component might get hammered by hundreds of requests per second, and we will have to try to respond faster than the users can continue typing. Consider if a cache of some sort might help to keep the latency as low as possible.
>5. Simplicity is key! We could, of course, use a key-value database, using every possible prefix as key and corresponding completions as values, but we want to be more efficient. The solution should use as few resources (CPU, memory) as possible, ideally fitting on a single machine (you can leave high-availability concerns completely to our competent SRE team). If you use a language like Java, Python, or JS, you can ignore the overhead that the JVM/interpreter by itself adds to the solution, but your data structure should be designed for efficiency. A simple solution is much easier to operate, and an efficient one will save valuable cloud-provider dollars.
