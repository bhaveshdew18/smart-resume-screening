package com.bhavesh.resume.service;

import com.bhavesh.resume.entity.User;

public interface JwtService {

    String generateToken(User user);
}
