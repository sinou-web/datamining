package UI;

import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.List;

import javafx.embed.swing.SwingNode;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.CategoryAxis;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.labels.BoxAndWhiskerToolTipGenerator;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PlotRenderingInfo;
import org.jfree.chart.renderer.category.BoxAndWhiskerRenderer;
import org.jfree.chart.renderer.category.CategoryItemRendererState;
import org.jfree.data.statistics.BoxAndWhiskerCategoryDataset;
import org.jfree.data.statistics.DefaultBoxAndWhiskerCategoryDataset;


public class BoxPlot {


    public BoxPlot( Dataset weka,SwingNode swing) {

        final BoxAndWhiskerCategoryDataset dataset = createSampleDataset(weka);

        final CategoryAxis xAxis = new CategoryAxis("Attribut");
        final NumberAxis yAxis = new NumberAxis("Valeur");
        yAxis.setAutoRangeIncludesZero(false);

        BoxAndWhiskerRenderer renderer = new BoxAndWhiskerRenderer() {
            public CategoryItemRendererState initialise(Graphics2D g2,
                                                        Rectangle2D dataArea,
                                                        CategoryPlot plot,
                                                        int rendererIndex,
                                                        PlotRenderingInfo info) {
                CategoryItemRendererState state = super.initialise(g2, dataArea, plot, rendererIndex, info);
                if (state.getBarWidth() > 35)
                    state.setBarWidth(35); // Keeps the circle and chart from being huge
                return state;
            }
        };

        renderer.setFillBox(true);
        renderer.setSeriesPaint(0, Color.ORANGE);
        renderer.setSeriesOutlinePaint(0, Color.BLACK);
        renderer.setUseOutlinePaintForWhiskers(true);
        //renderer.setMeanVisible(false);

        renderer.setDefaultToolTipGenerator(new BoxAndWhiskerToolTipGenerator());
        final CategoryPlot plot = new CategoryPlot(dataset, xAxis, yAxis, renderer);

        final JFreeChart chart = new JFreeChart(
                "Boite à moustache",
                new Font("SansSerif", Font.BOLD, 14),
                plot,
                true
        );
        final ChartPanel chartPanel = new ChartPanel(chart);
        chartPanel.setPreferredSize(new Dimension(530, 390));

        swing.setContent(chartPanel);

    }



    private BoxAndWhiskerCategoryDataset createSampleDataset(Dataset weka) {

        final int seriesCount = 1;
        final Double categoryCount = Double.valueOf(weka.numatt);
        final int entityCount = weka.numinstances;

        final DefaultBoxAndWhiskerCategoryDataset dataset
                = new DefaultBoxAndWhiskerCategoryDataset();
        for (int i = 0; i < seriesCount; i++) {
            for (int j = 0; j < categoryCount; j++) {
                final List list = new ArrayList();
                // adding the values from dataset

                if(weka.attributes.get(j).isNumeric()){
                    for (int k = 0; k < entityCount; k++) {
                        final double value1 = weka.instances.get(k).value(j);
                        list.add(new Double(value1));
                    }

                    dataset.add(list, " " , weka.attributes.get(j).name());
                }
            }

        }
        return dataset;
    }


}