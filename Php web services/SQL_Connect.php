<?php
$mysqli = new mysqli("stevie.heliohost.org", "user", "pass", "database");
if ($mysqli->connect_errno) {
	echo "Failed to connect to MySQL: (" . $mysqli->connect_errno . ") " . $mysqli->connect_error;
}
/*$executed=$mysqli->query("insert into Mesto (ime, opis, kategorija, koordinatax, koordinatay)
		values ('Gino','Italijanski restoran', '1', '21312','123123')");
if ($executed!=false)
{
	echo "Uspesno";
}*/
	
if (isset($_POST['ime']))
{
	$executed=$mysqli->query("insert into Mesto (ime, opis, kategorija, koordinatax, koordinatay, slika)
		values ('".$_POST['ime']."','".$_POST['opis']."', '".$_POST['kategorija']."', '".$_POST['koordinatax']."','".$_POST['koordinatay']."', '".$_POST['slika']."')");
	
	
	/*if ($stmt=$mysqli->prepare("insert into Mesto (ime, opis, kategorija, koordinatax, koordinatay, slika)
		values (?,?,?,?,?,?)"))
	{
		$stmt->bind_param("s", $_POST['ime']);
		$stmt->bind_param("s", $_POST['opis']);
		$stmt->bind_param("i", $_POST['kategorija']);
		$stmt->bind_param("d", $_POST['koordinatax']);
		$stmt->bind_param("d", $_POST['koordinatay']);
		$stmt->bind_param("s", $_POST['slika']);
		
		$stmt->execute();
	}*/
	
	$last_row=$mysqli->insert_id;
	
	$mysqli->query("update Mesto set slika='IMG_".$last_row.".jpg' where mesto_id=".$last_row);
	
	
	$uploadedFile = $_FILES['slika']['name'];
	
	$uploadedType = $_FILES['slika']['type'];
	
	$temp = $_FILES['slika']['tmp_name'];
	
	$error = $_FILES['slika']['error'];
	
	
	move_uploaded_file($temp, "Sliki/IMG_".$last_row.".jpg");
	
	if ($executed!=false)
	{
		echo "Uspesno";
	}
}

	
