var search = "party";
var category = "music";
var vidURL;
var results;
//http://www.youtube.com/v/VIDEO_ID?version=3&enablejsapi=1

/*function searchVideos() {

    gapi.client.load('youtube', 'v3', function() {
        console.log('youtube API loaded...');

        var request = gapi.client.youtube.search.list({
            part: 'snippet',
            q: search
        });
        alert(request);
        var str = JSON.stringify(request);
        alert(str);
        request.execute(function(response) {

            var str = JSON.stringify(response.result);
            alert(str);
        });
    });
}*/

function searchVideos() {
    
    var searchURL = "https://www.googleapis.com/youtube/v3/search?part=snippet&q="+search+"&key=AIzaSyAtw8iyBHIKBTLzIH9SdAi0LpHw7IKQjjw";
    var xmlHttp = null;

    xmlHttp = new XMLHttpRequest();
    xmlHttp.open( "GET", searchURL, false );
    xmlHttp.send( null );
    alert(xmlHttp.responseText);
    
}