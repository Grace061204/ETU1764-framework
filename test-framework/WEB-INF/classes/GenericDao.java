package emp;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Vector;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.sql.*;

public class GenericDao {
    public ArrayList<String> select(Connection c){
        ArrayList<String> tab = new ArrayList<String>();
        try {
            Statement stmt = c.createStatement();
            System.out.println("taille aniza = "+this.getClass().getDeclaredFields().length+"");
            System.out.println("select * from "+this.getClass().getSimpleName());
            System.out.println("fields = "+this.getClass().getDeclaredFields()[0].getName());
            ResultSet res = stmt.executeQuery("select * from "+this.getClass().getSimpleName());
            while(res.next()){
                for (int i = 0; i < this.getClass().getDeclaredFields().length; i++) {
                    tab.add(res.getString(""+this.getClass().getDeclaredFields()[i].getName())+"");
                }
            }
        } catch (Exception e) {
            //TODO: handle exception
        }
        return tab;
    }

    public int count_nbre_ligne(Connection c){
        int count = 0;
        try {
            Statement stmt = c.createStatement();
            ResultSet res = stmt.executeQuery("select * from "+this.getClass().getSimpleName());
            while(res.next()){
               count++;
            }
        } catch (Exception e) {
            //TODO: handle exception
        }
        return count;
    }
    public int count_condition(){
        int nbre = 0;
        try {
            for (int i = 0; i < this.getClass().getDeclaredFields().length; i++) {
                if(this.getClass().getDeclaredFields()[i].get(this)!=null){
                    nbre++;
                }
            }
        } catch (Exception e) {
            //TODO: handle exception
        }
        return nbre;
    }

    public int count_ligne_where(Connection c) throws Exception{
        String allCondition = this.allCondition();
        int count = 0;
        try {
            //System.out.println("getIndSelection = "+indice);
            Statement stmt = c.createStatement();
            String req = "select * from "+this.getClass().getSimpleName()+" where "+allCondition;
            ResultSet res = stmt.executeQuery(req);
            while(res.next()){
                count++;
            }
        } catch (Exception e) {
            //TODO: handle exception
        }
        return count;
    }
    public int countAllLigne(Connection c){
        String allCondition = "";
        Object valeurDefaut = this.getAllNullValue();
        int count = 0;
        String req = "";
        Object[][] tab = null;
        try {
            if(valeurDefaut!=null){
                allCondition = this.allCondition();
                Statement stmt = c.createStatement();
                ResultSet res = null;
                tab = new Object[this.count_ligne_where(c)][this.getClass().getDeclaredFields().length];
                req = "select * from "+this.getClass().getSimpleName()+" where "+allCondition;
                res = stmt.executeQuery(req);
                while(res.next()){ 
                    
                    count++;
                }
            }
            else{
                tab = new Object[this.count_nbre_ligne(c)][this.getClass().getDeclaredFields().length];
                Statement stmt = c.createStatement();
                ResultSet res = stmt.executeQuery("select * from "+this.getClass().getSimpleName());
                while(res.next()){ 
                   
                    count++;
                }
            }
            
        } catch (Exception e) {
            System.out.println("erreur bddobject = "+e);
            //TODO: handle exception
        }
        return count;
    }

    public Vector<Integer> getIndSelection(){
        Vector<Integer> indice = new Vector<Integer>();
        try {
            for (int i = 0; i < this.getClass().getDeclaredFields().length; i++) {
                if(this.getClass().getDeclaredFields()[i].get(this)!=null && this.getClass().getDeclaredFields()[i].getType().getSimpleName().compareToIgnoreCase("string")==0){
                    indice.add(i);
                }
            }
        } catch (Exception e) {
            //TODO: handle exception
            System.out.println(e);
        }
        return indice;
    }
    
   
    public Object getAllNullValue(){
        // System.out.println("mba makato ve?");
        Object value = null;
        Object val = null;
        try {
            for (int i = 0; i < this.getClass().getDeclaredFields().length; i++) {
                if(this.getClass().getDeclaredFields()[i].getType().getSimpleName().compareToIgnoreCase("string")==0){
                    val = this.getClass().getDeclaredFields()[i].get(this);
                    // System.out.println("val = "+val);

                    if(val!=null){
                        value = "tonga de misy";
                        break;
                    }
                    else{
                        value = null;
                    }
                }
                // System.out.println("val = "+val);
                
            }
            
        } catch (Exception e) {
            //TODO: handle exception
            e.printStackTrace();
        }
        return value;
    }


