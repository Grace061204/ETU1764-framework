package emp;

import model.Scope;
import singleton.Issingleton;
import traitement.ModelView;
import model.Model;

import emp.Emp;

@Scope( type = Issingleton.TRUE)
public class Testsingleton {
    @Model( value = "singleton.do")
    public ModelView singletonMethod() {
        ModelView mv = new ModelView();

        Emp emp = new Emp();
        emp.setNomEmp("emp Singleton");

        mv.setView("/test.jsp");
        mv.addItem("emp", emp);



        return mv;
    }
}
