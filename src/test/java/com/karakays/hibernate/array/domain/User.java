/**
 * Copyright (C) 2018 Selçuk Karakayalı (skarakayali@gmail.com)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.karakays.hibernate.array.domain;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Parameter;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.util.Arrays;
import java.util.List;

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
    @Column(name = "badges_as_int", columnDefinition="int[]")
    @Type(type = "com.karakays.hibernate.array.EnumIntArrayType",
            parameters = { @Parameter(name="enumClass", value="com.karakays.hibernate.array.domain.User$Badge") })
    private List<Badge> badgesAsInt;
    
    public User(String name) {
        this.name = name;
    }
    
    public User(String name, Badge... badges) {
        this(name);
        this.badges = Arrays.asList(badges);
        this.badgesAsInt = Arrays.asList(badges);
     }
    
    public enum Badge {
        GURU, MASTER, CHALLENGER, ORACLE;
    }
}
