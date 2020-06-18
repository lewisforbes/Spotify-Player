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
<!--    <meta http-equiv="Refresh" content="<%=refreshTime %>" />-->

    <style>
    .ib {
        display:inline-block;
    }

    .button {
        width: 6%;
        padding: 2%;
    }
    </style>

</head>

<body>
<%
String songTitle = (String) request.getAttribute("songTitle");
String albumCover = (String) request.getAttribute("albumCoverURL");
%>
<center>
    <div class="ib" style="padding-top:3%;">
        <h2><% out.println(songTitle); %></h2>
        <img src="<%=albumCover %>" style="width:50%;"/>
        <div>
            <img src="https://image.flaticon.com/icons/svg/860/860739.svg" class="button"/>
            <img src="https://image.flaticon.com/icons/svg/860/860780.svg" class="button"/>
            <img src="https://image.flaticon.com/icons/svg/860/860777.svg" class="button"/>
        </div>
        <div>
            <a href="go"><img src="https://image.flaticon.com/icons/png/512/860/860822.png" class="button"/></a>
        </div>
    </div>
</center>
</body>

</html>