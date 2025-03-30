<?php
// error_reporting(E_ERROR | E_PARSE);
// $db_host = "localhost";
// $db_uid = "root";
// $db_pass = "root";
// $db_name = "powerhome_db";

// $db_con = mysqli_connect($db_host, $db_uid, $db_pass, $db_name);

// $token = $_GET['token'];

// $sql = "SELECT token, expired_at FROM `user` WHERE token='$token'";
// $result = mysqli_query($db_con, $sql);
// $row = mysqli_fetch_assoc($result);

// if ($row['token'] == null or intval(strtotime($row['expired_at'])) < time()) {
//    print(json_encode([
//         "error" => true,
//         "response" => "Token manquant"
//     ]));
// }
// else{

//     print(json_encode([
//         "error" => false,
//         "response" => "Token correct"
//     ]));
// }

// header('Content-Type: application/json');
// http_response_code(200);
error_reporting(E_ERROR | E_PARSE);
header('Content-Type: application/json');
http_response_code(200);
ob_clean(); // Vide le buffer de sortie
ob_start(); // Active le buffering

// Connexion à la base de données
$db_host = "localhost";
$db_uid = "root";
$db_pass = "root";
$db_name = "powerhome_db";

$db_con = mysqli_connect($db_host, $db_uid, $db_pass, $db_name);

if (!$db_con) {
    echo json_encode([
        "error" => true,
        "msg" => "Erreur de connexion à la base de données"
    ]);
    exit;
}

// Vérifier si le token est fourni
$token = $_GET['token'] ?? null;

if (!$token) {
    echo json_encode([
        "error" => true,
        "msg" => "Token manquant"
    ]);
    exit;
}

// Requête SQL sécurisée pour éviter les injections SQL
$sql = "SELECT id, firstname, lastname, email, habitat_id, token, expired_at FROM `user` WHERE token = ?";
$stmt = mysqli_prepare($db_con, $sql);
mysqli_stmt_bind_param($stmt, "s", $token);
mysqli_stmt_execute($stmt);
$result = mysqli_stmt_get_result($stmt);
$row = mysqli_fetch_assoc($result);

if (!$row || strtotime($row['expired_at']) < time()) {
    echo json_encode([
        "error" => true,
        "msg" => "Token expiré ou incorrect"
    ]);
} else {
    echo json_encode([
        "error" => false,
        "msg" => "Token correct",
        "id" => $row['id'],
        "first" => $row['firstname'],
        "last" => $row['lastname'],
        "email" => $row['email'],
        "habitat_id" => $row['habitat_id']
    ], JSON_UNESCAPED_UNICODE | JSON_UNESCAPED_SLASHES);
}

flush(); // Envoie immédiatement le contenu du buffer
ob_end_flush(); // Envoie tout le buffer et ferme proprement

// Fermer la connexion
mysqli_close($db_con);
exit;
