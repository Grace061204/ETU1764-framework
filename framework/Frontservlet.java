package etu1764.framework.servlet;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import etu1764.framework.Mapping;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.ServletContext;
import model.Model;
import traitement.ModelView;
import traitement.Util;
import java.lang.annotation.*;
import java.lang.reflect.Method;
import java.util.*;
import java.lang.*;
import java.net.*;
import java.util.List;

public class Frontservlet extends HttpServlet {
    HashMap<String,Mapping> mappingUrls = new HashMap<>();
    Util util = new Util();
    String method = "";
    String nomClasse = "";
    String nomMethode = "";
    // public void init() throws ServletException{
    //     super.init();
    //     ServletContext context = this.getServletContext();
    //     Mapping mapping = null;
    //     // ArrayList<Class<?>> allClasses = this.util.getAllClass();
    //     HashMap<String,Mapping> hashMap = new HashMap<String,Mapping>();
    //     Util getpackage = new Util();
    //     String projectPath = getpackage.getProjectPath();
    //     // File file = new File(path); 
    //     try {
           
    //         ArrayList<Class<?>> cl = getpackage.getAllClasses(projectPath);
            
    //         for (int i = 0; i < cl.size(); i++) {
    //             System.out.println("class = "+cl.get(i).getSimpleName());
    //             System.out.println("class with annotation = "+cl.get(i).getSimpleName());
    //             for (int j = 0; j < cl.get(i).getDeclaredMethods().length; j++) {
    //                 if(cl.get(i).getDeclaredMethods()[j].getAnnotation(Model.class)!=null){
    //                     System.out.println("nom classe avec annotation = "+cl.get(i).getSimpleName());
    //                     System.out.println("nom attribut avec annotation = "+cl.get(i).getDeclaredMethods()[j].getName());
    //                     System.out.println("makato ve");
    //                     System.out.println("nom annotation = "+cl.get(i).getDeclaredMethods()[j].getAnnotation(Model.class).value());
    //                     mapping = new Mapping();
    //                     mapping.setClassName(cl.get(i).getSimpleName());
    //                     // this.nomClasse: = cl.get(i).getSimpleName();
    //                     mapping.setMethod(cl.get(i).getDeclaredMethods()[j].getName());
    //                     // this.nomMethode = cl.get(i).getDeclaredMethods()[j].getName();
    //                     method = cl.get(i).getDeclaredMethods()[j].getAnnotation(Model.class).value();
    //                     mappingUrls.put(method,mapping);
    //                 }
    //             }
    //         }
           
    //         // this.mappingUrls = hashMap;
    //     } catch (Exception e) {
    //         //TODO: handle exception
    //         e.printStackTrace();
    //     }
    // }

    // public void init() throws ServletException {
    //     super.init();
    //     ServletContext context = this.getServletContext();
    //     Util util = new Util();
    //     String projectPath = util.getProjectPath();
        
    //     try {
    //         ArrayList<Class<?>> classes = util.getAllClasses(projectPath);
    
    //         for (Class<?> clazz : classes) {
    //             for (Method method : clazz.getDeclaredMethods()) {
    //                 if (method.isAnnotationPresent(Model.class)) {
    //                     Model annotation = method.getAnnotation(Model.class);
    //                     String key = annotation.value();
    //                     String className = clazz.getSimpleName();
    //                     String methodName = method.getName();
                        
    //                     Mapping mapping = new Mapping();
    //                     mapping.setClassName(className);
    //                     mapping.setMethod(methodName);
                        
    //                     mappingUrls.put(key, mapping);
    //                 }
    //             }
    //         }
    //     } catch (Exception e) {
    //         e.printStackTrace();
    //     }
    // }

    public void init() throws ServletException {
        super.init();
        try {

            this.util = new Util();
            this.mappingUrls = new HashMap<>();

            final String tomPath = "/WEB-INF/classes/";
            String path = getServletContext().getRealPath(tomPath);
            List<Class<?>> allClass = util.getAllClass(path, tomPath,"donnees");

            Mapping mapping;
            Method[] allMethods;
            for(Class<?> c : allClass) {
                allMethods = c.getMethods();

                for(Method m : allMethods) {
                    if(m.isAnnotationPresent(Model.class)) {
                        mapping = new Mapping();
                        mapping.setClassName(c.getName());
                        mapping.setMethod(m.getName());
                        mappingUrls.put(m.getAnnotation(Model.class).value(), mapping);

                    }
                }
            }

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    

    protected void processRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        PrintWriter out = response.getWriter();
        
        try {
            String url = request.getRequestURI();
            String traitUrl = util.url(url);
            // traitUrl = traitUrl.replace(".jsp", "");
            out.println("traitUrl = " + traitUrl);
            Mapping map = mappingUrls.get(traitUrl);
            out.print("Valeur de map : " + map);
            if( map == null ) {
                out.println("Not Found");
            } 
                
            Class<?> clazz = Class.forName(map.getClassName());
            Object o = clazz.getDeclaredConstructor().newInstance();

            // String valeurDeRetour = "";
            // for (Map.Entry<String, Mapping> entry : mappingUrls.entrySet()) {
            //     out.println("makato ve");
            //     String key = entry.getKey();
            //     Mapping value = entry.getValue();
            //     Class<?> className = Class.forName(value.getClassName());
            //     Method method = className.getMethod(value.getMethod());
            //     if(key.equals(traitUrl)){

            //         if (method.getReturnType().equals(ModelView.class)) {
            //             ModelView modelView = (ModelView) method.invoke(null);
            //             valeurDeRetour = modelView.getView();
                        
            //             RequestDispatcher dispatcher = request.getRequestDispatcher(valeurDeRetour);
            //             dispatcher.forward(request, response);
            //         }
            //     }
            //     out.println("Clé : " + key + ", Valeur : " + value.getClassName() + ", " + value.getMethod());
            // }

            ModelView mv = (ModelView) o.getClass().getMethod(map.getMethod()).invoke(o);


           
            for (Map.Entry<String, Mapping> entry : mappingUrls.entrySet()) {
                String key = entry.getKey();
                Mapping value = entry.getValue();
                out.println("Clé : " + key + ", Valeur : " + value.getClassName() + ", " + value.getMethod());
            }

            RequestDispatcher dispatcher = request.getRequestDispatcher(mv.getView());
            dispatcher.forward(request, response);

            
        } catch (Exception e) {
            e.printStackTrace();
            StringWriter sw = new StringWriter();
            PrintWriter pw = new PrintWriter(sw);
            e.printStackTrace(pw);
            String stackTrace = sw.toString();
            out.println(stackTrace);
        }
    }
    
    protected void doPost(HttpServletRequest request, HttpServletResponse response)throws ServletException, IOException {
        processRequest(request, response);

    }
    protected void doGet(HttpServletRequest request, HttpServletResponse response)throws ServletException, IOException {
        processRequest(request, response);
    }
}