/**
 * Created by rcrisan on 6/3/17.
 */
var token = "";

$(document).ready(function () {
    $('#connectRealTimeBtn').click(function () {
        var code = getCodeFromUrl();
        var redirectUri = getRedirectUri();

        if (code.length === 0) {
            $('#errorMessage').css('display', 'block');
        } else {
            connectRealTime(code, redirectUri);
        }
    });

    $('#sendBtn').click(function () {
        sendRealTimeMessage();
    })
});

function connectRealTime(code, redirectUri) {

    var data = {};
    data.code = code;
    data.redirectUri = redirectUri;

    $.ajax({
        method: 'POST',
        url: '/connectRealTime',
        data: JSON.stringify(data),
        contentType: 'application/json'
    }).done(function (message, textStatus, xhr) {
    });
}

function sendRealTimeMessage() {
    var message = $('#message').val();

    if (message.length === 0) {
        $('#errorMessage').text('Empty message');
        $('#errorMessage').css('display', 'block');
        return;
    } else {
        $('#errorMessage').css('display', 'none');
    }

    var data = {};
    data.text = message;

    $.ajax({
        url: '/sendMessageRealTime',
        method: 'POST',
        data: JSON.stringify(data),
        contentType: 'application/json'
    }).done(function (message, textStatus, xhr) {
        if (xhr.status === 200) {
            $('#errorMessage').css('display', 'none');
            console.log('Message was send succesfully');
            console.log(message);
        } else {
            $('#errorMessage').css('display', 'block');
        }
    })
}