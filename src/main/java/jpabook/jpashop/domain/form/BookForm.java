package jpabook.jpashop.domain.form;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Getter @Setter
public class BookForm {

    private Long id;

    @NotEmpty
    private String name;

    @NotNull
    private int price;

    @NotNull
    private int stockQuantity;

    @NotEmpty
    private String author;

    @NotEmpty
    private String isbn;

}
