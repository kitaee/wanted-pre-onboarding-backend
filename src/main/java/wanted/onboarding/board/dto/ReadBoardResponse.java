package wanted.onboarding.board.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import wanted.onboarding.board.domain.Board;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
@Builder
public class ReadBoardResponse {
    private Long boardId;
    private String title;
    private String content;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public static ReadBoardResponse fromEntityToDto(Board board) {
        return ReadBoardResponse.builder()
                .boardId(board.getBoardId())
                .title(board.getTitle())
                .content(board.getContent())
                .createdAt(board.getCreatedAt())
                .updatedAt(board.getUpdatedAt())
                .build();
    }
}
