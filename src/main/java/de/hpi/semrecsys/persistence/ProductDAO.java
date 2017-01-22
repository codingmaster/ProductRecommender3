package de.hpi.semrecsys.persistence;

import java.io.Serializable;
import java.util.List;
import java.util.Random;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;

import de.hpi.semrecsys.DBObject;
import de.hpi.semrecsys.ProductTable;
import de.hpi.semrecsys.model.Product;

/**
 * Database functionality for category {@link de.hpi.semrecsys.ProductTable}
 */
public class ProductDAO extends AbstractDAO {

	private static ProductDAO productManager;
	int maxId;
	int minId;

	public static ProductDAO getDefault() {
		if (productManager == null) {
			productManager = new ProductDAO();
			productManager.maxId = productManager.getMaxId();
			productManager.minId = productManager.getMinId();
		}
		return productManager;
	}

	private ProductDAO() {

	}

    /**
     * returns random product
     * @return
     */
	public Product getRandom() {
		Product product = null;
		while (product == null || product.getTitle() == null) {
			Random rand = new Random();
			int productId = productManager.minId + rand.nextInt(productManager.maxId - productManager.minId);
			product = findById(productId);
		}
		return product;
	}

	@Override
	public Product findById(Serializable serProductId) {
		Integer id = (Integer) serProductId;
		List<ProductTable> productLines = findProductLines(id);
		Product product = new Product(id, productLines);

		return product;
	}


    /**
     * find product by name
     * @param name
     * @return product with name
     */
	public Product findByName(String name) {
		List<ProductTable> productLines = findProductLines("name", name);
		Integer id = productLines.get(0).getId().getEntityId();
		Product product = findById(id);
		return product;
	}

	private List<ProductTable> findProductLines(String attributeCode, String value) {
		Session session = getSession();
		Criteria criteria = session.createCriteria(getType());
		criteria.add(Restrictions.eq("id.attributeCode", attributeCode));
		if (value != null && !value.isEmpty()) {
			criteria.add(Restrictions.eq("value", value));
		}
		@SuppressWarnings("unchecked")
		List<ProductTable> result = criteria.list();
		return result;
	}

    /**
     * find all color values from the product table
     * @return color names
     */
	public List<String> getAllColors() {
		Session session = getSession();
		String hql = "select distinct value from " + getType().getSimpleName()
				+ " where id.attributeCode like '%color%' ";
		@SuppressWarnings("unchecked")
		List<String> result = session.createQuery(hql).list();

		return result;
	}

	private List<ProductTable> findProductLines(int productId) {
		Session session = getSession();
		Criteria criteria = session.createCriteria(getType());
		criteria.add(Restrictions.eq("id.entityId", productId));
		@SuppressWarnings("unchecked")
		List<ProductTable> result = criteria.list();
		commitTransaction(session);
		return result;
	}

	@Override
	public DBObject persist(DBObject instance) {
		List<DBObject> products = findByExample(instance);
		DBObject result;
		if (isEntityExists(instance)) {
			result = products.get(0);
		} else {
			result = super.persist(instance);
		}
		return result;
	}

	@Override
	protected Class<ProductTable> getType() {
		return ProductTable.class;
	}

    /**
     * get maximal product id
     * @return max product id
     */
	public int getMaxId() {
		Session session = getSession();
		String hql = "select max(id.entityId) from " + getType().getSimpleName();
		Integer size = ((Integer) session.createQuery(hql).uniqueResult()).intValue();
		return size;
	}

    /**
     * get minimal product id
     * @return minimal product id
     */
	public int getMinId() {
		Session session = getSession();
		String hql = "select min(id.entityId) from " + getType().getSimpleName();
		Integer size = ((Integer) session.createQuery(hql).uniqueResult()).intValue();
		return size;
	}

    /**
     * get number of products in the database
     * @return number of products
     */
	public long getProductSize() {
		Session session = getSession();
		String hql = "select count( distinct id.entityId) from " + getType().getSimpleName();
		Long size = ((Long) session.createQuery(hql).uniqueResult()).longValue();
		return size;

	}

}
