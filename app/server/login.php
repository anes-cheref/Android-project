<?php
error_reporting(E_ERROR | E_PARSE);
$db_host = "localhost";
$db_uid = "root";
$db_pass = "root";
$db_name = "powerhome_db";

$db_con = mysqli_connect($db_host, $db_uid, $db_pass, $db_name);

$email = $_GET['email'];
$password = $_GET['password'];

$sql = "SELECT token,expired_at FROM `user` WHERE email='$email' and password='$password'";
$result = mysqli_query($db_con, $sql);
$row = mysqli_fetch_assoc($result);

if ($row == null) {
    print(json_encode('incorrect email or password !'));
}
elseif ($row['token']== null or intval(strtotime($row['expired_at'])) < time()) {
    $token = md5(uniqid().rand(10000, 99999));
    $expire = date('Y-m-d H:i:s', strtotime('+30 days',time())) ;
    $sql = "UPDATE user SET token='$token', expired_at='$expire' WHERE email='$email'";
    mysqli_query($db_con, $sql);
    print(json_encode(array("token" => $token, "expired_at" => $expire) ));
}
else {
    print(json_encode($row));
}
