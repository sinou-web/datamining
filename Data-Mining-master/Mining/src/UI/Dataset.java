package UI;

import DBSCAN.Cluster;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.TableColumn;
import javafx.util.Callback;
import weka.core.Attribute;
import weka.core.Instance;
import weka.core.Instances;
import weka.core.converters.ConverterUtils;
import weka.filters.Filter;
import weka.filters.unsupervised.attribute.ReplaceMissingValues;

import java.io.File;
import java.util.*;

public class Dataset
{


        public   Instances instances;
        public   ConverterUtils.DataSource source;
        public   ArrayList<TableColumn> colonnes;
        public   ArrayList <Attribute>attributes;

        public int numatt;
        public int numinstances;
        public Dictionary index_dic;
        public  ArrayList<Dictionary> index_by_att;

        public HashMap<Object, double[]> MeanbyClass;
        public Statistic statClass = new Statistic ();







    public Dataset(String filename)
    {

        try {
            getinstances ( filename );
        } catch (Exception e) {
            e.printStackTrace ( );
        }


    }

    public Dataset()
    {
        attributes=new ArrayList (  );
        source=null;
        colonnes=null;

        attributes=null;
        numatt=numinstances=0;
    }


    public void getinstances (String filename )throws Exception {
        source=new ConverterUtils.DataSource(filename);
        instances = source.getDataSet();
        if(instances!=null) {
            numatt = instances.numAttributes ( );
            numinstances=instances.numInstances ();
            attributes=new ArrayList (  );

            ToString();
        }

    }
    public  double meanbyclass (HashSet<Instance> values_class, int attindex)
    {

        double sum=0;

     /* numeric case*/
        for(Instance value:values_class)
        {
            sum=sum+value.value ( attindex );


        }

        return(sum/values_class.size ());


    }

    public  double [] meanbyatt( HashSet<Instance> classe)
    {
        double [ ] valuesmissingindex=checkMissingValues ();
        if(valuesmissingindex!=null)

        {   for(int i=0;i<numatt;i++)
        {
            if(valuesmissingindex[i]==1)
                valuesmissingindex[i]=meanbyclass ( classe,i );



        }
            return valuesmissingindex;

        }
        return null;

    }




    public double [] checkMissingValues ()
    {

        int index=-1;
        double [] tab = new double[numatt];

        Enumeration<Attribute> enums_att= instances.enumerateAttributes ();

        while(enums_att.hasMoreElements ())
        {

            Attribute att = enums_att.nextElement ();

            index = this.instances.attributeStats (att.index () ).missingCount;
            if(index>0)
            {tab[att.index()]=1; }
        }

        return tab;

    }



