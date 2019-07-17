$(document).ready(function() {
    if (localStorage.getItem("token") != "undefined" && localStorage.getItem("token") != "") {
        if (localStorage.getItem("admin") == "true") {
            $(".foruser").remove();
            $(".fornormaluser").remove();
        } else {
            $(".foradmin").remove();
        }
        $("#login").remove();
    } else {
        $(".foruser").remove();
        $(".foradmin").remove();
        $(".logged").remove();
    }
    $("#logout").click(function() {
        localStorage.setItem("token", "");
        localStorage.setItem("admin", "");
        localStorage.setItem("username", "");
        localStorage.setItem("_id", "");
        $('<a href="authenticate.html" id="aa"></a>').appendTo("body");
        document.getElementById("aa").click();
    });
});