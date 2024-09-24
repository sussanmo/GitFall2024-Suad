/* ===========================================================
* SOURCE CODE FOUND ONLINE: Open Source Citation Down Below:
 * JFreeChart : a free chart library for the Java(tm) platform
 * ===========================================================
 *
 * (C) Copyright 2000-2004, by Object Refinery Limited and Contributors.
 *
 * Project Info:  http://www.jfree.org/jfreechart/index.html
 *
 * This library is free software; you can redistribute it and/or modify it under the terms
 * of the GNU Lesser General Public License as published by the Free Software Foundation;
 * either version 2.1 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY;
 * without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * See the GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License along with this
 * library; if not, write to the Free Software Foundation, Inc., 59 Temple Place, Suite 330,
 * Boston, MA 02111-1307, USA.
 *
 * [Java is a trademark or registered trademark of Sun Microsystems, Inc.
 * in the United States and other countries.]

 *
 * ------------------
 * HistogramTest.java
 * ------------------
 * (C) Copyright 2003, 2004, by Jelai Wang and Contributors.
 *
 * Original Author:  Jelai Wang (jelaiw AT mindspring.com);
 * Contributor(s):   David Gilbert (for Object Refinery Limited);
 *
 */


import java.util.ArrayList;

import java.io.File;
import java.io.IOException;

import org.jfree.chart.ChartUtils;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.CategoryAxis;
import org.jfree.chart.axis.CategoryLabelPositions;
import org.jfree.chart.axis.ValueAxis;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PlotOrientation;

import org.jfree.data.category.CategoryDataset;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.statistics.HistogramDataset;
import org.jfree.ui.ApplicationFrame;

/**
 * A demo of the {@link HistogramDataset} class.
 *
 * @author Jelai Wang, jelaiw AT mindspring.com
 */
public class Histogram extends ApplicationFrame {
    /**
     * Creates a new demo.
     *
     * @param title the frame title.
     */
    protected Histogram(String title, ArrayList<String[]> data) {
        super(title);
        //IntervalXYDataset dataset = createDataset();
        CategoryDataset dataset = createDataset(data);
        JFreeChart chart = createChart(dataset);
        ChartPanel chartPanel = new ChartPanel(chart);
        chartPanel.setPreferredSize(new java.awt.Dimension(500, 270));
        chartPanel.setMouseZoomable(true, false);
        setContentPane(chartPanel);
    }

    /**
     * Creates a sample {@link HistogramDataset}.
     *
     * @return The dataset.
     */
    protected CategoryDataset createDataset(ArrayList<String[]> data) {
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();

        for (String[] entry : data) {
            String cardName = entry[0];
            Integer cardCost = Integer.parseInt(entry[1]);
            dataset.addValue(cardCost, "Frequency", cardName);
        }
        return dataset;
    }

    /**
     * Creates a chart.
     *
     * @param dataset a dataset.
     * @return The chart.
     */
    protected JFreeChart createChart(CategoryDataset dataset) {
        JFreeChart chart = ChartFactory.createBarChart(
                "Histogram ",      // Chart title
                "CardName",            // X-axis Label
                "Energy",                // Y-axis Label
                dataset,               // Dataset
                PlotOrientation.VERTICAL, // Plot Orientation
                true,                  // Include legend
                true,                  // Tooltips
                false                  // URLs
        );

        // Customize the plot
        //chart.getPlot().setForegroundAlpha(0.75f);

        // Customize the plot
        CategoryPlot plot = (CategoryPlot) chart.getPlot();

        // Customize the x-axis to prevent truncation
        CategoryAxis xAxis = plot.getDomainAxis();

        // Increase the y-axis range if necessary
        ValueAxis yAxis = plot.getRangeAxis();
        yAxis.setRange(0, 10); // Set the range from 0 to 10 (adjust as needed)

        // Set label angle (e.g., 45 degrees)
        xAxis.setCategoryLabelPositions(
                CategoryLabelPositions.createUpRotationLabelPositions(Math.PI / 4.0) // 45-degree rotation
        );

        // Increase the margin between labels if needed
        xAxis.setCategoryMargin(0.3); // Adjust the margin between categories

        // Returns historgram chart
        return chart;
    }


    /**
     * Saves a given `JFreeChart` object as a PNG image and returns the file path.
     *
     * This method creates a temporary file to store the chart image and uses the `ChartUtils` class
     * to write the `JFreeChart` object to the file as a PNG with specified dimensions.
     *
     * @param chart The `JFreeChart` object representing the chart to be saved as an image.
     *
     * @return The absolute file path of the saved PNG image, or `null` if an error occurs.
     *
     * @throws IOException If an input/output exception occurs while creating the file or saving the chart.
     */
    protected String saveChartAsImage(JFreeChart chart) {
        try {
            // Create a temporary file to save the chart as an image
            File tempChartFile = File.createTempFile("chart", ".png");

            // Save the chart as PNG with defined width and height
            ChartUtils.saveChartAsPNG(tempChartFile, chart, 500, 300);  // Adjust width and height as needed

            // Return the absolute path of the temporary file
            return tempChartFile.getAbsolutePath();

        // Handles exception with file creation
        } catch (IOException e) {
            System.err.println(e);
            return null;  // Return null if there's an issue
        }
    }
}




