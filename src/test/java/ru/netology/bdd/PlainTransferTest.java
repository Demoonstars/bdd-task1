package ru.netology.bdd;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.netology.bdd.data.DataHelper;
import ru.netology.bdd.page.LoginPage;

import static com.codeborne.selenide.Selenide.open;

public class PlainTransferTest {

    @BeforeEach
    void setup() {
        open("http://localhost:9999");
    }

    @Test
    void shouldTransferMoneyBetweenOwnCards() {
        var loginPage = new LoginPage();
        var authInfo = DataHelper.getAuthInfo();
        var verificationPage = loginPage.validLogin(authInfo);
        var verificationCode = DataHelper.getVerificationCodeFor(authInfo);
        var dashboardPage = verificationPage.validVerify(verificationCode);

        var firstCardInfo = DataHelper.getFirstCardInfo();
        var secondCardInfo = DataHelper.getSecondCardInfo();

        int initialFirstCardBalance = dashboardPage.getCardBalance(0);
        int initialSecondCardBalance = dashboardPage.getCardBalance(1);

        int transferAmount = initialSecondCardBalance / 2;

        var transferPage = dashboardPage.selectCardToTransfer(0);
        transferPage.makeTransfer(String.valueOf(transferAmount), secondCardInfo.getCardNumber());

        int expectedFirstCardBalance = initialFirstCardBalance + transferAmount;
        int expectedSecondCardBalance = initialSecondCardBalance - transferAmount;

        Assertions.assertEquals(expectedFirstCardBalance, dashboardPage.getCardBalance(0));
        Assertions.assertEquals(expectedSecondCardBalance, dashboardPage.getCardBalance(1));
    }
}