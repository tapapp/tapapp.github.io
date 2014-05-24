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

/* function searchVideos() {

    gapi.client.load('youtube', 'v3', function() {
        console.log('youtube API loaded...');
        // Step 5: Assemble the API request
        var qVar = "Kanye West Amazing"
        // changed. added: type
        var request = gapi.client.youtube.search.list({
            type: 'video',
            part: 'id',
            q: qVar
        });
        
        var str = JSON.stringify(request);
        alert(str);

        /*request.execute(function(resp) {
          document.getElementById('vid').value = resp.items[0].id.videoId;
          console.log('saved video id successfully');
        });
      });
  }/*
