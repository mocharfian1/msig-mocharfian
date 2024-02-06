package com.example.demo.services;

import com.example.demo.models.ResponseApi;
import com.example.demo.models.entities.Contact;
import com.example.demo.models.request.ContactRequestBody;
import com.example.demo.models.response.ContactResponseMapping;
import com.example.demo.repositories.ContactRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ContactService {
    @Autowired
    ContactRepository contactRepository;

    public ResponseApi<List<Contact>> getListContact() {
        List<Contact> findAllContact = contactRepository.findAll();
        return new ResponseApi<>(HttpStatus.OK.value(), "Success get all data", findAllContact);
    }

    public ResponseApi<ContactResponseMapping> createUpdateContact(ContactRequestBody contactBody, boolean isUpdate) {
        try {
            if(isUpdate){
                if (!checkIdExisting(contactBody.getId())){
                    return new ResponseApi<>(HttpStatus.BAD_REQUEST.value(), "ID Not Found", null);
                }
            }

            Contact contact = contactRepository.save(mappingContactBodyToJPA(contactBody));
            return new ResponseApi<>(HttpStatus.OK.value(), "Success Update Contact", mappingContact(contact));
        } catch (DataIntegrityViolationException e) {
            return new ResponseApi<>(HttpStatus.BAD_REQUEST.value(), "Error: Duplicate Contact", null);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseApi<>(HttpStatus.INTERNAL_SERVER_ERROR.value(), "Error: Unknown Error. Please check log.", null);
        }
    }

    public ResponseApi<ContactResponseMapping> deleteContact(long contactId) {
        try {
            Optional<Contact> contact = checkIdContact(contactId);
            if (contact.isPresent()){
                contactRepository.delete(contact.get());
                return new ResponseApi<>(HttpStatus.OK.value(), "Contact Deleted", null);
            }
            return new ResponseApi<>(HttpStatus.BAD_REQUEST.value(), "ID Not Found", null);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseApi<>(HttpStatus.INTERNAL_SERVER_ERROR.value(), "Error: Unknown Error. Please check log.", null);
        }
    }

    private ContactResponseMapping mappingContact(Contact contact) {
        ContactResponseMapping contactResponse = new ContactResponseMapping();
        contactResponse.setName(contact.getName());
        contactResponse.setMobile(contact.getMobile());
        contactResponse.setPhone(contact.getPhone());
        contactResponse.setEmail(contact.getEmail());

        return contactResponse;
    }

    private Contact mappingContactBodyToJPA(ContactRequestBody contactRequestBody) {
        Contact contact = new Contact();
        if (contactRequestBody.getId() != null) contact.setId(contactRequestBody.getId());
        contact.setName(contactRequestBody.getName());
        contact.setMobile(contactRequestBody.getMobile());
        contact.setPhone(contactRequestBody.getPhone());
        contact.setEmail(contactRequestBody.getEmail());

        return contact;
    }

    private boolean checkIdExisting(Long id){
        if(id != null){
            Optional<Contact> contact = contactRepository.findById(id);
            return contact.isPresent();
        }
        return false;
    }

    private Optional<Contact> checkIdContact(Long id){
        if(id != null){
            return contactRepository.findById(id);
        }
        return Optional.empty();
    }
}
