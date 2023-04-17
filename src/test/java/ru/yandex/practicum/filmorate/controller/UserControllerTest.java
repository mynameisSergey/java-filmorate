package ru.yandex.practicum.filmorate.controller;

import lombok.RequiredArgsConstructor;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@ComponentScan(basePackages = {"ru.yandex.practicum.filmorate"})
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class UserControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Test
    public void addUsersTest() throws Exception {
        String userJson = "{\"login\": \"dolore\"," +
                " \"name\": \"Nick Name\"," +
                " \"email\": \"mail@mail.ru\"," +
                "  \"birthday\": \"1946-08-20\"}";

        mockMvc.perform(post("/users").contentType(MediaType.APPLICATION_JSON)
                        .content(userJson))
                .andExpect(status().isOk()).andExpect(content().string(containsString("\"id\":1")));

        mockMvc.perform(delete("/users/1"));
    }

    @Test
    public void getUserTest() throws Exception {
        String userJson = "{\"login\": \"dolore\"," +
                " \"name\": \"Nick Name\"," +
                " \"email\": \"mail@mail.ru\"," +
                "  \"birthday\": \"1946-08-20\"}";

        mockMvc.perform(post("/users").contentType(MediaType.APPLICATION_JSON).content(userJson));
        mockMvc.perform(get("/users/1"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("\"id\":1")));

        mockMvc.perform(delete("/users/1"));

    }

    @Test
    public void getAllUsersTest() throws Exception {
        String userJson = "{\"login\": \"dolore\"," +
                " \"name\": \"Nick Name\"," +
                " \"email\": \"mail@mail.ru\"," +
                "  \"birthday\": \"1946-08-20\"}";

        String userJson2 = "{\"login\": \"user2\"," +
                " \"name\": \"user2 Name\"," +
                " \"email\": \"user2@mail.ru\"," +
                "  \"birthday\": \"1946-08-20\"}";

        mockMvc.perform(post("/users").contentType(MediaType.APPLICATION_JSON).content(userJson));
        mockMvc.perform(post("/users").contentType(MediaType.APPLICATION_JSON).content(userJson2));


        mockMvc.perform(get("/users"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("\"id\":1")))
                .andExpect(content().string(containsString("\"id\":2")));

        mockMvc.perform(delete("/users/3"));
        mockMvc.perform(delete("/users/4"));

    }


    @Test
    public void addUsersTestFailEmail() throws Exception {
        String userJson = "{\"login\": \"dolore\"," +
                " \"name\": \"Nick Name\"," +
                " \"email\": \"mailmailru\"," +
                "  \"birthday\": \"1946-08-20\"}";

        mockMvc.perform(post("/users").contentType(MediaType.APPLICATION_JSON)
                        .content(userJson))
                .andExpect(status().is4xxClientError());

    }

    @Test
    public void addUsersTestFailLogin() throws Exception {
        String userJson = "{\"login\": \"\"," +
                " \"name\": \"Nick Name\"," +
                " \"email\": \"mail@mail.ru\"," +
                "  \"birthday\": \"1946-08-20\"}";
        mockMvc.perform(post("/users").contentType(MediaType.APPLICATION_JSON)
                        .content(userJson))
                .andExpect(status().is4xxClientError());

    }

    @Test
    public void addUsersTestFailBirthday() throws Exception {
        String userJson = "{\"login\": \"dolore\"," +
                " \"name\": \"Nick Name\"," +
                " \"email\": \"mail@mail.ru\"," +
                "  \"birthday\": \"2046-08-20\"}";
        mockMvc.perform(post("/users").contentType(MediaType.APPLICATION_JSON)
                        .content(userJson))
                .andExpect(status().is4xxClientError());

    }


    @Test
    public void updateUser() throws Exception {
        String userJson = "{\"login\": \"dolore\"," +
                " \"name\": \"\"," +
                " \"email\": \"mail@mail.ru\"," +
                "  \"birthday\": \"1946-08-20\"}";
        mockMvc.perform(post("/users").contentType(MediaType.APPLICATION_JSON)
                .content(userJson));

        String userJsonUpdate = "{\"id\": 7,\"login\": \"update_user\"," +
                " \"name\": \"update_user\"," +
                " \"email\": \"update_user@mail.ru\"," +
                "  \"birthday\": \"1986-08-20\"}";
        mockMvc.perform(put("/users").contentType(MediaType.APPLICATION_JSON)
                        .content(userJsonUpdate))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("\"email\":\"update_user@mail.ru\"," +
                        "\"login\":\"update_user\"," +
                        "\"name\":\"update_user\"," +
                        "\"birthday\":\"1986-08-20\"")));

        mockMvc.perform(delete("/users/7"));
    }

    @Test
    public void deleteUserTest() throws Exception {
        String userJson = "{\"login\": \"dolore\"," +
                " \"name\": \"Nick Name\"," +
                " \"email\": \"mail@mail.ru\"," +
                "  \"birthday\": \"1946-08-20\"}";

        mockMvc.perform(post("/users").contentType(MediaType.APPLICATION_JSON)
                        .content(userJson))
                .andExpect(status().isOk()).andExpect(content().string(containsString("\"id\":1")));

        mockMvc.perform(delete("/users/1")).andExpect(status().isOk());

        mockMvc.perform(get("/users/1"))
                .andDo(print())
                .andExpect(status().isNotFound())
                .andExpect(content().string(containsString("\"error\"")));
    }
    //------------------------------------------test Friends-----------------------------------------------

    @Test
    public void addFriendTest() throws Exception {
        String userJson = "{\"login\": \"dolore\"," +
                " \"name\": \"Nick Name\"," +
                " \"email\": \"mail@mail.ru\"," +
                "  \"birthday\": \"1946-08-20\"}";

        String userJson2 = "{\"login\": \"user2\"," +
                " \"name\": \"user2 Name\"," +
                " \"email\": \"user2@mail.ru\"," +
                "  \"birthday\": \"1946-08-20\"}";

        mockMvc.perform(post("/users").contentType(MediaType.APPLICATION_JSON).content(userJson));
        mockMvc.perform(post("/users").contentType(MediaType.APPLICATION_JSON).content(userJson2));

        mockMvc.perform(put("/users/1/friends/2"))
                .andExpect(status().isOk());


        mockMvc.perform(get("/users/{id}/friends", 1))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("\"id\":2")));

        mockMvc.perform(get("/users/{id}/friends", 2))
                .andExpect(status().isOk())
                .andExpect(content().string("[]"));

        mockMvc.perform(put("/users/2/friends/1"))
                .andExpect(status().isOk());


        mockMvc.perform(delete("/users/1/friends/2")).andExpect(status().isOk());
        mockMvc.perform(delete("/users/2/friends/1")).andExpect(status().isOk());
        mockMvc.perform(delete("/users/1")).andExpect(status().isOk());
        mockMvc.perform(delete("/users/2")).andExpect(status().isOk());


    }

    @Test
    public void addFriendIncorrectIdTest() throws Exception {


        mockMvc.perform(put("/users/{id}/friends/{friendId}", 1, 111))
                .andExpect(status().is4xxClientError());

    }

    @Test
    public void addFriendIncorrectIdOwnHimselfTest() throws Exception {


        mockMvc.perform(put("/users/{id}/friends/{friendId}", 1, 1))
                .andExpect(status().is4xxClientError())
                .andExpect(content().string(containsString("The user cannot be his own friend")));

    }

    @Test
    public void deleteFriendTest() throws Exception {
        String userJson = "{\"login\": \"dolore\"," +
                " \"name\": \"Nick Name\"," +
                " \"email\": \"mail@mail.ru\"," +
                "  \"birthday\": \"1946-08-20\"}";

        String userJson2 = "{\"login\": \"user2\"," +
                " \"name\": \"user2 Name\"," +
                " \"email\": \"user2@mail.ru\"," +
                "  \"birthday\": \"1946-08-20\"}";

        mockMvc.perform(post("/users").contentType(MediaType.APPLICATION_JSON).content(userJson));
        mockMvc.perform(post("/users").contentType(MediaType.APPLICATION_JSON).content(userJson2));

        mockMvc.perform(put("/users/9/friends/10"))
                .andExpect(status().isOk());


        mockMvc.perform(get("/users/{id}/friends", 9))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("\"id\":10")));

        mockMvc.perform(get("/users/{id}/friends", 10))
                .andExpect(status().isOk())
                .andExpect(content().string("[]"));

        mockMvc.perform(put("/users/10/friends/9"))
                .andExpect(status().isOk());


        mockMvc.perform(delete("/users/9/friends/10")).andExpect(status().isOk());
        mockMvc.perform(delete("/users/10/friends/9")).andExpect(status().isOk());
        mockMvc.perform(delete("/users/9")).andExpect(status().isOk());
        mockMvc.perform(delete("/users/10")).andExpect(status().isOk());
    }

    @Test
    public void showFriendsTest() throws Exception {

        String userJson = "{\"login\": \"dolore\"," +
                " \"name\": \"Nick Name\"," +
                " \"email\": \"mail@mail.ru\"," +
                "  \"birthday\": \"1946-08-20\"}";

        String userJson2 = "{\"login\": \"user2\"," +
                " \"name\": \"user2 Name\"," +
                " \"email\": \"user2@mail.ru\"," +
                "  \"birthday\": \"1946-08-20\"}";

        mockMvc.perform(post("/users").contentType(MediaType.APPLICATION_JSON).content(userJson));
        mockMvc.perform(post("/users").contentType(MediaType.APPLICATION_JSON).content(userJson2));

        mockMvc.perform(put("/users/5/friends/6"))
                .andExpect(status().isOk());


        mockMvc.perform(get("/users/5/friends"))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("\"id\":6")));

        mockMvc.perform(get("/users/6/friends"))
                .andExpect(status().isOk())
                .andExpect(content().string("[]"));

        mockMvc.perform(put("/users/6/friends/5"))
                .andExpect(status().isOk());


        mockMvc.perform(delete("/users/5/friends/6")).andExpect(status().isOk());
        mockMvc.perform(delete("/users/6/friends/5")).andExpect(status().isOk());
        mockMvc.perform(delete("/users/5")).andExpect(status().isOk());
        mockMvc.perform(delete("/users/6")).andExpect(status().isOk());
    }
}