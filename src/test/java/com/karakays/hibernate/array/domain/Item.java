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

import lombok.*;
import org.hibernate.annotations.Parameter;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.util.Arrays;
import java.util.List;

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
    @Column(name = "properties_as_int", columnDefinition="int[]")
    @Type(type = "com.karakays.hibernate.array.EnumIntArrayType",
            parameters = { @Parameter(name="enumClass", value="com.karakays.hibernate.array.domain.Item$Property") })
    private List<Property> propertiesAsInt;

    public Item(User user, Property... properties) {
       this(null, user, Arrays.asList(properties), Arrays.asList(properties));
    }
    
    public enum Property {
        READ, WRITE, EXECUTE, ALL, DEFAULT;
    }
} 
