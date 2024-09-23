import org.jfree.ui.RefineryUtilities;

import java.io.*;
import java.util.ArrayList;
import java.util.Random;
import org.jfree.chart.JFreeChart;

// Valid costs for a card in the deck are positive integers between 0 and 6 (inclusive)
// Each card has an associated energy cost, determining how many energy points are required to play that card

// Program that calculates the total energy cost of all the cards in a Slay the Spire deck

// Program prompts the user to input the name of a file that contains the card names and respective energy cost

// Creates a report that tallies the total energy cost and a energy cost histogram for the deck
// Output the report in a pdf file

public class Main {

    private static String deckId;
    private static double totalCost;

    private static int cardCount;

    protected static ArrayList<String[]> cardList = new ArrayList<>();
    protected static ArrayList<String[]> invalidCardList = new ArrayList<>();

    /**
     * Reads the deck from a plain text file in which each row contains a single card name and its cost,
     * separated by a colon. This method processes the file and populates the valid and invalid card lists.
     *
     *
     * Each line is expected to have the format: "CardName:CardCost".
     * The method ensures each line contains exactly two elements and checks the validity of each card.
     * Valid cards are added to the `cardList`, while invalid cards are added to the `invalidCardList`.
     * The method handles up to 1000 cards.
     *
     *
     * @param file The path to the input file containing the card data.
     *
     * @return The list of valid cards, where each entry is a String array containing:
     *         - card name (index 0)
     *         - card cost (index 1)
     *
     * @throws IOException If an input or output exception occurred while reading the file.
     */
    private static  ArrayList<String[]> readInputFile(String file) {
        BufferedReader reader = null;
        boolean cardValidity;
        int cardIndex = 0; // Counter to ensure the program handles up to 1000 cards

        try {
            // Initialize file reader
            reader = new BufferedReader(new FileReader(file));
            String line = reader.readLine();

            // Loop through each line in the file
            while (line != null) {
                // Split line by colon delimiter to separate card name and energy cost
                String[] card = line.split(":");

                // Check if line is correctly formatted with 2 elements
                if (card.length == 2) {
                    String cardName = card[0];
                    String cardCost = card[1];

                    // Check validity of card
                    cardValidity = validCardChecker(cardName, cardCost);

                    if (cardValidity && cardIndex < 1000) {
                        cardList.add(card);  // Add valid card
                    } else {
                        invalidCardList.add(card);  // Add invalid cards to list for void report
                    }

                    cardIndex++; // Increment counter to ensure there are no more than 1000 cards in a given deck
                } else {
                    System.err.println("Invalid line format: " + line);
                }

                line = reader.readLine();
            }

        // Handle I/O exceptions during file reading
        } catch (IOException e) {
            System.err.println(e);
        }

        // Declare total card count for later reference
        cardCount = cardIndex;

        // return list of valid cards
        return cardList;
    }


    /**
     * Generates a random 9-digit unique identifier for a deck based on the input file name.
     *
     * <p>
     * The method creates a random numeric string of 9 digits using a `Random` object. This string is
     * then assigned to the `deckId` variable and returned as the unique deck identifier.
     * </p>
     *
     * @param file The name of the input file (used here only as a parameter for compatibility).
     *
     * @return A 9-digit string that serves as the unique deck identifier.
     */
    private static String generateFileId(String file){
        // Initialize a StringBuilder to accumulate digits
        StringBuilder randomString = new StringBuilder();
        Random digit = new Random();

        // Generate a random 9-digit string
        for(int i = 0; i < 9; i++){
          randomString.append((digit.nextInt(10))); // Bound range from 0-9 for each digit
        }

        // Convert stringBuilder back to a string for unique deck identifier
        deckId = randomString.toString();

        // Return the unique deck ID
        return deckId;
    }


