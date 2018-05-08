<?php

//Connect to MariaDB
$dbhost = 'localhost';
$dbuser = 'database-user';
$dbname = 'database-name';
$dbpass = 'database-user-password';
$dbtable = 'database-table';

$conn = new mysqli($dbhost, $dbuser, $dbpass, $dbname);
if ($conn->connect_errno) {
    die("Verbindung fehlgeschlagen: " . $mysqli->connect_error);
}

$product = $_GET['product'];
$requester = $_GET['requester'];
$price = $_GET['price'];

//Einzelne Abfragen
$sql_getCredit = "SELECT credit FROM $dbtable ORDER BY id DESC LIMIT 1";

//Vorbereitung zu Abfrage
$retval = $conn->prepare($sql_getCredit);
$retval->execute();

//Holen und Verarbeiten der Ergebnisse
$result = $retval->get_result();
while($row = $result->fetch_assoc())
{
    $lastCredit = $row['credit'];
}

//neues Preis berechnen
$credit = $lastCredit-$price;

//INSERT INTO DATABASE
$sql_setNew = "INSERT INTO $dbtable (credit, product, requester, price)
                VALUES ('$credit', '$product', '$requester', '$price')";


if ($conn->query($sql_setNew) === FALSE) {
    echo "Error: " . $sql_setNew . "<br>" . $conn->error;
}

$conn->close();	

echo $credit;
?>