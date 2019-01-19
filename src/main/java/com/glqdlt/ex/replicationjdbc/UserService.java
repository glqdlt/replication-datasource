package com.glqdlt.ex.replicationjdbc;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class UserService {

    @Autowired
    private UserRepo userRepo;

    @Transactional(transactionManager = "transactionManager",propagation = Propagation.REQUIRES_NEW)
    public List<User> findByMaster() {
        return userRepo.findAll();
    }

    @Transactional(readOnly = true, transactionManager = "transactionManager",propagation = Propagation.REQUIRES_NEW)
    public List<User> findBySlave() {
        return userRepo.findAll();
    }
}
