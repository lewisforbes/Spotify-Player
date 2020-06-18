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
        padding-top:3%;
    }

    .button {
        width: 6%;
        padding: 2%;
    }
    </style>

    <script type="text/javascript" src="https://www.gstatic.com/charts/loader.js"></script>
    <script type="text/javascript">
        <%
        String chartJS = (String) request.getAttribute("featureChartJS");
        out.println(chartJS);
        %>
    </script>

</head>

<body>
<%
String songTitle = (String) request.getAttribute("songTitle");
String albumCover = (String) request.getAttribute("albumCoverURL");
%>
<center>
    <div class="ib">
        <h2><% out.println(songTitle); %></h2>
        <img src="<%=albumCover %>" style="width:50%;"/>
        <div>
            <a href="go"><img src="https://image.flaticon.com/icons/png/512/860/860822.png" class="button"/></a>
            <a href="skip"><img src="https://image.flaticon.com/icons/svg/860/860777.svg" class="button"/></a>
        </div>
    </div>
    <div class="ib">
        <div id="features_div" class = "ib"></div>
    </div>
</center>
</body>

</html>