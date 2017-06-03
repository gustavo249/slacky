/**
 * Created by rcrisan on 6/3/17.
 */
$(document).ready(function () {
    $('#webhookOption').click(function () {
          redirectTo("webhooks.html");
    });

    $('#realTimeOption').click(function () {
        redirectTo("realTimeMessaging.html")
    });
});

function redirectTo(htmlLocalFile) {
    window.location.href = htmlLocalFile;
}

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

function getRedirectUri() {
    var url = window.location.href;
    return url.substring(0, url.indexOf('?'));
}