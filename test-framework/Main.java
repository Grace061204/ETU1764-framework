package main;


import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import donnees.Emp;
import etu1764.framework.Mapping;

import model.Model;
import traitement.*;
import java.lang.annotation.*;

public class Main {
    public static void main(String[] args) {
        Util allClass = new Util();
        String path = "donnees";
        File file = new File(path);
        Mapping mapping = null; 
        String method = "";
        HashMap<String,Mapping> mappingUrls = new HashMap<>();
        Util getpackage = new Util();
        String projectPath = getpackage.getProjectPath();
        try {
            // ArrayList<String> list = allClass.findAllFilesInAllFolder();
            // ArrayList<String> allPackages = getpackage.getAllPackages(projectPath);
            // for (String packageName : allPackages) {
            //     System.out.println(packageName);
            // }
            ArrayList<Class<?>> cl = getpackage.getAllClasses(projectPath); 
            // for (int i = 0; i < list.size(); i++) {
            //     cl.add(Class.forName(list.get(i)));
            // }
            for (int i = 0; i < cl.size(); i++) {
                // System.out.println("class = "+cl.get(i).getSimpleName());
                // System.out.println("class with annotation = "+cl.get(i).getSimpleName());
                for (int j = 0; j < cl.get(i).getDeclaredMethods().length; j++) {
                    if(cl.get(i).getDeclaredMethods()[j].getAnnotation(Model.class)!=null){
                        // System.out.println("nom classe avec annotation = "+cl.get(i).getSimpleName());
                        // System.out.println("nom attribut avec annotation = "+cl.get(i).getDeclaredMethods()[j].getName());
                        // System.out.println("makato ve");
                        System.out.println("nom annotation = "+cl.get(i).getDeclaredMethods()[j].getAnnotation(Model.class).value());
                        mapping = new Mapping();
                        mapping.setClassName(cl.get(i).getSimpleName());
                        // this.nomClasse: = cl.get(i).getSimpleName();
                        mapping.setMethod(cl.get(i).getDeclaredMethods()[j].getName());
                        // this.nomMethode = cl.get(i).getDeclaredMethods()[j].getName();
                        method = cl.get(i).getDeclaredMethods()[j].getAnnotation(Model.class).value();
                        mappingUrls.put(method,mapping);
                    }
                }
            }
            for (Map.Entry<String, Mapping> entry : mappingUrls.entrySet()) {
                String key = entry.getKey();
                Mapping value = entry.getValue();
                System.out.println("Cle : " + key + ", Valeur : " + value.getClassName()+" , "+value.getMethod());
            }
        } catch (Exception e) {
            //TODO: handle exception
            e.printStackTrace();
        }
    }
}
