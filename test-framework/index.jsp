<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Test framework fromulaire</title>
</head>
<body>
    <p>Choix de profil</p>
    <form action="connect.do" method="POST">
        <select name="profil">
            <option value="admin">Admin</option>
            <option value="client">Client</option>
        </select>
        <input type="submit" value="Valider">
    </form>
<p>Recuperer donn&eacute venant de formulaire via attribut de classes </p>
    <form action="save.do" method="GET">
        <input type="text" name="nomEmp" placeholder="Entrer le nom employe">
        <input type="number" name="ageEmp" placeholder="Entrer l'age employe">
        <input type="submit" value="Valider">
    </form>
<p>Recuperer donn&eacute venant de formulaire via attribut de fonction </p>
    <form action="saveFunction.do" method="POST">
        <input type="text" name="nomEmp" placeholder="Entrer le nom employe">
        <input type="number" name="ageEmp" placeholder="Entrer l'age employe">
        <input type="submit" value="Valider">
    </form>
    <form action="list.do" method="POST">
        <input type="submit" value="Voir liste employe">
    </form>
</body>
</html>