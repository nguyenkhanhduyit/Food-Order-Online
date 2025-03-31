package com.foodorder.service;

import com.foodorder.dto.request.CartItemRequest;
import com.foodorder.dto.response.CartItemResponse;
import com.foodorder.dto.response.CartResponse;
import com.foodorder.exception.declare.ResourceNotAvailableException;
import com.foodorder.mapper.CartItemMapper;
import com.foodorder.mapper.CartMapper;
import com.foodorder.model.Cart;
import com.foodorder.model.CartItem;
import com.foodorder.model.IngredientItem;
import com.foodorder.repository.CartItemRepository;
import com.foodorder.repository.CartRepository;
import com.foodorder.repository.FoodRepository;
import com.foodorder.repository.IngredientItemRepository;
import com.foodorder.service.iservice.ICartService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class CartService implements ICartService {

    CartRepository cartRepository;
    CartItemMapper cartItemMapper;
    FoodRepository foodRepository;
    IngredientItemRepository ingredientItemRepository;
    CartMapper cartMapper;
    CartItemRepository cartItemRepository;

    @Override
    @Transactional(isolation = Isolation.SERIALIZABLE,
    timeout = 3,
    rollbackFor = {
            Exception.class,
            ResourceNotAvailableException.class
    })
    public CartResponse addCartItemToCart(Long cartId, CartItemRequest request) {

        CartItem cartItem = cartItemMapper.toCartItem(request);
        cartItem.setFood(foodRepository.findById(request.getFoodId())
                .orElseThrow(()-> new ResourceNotAvailableException("Food not found")));

        List<IngredientItem> ingredientItems = request.getIngredientItemsId()
                        .stream()
                                .map(
                                        id -> ingredientItemRepository.findById(id)
                                                .orElseThrow(()-> new ResourceNotAvailableException("Ingredient Item not found"))
                                )
                                        .toList();

        cartItem.setIngredientItems(ingredientItems);

        Cart cart = cartRepository.findById(cartId)
                .orElseThrow((()-> new ResourceNotAvailableException("Cart not found")));

        Optional<CartItem> cartItemIsExist = cart.getCartItems()
                .stream()
                .filter(item -> item.getFood().getId().equals(cartItem.getFood().getId()))
                .findFirst();

        if(cartItemIsExist.isPresent()){
            CartItem item = cartItemIsExist.get();
            BigDecimal newPrice = item.getTotalPrice().add(cartItem.getTotalPrice());
            item.setTotalPrice(newPrice);
            item.setQuantity(item.getQuantity() + cartItem.getQuantity());
        }else{
            cart.getCartItems().add(cartItem);
            cartItem.setCart(cart);
        }
        cart.setTotalPrice(calculatorTotalPriceForCart(cart.getCartItems()));
        cart.setTotalItem(calculatorTotalItemForCart(cart.getCartItems()));
        return toCartResponse(cartRepository.save(cart));
    }


    private BigDecimal calculatorTotalPriceForCart(List<CartItem> cartItems){
       return cartItems.stream().map(CartItem::getTotalPrice)
                .reduce(BigDecimal.ZERO,BigDecimal::add);
    }
    private int calculatorTotalItemForCart(List<CartItem> cartItems){
        return cartItems == null ? 0 : cartItems.size();
    }

    @Override
    public CartResponse removeCartItemFromCart(Long cartId, Long cartItemId) {
        Cart cart = cartRepository.findById(cartId)
                .orElseThrow(()-> new ResourceNotAvailableException("Cart not found"));
        cart.getCartItems().remove(cartItemRepository.findById(cartItemId)
                .orElseThrow(()-> new ResourceNotAvailableException("Cart Item not found")));
        cart.setTotalPrice(calculatorTotalPriceForCart(cart.getCartItems()));
        cart.setTotalItem(calculatorTotalItemForCart(cart.getCartItems()));
        return toCartResponse(cartRepository.save(cart));
    }


    @Override
    public CartResponse getCartByUserId(Long userId) {
        Cart cart = cartRepository.findByUserId(userId)
                .orElseThrow(()-> new ResourceNotAvailableException("User not have cart"));
        return toCartResponse(cart);
    }

    @Override
    public boolean checkUserOwnerCart(String userName,Long cartId) {
        Optional<Cart> cart = cartRepository.checkUserOwnCart(userName,cartId);
        return cart.isPresent();
    }

    @Override
    public CartResponse getCartById(Long cartId) {
        Cart cart = cartRepository.findById(cartId)
                .orElseThrow(()-> new ResourceNotAvailableException("Cart not found"));
        return toCartResponse(cart);
    }


    @Override
    public List<CartItemResponse> getAllCartItemsFromCart(Long cartId) {
        Cart cart = cartRepository.findById(cartId)
                .orElseThrow(()-> new ResourceNotAvailableException("Cart not found"));
        List<CartItem> cartItems = cart.getCartItems();
        return cartItems.stream().map(cartItem -> {
            CartItemResponse cartItemResponse = cartItemMapper.toResponse(cartItem);
            cartItemResponse.setFoodName(cartItem.getFood().getName());
            cartItemResponse.setImages(cartItem.getFood().getImages());
            cartItem.getIngredientItems().forEach(
                    ingredientItem ->
                        cartItemResponse.getNameIngredientItems().add(ingredientItem.getName())
            );
            return cartItemResponse;
        }).toList();
    }

    @Override
    public boolean removeAllCartItemsFromCart(Long cartId) {
        Cart cart = cartRepository.findById(cartId)
                .orElseThrow(()-> new ResourceNotAvailableException("Cart not found"));
        cart.getCartItems().clear();
        cartRepository.save(cart);
        return true;
    }

    private CartResponse toCartResponse (Cart request){
        CartResponse cartResponse = cartMapper.toResponse(cartRepository.save(request));
        List<CartItemResponse> cartItemResponses = new ArrayList<>();
        request.getCartItems().forEach(
                item -> {
                    cartItemResponses.add(
                            CartItemResponse.builder()
                                    .foodName(item.getFood().getName())
                                    .quantity(item.getQuantity())
                                    .totalPrice(item.getTotalPrice())
                                    .nameIngredientItems(
                                            item.getIngredientItems().stream().map(IngredientItem::getName).toList()
                                    )
                                    .images(item.getFood().getImages())
                                    .build());
                }
        );
        cartResponse.setCartItems(cartItemResponses);
        return cartResponse;
    }
}
