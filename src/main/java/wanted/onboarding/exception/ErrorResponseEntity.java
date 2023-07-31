package wanted.onboarding.exception;

import lombok.Builder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@Builder
public class ErrorResponseEntity {
    private int status;
    private String code;
    private String message;

    public static ResponseEntity<ErrorResponseEntity> toResponseEntity(ErrorCode e){
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(ErrorResponseEntity.builder()
                        .status(e.getCode())
                        .code(e.name())
                        .message(e.getMessage())
                        .build()
                );
    }
}
