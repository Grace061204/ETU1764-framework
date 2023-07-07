cd "C:\Users\lenovo\Documents\S4\frameworkOK\ETU1764-framework\framework"
javac *.java -d "C:\Users\lenovo\Documents\S4\frameworkOK\ETU1764-framework\framework\out"
cd "C:\Users\lenovo\Documents\S4\frameworkOK\ETU1764-framework\framework\out"
jar cvf framework.jar *
copy "./framework.jar" "C:\Users\lenovo\Documents\S4\frameworkOK\ETU1764-framework\test-framework\WEB-INF\lib"