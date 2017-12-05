package de.hpi.semrecsys;

import javax.persistence.AttributeOverride;
import javax.persistence.AttributeOverrides;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;

import de.hpi.semrecsys.model.Product;
import de.hpi.semrecsys.persistence.ProductDAO;
import de.hpi.semrecsys.utils.StringUtils;

@Entity
@Table(name = "recommendation")
public abstract class RecommendationImpl extends BaseEntity implements Recommendation {

	protected RecommendationId id;
	protected int linkedProductId;
	private Double score;
	protected Double relativeScore;

	public RecommendationImpl() {
		super();
	}

	public RecommendationImpl(RecommendationId id, int linkedProductId) {
		id.setType(recommendationType());
		this.id = id;
		this.linkedProductId = linkedProductId;
	}

	public abstract String recommendationType();

	@EmbeddedId
	@AttributeOverrides({
			@AttributeOverride(name = "productId", column = @Column(name = "product_id", nullable = false)),
			@AttributeOverride(name = "type", column = @Column(name = "type", nullable = false, length = 32)),
			@AttributeOverride(name = "position", column = @Column(name = "position", nullable = false)) })
	public RecommendationId getId() {
		return this.id;
	}

	public void setId(RecommendationId id) {
		id.setType(recommendationType());
		this.id = id;
	}

	@Column(name = "linked_product_id", nullable = false)
	public int getLinkedProductId() {
		return this.linkedProductId;
	}

	public void setLinkedProductId(int linkedProductId) {
		this.linkedProductId = linkedProductId;
	}

	@Override
	public String toString() {
		String result = recommendationType() + " " + id.getPosition() + " : " + recommendedProduct()
				+ recommendationScoreToString() + "\n";
		return result;
	}

	public Product recommendedProduct() {
		return ProductDAO.getDefault().findById(getLinkedProductId());
	}

	public String recommendationScoreToString() {

		String result = " ";
		if (score != null && relativeScore != null) {
			result = "(score = " + StringUtils.doubleToString(score) + "; relative = "
					+ StringUtils.doubleToString(relativeScore) + ")";
		}
		return result;
	}

	@Override
	@Column(name = "score", precision = 22, scale = 0)
	public Double getScore() {
		return this.score;
	}

	public void setScore(Double score) {
		this.score = score;
	}

	@Override
	@Column(name = "relative_score", precision = 22, scale = 0)
	public Double getRelativeScore() {
		return this.relativeScore;
	}

	public void setRelativeScore(Double relativeScore) {
		this.relativeScore = relativeScore;
	}

	public boolean empty() {
		return recommendedProduct() == null || recommendedProduct().getTitle() == null;
	}

}