<?php
header("Content-Type: application/json");

// Connexion à la base de données
$host = "127.0.0.1";
$user = "root";
$pass = "anes";  // Mets ton mot de passe MySQL si besoin
$dbname = "mobile";
$port = "8889";

$conn = new mysqli($host, $user, $pass, $dbname,$port);

if ($conn->connect_error) {
    die(json_encode(["message" => "Erreur de connexion à la base de données"]));
}

// Récupération des données envoyées par POST
$fullname = $_POST['fullname'] ?? '';
$email = $_POST['email'] ?? '';
$password = $_POST['password'] ?? '';
$phone = $_POST['phone'] ?? '';

if ($fullname && $email && $password && $phone) {
    $password_hash = password_hash($password, PASSWORD_DEFAULT); // Sécurisation du mot de passe
    $stmt = $conn->prepare("INSERT INTO User (name, email, password, phone) VALUES (?, ?, ?, ?)");
    $stmt->bind_param("ssss", $fullname, $email, $password_hash, $phone);

    if ($stmt->execute()) {
        echo json_encode(["message" => "Inscription réussie ✅"]);
    } else {
        echo json_encode(["message" => "Erreur lors de l'inscription ❌"]);
    }
    $stmt->close();
} else {
    echo json_encode(["message" => "Tous les champs sont requis ❌"]);
}

$conn->close();
?>