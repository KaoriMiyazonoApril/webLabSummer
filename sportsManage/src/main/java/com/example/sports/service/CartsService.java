package com.example.sports.service;

import com.example.sports.RRVO.CheckoutRequestVO;
import com.example.sports.RRVO.CheckoutResultVO;
import com.example.sports.exception.SportsException;
import com.example.sports.po.*;
import com.example.sports.repository.*;
import com.example.sports.util.SecurityUtil;
import com.example.sports.RRVO.AddCartResultVO;
import com.example.sports.RRVO.CartResultVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class CartsService {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private CartsRepository cartsRepository;

    @Autowired
    private ActivityRepository activityRepository;

    @Autowired
    private SecurityUtil securityUtil;

    @Autowired
    private OrderDetailRepository orderDetailRepository;
    @Autowired
    private ProductAmountRepository productAmountRepository;

    // 新增商品到当前用户购物车
    public AddCartResultVO addCart(String productId, Integer quantity) {
        Account currentUser = securityUtil.getCurrentUser();
        if (currentUser == null) {
            throw SportsException.notLogin();
        }
        if (currentUser.getId() == null) {
            throw  SportsException.invalidUserId();
        }
        Product product = activityRepository.findById(Integer.valueOf(productId))
                .orElseThrow(SportsException::ProductNotFound);
        Carts existingCartItem = cartsRepository.findByAccountAndProduct(
                currentUser,
                product
        );
        Integer stock=productAmountRepository.findByProductId(product.getId()).getAmount();
        if(stock==null||stock==0){
            throw SportsException.productSoldOut();
        }
        if(stock < quantity) {
            throw SportsException.notEnoughStock();
        }

        if (existingCartItem != null) {
            if(stock < existingCartItem.getQuantity()+quantity){
                throw SportsException.notEnoughStock();
            }
            existingCartItem.setQuantity(existingCartItem.getQuantity() + quantity);
            cartsRepository.save(existingCartItem);
        }else{
            Carts carts = new Carts();
            carts.setAccount(currentUser);
            carts.setProduct(product);
            carts.setQuantity(quantity);
            cartsRepository.save(carts);
            existingCartItem = carts;
        }
        AddCartResultVO resultVO = new AddCartResultVO();
        resultVO.setCartItemId(String.valueOf(existingCartItem.getCartItemId()));
        resultVO.setProductId(productId);
        resultVO.setTitle(product.getTitle());
        resultVO.setPrice(product.getPrice());
        resultVO.setDescription(product.getDescription());
        resultVO.setCover(product.getCover());
        resultVO.setQuantity(existingCartItem.getQuantity());
        return resultVO;
    }

    // 从当前用户购物车删除商品
    public String deleteCart(String cartItemId) {
        Account currentUser = securityUtil.getCurrentUser();
        if (currentUser == null) {
            throw SportsException.notLogin();
        }
        if (currentUser.getId() == null) {
            throw SportsException.invalidUserId();
        }

        Carts cartItem=cartsRepository.findByCartItemId(Integer.valueOf(cartItemId));
        if(cartItem==null){
            return null;
        }
        List<OrderDetail> orderDetails = orderDetailRepository.findByCarts(cartItem);
        if (orderDetails != null && !orderDetails.isEmpty()) {
            orderDetailRepository.deleteAll(orderDetails);
        }
        cartsRepository.delete(cartItem);
        return "删除成功";
    }

    // 更新购物车商品数量
    public Boolean updateCart(String cartItemId, Integer quantity) {
        Account currentUser=securityUtil.getCurrentUser();
        if (currentUser == null) {
            throw SportsException.notLogin();
        }
        if (currentUser.getId() == null) {
            throw SportsException.invalidUserId();
        }
        Carts cartItem=cartsRepository.findByCartItemId(Integer.valueOf(cartItemId));
        if(cartItem==null){
            throw SportsException.cartItemNotFound();
        }
        Product product= activityRepository.findById(cartItem.getProduct().getId())
                .orElseThrow(SportsException::ProductNotFound);
        Integer stock=productAmountRepository.findByProductId(product.getId()).getAmount();
        if(stock==null||stock==0){
            throw SportsException.productSoldOut();
        }
        if(stock<quantity){
            throw SportsException.notEnoughStock();
        }
        cartItem.setQuantity(quantity);
        cartsRepository.save(cartItem);
        return true;
    }

    // 获取当前用户购物车
    public CartResultVO getCart() {
        Account currentUser = securityUtil.getCurrentUser();
        if (currentUser == null) {
            throw SportsException.notLogin();
        }
        List<Carts> cartItems = cartsRepository.findByAccount(currentUser);
        if (cartItems == null) {
            throw SportsException.getItemListFailed();
        }
        List<AddCartResultVO> result = new ArrayList<>();
        double totalAmount = 0;
        int total = 0;
        for (Carts cartItem : cartItems) {
            Product product = activityRepository.findById(cartItem.getProduct().getId())
                    .orElseThrow(SportsException::ProductNotFound);
            AddCartResultVO item = new AddCartResultVO();
            item.setCartItemId(String.valueOf(cartItem.getCartItemId()));
            item.setProductId(String.valueOf(cartItem.getProduct().getId()));
            item.setTitle(product.getTitle());
            item.setPrice(product.getPrice());
            item.setDescription(product.getDescription());
            item.setQuantity(cartItem.getQuantity());
            item.setCover(product.getCover());

            result.add(item);
            totalAmount += cartItem.getQuantity()*product.getPrice();
            total ++;
        }

        CartResultVO cartResult = new CartResultVO();
        cartResult.setItems(result);
        cartResult.setTotalAmount(totalAmount);
        cartResult.setTotal(total);
        return cartResult;
    }

    public CheckoutResultVO checkout(CheckoutRequestVO request) {
        // 1. 获取当前用户
        Account currentUser = securityUtil.getCurrentUser();
        if (currentUser == null) {
            throw SportsException.notLogin();
        }

        // 2. 获取购物车商品
        List<String> cartItemIds = request.getCartItemIds();

        // 3. 验证库存并计算总金额
        double totalAmount = 0;
        for (String cartItemId : cartItemIds) {
            Carts cartItem = cartsRepository.findByCartItemId(Integer.valueOf(cartItemId));
            if (cartItem == null) {
                throw SportsException.cartItemNotFound();
            }
            Product product = cartItem.getProduct();
            if (product == null) {
                throw SportsException.ProductNotFound();
            }
            Integer stock=productAmountRepository.findByProductId(product.getId()).getAmount();
            if (stock < cartsRepository.findByCartItemId
                    (Integer.valueOf(cartItemId)).getQuantity()) {
                throw SportsException.notEnoughStock();
            }

            totalAmount += cartsRepository.findByCartItemId
                    (Integer.valueOf(cartItemId)).getQuantity() * product.getPrice();
        }

        // 4. 创建订单
        Orders order = new Orders();
        order.setAccount(currentUser);
        order.setTotalAmount(totalAmount);
        order.setPaymentMethod(request.getPaymentMethod());
        order.setStatus("PENDING");
        order.setCreateTime(LocalDateTime.now());
        orderRepository.save(order);

        for (String cartItemId : request.getCartItemIds()) {
            Carts cartItem = cartsRepository.findByCartItemId(Integer.valueOf(cartItemId));
            if (cartItem != null) {
                OrderDetail orderDetail = new OrderDetail();
                orderDetail.setOrder(order);
                orderDetail.setProduct(cartItem.getProduct());
                orderDetail.setCarts(cartItem);
                orderDetailRepository.save(orderDetail);
            }
        }

        List<String> sucCartItemIds = new ArrayList<>();

        // 5. 锁定库存
        try {
            for (String cartItemId : cartItemIds) {
                activityRepository.lockStock(cartsRepository.findByCartItemId(Integer.valueOf(cartItemId)).getProduct().getId(),
                        cartsRepository.findByCartItemId(Integer.valueOf(cartItemId)).getQuantity());
                sucCartItemIds.add(cartItemId);
            }
        } catch(SportsException e){
            for (String sucCartItemId : sucCartItemIds) {
                activityRepository.releaseStock(cartsRepository.findByCartItemId(Integer.valueOf(sucCartItemId)).getProduct().getId(),
                        cartsRepository.findByCartItemId(Integer.valueOf(sucCartItemId)).getQuantity());
            }
            throw e;
        }


        // 6. 返回结果
        CheckoutResultVO result = new CheckoutResultVO();
        result.setOrderId(order.getId().toString());
        result.setUsername(currentUser.getUsername());
        result.setTotalAmount(totalAmount);
        result.setPaymentMethod(request.getPaymentMethod());
        result.setCreateTime(order.getCreateTime().toString());
        result.setStatus(order.getStatus());

        return result;
    }

    public CheckoutResultVO cancelOrder(String orderId) {
        // 1. 获取当前用户
        Account currentUser = securityUtil.getCurrentUser();
        if (currentUser == null) {
            throw SportsException.notLogin();
        }

        // 2. 获取订单
        Orders order = orderRepository.findById(Integer.valueOf(orderId))
                .orElseThrow(SportsException::OrderNotFound);

        // 3. 验证订单状态
        if (!"PENDING".equals(order.getStatus())) {
            throw SportsException.OrderStatusError();
        }

        // 4. 释放库存
        List<OrderDetail> orderDetails = orderDetailRepository.findByOrder(order);
        for (OrderDetail detail : orderDetails) {
            activityRepository.releaseStock(detail.getProduct().getId(), detail.getCarts().getQuantity());
        }

        // 5. 更新订单状态
        order.setStatus("CANCELED");
        orderRepository.save(order);

        // 6. 返回结果
        CheckoutResultVO result = new CheckoutResultVO();
        result.setOrderId(order.getId().toString());
        result.setUsername(currentUser.getUsername());
        result.setTotalAmount(order.getTotalAmount());
        result.setPaymentMethod(order.getPaymentMethod());
        result.setCreateTime(order.getCreateTime().toString());
        result.setStatus(order.getStatus());

        return result;
    }
}
