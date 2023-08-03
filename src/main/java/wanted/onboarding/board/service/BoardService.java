package wanted.onboarding.board.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import wanted.onboarding.board.domain.Board;
import wanted.onboarding.board.dto.CreateBoardResponse;
import wanted.onboarding.board.dto.ReadBoardResponse;
import wanted.onboarding.board.repository.BoardRepository;
import wanted.onboarding.exception.CustomException;
import wanted.onboarding.exception.ErrorCode;
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

    public Page<ReadBoardResponse> readBoard(Pageable pageable) {
        return boardRepository.getPageableBoard(pageable);
    }

    public ReadBoardResponse readDetailBoard(Long boardId) {
        return ReadBoardResponse.fromEntityToDto(getBoardEntity(boardId));
    }

    private Board getBoardEntity(Long boardId) {
        return boardRepository.findById(boardId).orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_BOARD));
    }

    private void isWriter(Long boardId, User user) {
        if(!getBoardEntity(boardId).getUser().equals(user)) {
            throw new CustomException(ErrorCode.NOT_AUTHORIZED_USER);
        }
    }

    @Transactional
    public void deleteBoard(Long boardId, User user) {
        isWriter(boardId, user);
        boardRepository.delete(getBoardEntity(boardId));
    }
}
