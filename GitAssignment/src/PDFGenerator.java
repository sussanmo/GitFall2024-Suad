import com.itextpdf.io.image.ImageDataFactory;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Image;
import com.itextpdf.layout.element.Paragraph;
import org.jfree.chart.JFreeChart;

import com.itextpdf.io.image.ImageData;

public class PDFGenerator {

    /**
     * Generates a PDF report containing deck information and a chart image.
     *
     * This method creates a PDF report that includes a deck ID, the total cost of the deck, and a chart image if provided.
     *
     * @param filePath The file path where the PDF will be saved.
     * @param totalCost The total cost of the deck.
     * @param deckId The identifier of the deck.
     * @param chartImagePath The file path of the chart image to be included in the PDF. If `null`, the image is not included.
     *
     * @throws Exception if an error occurs while generating the PDF file.
     */
    public static void generatePDFVariables(String filePath, double totalCost, String deckId, String chartImagePath) {
        try {
            // Set up the PDF writer
            PdfWriter writer = new PdfWriter(filePath);
            PdfDocument pdfDoc = new PdfDocument(writer);
            Document document = new Document(pdfDoc);

            // Add title
            document.add(new Paragraph("Deck Report: " + deckId));

            // Add Deck ID and Total Cost
            document.add(new Paragraph("Deck ID: " + deckId));
            document.add(new Paragraph("Total Cost: " + totalCost));

            // Add the chart image to the PDF
            if (chartImagePath != null) {
                ImageData imageData = ImageDataFactory.create(chartImagePath);
                Image chartImage = new Image(imageData);
                document.add(chartImage);
            } else {
                System.out.println("Error: Could not add chart image to PDF.");
            }

            // Close the document to ensure
            document.close();

            System.out.println("PDF report of cards successfully generated " + filePath);

        } catch (Exception e) {
           System.err.println(e);
        }
    }

    /**
     * Generates a simple "VOID" PDF report.
     *
     * This method generates a PDF with a placeholder text "VOID" when no relevant data is provided.
     *
     * @param filePath The file path where the PDF will be saved.
     *
     * @throws Exception if an error occurs while generating the PDF file.
     */
    public static void generatePDFVariables(String filePath){
        try {
            // Set up the PDF writer
            PdfWriter writer = new PdfWriter(filePath);
            PdfDocument pdfDoc = new PdfDocument(writer);
            Document document = new Document(pdfDoc);

            // Add title which should be a single void for invalid deck reports
            document.add(new Paragraph("VOID"));

            // Close the document
            document.close();

            System.out.println("Void Report successfully generated " + filePath);

        } catch (Exception e) {
            System.err.println(e);
        }
    }


    /**
     * Generates a PDF that includes a chart image and saves it to the specified file path to test pdf geenration.
     *
     * This method creates a PDF containing a single chart image which serves as a test method to ensure pdf is being created.
     *
     * @param filePath The file path where the PDF will be saved.
     * @param chartImagePath The file path of the chart image to be included in the PDF. If `null`, the image is not included.
     * @param chart The `JFreeChart` object used to generate the chart image.
     *
     * @throws Exception if an error occurs while generating the PDF file.
     */
    public static void generatePDF(String filePath, String chartImagePath, JFreeChart chart) {
        try {
                // Set up PDF Document which requires writer and pdf doc objects
                PdfWriter writer = new PdfWriter(filePath);
                PdfDocument pdfDoc = new PdfDocument(writer);
                Document document = new Document(pdfDoc);

                // Adds title for histogram
                document.add(new Paragraph("Histogram Test"));

                // Adds the chart image to the PDF
                if (chartImagePath != null) {
                    ImageData imageData = ImageDataFactory.create(chartImagePath);
                    Image chartImage = new Image(imageData);
                    document.add(chartImage);
                } else {
                    System.out.println("Error: Could not add histogram image to PDF.");
                }

                // Closes document once complete
                document.close();


        } catch (Exception e) {
                e.printStackTrace();
            }
        }



    }
