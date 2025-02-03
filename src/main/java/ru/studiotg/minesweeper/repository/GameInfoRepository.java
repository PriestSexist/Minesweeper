package ru.studiotg.minesweeper.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.studiotg.minesweeper.model.GameInfo;

import java.util.UUID;

@Repository
public interface GameInfoRepository extends JpaRepository <GameInfo, UUID> {
}
