// Show marker creation popup
function showMarkerCreationPopup(latlng, map) {
    var popupContent = `
        <div style="font-family: Arial; font-size: 12px;">
            <label for="marker-name">Name:</label><br>
            <input type="text" id="marker-name" style="width: 100%;"><br><br>
            <label for="marker-symbol">Symbol (Emoji):</label><br>
            <input type="text" id="marker-symbol" maxlength="2" placeholder="e.g., ⭐" style="width: 100%;"><br><br>
            <label for="marker-description">Description:</label><br>
            <textarea id="marker-description" style="width: 100%; height: 50px;"></textarea><br><br>
            <input type="checkbox" id="marker-private"> Private<br><br>
            <button onclick="saveMarker(${latlng.lat}, ${latlng.lng})" style="margin-right: 5px;">Save</button>
            <button onclick="cancelMarkerCreation()">Cancel</button>
        </div>
    `;

    // Create and open the popup
    var popup = L.popup()
        .setLatLng(latlng)
        .setContent(popupContent)
        .openOn(map);

    // Store the popup globally for later use
    window.currentPopup = popup;
}

// Save the marker
function saveMarker(lat, lng) {
    var name = document.getElementById('marker-name').value;
    var symbol = document.getElementById('marker-symbol').value;
    var description = document.getElementById('marker-description').value;
    var isPrivate = document.getElementById('marker-private').checked;

    // Validate input
    if (!name || !symbol || !description) {
        alert('Please fill in all fields.');
        return;
    }

    // Post data to API
    const postData = {
        name: name,
        symbol: symbol,
        description: description,
        isPrivate: isPrivate,
        lat: lat,
        lon: lng
    };

    fetch('/api/playgrounds', {
        method: 'POST',
        headers: {'Content-Type': 'application/json'},
        body: JSON.stringify(postData)
    })
        .then(data => {
            console.log('Marker saved:', data);
            window.map.closePopup(window.currentPopup);
            fetchMapData(window.map, window.markers);
        })
        .catch(error => {
            console.error('Error saving marker:', error);
        });
}

// Cancel marker creation
function cancelMarkerCreation() {
    window.map.closePopup(window.currentPopup);
}

function loadLeafletScript(callback) {
    var script = document.createElement('script');
    script.src = 'https://unpkg.com/leaflet@1.7.1/dist/leaflet.js';
    script.onload = function () {
        var clusterScript = document.createElement('script');
        clusterScript.src = 'https://unpkg.com/leaflet.markercluster/dist/leaflet.markercluster-src.js';
        clusterScript.onload = callback;
        document.head.appendChild(clusterScript);
    };
    document.head.appendChild(script);
}

function initializeMap() {
    window.markerSet = new Set(); // Initialize the global marker set

    var map = L.map('map').setView([56.6616, 16.3616], 4);

    L.tileLayer('https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png', {
        attribution: '&copy; <a href="https://www.openstreetmap.org/copyright">OpenStreetMap</a> contributors'
    }).addTo(map);

    // Custom heart icon
    var heartIcon = L.divIcon({
        html: '<div class="custom-icon">❤️</div>',
        className: '',
        iconSize: [24, 24],
        iconAnchor: [12, 12],
        popupAnchor: [0, -12]
    });

    // Create a marker cluster group
    var markers = L.markerClusterGroup();

    // Fetch initial markers based on map bounds
    fetchMapData(map, markers);

    // Add markers to the map
    map.addLayer(markers);

    // Track the current zoom level
    let currentZoom = map.getZoom();

    // Add event listener for when the map's view changes
    map.on('moveend', function () {
        var newZoom = map.getZoom();
        if (newZoom <= currentZoom) {
            fetchMapData(map, markers); // Fetch markers on zoom out or pan
        }
        currentZoom = newZoom; // Update the zoom level
    });

    // Add a click event to create new markers
    map.on('click', function (event) {
        showMarkerCreationPopup(event.latlng, map);
    });

    // Attach the map and markers globally for other functions
    window.map = map;
    window.markers = markers;
}

// Function to fetch data from the API and place markers
function fetchMapData(map, markers) {
    var bounds = map.getBounds(); // Get the visible area

    // Extract corner coordinates
    var northEast = bounds.getNorthEast(); // Top-right corner
    var southWest = bounds.getSouthWest(); // Bottom-left corner

    // Build the API URL with query parameters
    var apiUrl = `/api/playgrounds?ne_lat=${northEast.lat}&ne_lng=${northEast.lng}&sw_lat=${southWest.lat}&sw_lng=${southWest.lng}`;

    // Send the GET request
    fetch(apiUrl)
        .then(response => response.json())
        .then(data => {
            data.forEach(location => {
                // Generate a unique key for the marker based on its coordinates
                var markerKey = `${location.lat},${location.lon}`;

                // Check if the marker already exists
                if (!window.markerSet.has(markerKey)) {
                    var icon = L.divIcon({
                        html: '<div class="custom-icon">❤️</div>',
                        className: '',
                        iconSize: [24, 24],
                        iconAnchor: [12, 12],
                        popupAnchor: [0, -12]
                    })
                    // Add marker if it doesn't exist
                    var marker = L.marker([location.lat, location.lon], {icon: icon})
                        .bindPopup(`<b>Latitude:</b> ${location.lat}<br><b>Longitude:</b> ${location.lon}`);
                    markers.addLayer(marker);

                    // Add the marker key to the set
                    window.markerSet.add(markerKey);
                }
            });
        })
        .catch(error => {
            console.error('Error fetching map data:', error);
        });
}

// Refresh button handler
function refreshMap() {
    if (window.map && window.markers) {
        // Clear existing markers
        window.markers.clearLayers();

        // Reset the markerSet
        window.markerSet.clear();

        // Fetch new markers
        fetchMapData(window.map, window.markers);
    }
}


// Load the Leaflet.js script and initialize the map
loadLeafletScript(initializeMap);