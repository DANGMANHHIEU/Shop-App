package com.example.shopapp.service.category;

import com.example.shopapp.dto.request.CategoryDTO;
import com.example.shopapp.models.Category;
import com.example.shopapp.repositories.CategoryRepository;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@AllArgsConstructor
@NoArgsConstructor
public class CategoryService implements ICategoryService {

    @Autowired
    private CategoryRepository categoryRepository;

    /**
     * Thêm mới danh mục sản phẩm
     **/
    @Override
    @Transactional
    public Category createCategory(CategoryDTO category) {
        Category newCategory = Category.builder().name("a").build();
        return categoryRepository.save(newCategory);

    }

    /**
     * lấy lên 1 danh mục sản phẩm
     *
     * @param id Id danh mục
     * @return danh mục sản phẩm
     */
    @Override
    public Category getCategoryById(long id) {
        return categoryRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Category not found"));
    }

    @Override
    @Transactional
    public List<Category> getAllCategories() {
        return null;
    }

    @Override
    @Transactional
    public Category updateCategory(long categoryId, CategoryDTO category) {
        Category categoryUpdate = getCategoryById(categoryId);
        categoryUpdate.setName(categoryUpdate.getName());
        categoryRepository.save(categoryUpdate);
        return categoryUpdate;
    }

    @Override
    @Transactional
    public Category deleteCategory(long id) throws Exception {
        Category category = getCategoryById(id);
        categoryRepository.delete(category);
        return null;
    }

}
