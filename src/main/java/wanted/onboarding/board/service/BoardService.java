package wanted.onboarding.board.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import wanted.onboarding.board.domain.Board;
import wanted.onboarding.board.dto.CreateBoardResponse;
import wanted.onboarding.board.repository.BoardRepository;
import wanted.onboarding.user.domain.User;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class BoardService {

    private final BoardRepository boardRepository;

    @Transactional
    public CreateBoardResponse createBoard(String title, String content, User user) {
        Board board = Board.builder()
                .title(title)
                .content(content)
                .user(user)
                .build();

        boardRepository.save(board);

        return CreateBoardResponse.builder().boardId(board.getBoardId()).build();
    }
}
