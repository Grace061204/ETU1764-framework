package emp;

import model.Model;
import model.Auth;
import traitement.Util;
import traitement.ModelView;

import java.sql.Connection;

import emp.*;

public class Emp extends GenericDao{
    String idEmp;
    String nomEmp;
    int ageEmp;

    @Model( value = "connect.do", nameInput="profil")
    public ModelView connect(String profil) {
        ModelView mv = new ModelView();

        mv.setView("/index.jsp");
        mv.addSession("userProfil", profil);

        return mv;
    }

    @Auth(profil = "admin")
    @Model( value = "json.do")
    public ModelView jsonDo() {
        ModelView mv = new ModelView();

        mv.setView("/test.jsp");
        mv.addItem("emp", this);
        mv.setJson(true);

        return mv;
    }

    @Model(value="dept")
    public void getAll(){

    }

    public void voidInsert(){

    }

    @Model(value="test")
    public void getTest(){
        
    }

    @Model( value  = "test.do")
    public ModelView test() {
        ModelView mv = new ModelView();

        mv.setView("/test.jsp");
        mv.addItem("test", "Valeur depuis model-view");

        return mv;
    } 

    @Model( value = "save.do")
    public ModelView save() {
        ModelView mv = new ModelView();
        mv.setView("/test2.jsp");
        mv.addItem("test", this.getNomEmp());

        return mv;
    }

    @Model( value = "saveFunction.do", nameInput = "nomEmp,ageEmp")
    public ModelView newTest(String nomEmp, int ageEmp) {
        ModelView mv = new ModelView();

        this.setNomEmp(nomEmp);
        this.setAgeEmp(ageEmp);

        try {
            
            Connexion connexion = new Connexion();
            Connection c = connexion.connexionPostgres();
            this.insert(c);
            c.close();
        } catch (Exception e) {
            //TODO: handle exception
        }

        mv.setView("/test.jsp");
        mv.addItem("emp", this);

        return mv;
    }

    @Model(value="list.do")
    public ModelView listEmp(){
        ModelView mv = new ModelView();
        Object[][] o = null;
        try {
            
            Connexion connexion = new Connexion();
            Connection c = connexion.connexionPostgres();
            o = this.selection2(c);
            c.close();
        } catch (Exception e) {
            //TODO: handle exception
        }
        mv.setView("/list.jsp");
        mv.addItem("emp", o);

        return mv;
    }

    @Model(value="update.do", nameInput = "idEmp")
    public ModelView formModif(String idEmp){
        ModelView mv = new ModelView();
        Object[][] o = null;
        try {
            
            Connexion connexion = new Connexion();
            Connection c = connexion.connexionPostgres();
            this.setIdEmp(idEmp);
            o = this.selection2(c);
            c.close();
        } catch (Exception e) {
            //TODO: handle exception
        }
        mv.setView("/formUpdate.jsp");
        mv.addItem("emp", o);

        return mv;
    }

    @Model(value="updatebase.do", nameInput = "idEmp,nomEmp,ageEmp")
    public ModelView formUpdate(String idEmp,String nomEmp, int ageEmp){
        ModelView mv = new ModelView();
        Object[][] o = null;
        try {
            
            Connexion connexion = new Connexion();
            Connection c = connexion.connexionPostgres();
            this.setIdEmp(idEmp);
            this.setNomEmp(nomEmp);
            this.setAgeEmp(ageEmp);
            this.upDate(c);
            Emp emp = new Emp();
            o = emp.selection2(c);
            c.close();
        } catch (Exception e) {
            //TODO: handle exception
        }
        mv.setView("/list.jsp");
        mv.addItem("emp", o);

        return mv;
    }

    public String getNomEmp() {
        return this.nomEmp;
    }

    public void setNomEmp(String nomEmp) {
        this.nomEmp = nomEmp;
    }

    public int getAgeEmp() {
        return ageEmp;
    }

    public void setAgeEmp(int ageEmp) {
        this.ageEmp = ageEmp;
    }

    public String getIdEmp() {
        return idEmp;
    }

    public void setIdEmp(String idEmp) {
        this.idEmp = idEmp;
    }
       
}