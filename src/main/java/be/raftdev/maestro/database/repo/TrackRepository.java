package be.raftdev.maestro.database.repo;

import be.raftdev.maestro.database.entity.TrackEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface TrackRepository extends JpaRepository<TrackEntity, UUID> {
}
