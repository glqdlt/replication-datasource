package com.glqdlt.ex.replicationjdbc;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

@Service
public class UserService {

    @PersistenceContext
    private EntityManager entityManager;

    @Autowired
    private UserRepo userRepo;

    public List<User> findUsersByEm() {
        return (List<User>) entityManager.createQuery("select u from User u").getResultList();
    }

    public List<User> findUsersByJpaRepo(){
        return userRepo.findAll();
    }

}
