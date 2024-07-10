package br.com.kenzley.fiap.service.product.infrastructure.exceptions;

import lombok.*;

import java.util.Date;
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@EqualsAndHashCode
public class ExceptionDTO {
    private Date timestamp = new Date();
    private String status;
    private int statusCode;
    private String error;
}
