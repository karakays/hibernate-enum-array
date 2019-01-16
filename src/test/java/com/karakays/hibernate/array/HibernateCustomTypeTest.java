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

import com.karakays.hibernate.array.domain.Item;
import com.karakays.hibernate.array.domain.User;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.Metadata;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.Serializable;
import java.util.Arrays;

import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.Matchers.hasItems;

public class HibernateCustomTypeTest {
    private static SessionFactory sessionFactory;
    private Serializable item1;
    private Serializable item2;
    private User user1;
    private User user2;

    @BeforeClass
    public static void createFactory() {
        try {
            StandardServiceRegistry registry = new StandardServiceRegistryBuilder().configure().build();

            MetadataSources sources = new MetadataSources(registry);

            Metadata metadata = sources.getMetadataBuilder().build();

            sessionFactory = metadata.getSessionFactoryBuilder().build();
        } catch (Exception ex) {
            throw new ExceptionInInitializerError(ex);
        }
    }

    @Before
    public void createSession() throws Exception {
        Session session = sessionFactory.getCurrentSession();
        session.beginTransaction();
        
        user1 = session.load(User.class, session.save(new User("John Doe", User.Badge.MASTER)));
        user2 = session.load(User.class, session.save(new User("Frank Morris", User.Badge.CHALLENGER, User.Badge.ORACLE)));
        item1 = session.save(new Item(user1, Item.Property.DEFAULT));
        item2 = session.save(new Item(user2, Item.Property.WRITE, Item.Property.EXECUTE, Item.Property.READ));
        
        session.getTransaction().commit();
    }

    @AfterClass
    public static void shutdown() {
        sessionFactory.close();
    }

    @Test
    public void shouldStoreItem() {
        Item item = save(new Item(user2, Item.Property.DEFAULT));
        assertThat(item.getId(), greaterThan(0l));
    }
    
    @Test
    public void shouldStoreUser() {
        User user = save(new User("John Doe", User.Badge.MASTER));
        assertThat(user.getId(), greaterThan(0l));
    }
    
    @Test
    public void shouldLoadItem() {
        Item item = (Item) load(Item.class, item1);

        assertThat(item.getProperties(), hasItems(Item.Property.DEFAULT));
    }
    
    @Test
    public void shouldLoadItemWithMultipleProperties() {
        Item item = (Item) load(Item.class, item2);

        assertThat(item.getProperties(), hasItems(Item.Property.WRITE, Item.Property.EXECUTE, Item.Property.READ));
    }
    
    @Test
    public void shouldLoadUser() {
        User loadedUser = (User) load(User.class, user1.getId());

        assertThat(loadedUser.getBadges(), hasItems(User.Badge.MASTER));
        assertThat(loadedUser.getBadgesAsInt(), hasItems(User.Badge.MASTER));
    }
    
    @Test
    public void shouldUpdateProperty() {
        Item item = (Item) load(Item.class, item2);
        item.setProperties(Arrays.asList(Item.Property.ALL));
        item.setPropertiesAsInt(Arrays.asList(Item.Property.ALL));

        save(item);
        item = load(Item.class, item2);

        assertThat(item.getProperties(), hasItems(Item.Property.ALL));
        assertThat(item.getPropertiesAsInt(), hasItems(Item.Property.ALL));
    }
    
    @Test
    public void shouldDeleteProperty() {
        Item item = (Item) load(Item.class, item2);
        item.setProperties(null);
        item.setPropertiesAsInt(null);

        update(item);

        item = (Item) load(Item.class, item2);

        assertThat(item.getProperties(), nullValue());
        assertThat(item.getPropertiesAsInt(), nullValue());
    }

    @SuppressWarnings("unchecked")
    private <E> E save(E entity) {
        Session session = sessionFactory.openSession();
        session.beginTransaction();
        Object entityAttached = session.merge(entity);
        session.getTransaction().commit();
        return (E) entityAttached;
    }
    
    private <E> E load(Class<E> clazz, Serializable id) {
        Session session = sessionFactory.openSession();
        E entity = (E) session.get(clazz, id);
        session.close();
        return entity;
    }
    
    private void update(Object entity) {
        Session session = sessionFactory.openSession();
        session.beginTransaction();
        session.update(entity);
        session.getTransaction().commit();
    }
}
