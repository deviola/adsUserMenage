package com.ostasz.ads.repository;

import com.ostasz.ads.datamodel.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<User,Long> {

    User findByPesel(String pesel);

    @Query("SELECT DISTINCT u FROM User u " +
            "JOIN FETCH u.contact c " +
            "JOIN FETCH c.homeAddress ha " +
            "JOIN FETCH c.registeredAddress ra")
    List<User> findAllWithContactAndAddress();

}
