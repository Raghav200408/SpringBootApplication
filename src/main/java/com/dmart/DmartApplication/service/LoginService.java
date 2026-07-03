package com.dmart.DmartApplication.service;



import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.dmart.DmartApplication.repository.*;
import com.dmart.DmartApplication.model.*;

@Service
public class LoginService {

    @Autowired
    private LoginDAO dao;

    public boolean validateUser(LoginDTO dto) {

        return dao.validateUser(
                dto.getUsername(),
                dto.getPassword());
    }
}
