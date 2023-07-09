package com.ostasz.ads.service;

import com.ostasz.ads.datamodel.dto.ContactDTO;
import com.ostasz.ads.datamodel.entity.Contact;
import com.ostasz.ads.repository.ContactRepository;
import org.springframework.stereotype.Service;

@Service
public class ContactService {

    private final AddressService addressService;
    private final ContactRepository contactRepository;

    public ContactService(AddressService addressService, ContactRepository contactRepository) {
        this.addressService = addressService;
        this.contactRepository = contactRepository;
    }

    public Contact createContact(ContactDTO contactDTO) {
        Contact contact = Contact.builder()
                .email(contactDTO.getEmail())
                .homeAddress(addressService.addAddress(contactDTO.getHomeAddress()))
                .registeredAddress(addressService.addAddress(contactDTO.getRegisteredAddress()))
                .privatePhoneNumber(contactDTO.getPrivatePhoneNumber())
                .businessPhoneNumber(contactDTO.getBusinessPhoneNumber())
                .build();

        contactRepository.save(contact);
        return contact;
    }
}
