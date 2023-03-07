package traitement;

public class Util{
    public String url(String url){
        String[] tab = url.split("/");
        return tab[2];
    }
}