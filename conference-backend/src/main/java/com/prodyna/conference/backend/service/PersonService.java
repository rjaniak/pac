package com.prodyna.conference.backend.service;

import com.prodyna.conference.backend.model.*;
import com.prodyna.conference.backend.repository.OrganizationRepository;
import com.prodyna.conference.backend.repository.PersonRepository;
import io.micrometer.core.annotation.Timed;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * @author Ralf Janiak, PRODYNA SE
 */
@Service
@Transactional
@Timed
public class PersonService {

    @Autowired
    PersonRepository personRepository;

    @Autowired
    OrganizationRepository organizationRepository;

    public Person addPerson(PersonDTO personDTO) {
        Person person = mapToPerson(personDTO, new Person());
        return personRepository.save(person);
    }

    public void deletePerson(Long id) {
        personRepository.deleteById(id);
    }

    public Person getPerson(Long id) {
        Optional<Person> person = personRepository.findById(id);
        if (!person.isPresent()) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, "No Person found with id [" + id + "].");
        }
        return person.get();
    }

    public List<Person> getPersons() {
        List<Person> persons = new ArrayList<>();
        Iterable<Person> allPersons = personRepository.findAll();
        allPersons.forEach(persons::add);
        return persons;
    }

    public Person updatePerson(Long id, PersonDTO personDTO) {
        Optional<Person> personOptional = personRepository.findById(id);
        if (!personOptional.isPresent()) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, "No Person found with id [" + id + "].");
        }
        Person person = mapToPerson(personDTO, personOptional.get());
        return personRepository.save(person);
    }

    private Person addOrganization(Person person, Long organizationId) {
        Optional<Organization> organization = organizationRepository.findById(organizationId);
        if (organization.isPresent()) {
            person.setOrganization(organization.get());
        } else {
            person.setOrganization(null);
        }
        return person;
    }

    private Person mapToPerson(PersonDTO personDTO, Person person) {
        person.setName(personDTO.getName());
        person = addOrganization(person, personDTO.getOrganizationId());
        return person;
    }
}