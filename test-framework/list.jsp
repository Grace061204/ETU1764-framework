<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html>
<head>
    <title>Liste des employés</title>
    <style>
        table {
            border-collapse: collapse;
            width: 50%;
            margin: auto;
            margin-top: 20px;
            text-align: center;
        }
        th, td {
            border: 1px solid #dddddd;
            padding: 8px;
        }
        th {
            background-color: #f2f2f2;
        }
        .update-form {
            display: inline-block;
        }
    </style>
</head>
<body>
    <% 
    Object[][] empArray = (Object[][]) request.getAttribute("emp");

    if (empArray != null && empArray.length > 0) {
        %>
        <table>
            <tr>
                <th>Id Emp</th>
                <th>Nom</th>
                <th>Âge</th>
                <th>Action</th>
            </tr>
            <% for (int i = 0; i < empArray.length; i++) { %>
                <tr>
                    <% for (int j = 0; j < empArray[i].length; j++) { %>
                        <td><%= empArray[i][j] %></td>
                    <% } %>
                    <td>
                        <form action="update.do" method="post">
                            <input type="hidden" name="idEmp" value="<%= empArray[i][0] %>">
                            <input type="submit" value="Modifier">
                        </form>
                    </td>
                </tr>
            <% } %>
        </table>
    <% } else { %>
        <p>Aucun employé trouvé.</p>
    <% } %>
</body>
</html>
