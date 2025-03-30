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

// Vérification du token
$sql = "SELECT token, expired_at, habitat_id FROM user WHERE token='$token'";
$result = mysqli_query($db_con, $sql);
$row = mysqli_fetch_assoc($result);

if (!$row || intval(strtotime($row['expired_at'])) < time()) {
    echo json_encode(["error" => true, "message" => "Token is invalid or expired! Please relogin."]);
    exit();
}

$habitat_id = $row['habitat_id'];

$output = [];

// Récupérer les informations de l'habitat de l'utilisateur
$sql_habitat = "SELECT h.id, h.floor, 
                        CONCAT(u.firstname, ' ', u.lastname) AS owner_name
                FROM habitat h
                LEFT JOIN user u ON h.id = u.habitat_id
                WHERE h.id = $habitat_id";

$result_habitat = mysqli_query($db_con, $sql_habitat);
$habitat = mysqli_fetch_assoc($result_habitat);

if ($habitat) {
    $nom = $habitat['owner_name'] ? $habitat['owner_name'] : "Habitat Libre";

    // Récupération des équipements pour cet habitat
    $equipements = [];
    $sql_equipements = "SELECT id, name, reference, wattage FROM appliance WHERE habitat_id = $habitat_id";
    $result_equipements = mysqli_query($db_con, $sql_equipements);

    while ($row_equipement = mysqli_fetch_assoc($result_equipements)) {
        $equipements[] = $row_equipement;
    }

    // Construction de la réponse
    $output = [
        "nom" => $nom,
        "etage" => $habitat['floor'],
        "equipements" => $equipements
    ];
} else {
    echo json_encode(["error" => true, "message" => "Habitat not found for the user."]);
    exit();
}

flush(); // Envoie immédiatement le contenu du buffer
ob_end_flush(); // Envoie tout le buffer et ferme proprement
mysqli_close($db_con);

echo json_encode($output, JSON_PRETTY_PRINT);
?>
