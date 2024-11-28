package org.example.laboration2backend.playground;

import org.example.laboration2backend.entity.Playground;
import org.geolatte.geom.G2D;
import org.geolatte.geom.Geometries;
import org.springframework.stereotype.Service;

import java.util.List;

import static org.geolatte.geom.crs.CoordinateReferenceSystems.WGS84;

@Service
public class PlaygroundService {
    private final PlaygroundRepository playgroundRepository;

    public PlaygroundService(PlaygroundRepository playgroundRepository) {
        this.playgroundRepository = playgroundRepository;
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
}