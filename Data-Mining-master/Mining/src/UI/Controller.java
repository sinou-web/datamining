package UI;


import Apriori.Apriori;
import DBSCAN.Cluster;
import DBSCAN.Dbscan;
import FPgrowth.FpGrowth;
import FPgrowth.Item;
import FPgrowth.ItemSet;
import FPgrowth.Rule;
import KNN.KNN;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.embed.swing.SwingNode;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.*;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import javafx.util.Callback;
import org.jfree.ui.RefineryUtilities;

import java.awt.*;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;


public class Controller {
    /* je metterai un chemin relatif next time*/
    public static  String path="./data";
    public static Dataset weka;
    public  static Statistic statique_display;
    public static String texfieldtext="";
    public   int supportmin=2;
    public  int minconfidence=60;

    @FXML
    private Slider kslider;

    @FXML
    private Slider trainslider;

    @FXML
    private Slider testslider;
    @FXML
    private TextField precision;

    @FXML
    private TableView testset;
    @FXML
   private TextField support;

    @FXML
    private TextField confidence;

    @FXML
    private TableView tableView;

    @FXML
    private TableView tableAtt;

    @FXML
    private Label numAtt;

    @FXML
    private Label numInstances;

    @FXML
    private Label alltrue;

    @FXML
    private Label truepositive;

    @FXML
    private MenuButton listefiles;
    barChart BC ;

    @FXML // ResourceBundle that was given to the FXMLLoader
    private ResourceBundle resources;

    @FXML // URL location of the FXML file that was given to the FXMLLoader
    private URL location;

    @FXML // fx:id="list_instances"
    private ListView<?> list_instances; // Value injected by FXMLLoader

    @FXML // fx:id="afficher"
    private Button afficher; // Value injected by FXMLLoader

    // @FXML // fx:id="boxplotcheck"
    // private Checkbox boxplotcheck; // Value injected by FXMLLoader

    @FXML // fx:id="Barchartcheck"
    private Checkbox Barchartcheck; // Value injected by FXMLLoader

    @FXML // fx:id="fichiernom"
    private TextField fichiernom; // Value injected by FXMLLoader

    @FXML // fx:id="BarChartLab"
    private Label BarChartLab;

    @FXML // fx:id="BoxPlotPane"
    private Pane BoxPlotPane;

    @FXML // fx:id="BoxPlotSwingNode"
    private SwingNode BoxPlotSwingNode;

    @FXML // fx:id="ZoomButton"
    private Button ZoomButton;

    @FXML // fx:id="discrButton"
    private javafx.scene.control.MenuItem discrButton;

    @FXML // fx:id="normButton"
    private javafx.scene.control.MenuItem normButton;

    @FXML // fx:id="missingButton"
    private javafx.scene.control.MenuItem missingButton;

    @FXML
    private MenuItem apriori;

    @FXML
    private MenuItem fpgrowth;

    @FXML
    private TableView transactions;
/* next time ill make it an list view of ASSOCIATION RUles*/
    @FXML
    private ListView<String> associationrules;

    @FXML // fx:id="freqItems"
    private ListView<String> freqItems;

    @FXML
    private ListView<String> clusterList;

    @FXML
    private Label klabel;

    @FXML
    private Label trainlabel;

    @FXML
    private Label testlabel;

    @FXML
    private TextField eps;

    @FXML
    private TextField minvoisin;

    @FXML
    private TableView tableCluster;

    @FXML
    private Label nbrC;



    @FXML
    private void initialize() {

        assert list_instances != null : "fx:id=\"list_instances\" was not injected: check your FXML file 'UI.fxml'.";
        assert afficher != null : "fx:id=\"afficher\" was not injected: check your FXML file 'UI.fxml'.";
        assert fichiernom != null : "fx:id=\"fichiernom\" was not injected: check your FXML file 'UI.fxml'.";

       missingButton.setDisable(true);
       normButton.setDisable(true);
       discrButton.setDisable(true);
       this.setFrameSlider ( this.kslider,20,3,1 );
       this.setFrameSlider ( this.testslider,100,0,10 );
       this.setFrameSlider ( this.trainslider,100,0,10 );
       this.apriori.setOnAction ( new EventHandler<ActionEvent> ( ) {
           @Override
           public void handle(ActionEvent event) {
               Apriori();
           }
       } );

        this.fpgrowth.setOnAction ( new EventHandler<ActionEvent> ( ) {
            @Override
            public void handle(ActionEvent event) {
                Fpgrowth();
            }
        } );

        affiche();
    }

