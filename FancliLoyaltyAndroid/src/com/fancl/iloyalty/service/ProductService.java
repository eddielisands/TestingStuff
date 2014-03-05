package com.fancl.iloyalty.service;

import java.util.List;

import com.fancl.iloyalty.exception.FanclException;
import com.fancl.iloyalty.pojo.IchannelMagazine;
import com.fancl.iloyalty.pojo.Product;
import com.fancl.iloyalty.pojo.ProductAnswer;
import com.fancl.iloyalty.pojo.ProductCategory;
import com.fancl.iloyalty.pojo.ProductChoice;
import com.fancl.iloyalty.pojo.ProductQuestion;
import com.fancl.iloyalty.pojo.ProductSeries;
import com.fancl.iloyalty.pojo.Promotion;
import com.fancl.iloyalty.pojo.Setting;

public interface ProductService {
	public List<ProductSeries> getProductSeriesList() throws FanclException;
	
	public List<ProductCategory> getProductPartentCategoryListWithSeriesId(String seriesId) throws FanclException;
	
	public List<ProductCategory> getProductSubCategoryListWithParentId(String parentId) throws FanclException;
	
	public List<Product> getProductListWithCategoryId(String categoryId) throws FanclException;
	
	public Product getProductDetailWithProductId(String productId) throws FanclException;
	
	public List<ProductChoice> getProductChoiceWithProductId(String productId) throws FanclException;
	
	public ProductCategory getProductCategoryWithProductId(String productId) throws FanclException;
	
	public List<IchannelMagazine> getRelatedArticleWithProductId(String productId) throws FanclException;
	
	public List<Promotion> getRelatedPromotionWithProductId(String productId) throws FanclException;
	
	public List<Product> getProductSearchResultWithKeyword(String keyword) throws FanclException;
	
	public Setting getSesaonalDescription() throws FanclException;
	
	public List<ProductCategory> getSeasonalProductCategory() throws FanclException;
	
	public List<Product> getSeasonalProductWithCategoryId(String categoryId) throws FanclException;
	
	public List<ProductQuestion> getQnaProductQuestion() throws FanclException;
	
	public List<ProductAnswer> getQnaAnswerWithQuestionId(String questionId) throws FanclException;
	
	public void saveUserQnaAnwserId(List<String> userAnswerId, List<String> userAnswerCode);
	
	public List<ProductAnswer> getUserQnaAnswer() throws FanclException;
	
	public List<Product> getUserQnaSuggestProduct() throws FanclException;
	
	public List<ProductCategory> getUserQnaSuggestProductCategory() throws FanclException;
	
	public List<Product> getUserQnaSuggestProductWithCategoryId(String categoryId) throws FanclException;
	
	
}
