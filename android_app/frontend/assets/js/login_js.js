/*$(".container-form .btn").click(function(){
  $(".container").addClass("active");
});*/

$(document).ready(function () {
    $(".info-item .btn").click(function () {
        $(".login-container").toggleClass("log-in");
    });
    $(".submit-form").click(function () {
        $(".alert").hide();
        $(this).closest("form").submit()
    });

    $("#form").submit(function (event) {
        event.preventDefault();
        var data = $('#form').serialize();
        // data += '&username=blah&id=blah1';
        $.ajax({
            type: 'POST',
            url: 'http://localhost:3000/api/authenticate',
            data: data,
            dataType: "json",
            beforeSend: function () {
                $(".lbl_status").hide();
            },
            success: function (response) {
                //alert("a");
                console.log(response.Success);
                $('#form')[0].reset();
                $(".login-container").addClass("active");
                document.getElementById("check").innerHTML = response.message;
                //ADD THIS CODE
                setTimeout(function () {
                    document.getElementById("check").innerHTML = "";
                    if (response.Success == "Success!") {
                        if (typeof(Storage) !== "undefined") {
                            localStorage.setItem("token", response.token);
                            localStorage.setItem("admin", response.admin);
                            localStorage.setItem("username", response.username);
                            localStorage.setItem("_id", response._id);
                            $(".login-container").removeClass("active");
                        } else {
                            document.getElementById("check").innerHTML = "Sorry, your browser does not support Web Storage...";
                        }
                        $( '<a href="index.html" id="aa"></a>' ).appendTo( "body" );
                        document.getElementById("aa").click();
                    }else{
                        $(".login-container").removeClass("active");
                    }
                }, 3000);
                setTimeout(function () {
                    $(".lbl_status").show();
                }, 8000);

            },
            error: function () {
            }
        })
    });

    $("#form1").submit(function (event) {
        event.preventDefault();

        $.ajax({
            type: 'POST',
            url: 'http://localhost:3000/api/signup',
            data: $('#form1').serialize(),
            dataType: "json",
            beforeSend: function () {
                $(".lbl_status").hide();
            },
            success: function (response) {
                //alert("a");
                //console.log(response.Success);
                $('#form1')[0].reset();

                $(".login-container").addClass("active");
                document.getElementById("check").innerHTML = response.Success;
                //ADD THIS CODE
                setTimeout(function () {

                    document.getElementById("check").innerHTML = "";
                    if (response.Success == "You are regestered,You can login now.") {
                        $( '<a href="authenticate.html" id="aa"></a>' ).appendTo( "body" );
                        document.getElementById("aa").click();
                    } else {
                        $(".login-container").removeClass("active");
                    }
                }, 5000);
                setTimeout(function () {
                    $(".lbl_status").show();
                }, 8000);

            },
            error: function () {
            }
        })
    });

});
