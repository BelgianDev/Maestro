package be.raftdev.maestro.database.repo;

import be.raftdev.maestro.database.entity.ReleaseIdentity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ReleaseIdentityRepository extends JpaRepository<ReleaseIdentity, Long> {
    Optional<ReleaseIdentity> findByProviderId(String provider, String id);
}
