package com.example.shopapp.service.product;

import com.example.shopapp.Response.product.ProductResponse;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductRedisService {

    private final ObjectMapper redisObjectMapper;

    private final RedisTemplate<String, Object> redisTemplate;

    @Value("${spring.data.redis.use-redis-cache}")
    private boolean useRedisCache;

    public List<ProductResponse> getAllProducts(String keyword,
                                                Long categoryId,
                                                PageRequest pageRequest) throws JsonProcessingException {
        if(useRedisCache == false) {
            return null;
        }

        String key = this.getKeyFrom(keyword, categoryId, pageRequest);
        String json = (String) redisTemplate.opsForValue().get(key);
        List<ProductResponse> productResponses =
                json != null ?
                        redisObjectMapper.readValue(json, new TypeReference<List<ProductResponse>>() {})
                        : null;
        return productResponses;
    }

    private String getKeyFrom(String keyword,
                              Long categoryId,
                              PageRequest pageRequest) {
        int pageNumber = pageRequest.getPageNumber();
        int pageSize = pageRequest.getPageSize();
        Sort sort = pageRequest.getSort();
        String sortDirection = sort.getOrderFor("id")
                .getDirection() == Sort.Direction.ASC ? "asc": "desc";
        String key = String.format("all_products:%s:%d:%d:%d:%s",
                keyword, categoryId, pageNumber, pageSize, sortDirection);
        return key;
    }

    public void saveAllProduct(List<ProductResponse> productResponses, String keyword, Long category, PageRequest pageRequest ) throws JsonProcessingException {
        String key = this.getKeyFrom(keyword, category, pageRequest);
        String json = redisObjectMapper.writeValueAsString(productResponses);
        redisTemplate.opsForValue().set(key,json);
    }

    public void clear(){
        redisTemplate.getConnectionFactory().getConnection().flushAll();
    }
}
