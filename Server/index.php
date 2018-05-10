<?php
    //getting the values from app
    $product = $_GET['product'];
    $requester = $_GET['requester'];
    $price = $_GET['price'];
    $password = $_GET['password'];

    function sqlAction($product, $requester, $price, $password)
    {
    //get login credentials from outside of webroot
    $constants = parse_ini_file("path-to-ini-file-outside-of-webroot");
    $dbpass = $constants['database-user-password'];
    $auth = $constants['auth-code'];

    $appVersion = $constants['app_version'];
  
    $challenge = bin2hex(random_bytes(200));
     
    if (hash('sha256', $password) === $auth) {
        //Connect to MariaDB
        $dbhost = 'localhost';
        $dbuser = 'database-user';
        $dbname = 'database-name';
        $dbtable = 'database-table';
     
        $conn = new mysqli($dbhost, $dbuser, $dbpass, $dbname);
        if ($conn->connect_errno) {
            die("Connection failed: " . $mysqli->connect_error);
        }
    
        //single query
        $sql_getCredit = "SELECT credit FROM $dbtable ORDER BY id DESC LIMIT 1";
     
        //preparation for query
        $retvalCredit = $conn->prepare($sql_getCredit);
        $retvalCredit->execute();
        
        //getting and processing the results
        $resultCredit = $retvalCredit->get_result();
        while ($row = $resultCredit->fetch_assoc()) {
            $lastCredit = $row['credit'];
        }
     
        //calculate new credit-limit and date
        $credit = $lastCredit - $price;
        $date = date('Y-m-d');
     
        //INSERT INTO DATABASE
        $sql_setNew = "INSERT INTO $dbtable (credit, product, requester, price, date, done)
                    VALUES ('$credit', '$product', '$requester', '$price', '$date', '0')";

        if ($conn->query($sql_setNew) === false) {
            echo "Error: " . $sql_setNew . "<br>" . $conn->error;
        }
     
        //single query
        $sql_getUndone = "SELECT product, requester, price, date FROM $dbtable WHERE done=0";

        //getting and processing the results      
        if ( $retvalUndone = $conn->query($sql_getUndone)) {
            $resultString = "";
            /* fetch associative array */
            while ($row = $retvalUndone->fetch_assoc()) {
                $resultString .= '{ "requester":"' . $row["requester"] . '",' .
                    '"product":"' . $row["product"] . '",' . 
                    '"price":"' . $row['price'] . '",' .
                    '"date":"' . $row['date'] . '",' .
                    "},";
            } 

            /* free result set */
            $retvalUndone->free();
        }
        
        $conn->close();

        buildJSON($credit, $appVersion, $resultString);
     
    } else {echo "Your IP is logged. The the responsible authorities are informed.";} //else wrong password
        
}

function buildJSON($credit, $appVersion, $resultString)
{

    $return = "{" .
        '"credit":"' . $credit . '",' .
        '"newestAppVersion":"' . $appVersion . '",' .
        '"expenses":[' . $resultString .
        "]}";

    echo $return;
}
    
function sqlFakeAction($password) {  
    //get login credentials from outside of webroot
    $constants = parse_ini_file("path-to-ini-file-outside-of-webroot");
    $dbpass = $constants['database-user-password'];
    $auth = $constants['auth-code'];
    
    $challenge = bin2hex(random_bytes(200));
    
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
        $retvalCredit = $conn->prepare($sql_getCredit);
        $retvalCredit->execute();

        //getting and processing the results
        $resultCredit = $retvalCredit->get_result();
        while ($row = $resultCredit->fetch_assoc()) {
            $lastCredit = $row['credit'];
        }

        //calculate new credit-limit
        $credit = $lastCredit - $price;

        //single query
        $sql_getUndone = "SELECT product, requester, price, date FROM $dbtable WHERE done=0";

        //getting and processing the results      
        if ($retvalUndone = $conn->query($sql_getUndone)) {
            $resultString = "";
            /* fetch associative array */
            while ($row = $retvalUndone->fetch_assoc()) {
                $resultString .= '{ "requester":"' . $row["requester"] . '",' .
                    '"product":"' . $row["product"] . '",' . 
                    '"price":"' . $row['price'] . '",' .
                    '"date":"' . $row['date'] . '",' .
                    "},";
            } 

            /* free result set */
            $retvalUndone->free();
        }
        
        $conn->close();

        buildJSON($credit, $appVersion, $resultString);
    
    } else {echo "Your IP is logged. The the responsible authorities are informed.";} //else wrong password     
}
    

//all values are given
if (!empty($product) && !empty($price) && !empty($requester)) {
    sqlAction($product, $requester, $price, $password);
}

//only password given, just echo current credit
elseif ($product === "" && $price == 0.0 && !empty($requester)) {
    sqlFakeAction($password);
}

//some values seem to be blank
else {echo "Some important values seem to be blank";}
    
