package com.example.demo.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class GoogleAuthResponse {
    private String token;
    private Integer roleId;
    private Integer memberId;
    private String message;
    private String email;
    private boolean isNewUser;
    
    public GoogleAuthResponse(String token, Integer roleId, boolean isNewUser) {
        this.token = token;
        this.roleId = roleId;
        this.isNewUser = isNewUser;
        this.message = isNewUser ? "Google ile başarıyla kayıt oldunuz" : "Google ile başarıyla giriş yaptınız";
    }
    
    public GoogleAuthResponse(String token, Integer roleId, Integer memberId,String email, boolean isNewUser) {
        this.token = token;
        this.roleId = roleId;
        this.memberId = memberId;
        this.isNewUser = isNewUser;
        this.email=email;
        this.message = isNewUser ? "Google ile başarıyla kayıt oldunuz" : "Google ile başarıyla giriş yaptınız";
    }
} 