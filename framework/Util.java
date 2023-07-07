package traitement;

import java.io.File;
import java.util.ArrayList;
import jakarta.servlet.ServletException;
import java.util.List;
 
public class Util{
    public String url(String url){
        String[] tab = url.split("/");
        return tab[2];
    }

    public ArrayList<String> findAllFilesInFolder(File folder) {
		ArrayList<String> list= new ArrayList<>();
        for (File file : folder.listFiles()) {
            try {
                
                if (!file.isDirectory()) {
                    String filen =  file.getName();
                    String[] filename = filen.split(".class"); 
                    for (int i = 0; i < filename.length; i++) {                  
                        list.add(filename[i]);  
                    }
                } else {
                    findAllFilesInFolder(file);
                }
            } catch (Exception e) {
                //TODO: handle exception
            }
		}
        return list;
	}

    // public ArrayList<Class<?>> getAllClass(){
    //     String path = "donnees";
    //     System.out.println("path = "+path);
    //     // String path = "donnees";
    //     ArrayList<Class<?>> cl = new ArrayList<>(); 
    //     File file = new File(path); 
    //     ArrayList<String> list = this.findAllFilesInFolder(file);
    //     try {
    //         for (int i = 0; i < list.size(); i++) {
    //             cl.add(Class.forName(list.get(i)));
    //         }
    //     } catch (Exception e) {
    //         // TODO: handle exception
    //     }
    //     return cl;
    // }

    public  ArrayList<String> getAllPackages(String projectPath) {
        ArrayList<String> packages = new ArrayList<>();
        File root = new File(projectPath);
        if (!root.exists() || !root.isDirectory()) {
            throw new IllegalArgumentException("Le chemin spécifié n'est pas un répertoire valide.");
        }
        scanPackages(root, "", packages);
        return packages;
    }

    public  void scanPackages(File root, String packageName, List<String> packages) {
        File[] files = root.listFiles();
        if (files == null) {
            return;
        }
        for (File file : files) {
            if (file.isDirectory()) {
                String newPackageName = packageName.isEmpty() ? file.getName() : packageName + "." + file.getName();
                packages.add(newPackageName);
                scanPackages(file, newPackageName, packages);
            }
        }
    }

    // public ArrayList<String> findAllFilesInFolder(File folder) {
	// 	ArrayList<String> list= new ArrayList<>();
    //     for (File file : folder.listFiles()) {
    //         try {
                
    //             if (!file.isDirectory()) {
    //                 String filen =  file.getName();
    //                 String[] filename = filen.split(".class"); 
    //                 for (int i = 0; i < filename.length; i++) {                  
    //                     list.add(filename[i]);  
    //                 }
    //             } else {
    //                 findAllFilesInFolder(file);
    //             }
    //         } catch (Exception e) {
    //             //TODO: handle exception
    //         }
	// 	}
    //     return list;
	// }

    public  String getProjectPath() {
        String currentDir = System.getProperty("user.dir");
        return currentDir;
    }

    public ArrayList<String> findAllFilesInAllFolder(){
        ArrayList<String> list= new ArrayList<>();
        String projectPath = this.getProjectPath();
        ArrayList<String> folder = this.getAllPackages(projectPath);
        for (int i = 0; i < folder.size(); i++) {
            if(!folder.get(i).startsWith(".git") && new File(folder.get(i))!=null){
                System.out.println(folder.get(i)+" = "+i);

                for (File file : new File(folder.get(i)).listFiles()) {
                    try {
                        
                        if (!file.isDirectory()) {
                            String filen =  file.getName();
                            String[] filename = filen.split(".class"); 
                            for (int j = 0; j < filename.length; j++) {                  
                                list.add(filename[j]);  
                            }
                        } else {
                            findAllFilesInFolder(file);
                        }
                    } catch (Exception e) {
                        //TODO: handle exception
                    }
                }
            }
        }
        return list;
    }


    public  ArrayList<Class<?>> getAllClasses(String projectPath) {
        ArrayList<Class<?>> classes = new ArrayList<>();
        File projectDirectory = new File(projectPath);
        
        if (!projectDirectory.exists() || !projectDirectory.isDirectory()) {
            // Le chemin du projet est invalide ou n'est pas un répertoire
            return classes;
        }
        
        findClasses(projectDirectory, "", classes);
        
        return classes;
    }
    
    public  void findClasses(File directory, String packageName, ArrayList<Class<?>> classes) {
        if (!directory.exists()) {
            return;
        }
        
        File[] files = directory.listFiles();
        if (files == null) {
            return;
        }
        
        for (File file : files) {
            if (file.isDirectory()) {
                String subPackageName = packageName.isEmpty() ? file.getName() : packageName + "." + file.getName();
                findClasses(file, subPackageName, classes);
            } else if (file.getName().endsWith(".class")) {
                String className = packageName.isEmpty()
                        ? file.getName().substring(0, file.getName().length() - 6)
                        : packageName + "." + file.getName().substring(0, file.getName().length() - 6);
                try {
                    Class<?> clazz = Class.forName(className);
                    classes.add(clazz);
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    // public List<Class<?>> getAllClass(String path, String tomPath) throws ClassNotFoundException {
    //     List<Class<?>> classes = new ArrayList<>();
    //     File directory = new File(path);

    //     if (!directory.exists()) {
    //         return classes;
    //     }

    //     File[] files = directory.listFiles();
    //     assert files != null;
    //     for (File file : files) {
    //         if (file.isDirectory()) {
    //             List<Class<?>> innerClasses = this.getAllClass(file.getAbsolutePath(), tomPath);
    //             classes.addAll(innerClasses);
    //         } else if (file.getName().endsWith(".class")) {
    //             String absolute_path_class = file.getPath().replace("\\", "/");
    //             int tom_int_path = absolute_path_class.indexOf(tomPath);

    //             String className = absolute_path_class.substring(tom_int_path + tomPath.length())
    //                     .replace(".class", "")
    //                     .replace("/", ".");
    //             Class<?> clazz = Class.forName(className);

    //             classes.add(clazz);
    //         }
    //     }
    //     return classes;
    // }

    public List<Class<?>> getAllClass(String path, String tomPath, String packageName) throws ClassNotFoundException {
        List<Class<?>> classes = new ArrayList<>();
        File directory = new File(path);
    
        if (!directory.exists()) {
            return classes;
        }
    
        File[] files = directory.listFiles();
        assert files != null;
        for (File file : files) {
            if (file.isDirectory()) {
                // Vérifier si le dossier correspond au package spécifié
                if (file.getName().equals(packageName)) {
                    List<Class<?>> innerClasses = this.getAllClass(file.getAbsolutePath(), tomPath, packageName);
                    classes.addAll(innerClasses);
                }
            } else if (file.getName().endsWith(".class")) {
                String absolutePathClass = file.getPath().replace("\\", "/");
                int tomIntPath = absolutePathClass.indexOf(tomPath);
    
                String className = absolutePathClass.substring(tomIntPath + tomPath.length())
                        .replace(".class", "")
                        .replace("/", ".");
                
                // Vérifier si la classe appartient au package spécifié
                if (className.startsWith(packageName)) {
                    String simpleClassName = className.substring(packageName.length() + 1);
                    Class<?> clazz = Class.forName(className);
                    classes.add(clazz);
                }
            }
        }
        return classes;
    }
    public String processUrl(String url_input, String ctx) {
        ctx+="/";
        int ctx_ind = url_input.indexOf(ctx);
        String url = url_input.substring(ctx_ind + ctx.length());

        return url;
    }
    

}