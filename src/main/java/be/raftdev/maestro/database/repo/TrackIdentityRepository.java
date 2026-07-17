package be.raftdev.maestro.database.repo;

import be.raftdev.maestro.database.entity.TrackIdentity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TrackIdentityRepository extends JpaRepository<TrackIdentity, Long> {
    Optional<TrackIdentity> findByProviderId(String provider, String id);
}
