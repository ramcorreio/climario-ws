package br.com.climario.persistence;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceException;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.metamodel.Metamodel;
import javax.validation.Validation;
import javax.validation.Validator;

import org.hibernate.exception.ConstraintViolationException;
import org.springframework.beans.BeanUtils;
import org.springframework.dao.DataIntegrityViolationException;

public class BaseManager {

    protected EntityManager entityManager;

    private final Validator validator;

    protected BaseManager() {
        validator = Validation.buildDefaultValidatorFactory().getValidator();
    }
    
    public void setEntityManager(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    protected <T> T get(Class<T> clazz, Number id) {
        return entityManager.find(clazz, id);
    }

    /**
     * Copiar apenas dados primitivos
     * 
     * @param dest
     * @param src
     */
    protected void copyProperties(Object dest, Object src) {
        copyProperties(dest, src, new String[0]);
    }

    protected void copyPropertiesSemId(Object dest, Object src) {
        copyProperties(dest, src, new String[] { "id" });
    }

    protected void copyProperties(Object dest, Object src, String[] excludes) {
        BeanUtils.copyProperties(src, dest, excludes);
    }

    private void throwsException(Throwable e) {
        
        if (ConstraintViolationException.class.isInstance(e.getCause())) 
        {
            throw new ViolationException(e.getCause());
        } 
        else if (DataIntegrityViolationException.class.isInstance(e.getCause())) 
        {
            throw new DuplicateKeyException(e.getCause());
        } 
        else
        {
            throw new PersistenceException(e);
        }
    }

    protected <T> void create(T entity) {
        try {
            validator.validate(entity);
            entityManager.persist(entity);
        } catch (IllegalArgumentException | PersistenceException e) {
            throwsException(e);
        }
    }

    protected <T> TypedQuery<T> createNamedQuery(String q, Class<T> clazz) {
        return entityManager.createNamedQuery(q, clazz);
    }

    protected <T> T update(T entity) {
        try {
            validator.validate(entity);
            entity = entityManager.merge(entity);
        } catch (IllegalArgumentException | PersistenceException e) {
            throwsException(e);
        }

        return entity;
    }

    protected <T> void remove(T entity) {
        try {
            entityManager.remove(entity);
        } catch (IllegalArgumentException e) {
            throwsException(e);
        }
    }

    protected <T> boolean contains(T entity) {
        return entityManager.contains(entity);
    }
    
    protected CriteriaBuilder getCriteriaBuilder() {
        return entityManager.getCriteriaBuilder();
    }

    protected <T> TypedQuery<T> createCriteriaQuery(CriteriaQuery<T> criteriaQuery) {
        return entityManager.createQuery(criteriaQuery);
    }

    protected Metamodel getMetamodel() {
        return entityManager.getMetamodel();
    }
}