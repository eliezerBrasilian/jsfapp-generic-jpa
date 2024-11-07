package _jpa;


/**
 * The BaseRepository class extends GenericDao to provide a base implementation
 * for repository operations specific to the entity type T. This class can be
 * further extended to implement more specific repository functionality.
 *
 * @param <T> the type of the entity to be managed by this repository.
 */
public class BaseRepository<T> extends GenericDao<T> {
	
	 /**
     * Constructs a BaseRepository for the specified entity class.
     *
     * @param entityClass the class of the entity to be managed by this repository.
     */
	public BaseRepository(Class<T> entityClass) {
		super(entityClass);
	}
}
