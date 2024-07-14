package br.com.kenzley.fiap.service.product.infrastructure.exceptions;

public class ProductNotFoundException extends RuntimeException{
    public ProductNotFoundException(String message){
        super(message);
    }
}
