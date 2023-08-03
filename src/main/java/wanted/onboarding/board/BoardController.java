package wanted.onboarding.board;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import wanted.onboarding.board.dto.CreateBoardRequest;
import wanted.onboarding.board.dto.CreateBoardResponse;
import wanted.onboarding.board.dto.ReadBoardResponse;
import wanted.onboarding.board.service.BoardService;
import wanted.onboarding.user.service.UserService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/board")
public class BoardController {

    private final BoardService boardService;
    private final UserService userService;

    @PostMapping("")
    public ResponseEntity<CreateBoardResponse> createBoard(@RequestBody CreateBoardRequest createBoardRequest) {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String email = ((UserDetails) principal).getUsername();
        return new ResponseEntity<>(boardService.createBoard(createBoardRequest.getTitle(), createBoardRequest.getContent(), userService.getUserEntityByLogin(email)), HttpStatus.CREATED);
    }

    @GetMapping("")
    public ResponseEntity<Page<ReadBoardResponse>> readBoard(Pageable pageable) {
        return new ResponseEntity<>(boardService.readBoard(pageable), HttpStatus.OK);
    }
}
