package org.example.laboration2backend.playground;

import org.example.laboration2backend.entity.Place;
import org.example.laboration2backend.entity.Playground;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;
import java.util.Map;

@RestController
@CrossOrigin
public class PlayGroundController {

    private final PlaygroundService playgroundService;

    public PlayGroundController(PlaygroundService playgroundService) {
        this.playgroundService = playgroundService;
    }

    @GetMapping("/playgrounds")
    public List<APlace> allPlaygrounds(@RequestParam(required = false) Map<String, String> queryParams) {
        return playgroundService.all().stream()
                .map(p ->
                        new APlace(
                                p.getCoordinate().getPosition().getLat(),
                                p.getCoordinate().getPosition().getLon())
                ).toList();
    }

    @PostMapping("/playgrounds")
    public ResponseEntity<Void> addPlayground(@RequestBody Location location) {
        Playground p = playgroundService.createNewPlayGround(location.lat(), location.lon());
        return ResponseEntity.created(URI.create("/playgrounds/" + p.getId())).build();
    }
}
