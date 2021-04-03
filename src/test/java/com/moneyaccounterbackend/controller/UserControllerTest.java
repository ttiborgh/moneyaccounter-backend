package com.moneyaccounterbackend.controller;

import com.moneyaccounterbackend.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
public class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private UserRepository userRepository;

    @Test
    public void givenUserLoginAttemptWithoutUsername_whenLoggingIn_thenBadRequestIsReturned() throws Exception {
        this.mockMvc.perform(
                post("/api/login")
                        .param("username", "")
                        .param("password", "geheimniss"))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void givenUserLoginAttemptWithoutPassword_whenLoggingIn_thenBadRequestIsReturned() throws Exception {
        this.mockMvc.perform(
                post("/api/login")
                        .param("username", "konntemansehen")
                        .param("password", ""))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void givenUserWithoutRegistration_whenLoggingIn_thenBadRequestIsReturned() throws Exception {
        this.mockMvc.perform(
                post("/api/login")
                        .param("username", "konntemansehen")
                        .param("password", "geheimniss"))
                .andExpect(status().isBadRequest());
    }

}