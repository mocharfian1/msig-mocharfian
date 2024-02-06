package com.example.demo.controllers;


import com.example.demo.models.ResponseApi;
import com.example.demo.models.entities.Contact;
import com.example.demo.models.request.ContactRequestBody;
import com.example.demo.models.response.ContactResponseMapping;
import com.example.demo.services.ContactService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "api/contact")
public class ContactController {

    @Autowired
    ContactService contactService;

    @GetMapping
    public ResponseEntity<ResponseApi<List<Contact>>> listContact() {
        return ResponseEntity.status(contactService.getListContact().getStatusCode()).body(contactService.getListContact());
    }

    @PostMapping
    public ResponseEntity<ResponseApi<ContactResponseMapping>> createContact(@RequestBody @Valid ContactRequestBody contactBody) {
        try {
            ResponseApi<ContactResponseMapping> createContact = contactService.createUpdateContact(contactBody, false);
            return ResponseEntity.status(createContact.getStatusCode()).body(createContact);
        } catch (Exception e) {
            return ResponseEntity.status(500).body(new ResponseApi<>(500, "Error: Unknown error. Please check log.", null));
        }
    }

    @PutMapping
    public ResponseEntity<ResponseApi<ContactResponseMapping>> updateContact(@RequestBody @Valid ContactRequestBody contactBody) {
        try {
            ResponseApi<ContactResponseMapping> createContact = contactService.createUpdateContact(contactBody, true);
            return ResponseEntity.status(createContact.getStatusCode()).body(createContact);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR.value()).body(new ResponseApi<>(HttpStatus.INTERNAL_SERVER_ERROR.value(), "Error: Unknown error. Please check log.", null));
        }
    }

    @DeleteMapping("{id}")
    public ResponseEntity<ResponseApi<ContactResponseMapping>> deleteContact(@PathVariable(value = "id") long contactId) {
        try {
            ResponseApi<ContactResponseMapping> createContact = contactService.deleteContact(contactId);
            return ResponseEntity.status(createContact.getStatusCode()).body(createContact);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR.value()).body(new ResponseApi<>(HttpStatus.INTERNAL_SERVER_ERROR.value(), "Error: Unknown error. Please check log.", null));
        }
    }
}
