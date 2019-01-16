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
import org.hibernate.engine.spi.SharedSessionContractImplementor;

import java.sql.Array;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Array type that persists a list of custom enums as integer
 *
 * @author Selçuk Karakayalı
 */
public class EnumIntArrayType extends BaseEnumArrayType {
    public Object nullSafeGet(ResultSet rs, String[] names, SharedSessionContractImplementor session, Object owner)
            throws HibernateException, SQLException {
        if (names != null && names.length > 0 && rs != null && rs.getArray(names[0]) != null) {
            Integer[] values = ((Integer[]) rs.getArray(names[0]).getArray());

            List<Enum<?>> enumList = new ArrayList<>();

            for (Integer value : values) {
                for (Enum<?> enumConstant : mappedClass.getEnumConstants()) {
                    if (enumConstant.ordinal() == value) {
                        enumList.add(enumConstant);
                    }
                }
            }
            return enumList;
        }
        return null;
    }

    public void nullSafeSet(PreparedStatement st, Object value, int index, SharedSessionContractImplementor session)
            throws HibernateException, SQLException {
        if (value != null && st != null) {
            List<Enum<?>> list = (List<Enum<?>>) value;
            Integer[] castObject = list.stream().map(e -> {
                if (e == null) {
                    return null;
                } else {
                    return e.ordinal();
                }
            }).toArray(Integer[]::new);
            Array array = session.connection().createArrayOf("int", castObject);
            st.setArray(index, array);
        } else {
            st.setNull(index, arrayTypes[0]);
        }
    }
}