    public   void ToString() throws Exception
    {
       ArrayList<TableColumn >columnArrayList=new ArrayList<>();



        for(int j=0;j<numatt;j++)
        {
            //System.out.println("Attribut:"+instances.numAttributes()+":"+instances.attribute(j).name());
            TableColumn col=new TableColumn(instances.attribute(j).name());
            final int j1=j;

            col.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<ObservableList,String>,ObservableValue<String>> (){
                public ObservableValue<String> call(TableColumn.CellDataFeatures<ObservableList, String> param) {
                    return new SimpleStringProperty (param.getValue().get(j1).toString());
                }
            });

            attributes.add ( instances.attribute ( j ) );
            columnArrayList.add(col);


        }
        /* get attributes*/
        colonnes=columnArrayList;




    }
    public static ArrayList reading_directory (String dir_name) {

        ArrayList<String> list_of_files = new ArrayList<>();

        File f = null;
        String[] paths;

        try {

            // create new file
            f = new File(dir_name);

            // array of files and directory
            paths = f.list();
            if(paths==null) System.out.println("no files");


                else
            // for each name in the path array
            for (String path : paths) {


                list_of_files.add(path);
            }

        } catch (Exception e) {
            // if any error occurs
            e.printStackTrace();
        }

        return list_of_files;
    }
    public void missingvalues_detector()
    {    ArrayList<Dictionary> DictionnairebyATT= new ArrayList<> (  );

      String type="nominal";


        {  /* create le dictionnaire*/

            /*créer l'index qu'on cas de missing values on peut voir ça par stat*/

            if(  this.checkMissingValues()!=null)
            {
                for (Attribute att: this.attributes)
                { /* filling the dictionnary*/
                    if( this.instances.attribute (  this.numatt-1 ).isNominal ()
                            )
                    index_dic=new Dictionary<String,Instance> ();

                    else

                    {    index_dic=new Dictionary<Double,Instance> ();
                    type="numeric";
                    }

                    Enumeration<Object> enums= this.instances.attribute (  this.numatt-1).enumerateValues ();
                    if(enums!=null)
                        while (enums.hasMoreElements ())
                        {
                            if(type.contains ( "nominal" ))
                            index_dic.add( enums.nextElement ().toString () );
                            else
                                index_dic.add( enums.nextElement () );

                        }

                for(Instance inst: this.instances)
                {

                    if (!inst.isMissing ( att ))
                    {

                        if(type.contains ( "nominal" ))
                             index_dic.set_values ( inst.stringValue (  this.numatt-1 ),inst );
                        else
                            index_dic.set_values ( inst.value ( att ),inst );

                    }
                }
                /* tout ce qui est plus haut est pour la création du dictionnaire*/
   //             System.out.println ("Attribut:"+att );
     //           index_dic.Show_dic ();
                DictionnairebyATT.add ( index_dic );

/**********************traitement des missing values************************************/

                }
                    /* init the hashmap of meanbyclass: on calcule la moyenne pour chaque attribut par class*/

                    int i=0;

                for(Instance inst:this.instances)
                {
                    for (Attribute att: this.attributes)
                    {
                        assign_MeanbyClass (DictionnairebyATT.get ( att.index () ));
                        if(inst.isMissing (att) && att.isNumeric ())
                        {

                            //System.out.println ("meanbyclass:Avant"+inst.toString ( att ));
                           inst.setValue ( att,
                                    MeanbyClass.get ( inst.toString ( numatt-1 ) )[att.index ()] );
                           // System.out.println ("meanbyclass:Apres"+inst.toString ( att ) +":meanResulat:"+MeanbyClass.get ( inst.toString ( numatt-1 ) )[att.index ()]);

                            this.instances.set ( i,inst );
                        }
                        else


                        if(inst.isMissing ( att )&& att.isNominal ())
                        { /* remplacer avec la valeur la plus frequente MODE*/
                            HashMap<String,Integer> MODES = new HashMap<> (  );
                           // for(Object key :DictionnairebyATT.get ( att.index () ).getIndex_list ().keySet ())
                             Object key = (Object) inst.toString ( numatt-1 )
                                     ;
                            {

                                for (Object s:DictionnairebyATT.get ( att.index () ).get_values ( key ) )
                                {



                                    if( MODES.get ( ((Instance) s).toString ( att ) )==null)
                                     MODES.put ( ((Instance) s).toString ( att ),1);
                                    else
                                        MODES.replace ( ((Instance) s).toString ( att ) , MODES.get ( ((Instance) s).toString ( att ) ),MODES.get ( ((Instance) s).toString ( att ) )+1);



                                }

                                /* check max*/
                                int maximum=0;
                                String s="";
                                for (String value:MODES.keySet ())
                                {

                                    if( maximum < MODES.get ( value ))
                                    {
                                        maximum=MODES.get ( value );
                                        s=value;
                                    }

                                }



                                inst.setValue ( att,s );
                                //System.out.println ("cle:"+key +" count:"+ DictionnairebyATT.get ( att.index () ).get_values ( key ).size ());
                            }
                            this.instances.set ( i,inst );

                        }
                    }
                    i++;
                }

            }

        }

     this.index_by_att= DictionnairebyATT;
        /*crééer une list*/


      //  System.out.println ("class:values ranges in each classe"+this.instances.attributeStats ( this.numatt-1 ).nominalCounts[1]);

      //  System.out.println ("miiissiinnng valluuuues" );
//     Statistic.Mode_of_nominal ( attributes.get ( 4 ),this );


    }



    public void assign_MeanbyClass( Dictionary index_dic)
    {

       /* pour chaque class==> on associe un tableau de moyenne de chaque attribut having no missing values*/
        HashMap<Object,double []> MeanbyClass=new HashMap<> (  );

        for(Object key:index_dic.index_list.keySet ())
        {
            MeanbyClass.put (  key,meanbyatt ( index_dic.get_values ( key ) ) );
        }
        this.MeanbyClass=MeanbyClass;

    }


    public ArrayList Add_row() throws Exception {     /* pretraitement des valeurs manquantes*/
       //  this.missingvalues_detector ();
      /* inserer les valeurs des instances ligne par ligne*/
        try {
         //   this.normalizeDataSet ( );
        } catch (Exception e) {
            e.printStackTrace ( );
        }
        ArrayList<Instance> MyInstances= Collections.list(this.instances.enumerateInstances());
        ArrayList <ObservableList<String>> rows=new ArrayList<> (  );

        for(Instance inst:MyInstances){

            ObservableList<String> row = FXCollections.observableArrayList();
            for(int AttIndex=0;AttIndex<this.colonnes.size();AttIndex++) {

            row.add ( inst.toString ( AttIndex ) );
        }


        rows.add ( row );
    }
    return rows;
}
    public ArrayList Add_row(Instances test,ArrayList Prediction,ArrayList<TableColumn> colonnes) throws Exception {     /* pretraitement des valeurs manquantes*/
        //  this.missingvalues_detector ();
      /* inserer les valeurs des instances ligne par ligne*/
        try {
            //   this.normalizeDataSet ( );
        } catch (Exception e) {
            e.printStackTrace ( );
        }
        ArrayList<Instance> MyInstances= Collections.list(test.enumerateInstances());
        ArrayList <ObservableList<String>> rows=new ArrayList<> (  );
        int i=0;
        for(Instance inst:MyInstances){

            ObservableList<String> row = FXCollections.observableArrayList();
            for(int AttIndex=0;AttIndex<colonnes.size()-1;AttIndex++) {

                row.add ( inst.toString ( AttIndex ) );
            }
            row.add ( (String) Prediction.get ( i ) );
                    i++;


            rows.add ( row );
        }
        return rows;}



    public ArrayList Add_row_Cluster(Instances data, ArrayList<Cluster> listCluster, ArrayList<TableColumn> colonnes) throws Exception {     /* pretraitement des valeurs manquantes*/
        //  this.missingvalues_detector ();
        /* inserer les valeurs des instances ligne par ligne*/
        try {
            //   this.normalizeDataSet ( );
        } catch (Exception e) {
            e.printStackTrace ( );
        }
        ArrayList<Instance> MyInstances= Collections.list(data.enumerateInstances());
        ArrayList <ObservableList<String>> rows=new ArrayList<> (  );
        int i=0;
        for(Instance inst:MyInstances){

            ObservableList<String> row = FXCollections.observableArrayList();
            for(int AttIndex=0;AttIndex<colonnes.size()-1;AttIndex++) {

                row.add ( inst.toString ( AttIndex ) );
            }
            row.add ( (String) Cluster.WhichCluster(listCluster,inst) );
            i++;


            rows.add ( row );
        }
        return rows;}



    public void normalizer() throws Exception {
        Double MIN,MAX,max=1.0,min=0.0,normalisée,originale;
        for (Attribute attribute: attributes) {
            if(attribute.isNumeric()) {
                MIN = instances.attributeStats(attribute.index()).numericStats.min;
                MAX = instances.attributeStats(attribute.index()).numericStats.max;
                for (Instance instance : instances) {
                    originale = instance.value(attribute);
                    normalisée = (originale - MIN) * (max - min) / (MAX - MIN) + min;
                    instance.setValue(attribute, normalisée);
                }
            }
        }
    }

    /*
        [MIN,MAX] : interval d'origine
        [min,max] : interval cible
        originale : valeur dans l'interval d'origine
        normalisée : valeur normalisée dans l'interval cible
    */

    protected void missingValuesDataSet() throws Exception {

       ReplaceMissingValues m_MissingValuesFilter = new ReplaceMissingValues();
        m_MissingValuesFilter.setInputFormat(instances);
        instances = Filter.useFilter(instances, m_MissingValuesFilter);

    }

