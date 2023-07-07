package traitement;

public class ModelView {
    String view;
    boolean isJson;
    
    public ModelView() {}

    public ModelView(String view) {
        this.setView(view);
    }
    public String getView() {
        return view;
    }
    public void setView(String view) {
        this.view = view;
    }

    public boolean isJson() {
        return isJson;
    }

    public void setJson(boolean isJson) {
        this.isJson = isJson;
    }
}
