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
public class FilmControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Test
    public void showFilmsTest() throws Exception {

        String filmJson = "{\"name\": \"Film Updated\"," +
                " \"releaseDate\": \"1989-04-17\"," +
                " \"description\": \"New film update decription\"," +
                " \"duration\": 190," +
                " \"rate\": 4," +
                " \"mpa\": { " +
                "\"id\": 5}," +
                "\"genres\": []}\"";
        mockMvc.perform(post("/films").contentType(MediaType.APPLICATION_JSON).content(filmJson));
        mockMvc.perform(get("/films"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("\"id\":1")));

        mockMvc.perform(delete("/films/1"));
    }

    @Test
    public void showFilmByIdTest() throws Exception {
        String filmJson = "{\"name\": \"Film Updated\"," +
                " \"releaseDate\": \"1989-04-17\"," +
                " \"description\": \"New film update decription\"," +
                " \"duration\": 190," +
                " \"rate\": 4," +
                " \"mpa\": { " +
                "\"id\": 5}," +
                "\"genres\": []}\"";
        mockMvc.perform(post("/films").contentType(MediaType.APPLICATION_JSON).content(filmJson));
        mockMvc.perform(get("/films/6"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("\"id\":6")));

        mockMvc.perform(delete("/films/6"));
    }

    @Test
    public void showFilmByIdWrongTest() throws Exception {
        mockMvc.perform(get("/films/-1"))
                .andDo(print())
                .andExpect(status().is4xxClientError())
                .andExpect(content().string(containsString("Cannot search film by -1")));

    }

    @Test
    public void addFilmTest() throws Exception {
        String filmJson = "{\"name\": \"Film Updated\"," +
                " \"releaseDate\": \"1989-04-17\"," +
                " \"description\": \"New film update decription\"," +
                " \"duration\": 190," +
                " \"rate\": 4," +
                " \"mpa\": { " +
                "\"id\": 5}," +
                "\"genres\": []}\"";
        mockMvc.perform(post("/films").contentType(MediaType.APPLICATION_JSON).content(filmJson))
                .andExpect(status().isOk());


        mockMvc.perform(delete("/films/2"));
    }

    @Test
    public void addFilmFailNameTest() throws Exception {
        String filmJson = "{\"name\": \"\"," +
                " \"releaseDate\": \"1989-04-17\"," +
                " \"description\": \"New film update decription\"," +
                " \"duration\": 190," +
                " \"rate\": 4," +
                " \"mpa\": { " +
                "\"id\": 5}," +
                "\"genres\": []}\"";
        mockMvc.perform(post("/films").contentType(MediaType.APPLICATION_JSON).content(filmJson))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void addFilmFailLDescriptionMin1Test() throws Exception {
        String filmJson = "{\"name\": \"Film Updated\"," +
                " \"releaseDate\": \"1989-04-17\"," +
                " \"description\": \"\"," +
                " \"duration\": 190," +
                " \"rate\": 4," +
                " \"mpa\": { " +
                "\"id\": 5}," +
                "\"genres\": []}\"";

        mockMvc.perform(post("/films")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(filmJson))
                .andExpect(status().is4xxClientError());
    }


    @Test
    public void addFilmFailReleaseDateTest() throws Exception {
        String filmJson = "{\"name\": \"Film Updated\"," +
                " \"releaseDate\": \"1189-04-17\"," +
                " \"description\": \"New film update decription\"," +
                " \"duration\": 190," +
                " \"rate\": 4," +
                " \"mpa\": { " +
                "\"id\": 5}," +
                "\"genres\": []}\"";

        mockMvc.perform(post("/films")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(filmJson))
                .andExpect(status().is4xxClientError());
    }

    @Test
    public void addFilmFailDurationTest() throws Exception {
        String filmJson = "{\"name\": \"Film Updated\"," +
                " \"releaseDate\": \"1989-04-17\"," +
                " \"description\": \"New film update decription\"," +
                " \"duration\": -1," +
                " \"rate\": 4," +
                " \"mpa\": { " +
                "\"id\": 5}," +
                "\"genres\": []}\"";

        mockMvc.perform(post("/films")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(filmJson))
                .andExpect(status().is4xxClientError());
    }

    @Test
    public void updateFilmTest() throws Exception {
        String filmJson = "{\"name\": \"Film Updated\"," +
                " \"releaseDate\": \"1989-04-17\"," +
                " \"description\": \"New film update decription\"," +
                " \"duration\": 190," +
                " \"rate\": 4," +
                " \"mpa\": { " +
                "\"id\": 5}," +
                "\"genres\": []}\"";
        mockMvc.perform(post("/films").contentType(MediaType.APPLICATION_JSON).content(filmJson));
        String filmJson2 = "{\"id\": 4, \"name\": \"Film Updated\"," +
                " \"releaseDate\": \"1989-04-17\"," +
                " \"description\": \"New film update decription\"," +
                " \"duration\": 200," +
                " \"rate\": 7," +
                " \"mpa\": { " +
                "\"id\": 5}," +
                "\"genres\": []}\"";
        mockMvc.perform(put("/films").contentType(MediaType.APPLICATION_JSON).content(filmJson2))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("\"duration\":200")));


        mockMvc.perform(delete("/films/4"));
    }


    //______________________________________________Test like______________________________________

    @Test
    public void addLikeTest() throws Exception {
        String filmJson = "{\"name\": \"Film Updated\"," +
                " \"releaseDate\": \"1989-04-17\"," +
                " \"description\": \"New film update decription\"," +
                " \"duration\": -1," +
                " \"rate\": 4," +
                " \"mpa\": { " +
                "\"id\": 5}," +
                "\"genres\": []}\"";

        mockMvc.perform(post("/films").contentType(MediaType.APPLICATION_JSON).content(filmJson))
                .andExpect(status().isBadRequest());

        String userJson = "{\"login\": \"dolore\"," +
                " \"name\": \"Nick Name\"," +
                " \"email\": \"mailmailru\"," +
                "  \"birthday\": \"1946-08-20\"}";

        mockMvc.perform(post("/users").contentType(MediaType.APPLICATION_JSON)
                        .content(userJson))
                .andExpect(status().isBadRequest());

    }

    @Test
    public void deleteLikeTest() throws Exception {
        String filmJson = "{\"name\": \"Film Updated\"," +
                " \"releaseDate\": \"1989-04-17\"," +
                " \"description\": \"New film update decription\"," +
                " \"duration\": 190," +
                " \"rate\": 4," +
                " \"mpa\": { " +
                "\"id\": 5}," +
                "\"genres\": []}\"";
        mockMvc.perform(post("/films").contentType(MediaType.APPLICATION_JSON).content(filmJson))
                .andExpect(status().isOk());

        String userJson = "{\"login\": \"dolore\"," +
                " \"name\": \"Nick Name\"," +
                " \"email\": \"mail@mail.ru\"," +
                "  \"birthday\": \"1946-08-20\"}";

        mockMvc.perform(post("/users").contentType(MediaType.APPLICATION_JSON)
                        .content(userJson))
                .andExpect(status().isOk()).andExpect(content().string(containsString("\"id\":1")));

        mockMvc.perform(put("/films/3/like/1"))
                .andExpect(status().isOk())
                .andExpect(content().string(""));
        mockMvc.perform(delete("/films/3/like/1"))
                .andExpect(status().isOk())
                .andExpect(content().string(""));

        mockMvc.perform(delete("/films/3"));
        mockMvc.perform(delete("/users/1"));
    }

    @Test
    public void showPopularFilmsDefaultTest() throws Exception {
        String filmJson = "{\"name\": \"Film Updated\"," +
                " \"releaseDate\": \"1989-04-17\"," +
                " \"description\": \"New film update decription\"," +
                " \"duration\": 190," +
                " \"rate\": 4," +
                " \"mpa\": { " +
                "\"id\": 5}," +
                "\"genres\": []}\"";
        mockMvc.perform(post("/films").contentType(MediaType.APPLICATION_JSON).content(filmJson))
                .andExpect(status().isOk());

        String userJson = "{\"login\": \"dolore\"," +
                " \"name\": \"Nick Name\"," +
                " \"email\": \"mail@mail.ru\"," +
                "  \"birthday\": \"1946-08-20\"}";

        mockMvc.perform(post("/users").contentType(MediaType.APPLICATION_JSON)
                        .content(userJson))
                .andExpect(status().isOk()).andExpect(content().string(containsString("\"id\":2")));

        mockMvc.perform(put("/films/5/like/2"))
                .andExpect(status().isOk())
                .andExpect(content().string(""));
        mockMvc.perform(delete("/films/5/like/2"))
                .andExpect(status().isOk())
                .andExpect(content().string(""));

        mockMvc.perform(delete("/films/5"));
        mockMvc.perform(delete("/users/2"));
    }

    @Test
    public void showPopular5FilmsTest() throws Exception {
        String filmJson = "{\"name\": \"Film Updated\"," +
                " \"releaseDate\": \"1989-04-17\"," +
                " \"description\": \"New film update decription\"," +
                " \"duration\": 190," +
                " \"rate\": 4," +
                " \"mpa\": { " +
                "\"id\": 5}," +
                "\"genres\": []}\"";
        mockMvc.perform(post("/films").contentType(MediaType.APPLICATION_JSON).content(filmJson))
                .andExpect(status().isOk());

        String userJson = "{\"login\": \"dolore\"," +
                " \"name\": \"Nick Name\"," +
                " \"email\": \"mail@mail.ru\"," +
                "  \"birthday\": \"1946-08-20\"}";

        mockMvc.perform(post("/users").contentType(MediaType.APPLICATION_JSON).content(userJson))
                .andExpect(status().isOk());

        mockMvc.perform(put("/films/7/like/3"))
                .andExpect(status().isOk())
                .andExpect(content().string(""));

        mockMvc.perform(get("/films/popular?count={count}", 5))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("\"Film Updated\"")));

        mockMvc.perform(delete("/films/7/like/3"))
                .andExpect(status().isOk())
                .andExpect(content().string(""));

        mockMvc.perform(delete("/films/7"));
        mockMvc.perform(delete("/users/3"));
    }

}