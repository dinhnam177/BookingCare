var kinhDo = document.getElementById("longitude");
var viDo = document.getElementById("latitude");
var valueKinhDo = Number(105.7899284362793);
var valueViDo = Number( 21.017254034683987);
var marker;
initMap();
function initMap(latInput, lngInput) {
    var latLng
    if(!latInput) {
          latLng= { lat: valueViDo, lng: valueKinhDo };
    } else {
        latLng= { lat: latInput, lng: lngInput };
    }

    var map = new google.maps.Map(document.getElementById("map"), {
        center: latLng,
        zoom: 15,
    });
    marker = new google.maps.Marker({
        position: latLng,
        map: map,
    });

    var geocoder = new google.maps.Geocoder();
    var infowindow = new google.maps.InfoWindow();

    google.maps.event.addListener(map, "click", function (e) {
        setDataLocation(e.latLng);
        geocoder.geocode({ location: e.latLng }, function (results, status) {
            if (status === google.maps.GeocoderStatus.OK) {
                if (results[0]) {
                    infowindow.setContent(
                        "<div>" +
                        "<b>Address :</b> " +
                        results[0].formatted_address +
                        "<br>" +
                        "<b>Latitude :</b> " +
                        results[0].geometry.location.lat() +
                        "<br>" +
                        "<b>Longitude :</b> " +
                        results[0].geometry.location.lng() +
                        "</div>"
                    );
                    infowindow.open(map, marker);
                    $("#location").val(results[0].formatted_address);
                } else {
                    console.log("No results found");
                }
            } else {
                console.log("Geocoder failed due to: " + status);
            }
        });
    });

    let input = document.getElementById("location");
    const options = {
        fields: ["formatted_address", "geometry", "name"],
        strictBounds: false,
        types: ["establishment"],
    };
    let autocomplete = new google.maps.places.Autocomplete(input, options);
    // Bind the map's bounds (viewport) property to the autocomplete object,
    // so that the autocomplete requests use the current map bounds for the
    // bounds option in the request.
    autocomplete.bindTo("bounds", map);

    autocomplete.addListener("place_changed", () => {
        infowindow.close();
        marker.setVisible(false);

        const place = autocomplete.getPlace();

        if (!place.geometry || !place.geometry.location) {
            // User entered the name of a Place that was not suggested and
            // pressed the Enter key, or the Place Details request failed.
            window.alert("No details available for input: '" + place.name + "'");
            return;
        }

        // If the place has a geometry, then present it on a map.
        if (place.geometry.viewport) {
            map.fitBounds(place.geometry.viewport);
        } else {
            map.setCenter(place.geometry.location);
            map.setZoom(15);
        }

        marker.setPosition(place.geometry.location);
        setDataLocation(place.geometry.location)
        marker.setVisible(true);
        infowindowContent.children["place-name"].textContent = place.name;
        infowindowContent.children["place-address"].textContent =
            place.formatted_address;
        infowindow.open(map, marker);
    });

    function setDataLocation(location) {
        console.log('lat:' + location.lat() +'-long:' + location.lng());
        viDo.value = location.lat();
        kinhDo.value = location.lng();
        marker.setPosition(location);
    }
}

document.getElementById('btnSearchLocation').addEventListener("click", changeLocation);
function changeLocation(e) {
    e.preventDefault()
    valueKinhDo = Number(kinhDo.value);
    valueViDo= Number(viDo.value);
    console.log(valueKinhDo + '-' + valueViDo);
    initMap(valueKinhDo, valueViDo);
}