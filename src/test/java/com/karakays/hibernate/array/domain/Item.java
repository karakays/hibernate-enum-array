package com.karakays.hibernate.array.domain;

import java.util.Arrays;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.Parameter;
import org.hibernate.annotations.Type;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor(access=AccessLevel.PRIVATE)
@Getter
@Setter
@Table(name = "items")
@Entity
public class Item {
    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    private Long id;
    @JoinColumn(name="user_id")
    @ManyToOne(fetch=FetchType.EAGER)
    private User user;
    @Column(name = "properties", columnDefinition="text[]")
    @Type(type = "com.karakays.hibernate.array.EnumArrayType",
            parameters = { @Parameter(name="enumClass", value="com.karakays.hibernate.array.domain.Item$Property") })
    private List<Property> properties;
    
    public Item(User user, Property... properties) {
       this(null, user, Arrays.asList(properties));
    }
    
    public enum Property {
        READ, WRITE, EXECUTE, ALL, DEFAULT;
    }
} 
