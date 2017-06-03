/**
 * Created by rcrisan on 5/31/17.
 */

var authenticated = false;
var webhook;

$(document).ready(function () {
    $('.dropdown-toggle').dropdown();

    $('#sendBtn').click(function () {
        var code = getCodeFromUrl();
        var redirectUri = getRedirectUri();

        if (code.length === 0) {
            $('#errorMessage').css('display', 'block');
        } else {
            getWebhook(code, redirectUri);
        }
    });
});




function getWebhook(code, redirectUri) {

    if (authenticated === true) {
        sendMessage(webhook.url);
        return;
    }
    var requestInfo = {};
    requestInfo.code = code;
    requestInfo.redirectUri = redirectUri;

    $.ajax({
        url: "/getWebhook/",
        type: 'POST',
        data: JSON.stringify(requestInfo),
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
        method: 'POST',
        data: JSON.stringify(data),
        contentType: 'application/json'
    }).done(function (data, textStatus, xhr) {
        if (xhr.status === 200) {
            console.log("Message send successfully");
        }
    })
}

