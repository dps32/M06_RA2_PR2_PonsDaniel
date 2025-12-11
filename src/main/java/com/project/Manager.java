package com.project;

import java.io.Serializable;
import java.util.Collection;
import java.util.Set;

import org.hibernate.HibernateException;
import org.hibernate.Session; 
import org.hibernate.Transaction;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

public class Manager {

    private static SessionFactory factory; 
    
    public static void createSessionFactory() {
        try {
            Configuration configuration = new Configuration();
            
            // Afegim les classes
            configuration.addAnnotatedClass(Ciutat.class);
            configuration.addAnnotatedClass(Ciutada.class);

            StandardServiceRegistry serviceRegistry = new StandardServiceRegistryBuilder()
                .applySettings(configuration.getProperties())
                .build();
                
            factory = configuration.buildSessionFactory(serviceRegistry);
        } catch (Throwable ex) { 
            System.err.println("Failed to create sessionFactory object." + ex);
            throw new ExceptionInInitializerError(ex); 
        }
    }

    public static void close () {
        factory.close();
    }


  
    // Afegir una ciutat
    public static Ciutat addCiutat(String nom, String pais, int poblacio){
        Session session = factory.openSession();
        Transaction tx = null;
        Ciutat result = null;

        try {
            tx = session.beginTransaction();
            result = new Ciutat(nom, pais, poblacio);
            session.persist(result);
            tx.commit();
        } catch (HibernateException e) {
            if (tx!=null) tx.rollback();
            e.printStackTrace(); 
            result = null;
        } finally {
            session.close(); 
        }

        return result;
    }


    // Afegir un ciutada
    public static Ciutada addCiutada(String nom, String cognom, int edat){
        Session session = factory.openSession();
        Transaction tx = null;
        Ciutada result = null;

        try {
            tx = session.beginTransaction();
            result = new Ciutada(nom, cognom, edat);
            session.persist(result);
            tx.commit();
        } catch (HibernateException e) {
            if (tx!=null) tx.rollback();
            e.printStackTrace(); 
            result = null;
        } finally {
            session.close(); 
        }

        return result;
    }



    // Actualitzar ciutat per assignar ciutadasn
    public static void updateCiutat(long ciutatId, String nom, String pais, int poblacio, Set<Ciutada> ciutadans){
        Session session = factory.openSession();
        Transaction tx = null;

        try {
            tx = session.beginTransaction();
            Ciutat obj = (Ciutat) session.get(Ciutat.class, ciutatId); 
            if (obj == null)
                return;

            obj.setNom(nom);
            obj.setPais(pais);
            obj.setPoblacio(poblacio);
            
            // Actualitzem la ciutat dels ciutadans
            for (Ciutada ciutada : ciutadans) {
                Ciutada managedCiutada = session.get(Ciutada.class, ciutada.getCiutadaId());
                if (managedCiutada != null) {
                    managedCiutada.setCiutat(obj);
                    session.merge(managedCiutada);
                }
            }
            
            session.merge(obj);
            tx.commit();
        } catch (HibernateException e) {
            if (tx!=null) tx.rollback();
            e.printStackTrace(); 
        } finally {
            session.close(); 
        }
    }


    // Actualitzar ciutada
    public static void updateCiutada(long ciutadaId, String nom, String cognom, int edat){
        Session session = factory.openSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            Ciutada obj = (Ciutada) session.get(Ciutada.class, ciutadaId); 
            if (obj == null)
                return;
            
            obj.setNom(nom);
            obj.setCognom(cognom);
            obj.setEdat(edat);
            session.merge(obj);
            tx.commit();
        } catch (HibernateException e) {
            if (tx!=null) tx.rollback();
            e.printStackTrace(); 
        } finally {
            session.close(); 
        }
    }



    // Eliminar objecte per id
    public static <T> void delete(Class<? extends T> clazz, Serializable id) {
        Session session = factory.openSession();
        Transaction tx = null;

        try {
            tx = session.beginTransaction();
            T obj = clazz.cast(session.get(clazz, id));
            if (obj != null) {
                session.remove(obj);
                tx.commit();
            }

        } catch (HibernateException e) {
            if (tx != null) tx.rollback();
            e.printStackTrace();
        } finally {
            session.close();
        }
    }


    // Llistar col·leccions
    public static <T> Collection<?> listCollection(Class<? extends T> clazz) {
        return listCollection(clazz, "");
    }

    public static <T> Collection<?> listCollection(Class<? extends T> clazz, String where){
        Session session = factory.openSession();
        Transaction tx = null;
        Collection<?> result = null;

        try {
            tx = session.beginTransaction();
            if (where.length() == 0) {
                result = session.createQuery("FROM " + clazz.getName(), clazz).list();
            } else {
                result = session.createQuery("FROM " + clazz.getName() + " WHERE " + where, clazz).list();
            }
            tx.commit();
        } catch (HibernateException e) {
            if (tx!=null) tx.rollback();
            e.printStackTrace(); 
        } finally {
            session.close(); 
        }

        return result;
    }

    // Convertir col·leccio a String
    public static <T> String collectionToString(Class<? extends T> clazz, Collection<?> collection){
        String txt = "";

        for(Object obj: collection) {
            T cObj = clazz.cast(obj);
            txt += "\n" + cObj.toString();
        }

        if (txt.length() > 0 && txt.substring(0, 1).compareTo("\n") == 0) {
            txt = txt.substring(1);
        }

        return txt;
    }



    // Obtindre ciutat amb els ciutadans
    public static Ciutat getCiutatWithCiutadans(long ciutatId) {
        Session session = factory.openSession();
        Transaction tx = null;
        Ciutat result = null;

        try {
            tx = session.beginTransaction();
            result = session.get(Ciutat.class, ciutatId);

            if (result != null) {
                result.getCiutadans().size();
            }

            tx.commit();
        } catch (HibernateException e) {
            if (tx != null) tx.rollback();
            e.printStackTrace();
        } finally {
            session.close();
        }

        return result;
    }
}
