package com.example.salo.account;

public interface JwtServiceHolder {
    void SaveJWTToken(String token);
    String getToken();
    void removeToken();
}
