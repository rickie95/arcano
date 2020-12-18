
let ENDPOINT_URL = "https://127.0.0.1:8443/arcano";

function appendUserToUserList(user){
    var row = "<li> <a href='" 
            + ENDPOINT_URL + "/users/" + user.id + "'>" 
            + user.username + "</a> </li>";
    console.log(row);
    $("#user-list").append(row);
}

function fetchEntity(entity_subpath,entity_id, callback, error_callback){
    $.ajax({
        url: ENDPOINT_URL + "/" + entity_subpath + "/" + entity_id,
        contentType: "application/json",
        dataType: 'json',
        success: function(result){
            callback(result);
        },
        error: function(result){
            error_callback(result);
        }
    });
}

function getEntityIdFromURL(parameter){
    var search_string = window.location.search;
    return search_string.split("?"+parameter+"=")[1];
}
