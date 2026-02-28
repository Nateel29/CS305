package com.snhu.sslserver;

import com.snhu.sslserver.controller.HashController;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.util.HexFormat;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class SslServerApplicationTests {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private HashController hashController;

    /** Verify the application context loads successfully. */
    @Test
    void contextLoads() {
        assertThat(hashController).isNotNull();
    }

    /**
     * The /hash endpoint must return the correct SHA-256 checksum for the
     * default data value ("Hello World!").
     */
    @Test
    @WithMockUser
    void hashEndpointReturnsCorrectChecksumForDefaultData() throws Exception {
        String data = "Hello World!";
        MessageDigest digest = MessageDigest.getInstance("SHA-256");
        String expectedChecksum = HexFormat.of().formatHex(
                digest.digest(data.getBytes(StandardCharsets.UTF_8)));

        mockMvc.perform(get("/hash").secure(true))
                .andExpect(status().isOk())
                .andExpect(content().string(org.hamcrest.Matchers.containsString(expectedChecksum)));
    }

    /**
     * The /hash endpoint must return the correct SHA-256 checksum for a
     * custom data string supplied as a query parameter.
     */
    @Test
    @WithMockUser
    void hashEndpointReturnsCorrectChecksumForCustomData() throws Exception {
        String data = "ArtemisFinancial";
        MessageDigest digest = MessageDigest.getInstance("SHA-256");
        String expectedChecksum = HexFormat.of().formatHex(
                digest.digest(data.getBytes(StandardCharsets.UTF_8)));

        mockMvc.perform(get("/hash").secure(true).param("data", data))
                .andExpect(status().isOk())
                .andExpect(content().string(org.hamcrest.Matchers.containsString(expectedChecksum)));
    }

    /**
     * Unauthenticated requests to /hash must be rejected (401 or 302 redirect to login).
     */
    @Test
    void hashEndpointRequiresAuthentication() throws Exception {
        mockMvc.perform(get("/hash").secure(true))
                .andExpect(result ->
                        assertThat(result.getResponse().getStatus()).isIn(401, 302));
    }
}
