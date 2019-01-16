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
package com.karakays.hibernate.array;

import org.hibernate.HibernateException;
import org.hibernate.usertype.ParameterizedType;
import org.hibernate.usertype.UserType;

import java.io.Serializable;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Properties;

/**
 * Base class for array type that persists a list of custom enums
 *
 * @author Selçuk Karakayalı
 */
public abstract class BaseEnumArrayType implements UserType, ParameterizedType {
    protected final int[] arrayTypes = new int[] { Types.ARRAY };

    protected Class<Enum<?>> mappedClass;

    protected void setMappedClass(Class<Enum<?>> mappedClass) {
        this.mappedClass = mappedClass;
    }

    protected Class<Enum<?>> getMappedClass() {
        return mappedClass;
    }

    public int[] sqlTypes() {
        return arrayTypes;
    }

    public Class<List> returnedClass() {
        return List.class;
    }

    public boolean equals(Object x, Object y) throws HibernateException {
        return Objects.equals(x, y);
    }

    public int hashCode(Object x) throws HibernateException {
        return x == null ? 0 : x.hashCode();
    }

    public Object deepCopy(Object value) throws HibernateException {
        if (value == null) {
            return null;
        }

        return new ArrayList<>((List<Enum<?>>) value);
    }

    public boolean isMutable() {
        return false;
    }

    public Serializable disassemble(Object value) throws HibernateException {
        return (Serializable) value;
    }

    public Object assemble(Serializable cached, Object owner) throws HibernateException {
        return cached;
    }

    public Object replace(Object original, Object target, Object owner) throws HibernateException {
        return original;
    }

    public void setParameterValues(Properties parameters) {
        if (parameters.containsKey("enumClass")) {
            String enumClassName = parameters.getProperty("enumClass");
            try {
                setMappedClass((Class<Enum<?>>) Class.forName(enumClassName));
            } catch (ClassNotFoundException e) {
                throw new HibernateException("Specified enum class could not be found", e);
            }
        }
    }
}
