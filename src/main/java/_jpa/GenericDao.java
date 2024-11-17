package _jpa;

import static _jpa.JpaUtil.getSession;
import _jpa.exceptions.EntityNotFoundException;
import _jpa.exceptions.PerformEntityOperationException;
import java.io.Serializable;
import java.util.List;
import java.util.function.Function;
import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.JDBCException;
import org.hibernate.Session;
import org.hibernate.Transaction;

/**
 * A GenericDao is an abstract class that provides basic CRUD operations for a
 * specified entity type. This class uses JPA (Java Persistence API) to interact
 * with the database.
 *
 * @version 1.0
 * @param <T> the type of the entity to be managed by this DAO.
 * @author Eliezer Brasilian
 *
 * Visit repository link on GitHub:
 * <a href="https://github.com/eliezerBrasilian/jsfapp-generic-jpa">GitHub</a>
 */
public abstract class GenericDao<T> {

    protected Class<T> entityClass;

    /**
     * Constructs a GenericDao for the specified entity class.
     *
     * @param entityClass the class of the entity to be managed.
     */
    public GenericDao(Class<T> entityClass) {
        this.entityClass = entityClass;
    }

    /**
     * Saves a new entity to the database.
     *
     * @param entity the entity to be saved.
     */
    public void save(T entity) throws PerformEntityOperationException {
        Session session = getSession();
        session.beginTransaction();

        try {
            session.save(entity);
            session.getTransaction().commit();
        } catch (JDBCException e) {
            e.printStackTrace();
            throw new PerformEntityOperationException();

        } finally {
            try {
                if (session.isOpen()) {
                    session.close();
                }
            } catch (HibernateException e) {
                throw new PerformEntityOperationException("Erro ao fechar operação de salvar. \n" + e.getMessage());
            }
        }
    }

    /**
     * Updates an existing entity in the database.
     *
     * @param entity the entity to be updated.
     * 
     * @throws EntityNotFoundException if the entity is null.
     */
    public void update(T entity) throws PerformEntityOperationException, IllegalArgumentException {
        Session session = getSession();
        session.beginTransaction();

        try {
            if (entity != null) {
                session.update(entity);
            } else {
                throw new IllegalArgumentException("O objeto a ser atualizado não pode ser nulo.");
            }

            session.getTransaction().commit();

        } catch (JDBCException e) {
            e.printStackTrace();
            throw new PerformEntityOperationException();
        } finally {
            try {
                if (session.isOpen()) {
                    session.close();
                }
            } catch (HibernateException e) {
                throw new PerformEntityOperationException("Erro ao fechar operação de atualizar. \n" + e.getMessage());
            }
        }

    }

    /**
     * Finds an entity by its ID.
     *
     * @param id the id of the entity to be found.
     * @return the found entity or null if not found
     * @throws PerformEntityOperationException if was not possible to find row in database
     */
    public T findById(Serializable id) throws PerformEntityOperationException {
        Session session = getSession();

        try {
            session.beginTransaction();
            T entity = (T) session.get(entityClass, id);
            session.getTransaction().commit();
            return entity;
        } catch (HibernateException e) {
            e.printStackTrace();
            throw new PerformEntityOperationException("Erro ao buscar entidade por ID. \n" + e.getMessage());
        } finally {
            try {
                if (session.isOpen()) {
                    session.close();
                }
            } catch (HibernateException e) {
                throw new PerformEntityOperationException("Erro ao fechar operação de busca por ID. \n" + e.getMessage());
            }
        }
    }

    /**
     * Retrives all entities of the specified type from the database
     *
     * @return a list of all rows
     */
    public List<T> findAll() {
        Session session = getSession();

        session.beginTransaction();

        Criteria query = session.createCriteria(entityClass);
        List<T> list = query.list();

        session.getTransaction().commit();
        return list;
    }

    /**
     *
     * @param id of entity to be deleted
     * @throws EntityNotFoundException if an entity with provided ID was not
     * founded
     */
    public void deleteById(Serializable id) throws EntityNotFoundException {
        Session session = getSession();
        Transaction transation = session.beginTransaction();

        try {

            T entity = (T) session.get(entityClass, id);

            if (entity == null) {
                throw new PerformEntityOperationException("Entity with ID " + id + " not found");
            }

            session.delete(entity);
            transation.commit();

        } catch (HibernateException e) {
            transation.rollback();
            e.printStackTrace();
            throw new PerformEntityOperationException("Error deleting entity by ID: " + e.getMessage());
        } finally {
            try {
                if (session.isOpen()) {
                    session.close();
                }
            } catch (HibernateException e) {
                throw new PerformEntityOperationException("Error closing session after delete operation: " + e.getMessage());
            }
        }
    }

    /**
     * Delete all rows of the specified entity from the database
     * @throws PerformEntityOperationException if an error occurrs while trying to delete all rows.
     * 
     */
    public void deleteAll() throws PerformEntityOperationException {
        Session session = getSession();
        Transaction transation = session.beginTransaction();

        try {

            String hql = "DELETE FROM " + entityClass.getName();
            session.createQuery(hql)
                    .executeUpdate();

            transation.commit();

        } catch (HibernateException e) {
            transation.rollback();
            e.printStackTrace();
            throw new PerformEntityOperationException("Error deleting all entities: " + e.getMessage());
        } finally {
            try {
                if (session.isOpen()) {
                    session.close();
                }
            } catch (HibernateException e) {
                throw new PerformEntityOperationException("Error closing session after delete operation: " + e.getMessage());
            }
        }
    }
    
 /**
 * Searches for entities based on a specific field and its value.
 *
 * @param fieldName the name of the field to search by.
 * @param value the value of the field to match.
 * @return a list of entities that match the field and value.
 * @throws PerformEntityOperationException if an error occurs during the search.
 */
    public List<T> searchByField(String fieldName, Object value) throws PerformEntityOperationException{
        Session session = getSession();
        Transaction transaction = session.beginTransaction();
        
        try{
            String hql = "FROM " + entityClass.getName() + " WHERE " + fieldName + " = :value";
            List<T> results = session.createQuery(hql)
                    .setParameter("value", value)
                    .list();
            
            transaction.commit();
            return results;
        }catch(HibernateException e){
            transaction.rollback();
            e.printStackTrace();
            throw new PerformEntityOperationException("Error searching by field: " + e.getMessage());
        }finally{
              try {
                if (session.isOpen()) {
                    session.close();
                }
            } catch (HibernateException e) {
                throw new PerformEntityOperationException("Error closing session after search by field: " + e.getMessage());
            }
        }
    }
    
     /**
        * Executes a custom query defined by a lambda function.
        *
        * @param queryFunction a lambda function that defines the custom query logic.
        * @param <R> the type of the result returned by the query.
        * @return the result of the query execution.
        * @throws PerformEntityOperationException if an error occurs during query execution.
    */
    public <R> R createCustom(Function<Session, R> queryFunction){
        Session session = getSession();
        Transaction transaction = null;
        
        try{
            transaction = session.beginTransaction();
           
            R result = queryFunction.apply(session);
            transaction.commit();
            
            return result;
            
    }catch(HibernateException e){
        e.printStackTrace();
        throw new PerformEntityOperationException("Error executing custom query: " + e.getMessage());
    }finally{
              try {
                if (session.isOpen()) {
                    session.close();
                }
            } catch (HibernateException e) {
                throw new PerformEntityOperationException("Error closing session after search by field: " + e.getMessage());
            }
        }
    }
    

}
