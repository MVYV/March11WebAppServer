package com.mvyv.march11webapp.service;

import com.mvyv.march11webapp.domain.Product;
import com.mvyv.march11webapp.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class ProductService {

  private final ProductRepository productRepository;

  @Autowired
  public ProductService(ProductRepository productRepository) {
    this.productRepository = productRepository;
  }

  public List<Product> getAll() {
    return productRepository.findAll();
  }

  public Optional<Product> getById(Long id) {
    return Optional.ofNullable(productRepository.getOne(id));
  }

  public List<Product> getByProductType(String type) {
    return productRepository.findAllByProductType(type);
  }

  public List<Product> getAllByLanguage(String language) {
    return productRepository.findAllByLanguage(language);
  }

  public Product save(Product product) {
    if (product.getId() == null) product.setProductDate(new Date());
    else product.setProductModificationDate(new Date());
    return productRepository.save(product);
  }

  public void delete(Long id) {
    productRepository.deleteById(id);
  }
}
