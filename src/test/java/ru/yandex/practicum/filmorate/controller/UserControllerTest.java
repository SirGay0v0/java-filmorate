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
public class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void addUserIsOkAndWithIncorrectEmailLoginNameLocalDate() throws Exception {

        //UserCorrect
        mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content("{\n" +
                                "  \"login\": \"dolore\",\n" +
                                "  \"name\": \"Nick Name\",\n" +
                                "  \"email\": \"mail@mail.ru\",\n" +
                                "  \"birthday\": \"1946-08-20\"\n" +
                                "}"))
                .andExpect(content().string("{" +
                        "\"id\":1," +
                        "\"email\":\"mail@mail.ru\"," +
                        "\"login\":\"dolore\"," +
                        "\"name\":\"Nick Name\"," +
                        "\"birthday\":\"1946-08-20\"" +
                        "}"))
                .andExpect(status().isOk());

        //IncorrectMailWithout@
        mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content("{\n" +
                                "  \"login\": \"dolore\",\n" +
                                "  \"name\": \"Nick Name\",\n" +
                                "  \"email\": \"mailmail.ru\",\n" +
                                "  \"birthday\": \"1946-08-20\"\n" +
                                "}"))
                .andExpect(status().isBadRequest());

        //IncorrectMailIsBlank
        mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content("{\n" +
                                "  \"login\": \"dolore\",\n" +
                                "  \"name\": \"Nick Name\",\n" +
                                "  \"birthday\": \"1946-08-20\"\n" +
                                "}"))
                .andExpect(status().isBadRequest());

        //IncorrectLoginIsBlank
        mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content("{\n" +
                                "  \"login\": \"\",\n" +
                                "  \"name\": \"Nick Name\",\n" +
                                "  \"email\": \"mail@mail.ru\",\n" +
                                "  \"birthday\": \"1946-08-20\"\n" +
                                "}"))
                .andExpect(status().isBadRequest());

        //IncorrectLoginWithSpace
        mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content("{\n" +
                                "  \"login\": \"do lore\",\n" +
                                "  \"name\": \"Nick Name\",\n" +
                                "  \"email\": \"mail@mail.ru\",\n" +
                                "  \"birthday\": \"1946-08-20\"\n" +
                                "}"))
                .andExpect(status().isBadRequest());

        //IncorrectName
        mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content("{\n" +
                                "  \"login\": \"dolore\",\n" +
                                "  \"email\": \"mail@mail.ru\",\n" +
                                "  \"birthday\": \"1946-08-20\"\n" +
                                "}"))
                .andExpect(content().string("{" +
                        "\"id\":2," +
                        "\"email\":\"mail@mail.ru\"," +
                        "\"login\":\"dolore\"," +
                        "\"name\":\"dolore\"," +
                        "\"birthday\":\"1946-08-20\"" +
                        "}"))
                .andExpect(status().isOk());

        //IncorrectLocalDate
        mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content("{\n" +
                                "  \"login\": \"dolore\",\n" +
                                "  \"name\": \"Nick Name\",\n" +
                                "  \"email\": \"mail@mail.ru\",\n" +
                                "  \"birthday\": \"2035-08-20\"\n" +
                                "}"))
                .andExpect(status().isBadRequest());
    }
}