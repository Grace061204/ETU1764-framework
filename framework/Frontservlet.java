package etu1764.framework.servlet;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import etu1764.framework.Mapping;
import java.lang.reflect.InvocationTargetException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

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
import java.lang.reflect.*;
import java.util.*;
import java.lang.*;
import java.net.*;
import java.util.List;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.io.File;
import java.util.ArrayList;
import model.Scope;
import singleton.Issingleton;
import model.Auth;




public class Frontservlet extends HttpServlet {
    
    HashMap<String,Mapping> mappingUrls = new HashMap<>();
    HashMap<String, Object> singleton = new HashMap<>();
    Util util = new Util();
    String method = "";
    String nomClasse = "";
    String nomMethode = "";
    String sessionVariable;

    public void getAnnotationPresent(String path, String tomPath, HashMap<String, Mapping> mappingUrls, HashMap<String, Object> singleton) throws ClassNotFoundException, NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {
        List<Class<?>> allClass = util.getAllClass(path, tomPath);
        Mapping mapping;
        Method[] allMethods;

        for(Class<?> c : allClass) {
            allMethods = c.getMethods();

            if (c.isAnnotationPresent(Scope.class)) {
                if (c.getAnnotation(Scope.class).type().equals(Issingleton.TRUE)){
                    Class<?> clazz = Class.forName(c.getName());
                    Object temp = clazz.getDeclaredConstructor().newInstance();
                    singleton.put(c.getName(), temp);
                }
            }

            for(Method m : allMethods) {
                if(m.isAnnotationPresent(Model.class)) {
                    mapping = new Mapping();
                    mapping.setClassName(c.getName());
                    mapping.setMethod(m.getName());
                    mappingUrls.put(m.getAnnotation(Model.class).value(), mapping);
                }
            }
        }
    }

    public boolean isSession(String[] data, String find) {
        for (String s : data) {
            if(s.trim().equals(find)) return true;
        }
        return false;
    }

    public ModelView invokeMethod(HttpServletRequest request, Mapping mapping, HashMap<String, Object> singleton, String session) throws Exception {
        ArrayList<Class<?>> type = new ArrayList<>();
        ArrayList<Object> value = new ArrayList<>();
        this.setArgValue(request, mapping, type, value);

        Object o = this.setObjectByRequest(request, mapping, singleton);

        Method m = o.getClass().getMethod(mapping.getMethod(), type.toArray(Class[]::new));
        if(m.isAnnotationPresent(Auth.class)){
            String[] allPermission = m.getAnnotation(Auth.class).profil().split(",");
            String userPermission = String.valueOf(request.getSession().getAttribute(session));
            if (isSession(allPermission, userPermission)) {
                return (ModelView) m.invoke(o, value.toArray(Object[]::new));
            }else throw new Exception("Permission denied");
        }else return (ModelView) m.invoke(o, value.toArray(Object[]::new));
    }

    public void setArgValue(HttpServletRequest request, Mapping mapping, ArrayList<Class<?>> type, ArrayList<Object> value) throws Exception {
        Method m = this.getMethodByClassName(mapping.getClassName(), mapping.getMethod());

        if(m.isAnnotationPresent(Model.class) && !m.getAnnotation(Model.class).nameInput().equals("") ) {
            type.addAll(List.of(m.getParameterTypes()));

            String[] nameInput = m.getAnnotation(Model.class).nameInput().split(",");

            if(nameInput.length != type.size()) throw new Exception("Number of argument exception \n" +
                    "\t" + nameInput.length + " declared but " + type.size() + " expected");

            String value_temp;
            for (int i=0; i< nameInput.length; i++) {
                value_temp = request.getParameter(nameInput[i].trim());
                value.add(this.castPrimaryType(value_temp, type.get(i)));
            }
        }
    }

    public Method getMethodByClassName(String className, String method) throws NoSuchMethodException, ClassNotFoundException, InvocationTargetException, InstantiationException, IllegalAccessException {
        Class<?> clazz = Class.forName(className);
        Object o = clazz.getDeclaredConstructor().newInstance();

        Method result = null;
        Method[] allMethod = o.getClass().getDeclaredMethods();
        for (Method m : allMethod) {
            if(m.getName() .equals(method)) {
                result = m;
                break;
            }
        }

        return result;
    }

