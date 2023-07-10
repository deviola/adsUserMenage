package com.ostasz.ads.user.repository;

import com.ostasz.ads.user.datamodel.entity.Contact;
import com.ostasz.ads.user.datamodel.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User,Long> {

    @Transactional
    @Modifying
    @Query("update User u " +
            "set u.contact = ?1 where u.id = ?2")
    void updateContactById(Contact contact, Long userId);

    Optional<User> findByPesel(String pesel);

    @Query("SELECT DISTINCT u FROM User u " +
            "JOIN FETCH u.contact c ")
    List<User> findAllWithContact();

}
