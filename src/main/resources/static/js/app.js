/**
 * Created by rcrisan on 5/31/17.
 */

var authenticated = false;
var webhook;

$(document).ready(function () {
    $('#sendBtn').click(function () {
        var code = getCodeFromUrl();

        if (code.length === 0) {
            $('#errorMessage').css('display', 'block');
        } else {
            authenticate(code);
        }
    })
});

function getCodeFromUrl() {
    var url = window.location.href;
    var startIndexReper = "code=";
    var start = url.indexOf(startIndexReper);
    var end = url.indexOf("&");
    var code = "";

    if (start >= 0 && end >= 0) {
        code = url.substring(start + startIndexReper.length, end);
    }
    return code;
}

function authenticate(code) {

    if (authenticated === true) {
        sendMessage(webhook.url);
        return;
    }

    var client_id = "TO_BE_OVERRIDEN";
    var client_secret = "TO_BE_OVERRIDEN";
    var redirect_uri = "TO_BE_OVERRIDEN";

    var url = 'https://slack.com/api/oauth.access?' +
        '&client_id=' + client_id + '&client_secret=' + client_secret + '&code=' + code + '&redirect_uri=' + redirect_uri;

    $.ajax({
        url: "/authenticate",
        type: 'POST',
        data: JSON.stringify(url),
        contentType: 'application/json',
        dataType: 'json'
    }).done(function (webhookCreated) {
        webhook = webhookCreated;
        authenticated = true;
        sendMessage(webhook.url);
    })
}

function sendMessage(webHookUrl) {

    var message = $('#message').val();

    if (message.length === 0) {
        $('#errorMessage').text('Empty message');
        $('#errorMessage').css('display', 'block');
        return;
    } else {
        $('#errorMessage').css('display', 'none');
    }

    var data = {};
    data.webHookUrl = webHookUrl;
    data.text = message;

    $.ajax({
        url: '/sendMessage',
        method:'POST',
        data: JSON.stringify(data),
        contentType: 'application/json'
    }).done(function (data, textStatus, xhr) {
        if (xhr.status === 200) {
            console.log("Message send successfully");
        }
    })
}