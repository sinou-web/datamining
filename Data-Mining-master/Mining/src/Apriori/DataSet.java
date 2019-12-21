package Apriori;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.util.Callback;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

/**
 * Created by masterubunto on 09/11/18.
 */
public class DataSet
{
   public SetofTransaction set_t;
   public String filename;
   /*L1*/
   public HashMap<String,Integer> Items;
   public ArrayList <javafx.scene.control.TableColumn> columns;
   public ArrayList<ArrayList<String>> rows;



   public void showitems()
   {
       for (String t : Items.keySet ())
       {
               System.out.print( "item:" + t.toString ( )+":"+Items.get ( t )+"," );
       }
   }

   public ArrayList buildColumns()
   {
       ArrayList <ObservableList<String>> rows=new ArrayList<> (  );

       for(ArrayList <String> inst:this.rows){

           ObservableList<String> row = FXCollections.observableArrayList();
           for(String column: inst) {

               row.add ( (column ) );
               //System.out.println (":"+column );
           }

           rows.add ( row );
       }
       return rows;
   }

   public DataSet (String filename)
   {   this.filename=filename;
       this.columns=new ArrayList<> (  );
       Items=new HashMap<> (  );

       try
       { //source=new ConverterUtils.DataSource(filename);
         //instances = source.getDataSet();
       } catch (Exception e)
       {
           e.printStackTrace ( );
       }
      set_t= new SetofTransaction ();
       rows=new ArrayList<> (  );
   }









    public void readcsvdata() throws IOException {
       File excelfile = new File("./"+this.filename);
        FileInputStream fils = new FileInputStream ( excelfile );
        XSSFWorkbook file = new XSSFWorkbook ( fils );
        XSSFSheet sheet = file.getSheetAt ( 0 );

        Iterator<org.apache.poi.ss.usermodel.Row> iter =sheet.rowIterator ();

        // Get iterator to all the rows in current sheet
        Iterator<Row> rowIterator = sheet.iterator();
           int i =-1;
        // Traversing over each row of XLSX file
        while (rowIterator.hasNext() ) {
            i++;
            Row row = rowIterator.next();


            // For each row, iterate through each columns
            Iterator<Cell> cellIterator = row.cellIterator();
            Transaction t=null ;


            if(i!=0) this.rows.add ( new ArrayList<> (  ) )
;
            int j=0;
            //System.out.println("num col = "+row.getLastCellNum());
            while (cellIterator.hasNext() && j< row.getLastCellNum()) {

                Cell cell = cellIterator.next();
                if(i==0 && !cell.toString().isEmpty () ){
                    {
                        javafx.scene.control.TableColumn col=new javafx.scene.control.TableColumn (cell.toString ());
                        final int j1=j;

                        col.setCellValueFactory(new Callback<javafx.scene.control.TableColumn.CellDataFeatures<ObservableList,String>,ObservableValue<String>> (){
                        public ObservableValue<String> call(javafx.scene.control.TableColumn.CellDataFeatures<ObservableList, String> param) {
                            return new SimpleStringProperty (param.getValue().get(j1).toString());
                        }
                    });

                       this.columns.add( col );

                      //  System.out.println ("j="+j+":cell:"+cell.toString () );
                        j++;
                    }
                }
                else
                    {
                      if(!cell.toString ().isEmpty ())
                        this.rows.get ( i-1).add ( cell.toString () );

                item_apriori item =null;

               if(cell.getColumnIndex ()==0)
               {
                          switch (cell.getCellType ( )) {

                       case Cell.CELL_TYPE_STRING:
                           if(cell.getStringCellValue ()!="null")
                           t= new Transaction ( cell.getStringCellValue ( )  );
                           break;
                       case Cell.CELL_TYPE_NUMERIC:
                           t= new Transaction ( String.valueOf (  cell.getNumericCellValue ( ) ));
                           break;

                       default:

                   }
                   /* ajout  de transaction */
                   if(t!=null && t.idtransaction!="null")
                   set_t.addTransaction ( t );
                  // set_t.Show ();
               }
               if(cell.getColumnIndex ()==1) {


                   switch (cell.getCellType ( )) {


                       case Cell.CELL_TYPE_STRING:
                           item = new item_apriori( cell.getStringCellValue ( ), null );
                          // System.out.print ("Itemname:"+item.getItemname () );
                           break;
                       case Cell.CELL_TYPE_NUMERIC:

                           item = new item_apriori( String.valueOf ( cell.getNumericCellValue ( ) ), null );
                          // System.out.print ("Itemnamenumeric:"+item.getItemname () );
                           break;

                       default:

                   }
                   /* si un item apparait donc au moins occurence 1*/
                   if(t!=null &&t.idtransaction!=null)
                   this.set_t.addItem_to_transaction ( t,item );

                   if(this.Items.get ( item.getItemname () )==null)
                   this.Items.put ( item.getItemname (),1 );
                   else
                       this.Items.replace ( item.getItemname (),this.Items.get ( item.getItemname () ), this.Items.get ( item.getItemname () )+1);
                   item=null;

               }

                if(cell.getColumnIndex ()==3) {
                    switch (cell.getCellType ( )) {


                        case Cell.CELL_TYPE_NUMERIC:
                          //  System.out.println ("item,"+item.toString ());
//                            item.setQuantity (  cell.getNumericCellValue () );
                            break;

                        default:

                    }


                }
               }
            }

        }

       }
    public SetofTransaction getSet_t() {
        return set_t;
    }

    public static void main(String[] args)
    {

        //Apriori ap = new Apriori ( "./dataset.xlsx",2 ,60);
        //ap.apriori ();
        DataSet dataSet = new DataSet("./retrail.xlsx");

        try {
            dataSet.readcsvdata();
        } catch (IOException e) {
            e.printStackTrace();
        }
        //System.out.println(dataSet.Items);



    }
}