    public String allCondition() throws Exception{
        Vector<Integer> indice = this.getIndSelection();
        String val = "";
        int count = this.count_condition();
        ArrayList<String> chaqueCondition = new ArrayList<String>();
        for (int i = 0; i < this.getClass().getDeclaredFields().length; i++) {
            for (int j = 0; j < indice.size(); j++) {
                if(i==indice.get(j)){
                    // chaqueCondition.add(""+this.getClass().getDeclaredFields()[i].getName()+" like '"+this.getClass().getDeclaredFields()[i].get(this)+"'");
                    chaqueCondition.add(""+this.getClass().getDeclaredFields()[i].getName()+" = '"+this.getClass().getDeclaredFields()[i].get(this)+"'");

                }
                
            }
        }
        val = String.join(" and ", chaqueCondition);
        return val;
    }

    public Object[][] selection2(Connection c) throws Exception{
        String allCondition = "";
        Object valeurDefaut = this.getAllNullValue();
        // System.out.println("valeur par defaut = "+valeurDefaut);
        int indice = 0;
        int count = 0;
        int ligne = 0;
        String req = "";
        Object[][] tab = null;
        try {
            if(valeurDefaut!=null){
                System.out.println("makato");
                allCondition = this.allCondition();
                Statement stmt = c.createStatement();
                ResultSet res = null;
                tab = new Object[this.countAllLigne(c)][this.getClass().getDeclaredFields().length];
                req = "select * from "+this.getClass().getSimpleName()+" where "+allCondition;
                System.out.println("select * from "+this.getClass().getSimpleName()+" where "+allCondition);
                res = stmt.executeQuery(req);
                while(res.next()){ 
                    for (int a = 0; a < tab.length; a++) {
                        for (int o = 0; o < this.getClass().getDeclaredFields().length; o++) {
                            tab[a][o] = res.getString(this.getClass().getDeclaredFields()[o].getName());
                        }
                    }
                    count++;
                }
            }
            else{
                System.out.println("makato");

                tab = new Object[this.countAllLigne(c)][this.getClass().getDeclaredFields().length];
                Statement stmt = c.createStatement();
                ResultSet res = stmt.executeQuery("select * from "+this.getClass().getSimpleName());
                while(res.next()){ 
                    for (int i = 0; i < this.getClass().getDeclaredFields().length; i++) {
                        tab[count][i] = res.getString(""+this.getClass().getDeclaredFields()[i].getName())+"";
                        //tab.add(res.getString(""+this.getClass().getDeclaredFields()[i].getName())+"");
                    }
                    count++;
                }
            }
            
        } catch (Exception e) {
            System.out.println("erreur bddobject = "+e);
            //TODO: handle exception
        }
        return tab;
    }

    public Object[][] selection(Connection c){
        int count = 0;
        int ligne = this.count_nbre_ligne(c);
        Object[][] tab = new Object[ligne][this.getClass().getDeclaredFields().length];
        try {
            Statement stmt = c.createStatement();
            ResultSet res = stmt.executeQuery("select * from "+this.getClass().getSimpleName());
            while(res.next()){ 
                for (int i = 0; i < this.getClass().getDeclaredFields().length; i++) {
                    tab[count][i] = res.getString(""+this.getClass().getDeclaredFields()[i].getName())+"";
                    //tab.add(res.getString(""+this.getClass().getDeclaredFields()[i].getName())+"");
                }
                count++;
            }
        } catch (Exception e) {
            //TODO: handle exception
        }
        return tab;
    }

    public String upperCaseFirst(String val) {
        char[] arr = val.toCharArray();
        arr[3] = Character.toUpperCase(arr[3]);
        return new String(arr);
    }

    public String getValueInsert(){
        Vector<Integer> indice = this.getIndSelection();
        String val = "";
        ArrayList<String> chaqueCondition = new ArrayList<String>();
        for (int i = 0; i < this.getClass().getDeclaredFields().length; i++) {
            for (int j = 0; j < indice.size(); j++) {
                if(i==indice.get(j)){
                    chaqueCondition.add(""+this.getClass().getDeclaredFields()[i].getName());
                }
                
            }
        }
        val = String.join(",", chaqueCondition);
        String req = "("+val+")";
        return req;
    }

