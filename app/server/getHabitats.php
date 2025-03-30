<?php
error_reporting(E_ERROR | E_PARSE);
$db_host = "localhost";
$db_uid = "root";
$db_pass = "root";
$db_name = "powerhome_db";

$db_con = mysqli_connect($db_host, $db_uid, $db_pass, $db_name);

$sql = "SELECT * FROM habitat";
$result = mysqli_query($db_con, $sql);
while($row = mysqli_fetch_assoc($result)){
    $output[] = $row;
}
mysqli_close($db_con);

header('Content-Type: application/json');
print(json_encode($output));
http_response_code(200);
