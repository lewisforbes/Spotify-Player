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
    .padding {
        width: 100%;
        padding-top: 7%;
    }

    .main_div {
        display: inline-block;
        width: 20%;
        vertical-align: center;
    }

    .inside {
        border: none;
    }

    </style>

    <script type="text/javascript" src="https://www.gstatic.com/charts/loader.js"></script>
    <script type="text/javascript">
        <%
        String chartsEmbed = (String) request.getAttribute("chartsEmbed");
        out.println(chartsEmbed);
        %>
    </script>

</head>

<body>
<%
String songTitle = (String) request.getAttribute("songTitle");
String albumCover = (String) request.getAttribute("albumCoverURL");
%>
<center>
    <div class="padding"></div>
    <div class="main_div">
        <h2 class="inside" style="width:50%;"> <% out.println(songTitle); %></h2>
        <img class="inside" src="<%=albumCover %>" style="width:90%;"/>
        <h3 style="color:black">Stereophonics</h3>
    </div>
    <div class="main_div">
        <div class="inside" id="features_chart" style="width:90%;"></div>
        <div class="inside" id="artists_map" style="width:90%;"></div>
    </div>
</center>
<!--<div class="corner">-->
<!--    <a href="go"><img src="https://image.flaticon.com/icons/png/512/860/860822.png" class="button"/></a><br>-->
<!--    <a href="skip"><img src="https://image.flaticon.com/icons/svg/860/860777.svg" class="button"/></a>-->
<!--</div>-->
</body>

</html>