    public String changerDate(String format){
        String rep = "";
        String[] annee = format.split("-", 3);
        rep = annee[2]+"-"+annee[1]+"-"+annee[0];
        return rep;
    }

    public void insert(Connection c){
        ArrayList<String> nomField = new ArrayList<>();
        for (int i = 1; i < this.getClass().getDeclaredFields().length; i++) {
            nomField.add(this.getClass().getDeclaredFields()[i].getName());
        }
        String allFields = String.join(",", nomField);
        allFields = allFields.replace("[", "").replace("]", "");
        String nomColonne = this.getValueInsert();
        String part1 = "INSERT INTO " + this.getClass().getSimpleName() + "(" + allFields + ")" + " VALUES ";
        // System.out.println("part1 = " + part1);
        // part1 = part1.substring(0, part1.length() - 2);
        String mitambatra = part1+this.getClass().getDeclaredFields()[0].getName()+","+this.getClass().getDeclaredFields()[1].getName()+","+this.getClass().getDeclaredFields()[2].getName()+")";
        System.out.println("requete = "+mitambatra);
        ArrayList<String> allAttributs = new ArrayList<String>();
        String bambaray = "";
       
        try {
            for (int i = 1; i < this.getClass().getDeclaredFields().length; i++) {
                //System.out.println("valeur = "+this.getClass().getDeclaredFields()[i].get(this));
                if(this.getClass().getDeclaredFields()[i].getType().getSimpleName().compareToIgnoreCase("date")==0){    //to_date('17-11-1981','dd-mm-yyyy')
                    allAttributs.add("to_date('"+this.changerDate(this.getClass().getDeclaredFields()[i].get(this).toString())+"','dd-mm-yyyy')");
                } 
                else if(!this.getClass().getDeclaredFields()[i].getType().getSimpleName().equals("Date")){

                    allAttributs.add("'"+this.getClass().getDeclaredFields()[i].get(this)+"'");
                }
            }
            //System.out.println("allAttribut = "+allAttributs.get(0));
            String str = String.join(",", allAttributs);
            System.out.println(str);
            bambaray = part1+"("+str+")";
            System.out.println("string = "+bambaray);

            Statement stmt = c.createStatement();
            stmt.executeUpdate(bambaray);
            // c.commit();
        } catch (Exception e) {
            System.out.println(e);
            //TODO: handle exception
        }
    }
    public int[] getInd(Connection c,String[] objet){
        int[] val = new int[2];
        Object[][] o = this.selection(c);
        for (int i = 0; i < o.length; i++) {
            for (int j = 0; j < o[i].length; j++) {
                for (int j2 = 0; j2 < objet.length; j2++) {
                    
                    if(o[i][j].equals(objet[j2])){
                        val[0] = i;
                        val[1] = j;
                    }
                }
            }
        }
        return val;
    }
    public String getAllValue(){
        String val = "";
        ArrayList<String> chaqueCondition = new ArrayList<String>();
        try {
            for (int i = 0; i < this.getClass().getDeclaredFields().length; i++){
                if(this.getClass().getDeclaredFields()[i].getType().getSimpleName().compareToIgnoreCase("date")==0){
                    System.out.println("makato ve o");
                    chaqueCondition.add(this.getClass().getDeclaredFields()[i].getName()+" = to_date('"+this.changerDate(this.getClass().getDeclaredFields()[i].get(this).toString())+"','dd-mm-yyyy')");
                }
                else if(!this.getClass().getDeclaredFields()[i].getType().getSimpleName().equals("Date")){

                    chaqueCondition.add(""+this.getClass().getDeclaredFields()[i].getName()+" = '"+this.getClass().getDeclaredFields()[i].get(this)+"'");
                }
            }
        } catch (Exception e) {
            // TODO: handle exception
        }
        val = String.join(",",chaqueCondition);
        return val;
    }
    public void upDate(Connection c) throws Exception{
        String part1 = "UPDATE "+this.getClass().getSimpleName()+" SET ";
        String str = this.getAllValue();
        try {
            String where = part1+str+" WHERE "+this.getClass().getDeclaredFields()[0].getName() +" = '"+this.getClass().getDeclaredFields()[0].get(this)+"'";
            System.out.println("string = "+str);
            System.out.println("where = "+where);
            Statement stmt = c.createStatement();
            stmt.executeUpdate(where);
            c.commit();
        } catch (Exception e) {
            e.printStackTrace();
            //TODO: handle exception
        }
        

    }
    
}
