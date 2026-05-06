package com.automation.utils.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class RegisterData {

    private String name;
    private String username;
    private String password;
    private String confirmPassword;
    private String expectedResult;
    private String expectedError;

    public RegisterData() {}

    public String getName()            { return name; }
    public String getUsername()        { return username; }
    public String getPassword()        { return password; }
    public String getConfirmPassword() { return confirmPassword; }
    public String getExpectedResult()  { return expectedResult; }
    public String getExpectedError()   { return expectedError; }

    public void setName(String name)                      { this.name = name; }
    public void setUsername(String username)               { this.username = username; }
    public void setPassword(String password)               { this.password = password; }
    public void setConfirmPassword(String confirmPassword) { this.confirmPassword = confirmPassword; }
    public void setExpectedResult(String expectedResult)   { this.expectedResult = expectedResult; }
    public void setExpectedError(String expectedError)     { this.expectedError = expectedError; }

    @Override
    public String toString() {
        return "RegisterData{username='" + username + "', expectedResult='" + expectedResult + "'}";
    }
}
