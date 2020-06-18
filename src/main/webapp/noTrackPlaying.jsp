<!DOCTYPE html>
<html lang="en">

<head>
    <%
    String refreshTime = (String) request.getAttribute("songLength");
    %>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <link rel="stylesheet" href="stylesheet.css">
    <title>Web Player</title>

    <style>
        .centre {
            position: fixed;
            top: 50%;
            left: 50%;
            transform: translate(-50%, -80%);
        }
    </style>

</head>

<body>
<div class="centre">
    <center>
        <h2>No track is currently playing.</h2>
        <a href="go"><img src="https://image.flaticon.com/icons/png/512/860/860822.png" style="width:10%; "/></a>
    </center>
</div>
</body>

</html>