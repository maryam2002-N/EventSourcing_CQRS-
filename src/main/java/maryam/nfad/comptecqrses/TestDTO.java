package maryam.nfad.comptecqrses;

import maryam.nfad.comptecqrses.commonApi.dtos.CreateAccountRequestDTO;

public class TestDTO {
    public static void main(String[] args) {
        CreateAccountRequestDTO dto = new CreateAccountRequestDTO(1000.0, "USD");
        System.out.println("Initial Balance: " + dto.getInitialBalance());
        System.out.println("Currency: " + dto.getCurrency());
    }
}
