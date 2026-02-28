package com.snhu.sslserver.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import org.springframework.web.util.HtmlUtils;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HexFormat;

/**
 * Provides a /hash endpoint that returns a SHA-256 checksum for data
 * integrity verification, transmitted over HTTPS.
 */
@RestController
public class HashController {

    /**
     * Returns a SHA-256 hex digest of the supplied {@code data} string.
     * If no data parameter is provided, defaults to "Hello World!".
     *
     * @param data the input string to hash
     * @return HTML snippet containing the original data and its SHA-256 checksum
     * @throws NoSuchAlgorithmException if SHA-256 is unavailable (should never happen on a standard JVM)
     */
    @GetMapping("/hash")
    public String getHash(@RequestParam(defaultValue = "Hello World!") String data)
            throws NoSuchAlgorithmException {
        MessageDigest digest = MessageDigest.getInstance("SHA-256");
        byte[] hashBytes = digest.digest(data.getBytes(StandardCharsets.UTF_8));
        String checksum = HexFormat.of().formatHex(hashBytes);
        String safeData = HtmlUtils.htmlEscape(data);
        return "<p>Data: " + safeData + "</p><p>SHA-256 Checksum: " + checksum + "</p>";
    }
}
