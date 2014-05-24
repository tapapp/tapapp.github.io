var search = "party";
var category = "music";
var vidURL;
var results;
//http://www.youtube.com/v/VIDEO_ID?version=3&enablejsapi=1

function searchVideos() {

    gapi.client.load('youtube', 'v3', function() {
        console.log('youtube API loaded...');
        // Step 5: Assemble the API request
        var qVar = "Kanye West Amazing";
        // changed. added: type
        var request = gapi.client.youtube.search.list({
            type: 'video',
            part: 'snippt',
            q: qVar
        });
        
        var str = JSON.stringify(request);
        alert(str);
      });
}