var search;
var category = "music";
var results;
var currentVideo;
var currentIndex;

var videoIDs = [];

function searchVideos() {
    
    var searchURL = "https://www.googleapis.com/youtube/v3/search?part=snippet&q="+search+"&key=AIzaSyBqoNc396Db0tYILTe8-qazHwuCwQkF0Kk&maxResults=50";
    
    var xmlHttp = null;

    xmlHttp = new XMLHttpRequest();
    xmlHttp.open( "GET", searchURL, false );
    xmlHttp.send();
    results = xmlHttp.responseText;
    console.log(results);
    
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
    search = document.getElementById("search").value;
    searchVideos();
}

function parseResults() {
    
    videoIDs = [];
    
    $.each(results.items, function(item) {
        videoIDs.push(results.items[item].id.videoId);
    });  
    
    shuffleArray();
    
    //window.open('http://www.youtube.com/watch?v='+videoIDs[0],'_blank');
    
    /*var searchURL = "https://gdata.youtube.com/feeds/api/videos/"+videoIDs[0]+"?v=1";
    
    var xmlHttp = null;

    xmlHttp = new XMLHttpRequest();
    xmlHttp.open( "GET", searchURL, false );
    xmlHttp.send( null );
    results = xmlHttp.responseText;

    var splits = results.split("duration='");
    splits.splice(0, 1);
    results = splits[0];
    var loc = results.indexOf("'");
    var duration = results.substring(0,loc);
    
    if (duration > 300) {
    
        
    }*/
    
    
}

function shuffleArray() {
    var array = videoIDs;
    
    for (var i = array.length - 1; i > 0; i--) {
        var j = Math.floor(Math.random() * (i + 1));
        var temp = array[i];
        array[i] = array[j];
        array[j] = temp;
    }
    
    videoIDs = array;
    
    embedVideo();
}

function embedVideo() {
    currentIndex = 0;
    currentVideo = videoIDs[currentIndex];
    
    $('#videoplayer').append( "<iframe width='560' height='310' src='http://www.youtube.com/embed/"+videoIDs[0]+"?&fs=0&controls=0&autohide=1&color=white&autoplay=1&version=3&enablejsapi=1' frameborder='0' ></iframe>'");
                        
}

function pause() {
       
    
}

function nextSong() {
    currentIndex++;
    currentVideo = videoIDs[currentIndex];
    embedVideo();
}