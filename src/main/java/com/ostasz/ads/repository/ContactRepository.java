package com.ostasz.ads.repository;

import com.ostasz.ads.datamodel.entity.Contact;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ContactRepository  extends JpaRepository<Contact,Long> {
}
