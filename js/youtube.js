var search = "party";
var category = "music";
var vidURL;
var results;
//http://www.youtube.com/v/VIDEO_ID?version=3&enablejsapi=1

function searchVideos() {

    gapi.client.load('youtube', 'v3', function() {
        console.log('youtube API loaded...');

        var request = gapi.client.youtube.search.list({
            part: 'snippt',
            q: search
        });
        request.execute(function(response) {

            var str = JSON.stringify(response.result);
            alert(str);
        });
}