    public void getextsupp()
    {
        supportmin=Integer.parseInt (  this.support.getText ());
    }

    public void getextconfidence()
    {
        minconfidence=Integer.parseInt (  this.confidence.getText ());
    }

    public void Apriori()
    {   getextconfidence();
        getextsupp();
        this.associationrules.getItems().clear();
        this.freqItems.getItems().clear();
        this.transactions.getItems().clear();
        this.transactions.getColumns().clear();

        Apriori ap = new Apriori ( "./retrail.xlsx",supportmin ,minconfidence);
        ap.apriori();

        ArrayList<Item> ListItems = ap.ListItemFreq();
        ArrayList<ItemSet> ListMotifFreq = new ArrayList<>(ap.toArrayofItemSe());

        System.out.println("lol "+ListMotifFreq);

        this.freqItems.getItems().addAll(("Items : Frequency),"+ListItems.toString()).split(","));

        this.associationrules.getItems ().addAll (("\t\t Apriori Rules ;"+Rule.show(ap.GenererRules(ListMotifFreq,
                ListItems))).split(";"));
        this.transactions.getColumns ().addAll (ap.data.columns);
        this.transactions.getItems ().addAll ( ap.data.buildColumns () );
    }

    /*************sliders************************/

    public void setFrameSlider( Slider s,int max , int min , int pace)
    {

        s.setMax(max);
        s.setMin(min);
        s.setId("hello");
        s.setShowTickLabels(true);

        s.setMajorTickUnit(pace);
        s.setMinorTickCount(0);
        s.showTickLabelsProperty();
        //  frameSlider.setOrientation(Orientation.HORIZONTAL);

    }
    public void setlabelvalues() {
        this.testlabel.setText ( "Test-set:" + String.valueOf ( this.testslider.getValue ( ) ).split ( "\\." )[0] + "%" );
        this.trainlabel.setText ( "Training-set:" + String.valueOf ( this.trainslider.getValue ( ) ).split ( "\\." )[0] + "%" );
        this.klabel.setText ( "K-nearest-Neighbors:" + String.valueOf ( this.kslider.getValue ( ) ).split ( "\\." )[0] + "%" );


    }
    public void getSliderValue()
    {
        //System.out.println ( "train value:"+ this.trainslider.getValue ());
        this.testslider.setValue ( 100- this.trainslider.getValue ());
        setlabelvalues ();


    }
    public void Getslidervaluetest()
    {
        //System.out.println ( "train value:"+ this.testslider.getValue ());
        this.trainslider.setValue ( 100- this.testslider.getValue ());
        setlabelvalues ();


    }


/*******************************sliders**************************************/
    public void Fpgrowth()
    {//retrail
        getextconfidence();
        getextsupp();
        this.associationrules.getItems().clear();
        this.freqItems.getItems().clear();
        this.transactions.getItems().clear();
        this.transactions.getColumns().clear();

        FpGrowth fpg = new FpGrowth( "./retrail.xlsx",supportmin ,minconfidence);

        ArrayList<Item> ListItems = fpg.ListItemFreq();
        ArrayList<ItemSet> ListMotifFreq = fpg.ListMotifFreq(ListItems);

        //System.out.println("huh "+ListMotifFreq);

        this.freqItems.getItems().addAll(("Items : Frequency),"+ListItems.toString()).split(","));
        this.associationrules.getItems ().addAll( ("\t\t Fp-Growth Rules ;"+Rule.show(fpg.GenererRules( ListMotifFreq,
                ListItems))).split(";"));
        this.transactions.getColumns ().addAll(fpg.dataSet.columns);
        this.transactions.getItems ().addAll( fpg.dataSet.buildColumns () );
    }





    /*******************************************************************/
    public void gettext()
    {
        System.out.println("fichier:"+fichiernom.getText());
    }


    public void setdataset(String s)
    {
        this.fichiernom.setText (s );

    }

    public void affiche()
    {

        ArrayList<javafx.scene.control.MenuItem> menu =new ArrayList<> (  );

        for (Object s :Dataset.reading_directory(path)){


            javafx.scene.control.MenuItem menuitem=new javafx.scene.control.MenuItem ( (String) s);
                    menuitem.setOnAction(new EventHandler<ActionEvent> () {
                        @Override public void handle(ActionEvent e)
                        {

                            texfieldtext=menuitem.getText ();
                            setdataset ( texfieldtext );
                        }
                    });

            menu.add ( menuitem );

        }

        listefiles.getItems ().addAll ( FXCollections.observableArrayList(
               menu ) );

    }


