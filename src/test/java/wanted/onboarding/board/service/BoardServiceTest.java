package wanted.onboarding.board.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class BoardServiceTest {

    private final MockMvc mockMvc;
    private final ObjectMapper objectMapper;

    private static final String loginRequestBody = "username=dlrlxo9999@naver.com&password=12345678";
    private static final String invalidLoginRequestBody = "username=dlrlxo999@naver.com&password=@@aa0332601";

    private String accessToken;
    private String invalidAccessToken;

    @Autowired
    BoardServiceTest(MockMvc mockMvc, ObjectMapper objectMapper) {
        this.mockMvc = mockMvc;
        this.objectMapper = objectMapper;
    }

    @BeforeEach
    public void 테스트_전_로그인_세팅() throws Exception {

        ResultActions loginResult = mockMvc.perform(post("/login")
                                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                                        .content(loginRequestBody));

        accessToken = loginResult.andReturn().getResponse().getHeader("Authorization");
        if(accessToken != null) {
            accessToken = accessToken.substring(7);
        }

        ResultActions anotherLoginResult = mockMvc.perform(post("/login")
                                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                                        .content(invalidLoginRequestBody));

        invalidAccessToken = anotherLoginResult.andReturn().getResponse().getHeader("Authorization");
        if(invalidAccessToken != null) {
            invalidAccessToken = invalidAccessToken.substring(7);
        }
    }

    @Test
    void 게시글_작성_실패_403() throws Exception {
        String createBoardRequest = "{\n" +
                "    \"title\": \"테스트 게시글 제목5\",\n" +
                "    \"content\": \"테스트 게시글 내용5\"\n" +
                "}";

        mockMvc.perform(post("/api/board")
//                .header("Authorization", "Bearer " + accessToken)
                .contentType(MediaType.APPLICATION_JSON)
                .content(createBoardRequest))
                .andExpect(status().isForbidden());
    }

    @Test
    void 게시글_작성_성공() throws Exception {
        String createBoardRequest = "{\n" +
                "    \"title\": \"테스트 게시글 제목5\",\n" +
                "    \"content\": \"테스트 게시글 내용5\"\n" +
                "}";

        mockMvc.perform(post("/api/board")
                .header("Authorization", "Bearer " + accessToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(createBoardRequest))
                .andExpect(status().isCreated());
    }

    @Test
    void 게시글_수정_실패_1() throws Exception {
        String updateBoardRequest = "{\n" +
                "    \"title\": \"새로운 게시글 제목~!\",\n" +
                "    \"content\": \"새로운 게시글 내용\"\n" +
                "}";

        String updateBoardResponse = mockMvc.perform(patch("/api/board/7")
                        .header("Authorization", "Bearer " + invalidAccessToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(updateBoardRequest))
                .andReturn()
                .getResponse()
                .getContentAsString();

        String code = objectMapper.readTree(updateBoardResponse)
                .get("code")
                .asText();

        Assertions.assertEquals("NOT_AUTHORIZED_USER", code);
    }

    @Test
    void 게시글_수정_실패_2() throws Exception {
        String updateBoardRequest = "{\n" +
                "    \"title\": \"새로운 게시글 제목~!\",\n" +
                "    \"content\": \"새로운 게시글 내용\"\n" +
                "}";

        String updateBoardResponse = mockMvc.perform(patch("/api/board/14")
                        .header("Authorization", "Bearer " + accessToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(updateBoardRequest))
                .andReturn()
                .getResponse()
                .getContentAsString();

        String code = objectMapper.readTree(updateBoardResponse)
                .get("code")
                .asText();

        Assertions.assertEquals("NOT_FOUND_BOARD", code);
    }

    @Test
    void 게시글_수정_실패_3() throws Exception {
        String updateBoardRequest = "{\n" +
                "    \"title\": \"새로운 게시글 제목~!\",\n" +
                "    \"content\": \"새로운 게시글 내용\"\n" +
                "}";

        mockMvc.perform(patch("/api/board/14")
//                        .header("Authorization", "Bearer " + accessToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(updateBoardRequest))
                .andExpect(status().isForbidden());
    }

    @Test
    void 게시글_수정_성공() throws Exception {
        String updateBoardRequest = "{\n" +
                "    \"title\": \"새로운 게시글 제목~!\",\n" +
                "    \"content\": \"새로운 게시글 내용\"\n" +
                "}";

        mockMvc.perform(patch("/api/board/7")
                        .header("Authorization", "Bearer " + accessToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(updateBoardRequest));

        String updateBoardResponse = mockMvc.perform(get("/api/board/7")
                        .header("Authorization", "Bearer " + accessToken)
                        .contentType(MediaType.APPLICATION_JSON))
                .andReturn()
                .getResponse()
                .getContentAsString();

        String title = objectMapper.readTree(updateBoardResponse)
                .get("title")
                .asText();

        String content = objectMapper.readTree(updateBoardResponse)
                .get("content")
                .asText();

        //title
        Assertions.assertEquals("새로운 게시글 제목~!", title);

        //content
        Assertions.assertEquals("새로운 게시글 내용", content);
    }

    @Test
    void 게시글_삭제_실패_1() throws Exception {;

        String updateBoardResponse = mockMvc.perform(delete("/api/board/7")
                        .header("Authorization", "Bearer " + invalidAccessToken)
                        .contentType(MediaType.APPLICATION_JSON))
                .andReturn()
                .getResponse()
                .getContentAsString();

        String code = objectMapper.readTree(updateBoardResponse)
                .get("code")
                .asText();

        Assertions.assertEquals("NOT_AUTHORIZED_USER", code);
    }

    @Test
    void 게시글_삭제_실패_2() throws Exception {;

        String updateBoardResponse = mockMvc.perform(delete("/api/board/14")
                        .header("Authorization", "Bearer " + accessToken)
                        .contentType(MediaType.APPLICATION_JSON))
                .andReturn()
                .getResponse()
                .getContentAsString();

        String code = objectMapper.readTree(updateBoardResponse)
                .get("code")
                .asText();

        Assertions.assertEquals("NOT_FOUND_BOARD", code);
    }

    @Test
    void 게시글_삭제_실패_3() throws Exception {

        mockMvc.perform(delete("/api/board/7")
//                        .header("Authorization", "Bearer " + accessToken)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isForbidden());
    }

    @Test
    void 게시글_삭제_성공() throws Exception {
        mockMvc.perform(delete("/api/board/7")
                        .header("Authorization", "Bearer " + accessToken)
                        .contentType(MediaType.APPLICATION_JSON))
                .andReturn()
                .getResponse()
                .getContentAsString();

        String afterDeleteResponse = mockMvc.perform(get("/api/board/7")
                        .header("Authorization", "Bearer " + accessToken)
                        .contentType(MediaType.APPLICATION_JSON))
                .andReturn()
                .getResponse()
                .getContentAsString();

        String code = objectMapper.readTree(afterDeleteResponse)
                .get("code")
                .asText();

        Assertions.assertEquals("NOT_FOUND_BOARD", code);
    }
}