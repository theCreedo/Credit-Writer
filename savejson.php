<?php
   $json = $_POST['json'];

   // $json = "{something: true}";

   // echo $json;

   // echo json_decode($json);

   // /* sanity check */
   // if (json_decode($json) != null)
   // {
   //   echo "it worked";
     $file = fopen('info.json','w+');
     // fwrite($file, $json);
     fwrite($file, $json);

     fclose($file);
   // }
   // else
   // {
   //  echo 'console.log($json)';
   //   // user has posted invalid JSON, handle the error 
   // }

?>
