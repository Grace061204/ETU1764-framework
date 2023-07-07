
cd "C:\Users\lenovo\Documents\S4\frameworkOK\ETU1764-framework\test-framework\WEB-INF\classes"
javac -cp "C:\Users\lenovo\Documents\S4\frameworkOK\ETU1764-framework\test-framework\WEB-INF\lib\framework.jar" *.java -d .
cd "C:\Users\lenovo\Documents\S4\frameworkOK\ETU1764-framework\test-framework"
jar cvf "frameworkSprint.war" *
copy "frameworkSprint.war" "C:\Program Files\Apache Software Foundation\Tomcat 10.0\webapps"