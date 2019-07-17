$(document).ready(function () {

    $("#form1").submit(function (event) {
        event.preventDefault();

        $.ajax({
            type: 'POST',
            url: 'http://127.0.0.1:3000/api/contact',
            data: $('#form1').serialize(),
            dataType: "json",
            beforeSend: function () {
                $(".main-btn").attr("disabled", true);
            },
            success: function (response) {
                $('#form1')[0].reset();
                console.log(response);
                $("#check").html(response.Success).addClass("alert");
                $(".main-btn").attr("disabled", false);
            },
            error: function () {
            }
        })
    });

    $("#appointment").submit(function (event) {
        event.preventDefault();

        $.ajax({
            type: 'POST',
            url: 'http://127.0.0.1:3000/api/appointment',
            data: $('#appointment').serialize(),
            dataType: "json",
            beforeSend: function () {
                $(".main-btn").attr("disabled", true);
            },
            success: function (response) {
                $('#appointment')[0].reset();
                console.log(response);
                $("#check").html(response.Success).addClass("alert");
                $(".main-btn").attr("disabled", false);

            },
            error: function () {
            }
        })
    });

    $("#forumPost").submit(function (event) {
        event.preventDefault();
        var _form = $(this);
        var postForm = { //Fetch form data
            'token': localStorage.getItem("token"),
            '_id': localStorage.getItem("_id"),
            'username': localStorage.getItem("username"),
            'title': $('input[name=title]').val(),
            'description': CKEDITOR.instances['editor2'].getData(),
        };
        $.ajax({
            type: 'POST',
            url: 'http://127.0.0.1:3000/api/forumPost',
            data: postForm,
            dataType: "json",
            beforeSend: function () {
                $(".main-btn").attr("disabled", true);
            },
            success: function (response) {
                _form[0].reset();
                CKEDITOR.instances['editor2'].setData('');
                $("#check").html(response.Success).addClass("alert");
                $(".main-btn").attr("disabled", false);
            },
            error: function () {
            }
        })
    });

    $("#profileDetail").submit(function (event) {
        event.preventDefault();
        var _form = $(this);
        var data = _form.serialize();
        data += '&_id='+localStorage.getItem("_id")+'&token='+localStorage.getItem("token");
        console.log(data);
        $.ajax({
            type: 'POST',
            url: 'http://127.0.0.1:3000/api/edtProfile',
            data: data,
            dataType: "json",
            beforeSend: function () {
                $(".main-btn").attr("disabled", true);
            },
            success: function (response) {
                console.log(response);
                $("#check").html(response.Success).addClass("alert");
                if(response.Success == "Profile Updated Successfully!!"){
                    localStorage.setItem("username",response.username);
                }
                $(".main-btn").attr("disabled", false);
            },
            error: function () {
            }
        })
    });

    $("#postComment").submit(function (event) {
        event.preventDefault();
        var searchParams = new URLSearchParams(window.location.search);
        var forum_id = "";
        if(searchParams.has('id')){
            forum_id = searchParams.get('id');
        }
        var _form = $(this);
        var msg =  CKEDITOR.instances['editor2'].getData();
        // var data = _form.serialize();
       var data = 'username='+localStorage.getItem("username")
            +'&description='+msg
            +'&_id='+localStorage.getItem("_id")
            +'&forum_id='+forum_id
            +'&token='+localStorage.getItem("token");
        $.ajax({
            type: 'POST',
            url: 'http://127.0.0.1:3000/api/post_comment',
            data: data,
            dataType: "json",
            beforeSend: function () {
                $(".main-btn").attr("disabled", true);
            },
            success: function (response) {
                _form[0].reset();
                console.log(response);
                $("#check").html(response.Success).addClass("alert");

                if(response.Success == "Your comment successfully posted"){

                    var comment = '<article><h4><a href="#">'+localStorage.getItem("username")+'</a></h4>'+msg+'</article>';
                    $(comment).prependTo('#app');
                }
                $(".main-btn").attr("disabled", false);
            },
            error: function () {
                alert('Login in first');
            }
        })
    });


});
