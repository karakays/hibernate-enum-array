hibernate-enum-array
====================


[![Build Status](https://travis-ci.org/karakays/hibernate-enum-array.svg?branch=master)](https://travis-ci.org/karakays/hibernate-enum-array?branch=master) [![Maven Central](https://maven-badges.herokuapp.com/maven-central/com.karakays.hibernate/hibernate-enum-array/badge.svg)](https://maven-badges.herokuapp.com/maven-central/com.karakays.hibernate/hibernate-enum-array/) [![License](https://img.shields.io/badge/License-Apache%202.0-blue.svg)](https://opensource.org/licenses/Apache-2.0)

Read/write an array of Java enums into a database table field without any join tables. 

Currently supported databases:
- Postgresql

### Installation

```
<dependency>
    <groupId>com.karakays.hibernate</groupId>
    <artifactId>hibernate-enum-array</artifactId>
    <version>0.1</version>
</dependency>
```

### Example 

```
@Entity
public class User {
    @Column(name = "badges", columnDefinition="text[]")
    @Type(type = "com.karakays.hibernate.array.EnumArrayType",
            parameters = { @Parameter(name="enumClass", value="com.karakays.hibernate.array.domain.User$Badge") })
    private List<Badge> badges;
}
```

where Badge is a custom Enum

```
public enum Badge {
    GURU, MASTER, CHALLENGER, ORACLE;
}
```
and its type is passed along using org.hibernate.annotations.Parameter annotation.

Now you can persist a list of enums in a table field with no further relation tables.  

Please see the test class to see a full example.

### DDL generation

Considering this class:
```
@Table(name = "users")
@Entity
public class User {
    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    private Long id;
    private String name;
    @Column(name = "badges", columnDefinition="text[]")
    @Type(type = "com.karakays.hibernate.array.EnumArrayType",
            parameters = { @Parameter(name="enumClass", value="com.karakays.hibernate.array.domain.User$Badge") })
    private List<Badge> badges;
}
```

Following DDL is generated:

```
create table users (
    id int8 not null,
    name varchar(255),
    badges text[],
    primary key (id)
)
```
