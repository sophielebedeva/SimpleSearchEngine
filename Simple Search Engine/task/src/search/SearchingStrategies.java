package search;

import java.util.*;

public class SearchingStrategies {
    private final HashMap<String, ArrayList<Integer>> invertedIndex;

    public SearchingStrategies(HashMap<String, ArrayList<Integer>> invertedIndex) {
        this.invertedIndex = invertedIndex;
    }

    public Collection<Integer> AllStrategy(String[] separatedWords) {
        ArrayList<ArrayList<Integer>> lineNumbers = new ArrayList<>();
        for (String partOfRequest : separatedWords) {
            if (invertedIndex.containsKey(partOfRequest)) {
                ArrayList<Integer> numbers = new ArrayList<>(invertedIndex.get(partOfRequest));
                lineNumbers.add(numbers);
            }
        }
        if (!lineNumbers.isEmpty()) {
            return lineNumbers.stream().reduce(lineNumbers.get(0), (a, b) -> {
                a.addAll(b);
                return a;
            });
        }
        return Collections.emptyList();

    }

    public Collection<Integer> AnyStrategy(String[] separatedWords) {
        HashSet<Integer> uniqueLineNumbers = new HashSet<>();
        for (String partOfRequest : separatedWords) {
            if (invertedIndex.containsKey(partOfRequest)) {
                uniqueLineNumbers.addAll(invertedIndex.get(partOfRequest));
            }
        }
        return uniqueLineNumbers;
    }
}
