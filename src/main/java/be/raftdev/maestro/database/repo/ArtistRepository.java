package be.raftdev.maestro.database.repo;

import be.raftdev.maestro.database.entity.ArtistEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface ArtistRepository extends JpaRepository<ArtistEntity, UUID> {
}
