package maryam.nfad.comptecqrses.commonApi.dtos;

import lombok.Data;

@Data
public class DebitAccountRequestDTO {
    private String accountId;
    private double amount;
    private String currency;
}