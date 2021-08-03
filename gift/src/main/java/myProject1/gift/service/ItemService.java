package myProject1.gift.service;

import lombok.RequiredArgsConstructor;
import myProject1.gift.domain.Category;
import myProject1.gift.domain.Item;
import myProject1.gift.dto.ItemDto;
import myProject1.gift.repository.ItemRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class ItemService {
    private final ItemRepository itemRepository;

    public Long createItem(ItemDto itemDto){
        Item item = Item.createItem(itemDto.getName(), itemDto.getPrice(), itemDto.getStockQuantity(), itemDto.getCategory());
        Long itemId = itemRepository.save(item);
        return itemId;
    }

    public void updateItem(Long itemId, String itemName, int itemPrice, int itemStockQuantity, Category category){
        itemRepository.update(itemId, itemName, itemPrice, itemStockQuantity, category);
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
