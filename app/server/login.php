<?php
header("Content-Type: application/json");

// Connexion à la base de données
$host = "127.0.0.1";
$user = "root";
$pass = "anes";  // Mets ton mot de passe MySQL si besoin
$dbname = "mobile";
$port = "8889";

$conn = new mysqli($host, $user, $pass, $dbname, $port);

if ($conn->connect_error) {
    die(json_encode(["success" => false, "message" => "Erreur de connexion à la base de données"]));
}

$email = $_POST['email'] ?? '';
$password = $_POST['password'] ?? '';

if ($email && $password) {
    // Vérifier si l'utilisateur existe
    $stmt = $conn->prepare("SELECT id, name, password FROM User WHERE email = ?");
    $stmt->bind_param("s", $email);
    $stmt->execute();
    $result = $stmt->get_result();

    if ($result->num_rows > 0) {
        $user = $result->fetch_assoc();
        // Vérification du mot de passe
        if (password_verify($password, $user['password'])) {
            echo json_encode([
                "success" => true,
                "message" => "Connexion réussie",
                "user_id" => $user['id'],
                "name" => $user['name']
            ]);
        } else {
            echo json_encode(["success" => false, "message" => "Mot de passe incorrect"]);
        }
    } else {
        echo json_encode(["success" => false, "message" => "Utilisateur non trouvé"]);
    }

    $stmt->close();
} else {
    echo json_encode(["success" => false, "message" => "Email et mot de passe requis"]);
}

$conn->close();
?>