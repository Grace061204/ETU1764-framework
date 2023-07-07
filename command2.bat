cd "C:\Program Files\Apache Software Foundation\Tomcat 10.0\webapps\sprint5-2\framework"
javac *.java -d "C:\Program Files\Apache Software Foundation\Tomcat 10.0\webapps\sprint5-2\framework\out"
cd "C:\Program Files\Apache Software Foundation\Tomcat 10.0\webapps\sprint5-2\framework\out"
jar cvf framework.jar *
copy "./framework.jar" "C:\Program Files\Apache Software Foundation\Tomcat 10.0\webapps\sprint5-2\test-framework\WEB-INF\lib"