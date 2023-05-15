package jpabook.jpashop.web;

import jpabook.jpashop.domain.Book;
import jpabook.jpashop.domain.Item;
import jpabook.jpashop.domain.form.BookForm;
import jpabook.jpashop.service.ItemService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@Slf4j
@Controller
@RequiredArgsConstructor
@RequestMapping("/items")
public class ItemController {
    private final ItemService itemService;

    @GetMapping("/new")
    public String addForm(Model model) {
        BookForm bookForm = new BookForm();
        model.addAttribute("bookForm", bookForm);
        return "items/createBookForm";
    }

    @PostMapping("/new")
    public String addItem(@Valid @ModelAttribute("bookForm") BookForm bookForm, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "items/createBookForm";
        } else {
            Book book = new Book();
            book.setName(bookForm.getName());
            book.setStockQuantity(bookForm.getStockQuantity());
            book.setPrice(bookForm.getPrice());
            book.setIsbn(bookForm.getIsbn());
            book.setAuthor(bookForm.getAuthor());

            itemService.save(book);
            return "redirect:/items";
        }
    }

    @GetMapping
    public String itemList(Model model) {
        List<Item> items = itemService.findAll();
        model.addAttribute("items", items);
        return "items/itemList";
    }

    @GetMapping("/{itemId}/edit")
    public String bookEditForm(@PathVariable("itemId") Long itemId, Model model) {
        Book findItem = (Book) itemService.findById(itemId);

        BookForm bookForm = new BookForm();
        bookForm.setId(findItem.getId());
        bookForm.setAuthor(findItem.getAuthor());
        bookForm.setIsbn(findItem.getIsbn());
        bookForm.setPrice(findItem.getPrice());
        bookForm.setStockQuantity(findItem.getStockQuantity());
        bookForm.setName(findItem.getName());

        model.addAttribute("bookForm", bookForm);
        return "items/updateItemForm";
    }

    @PostMapping("/{itemId}/edit")
    public String bookEdit(@PathVariable("itemId") Long itemId, @Valid @ModelAttribute("bookForm") BookForm bookForm, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "items/updateItemForm";
        }
        itemService.updateBook(itemId, bookForm);
        return "redirect:/items";
    }
}
