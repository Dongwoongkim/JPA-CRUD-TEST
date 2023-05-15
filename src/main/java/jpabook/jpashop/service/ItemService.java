package jpabook.jpashop.service;

import jpabook.jpashop.domain.Book;
import jpabook.jpashop.domain.Item;
import jpabook.jpashop.domain.form.BookForm;
import jpabook.jpashop.repository.ItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ItemService {

    private final ItemRepository itemRepository;

    @Transactional
    public void save(Item item) {
        itemRepository.save(item);
    }

    public Item findById(Long id) {
        return itemRepository.findById(id);
    }

    public List<Item> findByName(String name) {
        return itemRepository.findByName(name);
    }

    public List<Item> findAll() {
        return itemRepository.findAll();
    }

    @Transactional
    public void updateBook(Long itemId, BookForm bookForm) {
        Book findItem = (Book) itemRepository.findById(itemId);
        findItem.setName(bookForm.getName());
        findItem.setAuthor(bookForm.getAuthor());
        findItem.setIsbn(bookForm.getIsbn());
        findItem.setPrice(bookForm.getPrice());
        findItem.setStockQuantity(bookForm.getStockQuantity());

//        itemRepository.save(findItem);
    }
}
