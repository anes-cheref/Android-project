<?php
error_reporting(E_ERROR | E_PARSE);
header('Content-Type: application/json');
http_response_code(200);
ob_clean(); // Vide le buffer de sortie
ob_start(); // Active le buffering

$db_host = "localhost";
$db_uid = "root";
$db_pass = "root";
$db_name = "powerhome_db";

$db_con = mysqli_connect($db_host, $db_uid, $db_pass, $db_name);

$token = $_GET['token'];
$month = $_GET['month']; // Format attendu: YYYY-MM

// Vérification du token
$sql = "SELECT id, token, expired_at FROM user WHERE token='$token'";
$result = mysqli_query($db_con, $sql);
$user = mysqli_fetch_assoc($result);

if (!$user || intval(strtotime($user['expired_at'])) < time()) {
    echo json_encode(["error" => true, "message" => "Token is invalid or expired! Please relogin."]);
    exit();
}

$user_id = $user['id'];

// Récupérer l'habitat de l'utilisateur
$sql = "SELECT habitat_id FROM user WHERE id='$user_id'";
$result = mysqli_query($db_con, $sql);
$row = mysqli_fetch_assoc($result);
$habitat_id = $row['habitat_id'];

if (!$habitat_id) {
    echo json_encode(["error" => true, "message" => "User does not have an associated habitat."]);
    exit();
}

// Récupérer les time_slots du mois donné
$sql = "SELECT id, begin, end, max_wattage FROM time_slot 
        WHERE DATE_FORMAT(begin, '%Y-%m') = '$month'";
$result = mysqli_query($db_con, $sql);

$time_slots = [];

while ($row = mysqli_fetch_assoc($result)) {
    $time_slot_id = $row['id'];
    $appliance_time_slots = [];

    // Récupérer les appliance_time_slot associés
    $sql_ats = "SELECT ats.appliance_id, ats.order, ats.booked_at, 
                        a.name, a.reference, a.wattage 
                 FROM appliance_time_slot ats
                 JOIN appliance a ON ats.appliance_id = a.id
                 WHERE ats.time_slot_id = $time_slot_id AND a.habitat_id = $habitat_id";
    
    $result_ats = mysqli_query($db_con, $sql_ats);
    
    while ($row_ats = mysqli_fetch_assoc($result_ats)) {
        $appliance_time_slots[] = [
            "appliance" => [
                "id" => $row_ats['appliance_id'],
                "name" => $row_ats['name'],
                "reference" => $row_ats['reference'],
                "wattage" => $row_ats['wattage']
            ],
            "order" => $row_ats['order'],
            "booked_at" => $row_ats['booked_at']
        ];
    }

    $time_slots[] = [
        "id" => $time_slot_id,
        "begin" => $row['begin'],
        "end" => $row['end'],
        "max_wattage" => $row['max_wattage'],
        "appliances" => $appliance_time_slots
    ];
}

flush(); // Envoie immédiatement le contenu du buffer
ob_end_flush(); // Envoie tout le buffer et ferme proprement
mysqli_close($db_con);

echo json_encode($time_slots, JSON_PRETTY_PRINT);