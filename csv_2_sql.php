<?php

$str=file_get_contents('data_mentahan.csv');
$str=str_replace("\"", "", $str);
$str=str_replace("\'", "", $str);

file_put_contents('data_fixed.csv', $str);


