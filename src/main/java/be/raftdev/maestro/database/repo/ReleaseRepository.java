package be.raftdev.maestro.database.repo;

import be.raftdev.maestro.database.entity.ReleaseEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface ReleaseRepository extends JpaRepository<ReleaseEntity, UUID> {
}
