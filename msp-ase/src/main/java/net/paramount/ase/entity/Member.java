package net.paramount.ase.entity;

import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;

import lombok.Data;

@Data
@Entity
public class Member {
    @Id
    @GeneratedValue
    private long id;
    private String firstName;
    private String lastName;
    private String zipCode;
    private String interests;
    private boolean active;

    @JoinTable(name = "Member_Class_Cross_Ref",
            joinColumns = @JoinColumn(
                    name = "member_id",
                    referencedColumnName = "id"
            ),
            inverseJoinColumns = @JoinColumn(
                    name = "class_id",
                    referencedColumnName = "id"
            ))
    @ManyToMany
    private Set<MemberClass> memberClasses;

    public Member() { }
}
