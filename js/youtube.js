var search = "party";
var category = "music";
var vidURL;
var results;
//http://www.youtube.com/v/VIDEO_ID?version=3&enablejsapi=1

function searchVideos() {
  var q = search;
  var request = gapi.client.youtube.search.list({
    q: q,
    part: 'snippet'
  });
    request.execute(function(response) {
        var str = JSON.stringify(response.result);
        results = str;
  });
    alert(results);
}