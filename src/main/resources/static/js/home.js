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