    public void K_nearest_Neighbors() throws Exception {
     KNN cacou = new KNN ( (int) this.kslider.getValue (),weka,(int)this.trainslider.getValue (),(int) this.testslider.getValue () );
     precision.setText (  cacou.TestsetKNN ());

                /*vider la table view a chaque click*/
        testset.getItems().clear();
        testset.getColumns().clear();

        ArrayList <TableColumn> colonnes = new ArrayList<> (  );
        colonnes.addAll ( weka.colonnes );
        TableColumn blue =new TableColumn ( "Prediction" );
        blue.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<ObservableList,String>,ObservableValue<String>> (){
            public ObservableValue<String> call(TableColumn.CellDataFeatures<ObservableList, String> param) {
                return new SimpleStringProperty (param.getValue().get(colonnes.size ()-1).toString());
            }
        });
        this.alltrue.setText ("SizeOfTest:"+String.valueOf (  cacou.TestSet.size () ));
        this.truepositive.setText ("NumberofTruePositive:"+ String.valueOf (  cacou.truepostive ));
        colonnes.add (blue );

                /*ajouter les colonnes du dataset*/
        testset.getColumns ( ).addAll ( colonnes );
                /*ajouter des lignes:tuples*/
        testset.getItems().addAll (weka.Add_row ( cacou.TestSet,cacou.classes,colonnes ));
    }

    /********************************************************************************/

    public void DBscan() throws Exception {
        Double epsilon; int minVoisin;

        if(eps.getText() == "")
            epsilon = 0.0;
        else
            epsilon = Double.valueOf(eps.getText());

        if(minvoisin.getText() == "")
            minVoisin = 1;
        else
            minVoisin = Integer.parseInt(minvoisin.getText());



        Dbscan dbscan = new Dbscan();
        //Dataset data = weka.
        ArrayList<Cluster> listCluster = dbscan.dbscan(weka,epsilon,minVoisin);
        nbrC.setText(String.valueOf(listCluster.size()));

        this.clusterList.getItems().clear();
        this.clusterList.getItems().addAll(("Cluster : Number of instances ,"+ Cluster.show(listCluster)).split(","));


        /*vider la table view a chaque click*/
        tableCluster.getItems().clear();
        tableCluster.getColumns().clear();

        ArrayList <TableColumn> colonnes = new ArrayList<> (  );
        colonnes.addAll ( weka.colonnes );
        TableColumn Classe =new TableColumn ( "Cluster" );
        Classe.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<ObservableList,String>,ObservableValue<String>> (){
            public ObservableValue<String> call(TableColumn.CellDataFeatures<ObservableList, String> param) {
                return new SimpleStringProperty (param.getValue().get(colonnes.size ()-1).toString());
            }
        });
        //this.alltrue.setText ("SizeOfTest:"+String.valueOf (  cacou.TestSet.size () ));
        //this.truepositive.setText ("NumberofTruePositive:"+ String.valueOf (  cacou.truepostive ));
        colonnes.add (Classe );

        /*ajouter les colonnes du dataset*/
        tableCluster.getColumns ( ).addAll ( colonnes );
        /*ajouter des lignes:tuples*/
        tableCluster.getItems().addAll (weka.Add_row_Cluster( weka.instances,listCluster,colonnes ));
    }


    public void buttonaffiche()
    {
        String dataset= fichiernom.getText ();
        try {
            if(dataset!=null)
            { weka=new Dataset ( path+"/"+dataset) ;


                /* remplir les labels de num attributs et num instances*/
                Edit_numatt ( weka.numatt );
                Edit_numInstances ( weka.numinstances );

                /*vider la table view a chaque click*/
                tableView.getItems().clear();
                tableView.getColumns().clear();
                tableAtt.getItems().clear();
                tableAtt.getColumns().clear();
                /*ajouter les colonnes du dataset*/
                tableView.getColumns().addAll(weka.colonnes);
                /*ajouter des lignes:tuples*/
                tableView.getItems().addAll (weka.Add_row());

                /**************************************************************************/

                /*Type des attribut et min , max ...*/
                statique_display=new Statistic ();
                statique_display.Shape_stat ();
                tableAtt.getColumns ().addAll ( statique_display.Colonnes );


                /* remplissage d'une matrice des element "attribut, type, min, max" */
                //StatList = statique_display.Calcul_min_max( weka );
                //tableAtt.getItems ().addAll ( StatList );

                BoxPlotPane.setVisible(false);

                normButton.setDisable(true);
                discrButton.setDisable(true);
                missingButton.setDisable(false);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        missingButton.setDisable(false);

    }


    public void buttonBoxPlot() {
        String dataset = fichiernom.getText();

        try {
            if (dataset != null) {

                BoxPlotPane.setVisible(true);
                final BoxPlot demo = new BoxPlot(weka,BoxPlotSwingNode);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void buttonBoxPlot2(){

        String dataset = fichiernom.getText();

        try {
            if (dataset != null) {

                final BoxPlot2 demo = new BoxPlot2("Boite à moustache: "+dataset,weka);

                demo.pack();demo.setBackground(Color.lightGray);
                RefineryUtilities.centerFrameOnScreen(demo);
                demo.setVisible(true);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }



    public void buttonBarChart() throws IOException, InterruptedException {
        String dataset = fichiernom.getText();
        int indexAtt = 0,selected;

        try {
            if (dataset != null) {
                 indexAtt = weka.numatt-1;//par defaut la classe
                selected = tableAtt.getSelectionModel().getSelectedIndex();
                if(selected != -1) indexAtt=selected;

                BarChartLab.setText("");
                BC = new barChart();

                BC.start(weka, indexAtt, dataset);


            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        Thread.sleep ( 20 );
        //barChart.saveAsPng(BC,weka.instances.attribute(indexAtt).name()+"-barchart.png");
        //  ecrireFichier ();
    }

    public void buttonDiscr() throws Exception{
        tableAtt.getItems ().clear ();
        tableView.getItems ().clear ();
        weka.DiscetiserDataSet();
        tableAtt.getItems ().addAll ( statique_display.Calcul_min_max( weka ) );
        tableView.getItems().addAll(weka.Add_row());

        discrButton.setDisable(true);
        buttonBoxPlot();
    }

    public void buttonNormalize() throws Exception{
        tableAtt.getItems ().clear ();
        tableView.getItems ().clear ();
        weka.normalizer();
        tableAtt.getItems ().addAll ( statique_display.Calcul_min_max( weka ) );
        tableView.getItems().addAll(weka.Add_row());

        buttonBoxPlot();
    }

    public void buttonMissingValues() throws Exception {
        tableAtt.getItems ().clear ();
        tableView.getItems ().clear ();
        weka.missingvalues_detector();
        tableAtt.getItems ().addAll ( statique_display.Calcul_min_max( weka ) );
        tableView.getItems().addAll(weka.Add_row());

        normButton.setDisable(false);
        discrButton.setDisable(false);
        //ecrireFichier ();
        buttonBoxPlot();
    }





    public void Edit_numatt(int n){this.numAtt.setText (   Integer.toString (  n));}
    public void Edit_numInstances(int n){ this.numInstances.setText (   Integer.toString (  n));}


    public void filtercleaning () throws Exception {
        tableAtt.getItems ().clear ();
        tableView.getItems ().clear ();

        weka.missingValuesDataSet();
        weka.normalizer();

        tableAtt.getItems ().addAll ( statique_display.Calcul_min_max( weka ) );
        tableView.getItems().addAll(weka.Add_row());

        normButton.setDisable(false);
        discrButton.setDisable(false);

        buttonBoxPlot();
    }


    public void ecrireFichier() throws IOException, InterruptedException {
        String dataset = fichiernom.getText();
        int indexAtt ,selected;
        for(int i=0;i<weka.instances.numAttributes();i++)
        {
            try {
                if (dataset != null) {
                    //Dataset weka = new Dataset (path + "/" + dataset);

                    indexAtt=i;
                    selected = tableAtt.getSelectionModel().getSelectedIndex();
                    if(selected != -1) indexAtt=selected;

                    BarChartLab.setText("");
                    BC = new barChart();

                    BC.start(weka, indexAtt, dataset);

                    Thread.sleep(10);
                    barChart.saveAsPng(BC,weka.instances.attribute(i).name()+"-barchart.png");
                }
            } catch (Exception e) {
                e.printStackTrace();
            }



        }
    }
}
