package com.example.sports.vo;


import com.example.sports.po.Carts;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter
@Setter
@NoArgsConstructor
public class CartsVO {
    private Integer cartItemId;
    private ProductVO product;
    private AccountVO account;
    private Integer quantity;

    public Carts toPO() {
        Carts carts = new Carts();
        carts.setCartItemId(cartItemId);
        carts.setQuantity(quantity);
        if (product != null) {
            carts.setProduct(product.toPO());
        }
        if (account != null) {
            carts.setAccount(account.toPO());
        }
        return carts;
    }

}
