package org.example.laboration2backend.playground;

import org.example.laboration2backend.entity.Place;
import org.example.laboration2backend.entity.Playground;
import org.example.laboration2backend.place.PlaceRepository;
import org.geolatte.geom.G2D;
import org.geolatte.geom.Geometries;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

import static org.geolatte.geom.crs.CoordinateReferenceSystems.WGS84;

@Service
public class PlaygroundService {
    private final PlaygroundRepository playgroundRepository;
    PlaceRepository placeRepository;

    public PlaygroundService(PlaygroundRepository playgroundRepository, PlaceRepository placeRepository) {
        this.playgroundRepository = playgroundRepository;
        this.placeRepository = placeRepository;
    }

    public List<Playground> all() {
        return playgroundRepository.findAll();
    }

    public Playground createNewPlayGround(float lat, float lon) {
        if (lat < -90 || lat > 90 || lon < -180 || lon > 180) {
            throw new IllegalArgumentException("Invalid latitude or longitude");
        }
        Playground playground = new Playground();
        var geo = Geometries.mkPoint(new G2D(lon, lat), WGS84);
        playground.setCoordinate(geo);
        return playgroundRepository.save(playground);
    }

    public List<Playground> getPlaygroundsWithinRadius(double lat, double lon, double radius) {
        return playgroundRepository.findAll().stream()
                .filter(p -> calculateDistance(lat, lon,
                        p.getCoordinate().getPosition().getLat(),
                        p.getCoordinate().getPosition().getLon()) <= radius)
                .collect(Collectors.toList());
    }

    public List<Place> getPlaceByPlaygroundId(Integer playgroundId) {
        return placeRepository.findByPlaygroundId(playgroundId);
    }

   private double calculateDistance(double lat1, double lon1, double lat2, double lon2) {
        final int R = 6371;
        double latDistance = Math.toRadians(lat2 - lat1);
        double lonDistance = Math.toRadians(lon2 - lon1);
        double a = Math.sin(latDistance / 2) * Math.sin(latDistance / 2)
                + Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2))
                * Math.sin(lonDistance / 2) * Math.sin(lonDistance / 2);

        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        return R * c;
   }
}