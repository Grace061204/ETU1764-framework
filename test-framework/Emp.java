package donnees;

import model.Model;
import traitement.Util;
import traitement.ModelView;

public class Emp{
    int idEmp;
    String nomEmp;
    int age;

    @Model(value="dept")
    public void getAll(){

    }

    public void insert(){

    }

    @Model(value="test")
    public void getTest(){
        
    }

    @Model( value  = "testview")
    public ModelView testview() {
        ModelView mv = new ModelView();

        mv.setView("/testview.jsp");
        //  mv.addItem("test", this.getNom());

        return mv;
    }
       
}