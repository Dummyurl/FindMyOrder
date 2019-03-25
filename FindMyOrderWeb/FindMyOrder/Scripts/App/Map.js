
(function DatePickerFct() {

    $("#datepicker").datepicker({
        onSelect: function (dateText) {

            var courierId = document.getElementById("courierId").value;

            $.ajax({
                url: "/Statistics/GetLocationsByDate",
                type: "GET",
                contentType: 'application/json',
                data: { chosenDate: dateText, courierId: courierId},
                success: function (DateList) {

                    $(".childDiv").remove();

                    $("#parentDiv").append('<div class="childDiv"></div>');
                    for (var i = 0; i < DateList.CourierLocations.length; i++) {
                        $(".childDiv").append('<input value="' + DateList.CourierLocations[i].Latitude + '" class="latInput" style="display: none" />');
                        $(".childDiv").append('<input value="' + DateList.CourierLocations[i].Longitude + '" class="longInput" style="display: none" />');
                        $(".childDiv").append('<input value="' + DateList.CourierLocations[i].StringLocationTime + '" class="timeInput" style="display: none" />');
                    }

                    $("#courierId").val(DateList.CourierId);
                    $("#coleteLivrate").val(DateList.NrOfDeliveredPackages);
                    $("#kmParcursi").val(DateList.Mileage);
                    $("#combustibilConsumat").val(DateList.AproxSpentFuel);
                    $("#observations").val(DateList.Observations);
                    

                    

                    updateMap();
                }
            });
        }
    });
})();


var markers = [];
var map = null;
var polyMarkers = [];
var flightPath;

function updateMap() {

    var longList = document.getElementsByClassName("longInput");
    var latList = document.getElementsByClassName("latInput");
    var timeList = document.getElementsByClassName("timeInput");

    if (flightPath !== undefined) {
        flightPath.setMap(null);
        polyMarkers = [];
    }

    polyMarkers = [];

    for (var i = 0; i < markers.length; i++) {
        markers[i].setMap(null);
    }

    map.setCenter(new google.maps.LatLng(latList[0].value, longList[0].value));

    for (i = 0; i < longList.length; i++) {
        addMarker(latList[i].value, longList[i].value, timeList[i].value,  map);
    }

    flightPath = new google.maps.Polyline({
        path: polyMarkers,
        strokeColor: "#0000FF",
        strokeOpacity: 1.0,
        strokeWeight: 2
    });

    flightPath.setMap(map);

}

function initTheMap() {

    var longList = document.getElementsByClassName("longInput");
    var latList = document.getElementsByClassName("latInput");
    var timeList = document.getElementsByClassName("timeInput");

    var myCenter = new google.maps.LatLng(latList[0].value, longList[0].value);
    var mapProp = { center: myCenter, zoom: 14, mapTypeId: google.maps.MapTypeId.ROADMAP };
    map = new google.maps.Map(document.getElementById("googleMap"), mapProp);

    for (var i = 0; i < longList.length; i++) {
        addMarker(latList[i].value, longList[i].value, timeList[i].value, map);
    }

    var flightPath = new google.maps.Polyline({
        path: polyMarkers,
        strokeColor: "#0000FF",
        strokeOpacity: 1.0,
        strokeWeight: 2
    });

    flightPath.setMap(map);
}

function addMarker(lat, lng, time, map) {

    var image = 'http://maps.google.com/mapfiles/ms/icons/blue-dot.png';
    var pos = new google.maps.LatLng(lat, lng);
    marker = new google.maps.Marker({
        position: new google.maps.LatLng(lat, lng),
        map: map,
        icon: image,
        title: time.toString('HH:mm')
    });

    polyMarkers.push(pos)
    markers.push(marker);
}
