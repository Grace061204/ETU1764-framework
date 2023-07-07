
cd "C:\Program Files\Apache Software Foundation\Tomcat 10.0\webapps\sprint5-2\test-framework"
javac -cp "C:\Program Files\Apache Software Foundation\Tomcat 10.0\webapps\sprint5-2\test-framework\WEB-INF\lib\framework.jar" *.java -d "C:\Program Files\Apache Software Foundation\Tomcat 10.0\webapps\sprint5-2\test-framework\WEB-INF\classes"
jar cvf "frameworkOk.war" *
copy "frameworkOk.war" "C:\Program Files\Apache Software Foundation\Tomcat 10.0\webapps"