package de.hpi.semrecsys;

// default package
// Generated Jun 23, 2014 2:32:33 PM by Hibernate Tools 3.4.0.CR1

/**
 * ProductCategoryId generated by hbm2java
 */
public class ProductCategoryId implements java.io.Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private int productId;
	private int categoryId;

	public ProductCategoryId() {
	}

	public ProductCategoryId(int productId, int categoryId) {
		this.productId = productId;
		this.categoryId = categoryId;
	}

	public int getProductId() {
		return this.productId;
	}

	public void setProductId(int productId) {
		this.productId = productId;
	}

	public int getCategoryId() {
		return this.categoryId;
	}

	public void setCategoryId(int categoryId) {
		this.categoryId = categoryId;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + categoryId;
		result = prime * result + productId;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ProductCategoryId other = (ProductCategoryId) obj;
		if (categoryId != other.categoryId)
			return false;
		if (productId != other.productId)
			return false;
		return true;
	}

}
