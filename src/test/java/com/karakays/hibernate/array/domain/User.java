package com.karakays.hibernate.array.domain;

import java.util.Arrays;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.hibernate.annotations.Parameter;
import org.hibernate.annotations.Type;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor(access=AccessLevel.PRIVATE)
@Getter
@Table(name = "users")
@Entity
public class User {
    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    private Long id;
    private String name;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "user", fetch = FetchType.EAGER)
    private List<Item> items;
    @Column(name = "badges", columnDefinition="text[]")
    @Type(type = "com.karakays.hibernate.array.EnumArrayType",
            parameters = { @Parameter(name="enumClass", value="com.karakays.hibernate.array.domain.User$Badge") })
    private List<Badge> badges;
    
    public User(String name) {
        this.name = name;
    }
    
    public User(String name, Badge... badges) {
        this(name);
        this.badges = Arrays.asList(badges);
     }
    
    public enum Badge {
        GURU, MASTER, CHALLENGER, ORACLE;
    }
}
