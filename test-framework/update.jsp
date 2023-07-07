<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Test JSP</title>
</head>
<body>
<%@ page import="emp.Emp" %>

<p>Votre view</p>
<%= request.getAttribute("emp") %>
    <% 
        Emp emp = (Emp) request.getAttribute("emp");
            String nomEmp = emp.getNomEmp();
            int ageEmp = emp.getAgeEmp();
    %>
            nom = <%= nomEmp %><br>    
            age = <%= ageEmp %> 
</body>
</html>