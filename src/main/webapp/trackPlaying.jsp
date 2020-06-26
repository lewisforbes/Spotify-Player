<!DOCTYPE html>
<html lang="en">

<head>
    <%
    String refreshTime = (String) request.getAttribute("refreshTime");

    String loadFunction = "";
    String snackbarMessage = (String) request.getAttribute("snackbarMessage");
    if (snackbarMessage != null) {
        loadFunction = "showSnackbar()";
    }
    %>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <link rel="stylesheet" href="stylesheet.css">
    <title>Web Player</title>
    <meta http-equiv="Refresh" content="<%=refreshTime %>" />

    <style>
    * {
        color: #312C47;
    }

    .padding {
        width: 100%;
        padding-top: 4%;
    }

    .main_div {
        display: inline-block;
        width: 30%;
        vertical-align: centre;
    }

    .button {
        display: inline-block;
        width: 3%;
    }


    #snackbar {
        visibility: hidden;
        min-width: 250px;
        margin-left: -125px;
        background-color: #333;
        color: #fff;
        text-align: center;
        border-radius: 2px;
        padding: 16px;
        position: fixed;
        z-index: 1;
        left: 50%;
        bottom: 30px;
        font-size: 17px;
    }

    #snackbar.show {
      visibility: visible;
      -webkit-animation: fadein 0.5s, fadeout 0.5s 2.5s;
      animation: fadein 0.5s, fadeout 0.5s 2.5s;
    }

    @-webkit-keyframes fadein {
      from {bottom: 0; opacity: 0;}
      to {bottom: 30px; opacity: 1;}
    }

    @keyframes fadein {
      from {bottom: 0; opacity: 0;}
      to {bottom: 30px; opacity: 1;}
    }

    @-webkit-keyframes fadeout {
      from {bottom: 30px; opacity: 1;}
      to {bottom: 0; opacity: 0;}
    }

    @keyframes fadeout {
      from {bottom: 30px; opacity: 1;}
      to {bottom: 0; opacity: 0;}
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

<body onload="<%=loadFunction %>">
<%
String songTitle = (String) request.getAttribute("songTitle");
String albumCover = (String) request.getAttribute("albumCoverURL");
String artistsNames = (String) request.getAttribute("artistsNames");
%>
<center>
    <div class="padding"></div>
    <div class="main_div">
        <h2 class="inside" style="width:95%;"> <% out.println(songTitle); %></h2>
        <img class="inside" src="<%=albumCover %>" style="width:90%; border: solid #85828F 2px;"/>
        <h3 style="width:95%;"><% out.println(artistsNames); %></h3>
    </div>
    <div class="main_div">
        <div class="inside" id="features_chart" style="width:90%;"></div>
        <div class="inside" id="artists_map" style="width:90%;"></div>
        <div><p><br></p></div>
    </div>

<div class="corner">
    <a href="index.html"><img src="https://image.flaticon.com/icons/svg/860/860807.svg" class="button" style="padding-right:1.5%;"/></a>
    <a href="go"><img src="https://image.flaticon.com/icons/png/512/860/860822.png" class="button" style="padding-right:1%;"/></a>
    <a href="save"><img src="https://image.flaticon.com/icons/svg/1828/1828925.svg" class="button" style="padding-right:1%;"/></a>
    <a href="skip"><img src="https://image.flaticon.com/icons/svg/860/860828.svg" class="button"/></a>
</div>
</center>

<div id="snackbar"><% out.println(snackbarMessage);  %></div>
<script>
function showSnackbar() {
      var x = document.getElementById("snackbar");
      x.className = "show";
      setTimeout(function(){ x.className = x.className.replace("show", ""); }, 3000);
    }
</script>

</body>

</html>