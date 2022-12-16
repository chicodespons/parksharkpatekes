package com.switchfully.patekes.parksharkpatekes.model;

import lombok.EqualsAndHashCode;
import lombok.Getter;

import javax.persistence.Embeddable;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Embeddable
@Getter
@EqualsAndHashCode
public final class Name {

    private final String firstname;

    private final String lastname;

    public Name() {
        this("", "");
    }

    public Name(String firstname, String lastname) {
        this.firstname = firstname;
        this.lastname = lastname;
    }

    @Override
    public String toString() {
        return firstname + ' ' + lastname;
    }
}
