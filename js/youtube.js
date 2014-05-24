var search;
var category = "music";
var vidURL;
var results;

var videoIDs = [];

function searchVideos() {
    
    var searchURL = "https://www.googleapis.com/youtube/v3/search?part=snippet&q="+search+"&key=AIzaSyBqoNc396Db0tYILTe8-qazHwuCwQkF0Kk";
    var xmlHttp = null;

    xmlHttp = new XMLHttpRequest();
    xmlHttp.open( "GET", searchURL, false );
    xmlHttp.send( null );
    results = xmlHttp.responseText;
    results = $.parseJSON(results);

    parseResults();
}

function setChill() {
    search="chill music";
    searchVideos();
}
function setHappy() {
    search="happy music";
    searchVideos();
}
function setStudying() {
    search="studying music";
    searchVideos();
}
function setParty() {
    search="party music";
    searchVideos();
}
function setWorkout() {
    search="workout music";
    searchVideos();
}
function setSearch() {
    //set the search variable to parameter + music
}

function parseResults() {

    $.each(results.items, function(item) {
        videoIDs.push(results.items[item].id.videoId);
    });  
    
    window.open('http://www.youtube.com/watch?v='+videoIDs[0],'_blank');
}