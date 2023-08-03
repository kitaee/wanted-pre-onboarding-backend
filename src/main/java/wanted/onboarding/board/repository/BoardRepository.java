package wanted.onboarding.board.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import wanted.onboarding.board.domain.Board;
import wanted.onboarding.board.dto.ReadBoardResponse;

@Repository
public interface BoardRepository extends JpaRepository<Board, Long> {

    @Query("SELECT NEW wanted.onboarding.board.dto.ReadBoardResponse(b.boardId, b.title, b.content, b.createdAt, b.updatedAt) "
            + "FROM Board b ORDER BY b.createdAt")
    Page<ReadBoardResponse> getPageableBoard(Pageable pageable);
}
