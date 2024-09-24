import org.jfree.chart.JFreeChart;
import org.jfree.ui.RefineryUtilities;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

// Valid costs for a card in the deck are positive integers between 0 and 6 (inclusive)
// Each card has an associated energy cost, determining how many energy points are required to play that card

// Program that calculates the total energy cost of all the cards in a Slay the Spire deck

// Program prompts the user to input the name of a file that contains the card names and respective energy cost

// Creates a report that tallies the total energy cost and a energy cost histogram for the deck
// Output the report in a pdf file

public class MainExtraCredit {

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
                    cardValidity = validCardChecker(cardName, cardCost); // ValidCardName checked here EC1

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
                    // In all other cases, card must be invalid based on the line input fomat
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
        // Check invalid cost value, a card with empty name, a card with name that is only spaces or tabs, checks cardName as valid card in actual game
        if (cardName == null || cardName.trim().isEmpty()) {
            System.err.println("Invalid card name: '" + cardName + "'. It cannot be empty or only spaces.");
            valid = false;
        }
        // Ensure card name is valid
        if (cardName != null && !cardName.matches("[a-zA-Z ]+")) {
            System.err.println("Invalid card name format: '" + cardName + "'. Card names should only contain alphabetic characters and spaces.");
            valid = false;
        }

        // Implementation of EC1: Valid Card Name
        if(!validCardName(cardName)){
            System.err.println("CardName: '" + cardName + "' is not a valid card in the Splay the Spire Game");
            valid = false;
        }

        // Implementation of EC2: Custom Feature: Valid Card Name + Cost Combo
        if(!validCardCombo(cardName, cardCost)){
            System.err.println("CardName: '" + cardName + "' and CardEnergy : '" + cardCost + "' are not valid combinations in the Splay the Spire Game");
            //System.err.println("Valid Energy for your card : '" + cardName + " are " ); // Lists the correct card energy for the current card
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

    /**
     * Checks the validity of the card based on the name provided by comparing it to a list of card names
     * Extra Credit 1: (EC1) Card Name Validation (+5)
     * Text file derived from excel sheet found here: https://www.reddit.com/r/slaythespire/comments/yjll3t/pdf_printable_card_guide/
     * A valid card should have:
     * - A non-empty name.
     * - matched name corresponding to a name in the splay the spire card file
     *
     *
     * @param cardName The name of the card.
     * @return `true` if the cardName is valid, `false` otherwise.
     */
    private static boolean validCardName(String cardName) {
        boolean valid = false;
        BufferedReader reader = null;

        String cardFile = "AllCard.txt";
        try {
            reader = new BufferedReader(new FileReader(cardFile));
            String card = reader.readLine();

            // Loop through each line in the file
            while (card != null) {

                card.trim(); // Ensure additional spaces don't follow

                if (card.equalsIgnoreCase(cardName)) {  // Ensure case doesn't affect comparison
                    valid = true;  // Found a match
                    break;
                }

                card = reader.readLine();
            }

        }
        // Handle I/O exceptions during file reading
        catch (IOException e) {
        System.err.println(e);
    }

        // cardName is invalid by default if match was never found
        return valid;
    }


    /**
     * Checks the validity of the card based on the name provided by comparing it to a list of card names and the possible energy costs
     * Extra Credit 3: (EC3) Custom Feature (Up to 10 points):
     * Text file derived from excel sheet found here: https://www.reddit.com/r/slaythespire/comments/yjll3t/pdf_printable_card_guide/
     *
     * A valid card should have:
     * - A non-empty name.
     * - matched name corresponding to a name in the splay the spire card file
     * - match the possible energy cost of the corresponding card
     *
     *
     * @param cardName The name of the card
     * @param cardEnergy The energy cost of the card
     * @return `true` if the cardName and cardEnergy combo is valid, `false` otherwise.
     */
    private static boolean validCardCombo(String cardName, String cardEnergy){
        boolean valid = false;
        BufferedReader reader = null;
        String csvFile = "SlaytheSpireReference.csv";
        String line;
        String csvSplitBy = ",";


        try {
            reader = new BufferedReader(new FileReader(csvFile));
            line = reader.readLine();
            while (line != null) {
                // Split by comma
                String[] cardInfo = line.split(csvSplitBy);
                // card[0] is card name, card[1] is energy cost
                //System.out.println("Card Name: " + card[0] + " , Energy Cost: " + card[1] + " , Other Energy Cost: " + card[2] );

                if (cardInfo.length > 1) {  // Ensure there's at least a name and energy column
                    String csvCardName = cardInfo[0].trim();  // Get card name
                    String csvCardEnergy = cardInfo[1].trim();  // Get card energy (Cost)

                    // Check if the card name matches and the energy is valid
                    if (csvCardName.equalsIgnoreCase(cardName)) {
                        if (csvCardEnergy.equals(cardEnergy)) {
                            valid = true;  // Both name and energy match
                            break;  // No need to continue, exit the loop
                        }
                    }

                    // Check card's alternative energy card if it exists
                    if (cardInfo.length > 2 && cardInfo[2] != null && !cardInfo[2].trim().isEmpty()) {
                        String csvCardAltEnergy = cardInfo[2].trim();  // Alternative energy cost

                        // Check if the alternative energy cost matches
                        if (csvCardAltEnergy.equals(cardEnergy)) {
                            valid = true;  // Match found with alternative energy
                            break;

                        }
                    }

                    // Handle the case where cardName matches but has an invalid cardEnergy
                    // Call for method that will notify user of correct energy combination
                    if (csvCardName.equalsIgnoreCase(cardName)) {
                        // and CardEnergy doesn't equal it
                        if (!csvCardEnergy.equals(cardEnergy)) {
                            invalidCardEnergy(cardName, csvCardEnergy, cardInfo);
                            break;


                        }
                    }


                }

                line = reader.readLine();

            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return valid;
    }



    /**
     * Prints out the correct energy costs for a given cardName if there is an invalid card Energy corresponding to the cardName in the splice game
     * Extra Credit 3: (EC3) Custom Feature (Up to 10 points):
     * Text file derived from excel sheet found here: https://www.reddit.com/r/slaythespire/comments/yjll3t/pdf_printable_card_guide/
     *
     *
     * @param cardName The name of the card
     * @param primaryEnergy The energy cost of the card
     * @param cardInfo The current row containing the cardName and possible energy costs
     * @return `true` if the cardName and cardEnergy combo is valid, `false` otherwise.
     */
    private static void invalidCardEnergy(String cardName, String primaryEnergy, String[] cardInfo) {
        // Construct the message with valid energy options
        StringBuilder validEnergies = new StringBuilder();
        validEnergies.append(primaryEnergy);  // Append primary energy

        // If there's an alternative energy, append that as well
        if (cardInfo.length > 2 && cardInfo[2] != null && !cardInfo[2].trim().isEmpty()) {
            validEnergies.append(", ").append(cardInfo[2].trim());  // Append alternative energy cost
        }

        // Print out the message indicating valid energies
        System.err.println("CardName: '" + cardName + "' and CardEnergy: '" + cardInfo[1] + "' are not valid combinations in the Splay the Spire Game.");

        System.err.println("Valid Energy for your card: '" + cardName + "' are: " + validEnergies.toString());
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

            //System.out.println(filePath);

            // Generate PDF with the deck details and chart
            PDFGenerator.generatePDFVariables(filePath, totalCost, deckId, chartImagePath);
        }
    }
}