    /**
     * Reads the deck from the input file and calculates the total cost of all valid cards.
     *
     * This method processes the input file, where each line contains a card name and cost in the format
     * "CardName:CardCost". It checks the validity of each card and sums up the total cost for all valid cards.
     * Invalid card costs or improperly formatted lines are ignored.
     *
     * @param file The path to the input file containing card names and costs.
     *
     * @return The total cost of all valid cards in the deck. If no valid cards are found, returns 0.
     *
     * @throws IOException If an input/output exception occurs while reading the file.
     */
    private static double findTotalCost(String file){
        // Initialize the file reader
        BufferedReader reader = null;
        double cost = 0;
        try {
            reader = new BufferedReader(new FileReader(file));
            String line = reader.readLine();

            // Loop through each line in the file
            while (line != null) {

                // Ensure the line contains a colon to separate card name and cost
                if (line.contains(":")) {
                    String[] card = line.split(":");

                    // Ensure the line contains exactly two elements (name and cost)
                    if (card.length == 2) {
                        String cardName = card[0].trim();  // Trim spaces
                        String cardCost = card[1].trim();

                        // Validate the card name and cost
                        if (validCardChecker(cardName, cardCost)) {
                            try {
                                // Parse card cost and add to total cost (after converting to double)
                                cost += Double.parseDouble(cardCost);
                            } catch (NumberFormatException e) {
                                System.err.println("Invalid card cost format for '" + cardName + "': " + cardCost);
                            }
                    }
                }

                } else {
                    // Log a message if the line doesn't contain the correct format
                    System.out.println("Invalid card input. Cards should be in this format ");
                }

                // Reads next line
                line = reader.readLine();
            }

            // Handle any I/O exceptions that occur while reading the file
            } catch (IOException e) {
                System.err.println(e);
            }

        // returns total cost of deck
        return cost;

    }

    /**
     * Checks the validity of the card based on the name and cost provided.
     *
     *
     * A valid card should have:
     * - A non-empty name.
     * - A numeric cost that is non-negative.
     *
     *
     * @param cardName The name of the card.
     * @param cardCost The cost of the card as a string.
     * @return `true` if the card is valid, `false` otherwise.
     */
    private static boolean validCardChecker(String cardName, String cardCost){
        boolean valid = true;
        // Check invalid cost value, a card with empty name, a card with name that is only spaces or tabs
        if (cardName == null || cardName.trim().isEmpty()) {
            System.err.println("Invalid card name: '" + cardName + "'. It cannot be empty or only spaces.");
            valid = false;
        }
        // Ensure card name is strictly alphabetical (optional, but can be added if needed)
        if (cardName != null && !cardName.matches("[a-zA-Z ]+")) {
            System.err.println("Invalid card name format: '" + cardName + "'. Card names should only contain alphabetic characters and spaces.");
            valid = false;
        }
        try {
            int cost = Integer.parseInt(cardCost.trim()); // Convert string to integer
            if (cost < 0 || cost > 6) { // Energy cards are typically between 0 - 6
                //System.err.println("Invalid card cost for '" + cardName + "': " + cost + ". Card energy must be between 0 and 6.");
                valid = false;
            }
        } catch (NumberFormatException e) {
            System.err.println("Invalid card cost format for '" + cardName + "': " + cardCost + ". It must be a number.");
            valid = false;
        }
        return valid;
    }



    public static void main(String[] args) {
        String inputFile = "/Users/suadhm/IdeaProjects/GitAssignment/src/InputFile.txt";
        String filePath = "/Users/suadhm/IdeaProjects/GitAssignment/";

        cardList = readInputFile(inputFile);
        totalCost = findTotalCost(inputFile);
        deckId = generateFileId(inputFile);


        // Create and display the histogram
        Histogram histogram = new Histogram("Histogram Test", cardList);
        histogram.pack();
        RefineryUtilities.centerFrameOnScreen(histogram);
        histogram.setVisible(true);

        // Generate a chart from the histogram's dataset
        JFreeChart chart = histogram.createChart(histogram.createDataset(cardList));

        // Save the chart as a temporary image
        String chartImagePath = histogram.saveChartAsImage(chart);
        StringBuilder uniqueFileName = new StringBuilder("SpireDeck_");

        uniqueFileName.append(deckId);

        // Outputs a void report will follow the format SpireDeck_id(VOID).pdf
        if(invalidCardList.size() > 10 || cardCount > 999){
            uniqueFileName.append("(VOID).pdf");
            filePath += uniqueFileName;
            PDFGenerator.generatePDFVariables(filePath);

        // Outputs report using the format SpireDeck .pd
        }else{
            uniqueFileName.append(".pdf");
            filePath += uniqueFileName;

            System.out.println(filePath);

            // Generate PDF with the deck details and chart
            PDFGenerator.generatePDFVariables(filePath, totalCost, deckId, chartImagePath);
        }
    }
}