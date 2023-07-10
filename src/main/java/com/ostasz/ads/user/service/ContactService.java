package com.ostasz.ads.user.service;

import com.ostasz.ads.user.datamodel.dto.request.ContactRequest;
import com.ostasz.ads.user.datamodel.entity.Contact;
import com.ostasz.ads.user.repository.ContactRepository;
import org.springframework.stereotype.Service;

@Service
public class ContactService {

    private final ContactRepository contactRepository;

    public ContactService(ContactRepository contactRepository) {
        this.contactRepository = contactRepository;
    }

    public Contact createContact(ContactRequest contactRequest) {
        Contact contact = Contact.builder()
                .email(contactRequest.getEmail())
                .homeAddress(contactRequest.getHomeAddress())
                .registeredAddress(contactRequest.getRegisteredAddress())
                .privatePhoneNumber(contactRequest.getPrivatePhoneNumber())
                .businessPhoneNumber(contactRequest.getBusinessPhoneNumber())
                .build();
        contactRepository.save(contact);
        return contact;
    }
}
