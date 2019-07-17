$(document).ready(function () {

    $("#appointment").submit(function (event) {
        event.preventDefault();

        $.ajax({
            type: 'POST',
            url: 'http://127.0.0.1:3000/api/postAppointment',
            data: $('#appointment').serialize(),
            dataType: "json",
            beforeSend: function () {
                $(".main-btn").attr("disabled", true);
            },
            success: function (response) {
                $('#appointment')[0].reset();
                $("#check").html(response.Success).addClass("alert");
                $(".main-btn").attr("disabled", false);
                moredata = 'your custom data here';

// do what you like with the input
                $input = $('<input type="text" name="token"/>').val(morevalue);

// append to the form
                $('#myForm').append($input);

// then..
                data: $('#myForm').serialize()
            },
            error: function () {
            }
        })
    });

    $("#form2").submit(function (event) {
        event.preventDefault();

        $.ajax({
            type: 'POST',
            url: 'http://127.0.0.1:3000/api/appointment',
            data: $('#form2').serialize(),
            dataType: "json",
            beforeSend: function () {
                $(".main-btn").attr("disabled", true);
            },
            success: function (response) {
                $('#form2')[0].reset();
                console.log(response);
                $("#check").html(response.Success).addClass("alert");
                $(".main-btn").attr("disabled", false);

            },
            error: function () {
            }
        })
    });


});
