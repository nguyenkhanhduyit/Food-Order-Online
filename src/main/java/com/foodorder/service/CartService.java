package com.foodorder.service;

import com.foodorder.dto.request.CartItemRequest;
import com.foodorder.dto.response.CartItemResponse;
import com.foodorder.dto.response.CartResponse;
import com.foodorder.exception.declare.ResourceNotAvailableException;
import com.foodorder.mapper.CartItemMapper;
import com.foodorder.mapper.CartMapper;
import com.foodorder.mapper.IngredientItemMapper;
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
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

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
    UserService userService;
    IngredientItemMapper ingredientItemMapper;

    @Override
    @Transactional(isolation = Isolation.SERIALIZABLE,timeout = 20,rollbackFor = Exception.class,readOnly = true)
    public CartResponse getCartFromUser(String token) {
        /*method get cart from owner*/
        Cart cart = userService.findUserByToken(token).getCart();
        return toCartResponse(cart);
    }

    @Override
    @Transactional(isolation = Isolation.SERIALIZABLE,timeout = 20,rollbackFor = Exception.class,readOnly = true)
    public int getTotalQuantityItemInCart(String token) {
        /*method get number of quantity from cart*/
        return userService.findUserByToken(token).getCart().getTotalItem();
    }

    @Override
    @Transactional(isolation = Isolation.SERIALIZABLE, timeout = 20, rollbackFor = {Exception.class,})
    public CartResponse addCartItemToCart(String token, CartItemRequest request) {
        //mapped quantity
        CartItem cartItem = cartItemMapper.toCartItem(request);
        //assign food for it
        cartItem.setFood(foodRepository.findById(request.getFoodId())
                .orElseThrow(()-> new ResourceNotAvailableException("Food not found")));
        //calculate total price dependence on quantity and price of food
        cartItem.setTotalPrice(BigDecimal.valueOf(request.getQuantity()).multiply(cartItem.getFood().getPrice()));
        //find and assign ingredient item for it
        List<IngredientItem> ingredientItems = request.getIngredientItemsId()
                        .stream()
                        .map(
            id -> ingredientItemRepository.findById(id)
            .orElseThrow(()-> new ResourceNotAvailableException("Ingredient Item not found")))
                        .toList();
        cartItem.setIngredientItems(
                ingredientItems.stream()
                        .collect(Collectors.toMap(
                                IngredientItem::getId,
                                item -> item,
                                (item1,item2) -> item1
                        ))
                        .values()
                        .stream()
                        .toList()
        );
        //get cart from token
        Cart cart = userService.findUserByToken(token).getCart();
        //check whether food existed in cart
        Optional<CartItem> cartItemIsExist = cart.getCartItems()
                .stream()
                .filter(item ->
                        {
                            if (!item.getFood().getId().equals(cartItem.getFood().getId()))
                                return false;
                            List<Long> existingIngredientIds = item.getIngredientItems()
                                    .stream()
                                    .map(IngredientItem::getId)
                                    .sorted()
                                    .toList();

                            List<Long> newIngredientIds = ingredientItems
                                    .stream()
                                    .map(IngredientItem::getId)
                                    .sorted()
                                    .toList();
                            return existingIngredientIds.equals(newIngredientIds);
                        }
                )
                .findFirst();

        if(cartItemIsExist.isPresent()) {
            CartItem existingItem = cartItemIsExist.get();
            BigDecimal newPrice = existingItem.getTotalPrice().add(cartItem.getTotalPrice());
            existingItem.setTotalPrice(newPrice);
            existingItem.setQuantity(existingItem.getQuantity() + cartItem.getQuantity());
        } else {
            cartItem.setCart(cart);
            cart.getCartItems().add(cartItem);
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
    public CartResponse removeCartItemFromCart(String token,Long cartItemId) {
        Cart cart = userService.findUserByToken(token).getCart();
        cart.getCartItems().remove(cartItemRepository.findById(cartItemId)
                .orElseThrow(()-> new ResourceNotAvailableException("Cart Item not found")));
        cart.setTotalPrice(calculatorTotalPriceForCart(cart.getCartItems()));
        cart.setTotalItem(calculatorTotalItemForCart(cart.getCartItems()));
        return toCartResponse(cartRepository.save(cart));
    }

    @Override
    public CartResponse removeAllCartItemsFromCart(String token) {
        Cart cart = userService.findUserByToken(token).getCart();
        cart.getCartItems().clear();
        cart.setTotalPrice(calculatorTotalPriceForCart(cart.getCartItems()));
        cart.setTotalItem(calculatorTotalItemForCart(cart.getCartItems()));
        return toCartResponse(cartRepository.save(cart));
    }

    private CartResponse toCartResponse(Cart request){
        /*method convert cart to cart response*/
        CartResponse cartResponse = cartMapper.toResponse(cartRepository.save(request));
        List<CartItemResponse> cartItemResponses = request.getCartItems()
                .stream()
                .map(this::toCartItemResponse)
                .toList();
        cartResponse.setCartItems(cartItemResponses);
        return cartResponse;
    }

    private CartItemResponse toCartItemResponse(CartItem request){
        /*method convert cart item to cart item response*/
        CartItemResponse cartItemResponse = cartItemMapper.toResponse(request);
        cartItemResponse.setFoodImageUrl(request.getFood().getImageUrl());
        cartItemResponse.setFoodName(request.getFood().getName());
        cartItemResponse.setIngredientItemResponses(
                request.getIngredientItems()
                        .stream()
                        .map(ingredientItemMapper::toResponse)
                        .toList());
        return cartItemResponse;
    }

}
