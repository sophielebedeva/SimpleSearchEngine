package search;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class SearchEngine {
    String[] args;
    Scanner scanner = new Scanner(System.in);
    ArrayList<String> listOfPeople = new ArrayList<>();
    int choice;
    HashMap<String, ArrayList<Integer>> invertedIndex = new HashMap<>();


    public SearchEngine(String[] args) {
        this.args = args;
    }

    private void importInitial(ArrayList<String> listOfPeople) {
        if (args[0].toLowerCase(Locale.ROOT).equals("--data")) {
            File dataFromFile = new File(args[1]);
            int lineNumber = 0;
            try {
                Scanner scannerForFiles = new Scanner(dataFromFile);
                while (scannerForFiles.hasNextLine()) {
                    String line = scannerForFiles.nextLine();
                    listOfPeople.add(line);
                    String[] words = line.toLowerCase(Locale.ROOT).split(" ");
                    for (String word : words) {
                        if (!invertedIndex.containsKey(word)) {
                            ArrayList<Integer> occursLines = new ArrayList<>();
                            occursLines.add(lineNumber);
                            invertedIndex.put(word, occursLines);
                        } else {
                            invertedIndex.get(word).add(lineNumber);
                        }
                    }
                    lineNumber++;
                }
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
    }

    public void start() {
        importInitial(listOfPeople);
        printMenu();
        while (choice != 0) {
            choiceProcessing(choice);
            printMenu();
        }
        System.out.println("Bye!");
    }

    void choiceProcessing(int choice) {
        switch (choice) {
            case 1:
                queriesHandling();
                break;
            case 2:
                printAllPeople(listOfPeople);
                break;
            case 0:
                return;
            default:
                System.out.println("Incorrect option! Try again.");
                break;
        }
    }

    public void printMenu() {
        System.out.println("\n=== Menu ===\n" +
                "1. Find a person\n" +
                "2. Print all people\n" +
                "0. Exit");
        choice = scanner.nextInt();
        scanner.nextLine();
        System.out.println();
    }

    public void printAllPeople(ArrayList<String> listOfPeople) {
        System.out.println("=== List of people ===");
        listOfPeople.forEach(System.out::println);
    }

    public void queriesHandling() {
        SearchingStrategies strategies = new SearchingStrategies(invertedIndex);
        Collection<Integer> linesNumbers = new ArrayList<>();
        System.out.println("Select a matching strategy: ALL, ANY, NONE");
        String strategyChoice = scanner.nextLine();
        System.out.println("Enter a name or email to search all suitable people.");
        String data = scanner.nextLine().toLowerCase(Locale.ROOT);
        System.out.println();
        String[] separatedWords = data.split(" ");
        switch (strategyChoice) {
            case "ANY":
                linesNumbers = strategies.AnyStrategy(separatedWords);
                break;
            case "NONE":
                Collection<Integer> linesContainQuery = strategies.AnyStrategy(separatedWords);
                List<Integer> numLinesArray = IntStream.range(0, listOfPeople.size())
                        .boxed().collect(Collectors.toList());
                linesNumbers = numLinesArray
                        .stream()
                        .filter(lineNumber -> !linesContainQuery.contains(lineNumber)).collect(Collectors.toSet());
                break;
            case "ALL":
                linesNumbers = strategies.AllStrategy(separatedWords);
                break;

        }
        if (linesNumbers.isEmpty()) {
            System.out.println("No matching people found.");
        } else {
            System.out.printf("%d persons found:\n", linesNumbers.size());
            linesNumbers
                    .forEach(lineNumber -> System.out.println(listOfPeople.get(lineNumber)));
        }
    }
}