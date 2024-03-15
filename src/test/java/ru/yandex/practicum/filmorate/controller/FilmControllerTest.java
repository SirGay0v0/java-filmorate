package ru.yandex.practicum.filmorate.controller;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest
public class FilmControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Test
    public void addFilmIsOkAndWithIncorrectNameDescriptionReleaseDateDuration() throws Exception {

        //FilmCorrect
        mockMvc.perform(post("/films")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content("{\n" +
                                "  \"name\": \"nisi eiusmod\",\n" +
                                "  \"description\": \"adipisicing\",\n" +
                                "  \"releaseDate\": \"1967-03-25\",\n" +
                                "  \"duration\": \"100\"\n" +
                                "}"))
                .andExpect(content().string("{" +
                        "\"id\":1," +
                        "\"name\":\"nisi eiusmod\"," +
                        "\"description\":\"adipisicing\"," +
                        "\"releaseDate\":\"1967-03-25\"," +
                        "\"duration\":100" +
                        "}"))
                .andExpect(status().isOk());

        //IncorrectName
        mockMvc.perform(post("/films")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content("{\n" +
                                "  \"name\": \"\",\n" +
                                "  \"description\": \"adipisicing\",\n" +
                                "  \"releaseDate\": \"1967-03-25\",\n" +
                                "  \"duration\": \"100\"\n" +
                                "}"))
                .andExpect(status().isBadRequest());

        //IncorrectDescription(over 200 chars)
        mockMvc.perform(post("/films")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content("{\n" +
                                "  \"name\": \"nisi eiusmod\",\n" +
                                "  \"description\": \"adipisicingfdghdfhdhdfhdfhdfghdfghdfghdfdfdfdfdfdfdfdfdfdfdfdfdfd" +
                                "fdfdfdfddfgdfgdfgdfgdfgdfgdfgdfgdfgdfgdfgdfgdfgdfgdfgdfgdfgdfgdfgd" +
                                "fgdfgdfgdfgdfgdfgdfgdfgdfgdfgdfgdfgdfgdfgdfgdfgdfgdfgdfgdfgdfgdfgdfgdf" +
                                "gfdfdfdfghghghghghghghghghghghghghghghghghghghghghghghghgh\",\n" +
                                "  \"releaseDate\": \"1967-03-25\",\n" +
                                "  \"duration\": \"100\"\n" +
                                "}"))
                .andExpect(status().isBadRequest());

        //IncorrectReleaseDate
        mockMvc.perform(post("/films")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content("{\n" +
                                "  \"name\": \"nisi eiusmod\",\n" +
                                "  \"description\": \"adipisicing\",\n" +
                                "  \"releaseDate\": \"1895-12-27\",\n" +
                                "  \"duration\": \"100\"\n" +
                                "}"))
                .andExpect(status().isBadRequest());

        //IncorrectDuration
        mockMvc.perform(post("/films")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content("{\n" +
                                "  \"name\": \"nisi eiusmod\",\n" +
                                "  \"description\": \"adipisicing\",\n" +
                                "  \"releaseDate\": \"1900-12-27\",\n" +
                                "  \"duration\": \"0\"\n" +
                                "}"))
                .andExpect(status().isBadRequest());

    }
}
