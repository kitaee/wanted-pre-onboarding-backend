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

    /**
     *  Date : 2023-08-02
     *  Description: 게시글 작성 메소드
     * */
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

    /**
     *  Date : 2023-08-02
     *  Description: 게시글 조회(페이징) 메소드
     * */
    public Page<ReadBoardResponse> readBoard(Pageable pageable) {
        return boardRepository.getPageableBoard(pageable);
    }

    /**
     *  Date : 2023-08-02
     *  Description: 상세 게시글 조회 메소드
     * */
    public ReadBoardResponse readDetailBoard(Long boardId) {
        return ReadBoardResponse.fromEntityToDto(getBoardEntity(boardId));
    }

    /**
     *  Date : 2023-08-02
     *  Description: 게시글 찾기(엔티티) 메소드
     * */
    private Board getBoardEntity(Long boardId) {
        return boardRepository.findById(boardId).orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_BOARD));
    }

    /**
     *  Date : 2023-08-02
     *  Description: 게시글의 작성자가 맞는지 판단하는 메소드
     * */
    private void isWriter(Long boardId, User user) {
        if(!getBoardEntity(boardId).getUser().equals(user)) {
            throw new CustomException(ErrorCode.NOT_AUTHORIZED_USER);
        }
    }

    /**
     *  Date : 2023-08-02
     *  Description: 게시글 삭제 메소드
     * */
    @Transactional
    public void deleteBoard(Long boardId, User user) {
        isWriter(boardId, user);
        boardRepository.delete(getBoardEntity(boardId));
    }

    /**
     *  Date : 2023-08-02
     *  Description: 게시글 수정 메소드
     * */
    @Transactional
    public ReadBoardResponse updateBoard(Long boardId, User user, String title, String content) {
        isWriter(boardId, user);
        return ReadBoardResponse.fromEntityToDto(getBoardEntity(boardId).updateBoard(title, content));
    }
}
