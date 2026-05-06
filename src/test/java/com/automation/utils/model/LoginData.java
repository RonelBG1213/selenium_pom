package com.automation.utils.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class LoginData {

    private String username;
    private String password;
    private String expectedResult;

    public LoginData() {}

    public String getUsername()       { return username; }
    public String getPassword()       { return password; }
    public String getExpectedResult() { return expectedResult; }

    public void setUsername(String username)             { this.username = username; }
    public void setPassword(String password)             { this.password = password; }
    public void setExpectedResult(String expectedResult) { this.expectedResult = expectedResult; }

    @Override
    public String toString() {
        return "LoginData{username='" + username + "', expectedResult='" + expectedResult + "'}";
    }
}
