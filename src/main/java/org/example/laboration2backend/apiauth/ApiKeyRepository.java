package org.example.laboration2backend.apiauth;

import org.example.laboration2backend.entity.ApiKey;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.ListCrudRepository;

import java.util.List;
import java.util.Optional;

public interface ApiKeyRepository extends ListCrudRepository<ApiKey, Integer> {

//    //JPQL language, Will be converted to database specific query in runtime
//    @Query("""
//            SELECT apiKey FROM ApiKey apiKey
//                        WHERE apiKey.name=:name
//            """)
//    List<ApiKey> findApiKeysByName(String name);
//
//    //Native query, written as sql and for a specific database
//    @Query(value = "SELECT * FROM api_key where api_key =:apiKey", nativeQuery = true)
//    Optional<ApiKey> findByApiKey(String apiKey);
}
