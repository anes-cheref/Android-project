<?php
error_reporting(E_ERROR | E_PARSE);
$db_host = "localhost";
$db_uid = "root";
$db_pass = "root";
$db_name = "powerhome_db";

$db_con = mysqli_connect($db_host, $db_uid, $db_pass, $db_name);

$token = $_GET['token'];

$sql = "SELECT token, expired_at FROM `user` WHERE token='$token'";
$result = mysqli_query($db_con, $sql);
$row = mysqli_fetch_assoc($result);

if ($row['token'] == null or intval(strtotime($row['expired_at'])) < time()) {
   print(json_encode('Token is invalid or is already expired! Please relogin.'));
}
else{

    $sql = "SELECT * FROM habitat";
    $result = mysqli_query($db_con, $sql);
    while($row = mysqli_fetch_assoc($result)){

        $output_appliance = array();
        $sql_appliance = "SELECT * FROM appliance WHERE habitat_id=".$row['id'];
        $result_appliance = mysqli_query($db_con, $sql_appliance);
        while($row_appliance = mysqli_fetch_assoc($result_appliance)){
            $output_appliance[] = $row_appliance;
        }
        $row['appliances'] = $output_appliance;
        $output[] = $row;
    }
    mysqli_close($db_con);
    print(json_encode($output));
}

header('Content-Type: application/json');
http_response_code(200);
