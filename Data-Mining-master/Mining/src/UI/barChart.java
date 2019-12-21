package UI;

import javafx.collections.FXCollections;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.Scene;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.StackedBarChart;
import javafx.scene.chart.XYChart;
import javafx.scene.image.WritableImage;
import javafx.stage.Stage;


import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;

public class barChart {

    String austria = "Austria";
    CategoryAxis xAxis = new CategoryAxis();
    NumberAxis yAxis = new NumberAxis();
    StackedBarChart<String, Number> sbc ;
    XYChart.Series<String, Number> series1 ;

    barChart(){
        austria = "Austria";

        xAxis = new CategoryAxis();
        yAxis = new NumberAxis();
        sbc = new StackedBarChart<String, Number>(xAxis, yAxis);
        series1 = new XYChart.Series<String, Number>();
        sbc.setAnimated ( false );
    }



    public void start(Dataset weka, int indexAtt, String dataset) {
        Stage stage = new Stage();
        stage.setTitle("Bar Chart: " + dataset );
        sbc.setTitle("Histogrammes : " + dataset);
        xAxis.setLabel("Valeur");

        int freq;
        Double val1,val2,val;
        ArrayList al = new ArrayList<String>();//=  Collections.list(weka.attributes.get(indexAtt).enumerateValues());

        //for (Object inst: weka.instances) { }
        if(weka.attributes.get(indexAtt).isNominal()){
            al =  Collections.list(weka.attributes.get(indexAtt).enumerateValues());
        }else{
            for (int x=0; x < weka.numinstances ; x++){
                val = Math.round(Double.valueOf(weka.instances.get(x).value(indexAtt))*1000.0)/1000.0;
                if(al.size()==0 || !al.contains(String.valueOf(val))){
                    al.add(String.valueOf(val));
                }
            }
            Collections.sort(al);
        }

        xAxis.setCategories(FXCollections.<String>observableArrayList(al));

        yAxis.setLabel("Fréquence");
        series1.setName(weka.attributes.get(indexAtt).name());

        for (int j = 0; j< al.size();j++){
            freq = 0;
            for (int i=0; i < weka.numinstances; i++){

                if(weka.attributes.get(indexAtt).isNumeric()) {
                    val1 = Double.valueOf(weka.instances.get(i).toString(indexAtt));
                    val1 = Math.round(val1*1000.0)/1000.0;
                    val2 = Double.valueOf(al.get(j).toString());
                    val2 = Math.round(val2*1000.0)/1000.0;
                    if (val1.equals(val2)) {
                        freq++;
                    }
                }else{
                    if (weka.instances.get(i).toString(indexAtt).equalsIgnoreCase(al.get(j).toString())) {
                        freq++;
                    }
                }
            }
            series1.getData().add(new XYChart.Data<String, Number>(al.get(j).toString(), (Number)freq));
        }

        Scene scene = new Scene(sbc, 800, 550);
        sbc.getData().addAll(series1);
        stage.setScene(scene);
        stage.show();
     //   saveAsPng ( this, "./blue");
    }
    public static  void saveAsPng(barChart barChart, String path) {
        WritableImage image = barChart.series1.getChart ().snapshot ( null,null );
        File file = new File (path);
        try {
            ImageIO.write( SwingFXUtils.fromFXImage(image, null), "png", file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }





}
