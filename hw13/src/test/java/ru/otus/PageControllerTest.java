package ru.otus;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import ru.otus.controller.PageController;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@WebMvcTest(PageController.class)
public class PageControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @WithMockUser(
            username = "hello",
            authorities = {"role_user"}
    )
    @Test
    public void testAuthenticatedOnUser() throws Exception {
        mockMvc.perform(get("/edit"))
                .andExpect(status().isOk());
    }

    @WithAnonymousUser
    @Test
    public void testNotAuthenticatedOnFalseUser() throws Exception {
        mockMvc.perform(get("/edit"))
                .andExpect(status().isUnauthorized());
    }

    @WithMockUser(
            username = "hello",
            authorities = {"role_user"}
    )
    @Test
    public void testUserWatchBookList() throws Exception {
        mockMvc.perform(get("/"))
                .andExpect(status().isOk());
    }

}
