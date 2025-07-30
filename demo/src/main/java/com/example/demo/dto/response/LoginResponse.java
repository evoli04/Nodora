package com.example.demo.dto.response;

public class LoginResponse {
    private String token;
    private Integer roleId;
    private Integer memberId;

    public LoginResponse() {
    }

    public LoginResponse(String token, Integer roleId) {
        this.token = token;
        this.roleId = roleId;
    }

    public LoginResponse(String token, Integer roleId, Integer memberId) {
        this.token = token;
        this.roleId = roleId;
        this.memberId = memberId;
    }

    // Getters and Setters
    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public Integer getRoleId() {
        return roleId;
    }

    public void setRoleId(Integer roleId) {
        this.roleId = roleId;
    }

    public Integer getMemberId() {
        return memberId;
    }

    public void setMemberId(Integer memberId) {
        this.memberId = memberId;
    }
}
