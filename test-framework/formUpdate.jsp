<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html>
<head>
    <title>Liste des employés</title>
    <style>
        body {
            font-family: Arial, sans-serif;
            text-align: center;
            margin: 20px;
        }
        input[type="text"], input[type="number"] {
            padding: 5px;
            margin: 5px;
            width: 150px;
            border: 1px solid #ccc;
            border-radius: 4px;
        }
        input[type="submit"] {
            padding: 5px 10px;
            margin: 5px;
            background-color: #007bff;
            color: #fff;
            border: none;
            border-radius: 4px;
            cursor: pointer;
        }
        form {
            border: 1px solid #ccc;
            padding: 10px;
            margin: 10px;
        }
        p {
            font-size: 16px;
            margin-bottom: 5px;
        }
    </style>
</head>
<body>
    <% 
    Object[][] empArray = (Object[][]) request.getAttribute("emp");

    if (empArray != null && empArray.length > 0) {
        for (int i = 0; i < empArray.length; i++) { %>
            <form action="updatebase.do" method="post">
                <input type="hidden" name="idEmp" value="<%= empArray[i][0] %>"> 
                <p>Nom Employé : <input type="text" name="nomEmp" value="<%= empArray[i][1] %>"></p> 
                <p>Âge employé : <input type="number" name="ageEmp" value="<%= empArray[i][2] %>"></p> 
                <input type="submit" value="Modifier">
            </form>
            <%  
        }
    } else {
        out.println("Aucun employé trouvé.");
    }
    %>
</body>
</html>
