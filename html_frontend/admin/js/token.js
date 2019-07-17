$(document).ready(function () {
    if(localStorage.getItem("token")=="undefined" || localStorage.getItem("token")=="" ){
        $( '<a href="../authenticate.html" id="aa"></a>' ).appendTo( "body" );
        document.getElementById("aa").click();
    }
    $("#logout").click(function(){
        localStorage.setItem("token", "");
        $( '<a href="../authenticate.html" id="aa"></a>' ).appendTo( "body" );
        document.getElementById("aa").click();
    });
});
