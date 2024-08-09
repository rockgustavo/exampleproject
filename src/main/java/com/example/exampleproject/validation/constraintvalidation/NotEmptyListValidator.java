package com.example.exampleproject.validation.constraintvalidation;

import java.util.List;

import com.example.exampleproject.rest.dto.ItemPedidoDTO;
import com.example.exampleproject.validation.NotEmptyList;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class NotEmptyListValidator
        implements ConstraintValidator<NotEmptyList, List<ItemPedidoDTO>> {

    @Override
    public boolean isValid(List<ItemPedidoDTO> list,
            ConstraintValidatorContext constraintValidatorContext) {
        return list != null && !list.isEmpty();
    }

    @Override
    public void initialize(NotEmptyList constraintAnnotation) {
    }
}