    public void setAttributeRequest(HttpServletRequest request, ModelView mv) {
        HashMap<String, Object> donne = mv.getData();
        for(String key : donne.keySet()) {
            request.setAttribute(key, donne.get(key));
        }
    }

    private void initObject(Object o) throws IllegalAccessException, ParseException {
        for (Field field : o.getClass().getDeclaredFields()) {
            field.setAccessible(true);
            field.set(o, castPrimaryType("", field.getType()));
            field.setAccessible(false);
        }
    }

    public Object setObjectByRequest(HttpServletRequest request, Mapping map, HashMap<String, Object> singleton) throws ClassNotFoundException, NoSuchMethodException, InvocationTargetException, IllegalAccessException, InstantiationException, ServletException, IOException, ParseException {
        Object o = singleton.get(map.getClassName());
        if(o == null) {
            Class<?> clazz = Class.forName(map.getClassName());
            o = clazz.getDeclaredConstructor().newInstance();
        }
        this.initObject(o);

        Field[] allField = o.getClass().getDeclaredFields();
        String field_name;
        Object value_temp = null;
        Object value;

        for(Field f : allField) {
            field_name = f.getName();
            // value_temp = (f.getType().equals(FileUpload.class)) ? Util.getValueUploadedFile(request, field_name) : request.getParameter(field_name);

            if(value_temp != null) {
                try {
                    /*if(!f.getType().equals(FileUpload.class))*/ value = this.castPrimaryType(value_temp.toString(), f.getType());
                    //else value = value_temp;
                    o.getClass()
                            .getMethod("set"+util.casesString(field_name), f.getType())
                            .invoke(o, value);
                } catch (ParseException e) {
                    throw new RuntimeException(e);
                }
            }
        }
        return o;
    }


    public void loadMapping(String path, String tomPath, HashMap<String, Mapping> mappingUrls) throws ClassNotFoundException {
        List<Class<?>> allClass = util.getAllClass(path, tomPath);
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
    }

    public Object castPrimaryType(String data, Class<?> type) throws ParseException {
        if(data == null || type == null) return null;
        if(data.equals("")) {
            if(type.equals(Date.class) || type.equals(String.class)) return null;
            else return 0;
        }

        if (type.equals(Date.class)) {
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
            return type.cast(format.parse(data));
        }else if(type.equals(int.class)) return Integer.parseInt(data);
        else if(type.equals(float.class)) return Float.parseFloat(data);
        else if(type.equals(double.class)) return Double.parseDouble(data);
        else if(type.equals(boolean.class)) return Boolean.getBoolean(data);

        return data;
    }