/***********************************************************************************************/
/************************************** Discrétisation *****************************************/


    //Methode1 : pour 1 attribut donné calculer le nbr d'interval
    public Double NbrIntervalle1(Attribute attribute){
        statClass.Calcul_min_max(this);
        //Controller.weka.
        Double max = instances.attributeStats ( attribute.index() ).numericStats.max;
        Double min = instances.attributeStats ( attribute.index() ).numericStats.min;

        Double NbrInterval = ( max - min )/
                (2*statClass.IQR.get(attribute.index())* Math.pow((double)this.numinstances,(-1/3)));
        //System.out.println("IQR = "+statClass.IQR.get(attribute.index()));
        return Math.ceil(NbrInterval);
    }

    //Methode2 : pour 1 attribut donné calculer le nbr d'interval
    public Double NbrIntervalle2(Attribute attribute){

        return Math.log(this.numinstances+1);
    }

    //pour 1 attribut donné, retourner une liste des intervalles
    public ArrayList ListIntervalAtt(Attribute attribute){

        ArrayList ListInter = new ArrayList();
        ArrayList ListInfoAtt = statClass.Calcul_min_max(this);
        //Double NBR = NbrIntervalle1(attribute),
        Double NBR = NbrIntervalle2(attribute),
        Max = instances.attributeStats ( attribute.index() ).numericStats.max,
        Min = instances.attributeStats ( attribute.index() ).numericStats.min,
        x = Min;


        Double SizeInterval = Double.valueOf( (Max - Min)/NBR);


        for (int i = 0; i < NBR; i++){

            x = x + SizeInterval;
            ListInter.add(x);
        }//exemple: sizeinterval = 2 ;  liste= [min, min+2, min+4, ...]

        return ListInter;
    }

    //pour une valeur d'un attribut chercher a quel interval il apparitent
    public int WhichInterval(ArrayList ListInter, Double val){
        int position=0; boolean find=false;
        while (position<ListInter.size() && !find){
            if ((Double)ListInter.get(position)<val){
                position++;
            }else {
                find = true;
            }
        }
        return position;
    }


    //discritiser tout les attributs numerics
    public void DiscetiserDataSet() throws Exception{
        ArrayList ListInter;
        int positionInter;
        for (Attribute attribut: this.attributes) {
            if(attribut.isNumeric()){

                        //ListInter = new ArrayList();
                ListInter = ListIntervalAtt(attribut);
                for (Instance instance: this.instances) {

                    positionInter = WhichInterval(ListInter, instance.value(attribut));
                    instance.setValue(attribut, (Double) ListInter.get(positionInter));
                }

            }

        }
    }

    public  void showdataset()
    {
        for (Instance i : this.instances)
        {
            System.out.println (i.toString () );
        }
    }

}
