package net.paramount.ase.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import lombok.Data;

@Data
@Entity
public class MemberClass {
    @Id
    @GeneratedValue
    private long id;
    private String name;

    public MemberClass() { }
}