    @Override
    public void init() throws ServletException {
        super.init();
        try {

            this.util = new Util();
            this.mappingUrls = new HashMap<>();
            this.sessionVariable = getInitParameter("session");

            final String tomPath = "/WEB-INF/classes/";
            String path = getServletContext().getRealPath(tomPath);
            this.getAnnotationPresent(path, tomPath, mappingUrls, singleton);
            List<Class<?>> allClass = util.getAllClass(path, tomPath);

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

    protected void processRequest2(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
                PrintWriter out = response.getWriter();
                String url = request.getRequestURL().toString();
                url = util.processUrlGet(url, request.getContextPath());
                out.println(url);
                try {
                    Mapping map = mappingUrls.get(url);
        
                    if (map == null) {
                        throw new Exception("Not Found");
                    }
        
                    Class<?> clazz = Class.forName(map.getClassName());
                    Object o = clazz.getDeclaredConstructor().newInstance();
                    Field[] allField = o.getClass().getDeclaredFields();
                    String field_name;
                    String value;
                    for (Field f : allField) {
        
                        field_name = f.getName();
                        value = request.getParameter(field_name);
        
                        if (value != null) {
                            o.getClass().getMethod("set" + util.casesString(field_name), String.class).invoke(o, value);
                        }
                    }
        
                    ModelView mv = (ModelView) o.getClass().getMethod(map.getMethod()).invoke(o);
        
                    HashMap<String, Object> donne = mv.getData();
                    for (String key : donne.keySet()) {
                        System.out.println(key);
                        request.setAttribute(key, donne.get(key));
                    }
        
                    RequestDispatcher dispatcher = request.getRequestDispatcher(mv.getView());
                    String queryString = request.getQueryString();
                    if (queryString != null) {
                        // Rediriger avec les paramètres query
                        response.sendRedirect(mv.getView() + "?" + queryString);
                    } else {
                        // Sinon, rediriger sans les paramètres query
                        response.sendRedirect(mv.getView());
                    }

        
                } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | NoSuchMethodException
                        | InvocationTargetException ignored) {
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
    }
    

    protected void processRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        PrintWriter out = response.getWriter();
        String url = request.getRequestURL().toString();
        url = util.processUrl(url, request.getContextPath());

        try {
            Mapping map = mappingUrls.get(url);
            if (map == null) {
                throw new Exception("Not Found");
            }

            // Class<?> clazz = Class.forName(map.getClassName());
            // Object o = clazz.getDeclaredConstructor().newInstance();
            // Field[] allField = o.getClass().getDeclaredFields();
            // String field_name;
            // String value;
            // for (int i = 0; i < allField.length; i++) {
            //     if(allField[i].getAnnotation(Model.class).equals("save.do")){
            //         field_name = allField[i].getName();
            //         value = request.getParameter(field_name);

            //         if (value != null) {
            //             o.getClass().getMethod("set" + util.casesString(field_name), String.class).invoke(o, value);
            //         }
            //         ModelView mv = (ModelView) o.getClass().getMethod(map.getMethod()).invoke(o);

            //         HashMap<String, Object> donne = mv.getData();
            //         for (String key : donne.keySet()) {
            //             System.out.println(key);
            //             request.setAttribute(key, donne.get(key));
            //         }
                
            //         RequestDispatcher dispatcher = request.getRequestDispatcher(mv.getView());
            //         dispatcher.forward(request, response);
            //     }
            // }
            // for (Field f : allField) {

            //     field_name = f.getName();
            //     value = request.getParameter(field_name);

            //     if (value != null) {
            //         o.getClass().getMethod("set" + util.casse(field_name), String.class).invoke(o, value);
            //     }
            // }

            

            // Class<?> clazz = Class.forName(map.getClassName());
            // Object o = clazz.getDeclaredConstructor().newInstance();

            // Field[] allField = o.getClass().getDeclaredFields();
            // String field_name;
            // String value;
            // for (Field f : allField) {

            //     field_name = f.getName();
            //     value = request.getParameter(field_name);

            //     if (value != null) {
            //         o.getClass().getMethod("set" + util.casesString(field_name), String.class).invoke(o, value);
            //         // ModelView mv = (ModelView) o.getClass().getMethod(map.getMethod()).invoke(o);
            //         // RequestDispatcher dispatcher = request.getRequestDispatcher(mv.getView());
            //         // dispatcher.forward(request, response);
            //     }
            // }

            // HashMap<String, Object> donne = mv.getData();
            // for(String key : donne.keySet()) {
            //     request.setAttribute(key, donne.get(key));
            // }
            // // for (Map.Entry<String, Mapping> entry : mappingUrls.entrySet()) {
            // //             String key = entry.getKey();
            // //             Mapping value = entry.getValue();
            // //             out.println("Clé : " + key + ", Valeur : " + value.getClassName() + ", " + value.getMethod());
            // //         }

            // RequestDispatcher dispatcher = request.getRequestDispatcher(mv.getView());
            // dispatcher.forward(request, response);
            

            ModelView mv = this.invokeMethod(request, map, singleton, sessionVariable);

            // HashMap<String, Object> donne = mv.getData();
            // for (String key : donne.keySet()) {
            //     System.out.println(key);
            //     request.setAttribute(key, donne.get(key));
            // }

            this.setAttributeRequest(request, mv);
            request.getRequestDispatcher(mv.getView()).forward(request, response);
           

        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | NoSuchMethodException |
                 InvocationTargetException ignored) { } catch (
                Exception e) {
            throw new RuntimeException(e);
        }
    }

    // }
    
    protected void doPost(HttpServletRequest request, HttpServletResponse response)throws ServletException, IOException {
        processRequest(request, response);

    }
    protected void doGet(HttpServletRequest request, HttpServletResponse response)throws ServletException, IOException {
        processRequest2(request, response);
    }
}