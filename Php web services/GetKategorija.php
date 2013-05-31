<?php
$mysqli = new mysqli("stevie.heliohost.org", "user", "pass", "database");
if ($mysqli->connect_errno) {
	echo "Failed to connect to MySQL: (" . $mysqli->connect_errno . ") " . $mysqli->connect_error;
}

$result=$mysqli->query("select * from Kategorija");
while($row=$result->fetch_array(MYSQLI_ASSOC))
{
	$rows[]=$row;
}
echo json_encode($rows);