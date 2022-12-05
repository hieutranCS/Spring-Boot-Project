package com.spring.security.demo.dto;

import java.math.BigDecimal;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.Digits;
import javax.validation.constraints.NotNull;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@ToString
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TransactionDto {
    private Long id;
    private String date;
    private String comment;
    @DecimalMin(value = "0.0", inclusive = true, message = "Invalid Amount")
    @Digits(integer = 20,fraction= 2, message= "Invalid Amount")
    @NotNull(message = "Amount can not be empty")
    private BigDecimal amount;
    private Long accountNumber;
    private BigDecimal balance;
}
