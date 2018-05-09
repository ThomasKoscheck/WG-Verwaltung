<?php
//getting the values from app
$product = $_GET['product'];
$requester = $_GET['requester'];
$price = $_GET['price'];
$password = $_GET['password'];

//get login credentials from outside of webroot
$constants = parse_ini_file("path-to-ini-file-outside-of-webroot");
$dbpass = $constants['database-user-password'];
$auth = $constants['auth-code'];

if (hash('sha256', $password) === $auth) {
    //Connect to MariaDB
    $dbhost = 'localhost';
    $dbuser = 'database-user';
    $dbname = 'database-name';
    $dbpass = 'database-user-password';
    $dbtable = 'database-table';

    $conn = new mysqli($dbhost, $dbuser, $dbpass, $dbname);
    if ($conn->connect_errno) {
        die("Connection failed: " . $mysqli->connect_error);
    }

    //single query
    $sql_getCredit = "SELECT credit FROM $dbtable ORDER BY id DESC LIMIT 1";

    //preparation for query
    $retval = $conn->prepare($sql_getCredit);
    $retval->execute();

    //getting and processing the results
    $result = $retval->get_result();
    while ($row = $result->fetch_assoc()) {
        $lastCredit = $row['credit'];
    }

    //calculate new credit-limit
    $credit = $lastCredit - $price;

    //INSERT INTO DATABASE
    $sql_setNew = "INSERT INTO $dbtable (credit, product, requester, price)
                VALUES ('$credit', '$product', '$requester', '$price')";

    if ($conn->query($sql_setNew) === false) {
        echo "Error: " . $sql_setNew . "<br>" . $conn->error;
    }

    $conn->close();

    echo $credit;
} else {
    echo "Your IP is logged. The the responsible authorities are informed.";}