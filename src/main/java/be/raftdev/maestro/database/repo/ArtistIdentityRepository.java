package be.raftdev.maestro.database.repo;

import be.raftdev.maestro.database.entity.ArtistIdentity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ArtistIdentityRepository extends JpaRepository<ArtistIdentity, Long> {
    Optional<ArtistIdentity> findByProviderId(String provider, String id);
}
