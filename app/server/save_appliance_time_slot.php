<?php
error_reporting(E_ERROR | E_PARSE);
header('Content-Type: application/json');
http_response_code(200);
ob_clean();
ob_start();

$db_host = "localhost";
$db_uid = "root";
$db_pass = "root";
$db_name = "powerhome_db";

$db_con = mysqli_connect($db_host, $db_uid, $db_pass, $db_name);

$token = $_GET['token'];
$appliance_id = $_GET['appliance_id'];
$time_slot_id = $_GET['time_slot_id'];
$order = $_GET['order'];
$booked_at = date('Y-m-d H:i:s');

// Vérification du token
$sql = "SELECT token, expired_at FROM user WHERE token='$token'";
$result = mysqli_query($db_con, $sql);
$row = mysqli_fetch_assoc($result);

if (!$row || intval(strtotime($row['expired_at'])) < time()) {
    echo json_encode(["error" => true, "message" => "Token invalide ou expiré!"]);
    exit();
}

// Insertion du nouvel enregistrement
$sql_insert = "INSERT INTO appliance_time_slot (appliance_id, time_slot_id, `order`, booked_at) 
               VALUES ('$appliance_id', '$time_slot_id', '$order', '$booked_at')";

if (mysqli_query($db_con, $sql_insert)) {
    echo json_encode(["success" => true, "message" => "Appliance Time Slot ajouté avec succès."]);
} else {
    echo json_encode(["error" => true, "message" => "Erreur lors de l'ajout.", "details" => mysqli_error($db_con)]);
}

flush();
ob_end_flush();
mysqli_close($db_con);
?>
