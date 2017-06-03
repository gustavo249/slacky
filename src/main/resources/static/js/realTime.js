/**
 * Created by rcrisan on 6/3/17.
 */
function connectRealTime(code) {
    $.ajax({
        method: 'POST',
        url: '/connectRealTime',
        data: JSON.stringify(code),
        contentType: 'application/json'
    }).done(function (message, textStatus, xhr) {
        if (xhr.status === 200) {
            console.log("Connection real time established");
            console.log(message)
        }
    });
}