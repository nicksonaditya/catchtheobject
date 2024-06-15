<?php
$servername = "localhost";
$username = "root";
$password = "";
$dbname = "catchtheobject";

// Create connection
$conn = new mysqli($servername, $username, $password, $dbname);

// Check connection
if ($conn->connect_error) {
    die("Connection failed: " . $conn->connect_error);
}

// Query to fetch top 10 scores
$sql = "SELECT username, score FROM score ORDER BY score DESC LIMIT 10";
$result = $conn->query($sql);

if ($result->num_rows > 0) {
    // Output data of each row
    while($row = $result->fetch_assoc()) {
        echo $row["username"] . "," . $row["score"] . "\n";
    }
} else {
    echo "0 results";
}
$conn->close();
?>
