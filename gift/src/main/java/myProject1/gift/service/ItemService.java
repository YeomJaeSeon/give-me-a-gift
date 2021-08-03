package myProject1.gift.service;

import lombok.RequiredArgsConstructor;
import myProject1.gift.domain.Category;
import myProject1.gift.domain.Item;
import myProject1.gift.dto.ItemDto;
import myProject1.gift.repository.CategoryRepository;
import myProject1.gift.repository.ItemRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class ItemService {
    private final ItemRepository itemRepository;
    private final CategoryRepository categoryRepository;

    public Long createItem(ItemDto itemDto){
        Category category = null;
        if(itemDto.getCategory() != null) category = categoryRepository.findById(itemDto.getCategory());

        Item item = Item.createItem(itemDto.getName(), itemDto.getPrice(), itemDto.getStockQuantity(), category);
        Long itemId = itemRepository.save(item);
        return itemId;
    }

    public void updateItem(Long itemId, ItemDto itemDto){
        Category category = null;
        if(itemDto.getCategory() != null) category = categoryRepository.findById(itemDto.getCategory());
        itemRepository.update(itemId, itemDto.getName(), itemDto.getPrice(), itemDto.getStockQuantity(), category);
    }

    public Long deleteItem(Item item){
        Long deleteId = itemRepository.delete(item);
        return deleteId;
    }

    @Transactional(readOnly = true)
    public Item findById(Long id){
        return itemRepository.findOne(id);
    }

    @Transactional(readOnly = true)
    public List<Item> findItems(Category category){
        List<Item> items = itemRepository.findAll(category);
        return items;
    }
}
