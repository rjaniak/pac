package com.prodyna.conference.backend.controller;

import com.prodyna.conference.backend.model.Person;
import com.prodyna.conference.backend.model.PersonDTO;
import com.prodyna.conference.backend.service.PersonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.annotation.Secured;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Ralf Janiak, PRODYNA SE
 */
@RestController
@RequestMapping("/api/persons")
public class PersonController {

    @Autowired
    PersonService personService;

    @GetMapping
    public List<Person> getPersons() {
        return personService.getPersons();
    }

    @GetMapping("{id}")
    public Person getPerson(@PathVariable Long id) {
        return personService.getPerson(id);
    }

    @PostMapping
    @Secured("ROLE_USER")
    public Person addPerson(@Valid @RequestBody PersonDTO personDTO, Errors errors) {
        validate(personDTO, errors);
        return personService.addPerson(personDTO);
    }

    @PutMapping("{id}")
    @Secured("ROLE_USER")
    public Person updatePerson(@PathVariable Long id, @Valid @RequestBody PersonDTO personDTO, Errors errors) {
        validate(personDTO, errors);
        return personService.updatePerson(id, personDTO);
    }

    @DeleteMapping("{id}")
    @Secured("ROLE_USER")
    public void deletePerson(@PathVariable Long id) {
        personService.deletePerson(id);
    }

    private void validate(PersonDTO personDTO, Errors errors) {
        if (errors.hasErrors()) {
            String errorMsg = errors.getAllErrors()
                    .stream().map(error -> error.getDefaultMessage()).collect(Collectors.joining(", "));
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, errorMsg);
        }
    }
}
