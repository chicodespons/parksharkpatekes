package com.switchfully.patekes.parksharkpatekes.model;

import lombok.Data;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Data
public class ContactPerson {
    @Id
    @SequenceGenerator(name="contact_person_seq", sequenceName = "contact_person_seq", initialValue = 1, allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "contact_person_seq")
    private Long id;
    @Embedded
    private Name name;
    private String mobilePhoneNumber;
    private String telephonePhoneNumber;
    private String email;
    @OneToOne(cascade = CascadeType.ALL)
    private Address address;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ContactPerson that = (ContactPerson) o;
        return Objects.equals(name, that.name) && Objects.equals(mobilePhoneNumber, that.mobilePhoneNumber) && Objects.equals(telephonePhoneNumber, that.telephonePhoneNumber) && Objects.equals(email, that.email) && Objects.equals(address, that.address);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, mobilePhoneNumber, telephonePhoneNumber, email, address);
    }
}
