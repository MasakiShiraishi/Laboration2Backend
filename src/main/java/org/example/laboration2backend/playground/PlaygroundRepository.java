package org.example.laboration2backend.playground;

import org.example.laboration2backend.entity.Playground;
import org.springframework.data.repository.ListCrudRepository;

public interface PlaygroundRepository extends ListCrudRepository<Playground, Integer> {
}
