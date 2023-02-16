package ru.netology;

import org.junit.jupiter.api. *;

import java.time.Duration;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selectors.*;
import static com.codeborne.selenide.Selenide.*;

public class TestFormCard {
    public String dayMeeting(int daysToMeeting) {
        LocalDate dateOrder = LocalDate.now().plusDays(daysToMeeting);
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd.MM.yyyy");
        String newDate = dtf.format(dateOrder);
        return newDate;
    }

    @BeforeEach
    void setUp() {
        open("http://localhost:9999");
    }

    @AfterEach
    void memoryClear() {
        clearBrowserCookies();
        clearBrowserLocalStorage();
    }
    @Test
    void DataTestOk() {
        $("[data-test-id=city] input").setValue("Улан-Удэ").pressEnter();
        $("[data-test-id=date] input").doubleClick().sendKeys(dayMeeting(3));
        $("[data-test-id=name] input").setValue("Фамилия Имя");
        $("[data-test-id=phone] input").setValue("+79112113355");
        $("[data-test-id=agreement]").click();
        $(withText("Забронировать")).click();
        $(".notification").shouldBe(visible, Duration.ofSeconds(15));
        $(".notification__content").shouldHave(exactText("Встреча успешно забронирована на " + dayMeeting(3)));
    }

    @Test
    void DataTestFail() {
        $("[data-test-id=city] input").setValue("Улан-Удэ").pressEnter();
        $("[data-test-id=date] input").doubleClick().sendKeys(dayMeeting(2));
        $("[data-test-id=name] input").setValue("Фамилия Имя");
        $("[data-test-id=phone] input").setValue("+79112113355");
        $("[data-test-id=agreement]").click();
        $(withText("Забронировать")).click();
        $(".notification").shouldBe(visible, Duration.ofSeconds(7));
        $("[data-test-id=date].input_invalid .input__sub").shouldHave(exactText("Заказ на выбранную дату невозможен"));
    }

    @Test
    void WrongCityTest1() {
        $("[data-test-id=city] input").setValue("Кипр").pressEnter();
        $("[data-test-id=date] input").doubleClick().sendKeys(dayMeeting(3));
        $("[data-test-id=name] input").setValue("Фамилия Имя");
        $("[data-test-id=phone] input").setValue("+79112113355");
        $("[data-test-id=agreement]").click();
        $(withText("Забронировать")).click();
        $("[data-test-id=city].input_invalid .input__sub").shouldHave(exactText("Доставка в выбранный город недоступна"));
    }

    @Test
    void WrongCityTest2() {
        $("[data-test-id=city] input").setValue("123").pressEnter();
        $("[data-test-id=date] input").doubleClick().sendKeys(dayMeeting(3));
        $("[data-test-id=name] input").setValue("Фамилия Имя");
        $("[data-test-id=phone] input").setValue("+79112113355");
        $("[data-test-id=agreement]").click();
        $(withText("Забронировать")).click();
        $("[data-test-id=city].input_invalid .input__sub").shouldHave(exactText("Доставка в выбранный город недоступна"));

    }

    @Test
    void WrongCityTest13() {
        $("[data-test-id=city] input").setValue("Moscow").pressEnter();
        $("[data-test-id=date] input").doubleClick().sendKeys(dayMeeting(3));
        $("[data-test-id=name] input").setValue("Фамилия Имя");
        $("[data-test-id=phone] input").setValue("+79112113355");
        $("[data-test-id=agreement]").click();
        $(withText("Забронировать")).click();
        $("[data-test-id=city].input_invalid .input__sub").shouldHave(exactText("Доставка в выбранный город недоступна"));

    }

    @Test
    void DoubleNameTest() {
        $("[data-test-id=city] input").setValue("Улан-Удэ").pressEnter();
        $("[data-test-id=date] input").doubleClick().sendKeys(dayMeeting(3));
        $("[data-test-id=name] input").setValue("Фамилия Имя-имя");
        $("[data-test-id=phone] input").setValue("+79112113355");
        $("[data-test-id=agreement]").click();
        $(withText("Забронировать")).click();
        $(".notification__content").shouldHave(exactText("Встреча успешно забронирована на " + dayMeeting(3)));
    }

    @Test
    void OnlyNameTest() {
        $("[data-test-id=city] input").setValue("Москва").pressEnter();
        $("[data-test-id=date] input").doubleClick().sendKeys(dayMeeting(5));
        $("[data-test-id=name] input").setValue("Влад");
        $("[data-test-id=phone] input").setValue("+79222222223");
        $("[data-test-id=agreement]").click();
        $(withText("Забронировать")).click();
        $(".notification").shouldBe(visible, Duration.ofSeconds(7));
        $(".notification__content").shouldHave(exactText("Встреча успешно забронирована на " + dayMeeting(5)));
    }

    @Test
    void MiniLimitNameTest() {
        $("[data-test-id=city] input").setValue("Москва").pressEnter();
        $("[data-test-id=date] input").doubleClick().sendKeys(dayMeeting(5));
        $("[data-test-id=name] input").setValue("В");
        $("[data-test-id=phone] input").setValue("+79222222223");
        $("[data-test-id=agreement]").click();
        $(withText("Забронировать")).click();
        $(".notification").shouldBe(visible, Duration.ofSeconds(7));
        $(".notification__content").shouldHave(exactText("Встреча успешно забронирована на " + dayMeeting(5)));
    }

    @Test
    void WrongNameTest1() {
        $("[data-test-id=city] input").setValue("Москва").pressEnter();
        $("[data-test-id=date] input").doubleClick().sendKeys(dayMeeting(5));
        $("[data-test-id=name] input").setValue("Vlad Shell");
        $("[data-test-id=phone] input").setValue("+79222222224");
        $("[data-test-id=agreement]").click();
        $(withText("Забронировать")).click();
        $("[data-test-id=name].input_invalid .input__sub").shouldHave(exactText("Имя и Фамилия указаные неверно. Допустимы только русские буквы, пробелы и дефисы."));
    }

    @Test
    void WrongNameTest2() {
        $("[data-test-id=city] input").setValue("Москва").pressEnter();
        $("[data-test-id=date] input").doubleClick().sendKeys(dayMeeting(5));
        $("[data-test-id=name] input").setValue("12345");
        $("[data-test-id=phone] input").setValue("+79222222224");
        $("[data-test-id=agreement]").click();
        $(withText("Забронировать")).click();
        $("[data-test-id=name].input_invalid .input__sub").shouldHave(exactText("Имя и Фамилия указаные неверно. Допустимы только русские буквы, пробелы и дефисы."));
    }

    @Test
    void WrongPhoneTest1() {
        $("[data-test-id=city] input").setValue("Москва").pressEnter();
        $("[data-test-id=date] input").doubleClick().sendKeys(dayMeeting(5));
        $("[data-test-id=name] input").setValue("Фамилия Имя");
        $("[data-test-id=phone] input").setValue("89222222224");
        $("[data-test-id=agreement]").click();
        $(withText("Забронировать")).click();
        $("[data-test-id=phone].input_invalid .input__sub").shouldHave(exactText("Телефон указан неверно. Должно быть 11 цифр, например, +79012345678."));
    }

    @Test
    void WrongPhoneTest2() {
        $("[data-test-id=city] input").setValue("Москва").pressEnter();
        $("[data-test-id=date] input").doubleClick().sendKeys(dayMeeting(5));
        $("[data-test-id=name] input").setValue("Фамилия Имя");
        $("[data-test-id=phone] input").setValue("+7");
        $("[data-test-id=agreement]").click();
        $(withText("Забронировать")).click();
        $("[data-test-id=phone].input_invalid .input__sub").shouldHave(exactText("Телефон указан неверно. Должно быть 11 цифр, например, +79012345678."));
    }

    @Test
    void WrongPhoneTest3() {
        $("[data-test-id=city] input").setValue("Москва").pressEnter();
        $("[data-test-id=date] input").doubleClick().sendKeys(dayMeeting(5));
        $("[data-test-id=name] input").setValue("Фамилия Имя");
        $("[data-test-id=phone] input").setValue("+792222222245");
        $("[data-test-id=agreement]").click();
        $(withText("Забронировать")).click();
        $("[data-test-id=phone].input_invalid .input__sub").shouldHave(exactText("Телефон указан неверно. Должно быть 11 цифр, например, +79012345678."));
    }

    @Test
    void ObligatoryFieldTestCity() {
        $("[data-test-id=date] input").doubleClick().sendKeys(dayMeeting(3));
        $("[data-test-id=name] input").setValue("Фамилия Имя");
        $("[data-test-id=phone] input").setValue("+79112113355");
        $("[data-test-id=agreement]").click();
        $(withText("Забронировать")).click();
        $("[data-test-id=city].input_invalid .input__sub").shouldHave(exactText("Поле обязательно для заполнения"));
    }

    @Test
    void ObligatoryFieldTestName() {
        $("[data-test-id=city] input").setValue("Москва").pressEnter();
        $("[data-test-id=date] input").doubleClick().sendKeys(dayMeeting(5));
        $("[data-test-id=phone] input").setValue("+79222222224");
        $("[data-test-id=agreement]").click();
        $(withText("Забронировать")).click();
        $("[data-test-id=name].input_invalid .input__sub").shouldHave(exactText("Поле обязательно для заполнения"));
    }

    @Test
    void ObligatoryFieldTestPhone() {
        $("[data-test-id=city] input").setValue("Москва").pressEnter();
        $("[data-test-id=date] input").doubleClick().sendKeys(dayMeeting(3));
        $("[data-test-id=name] input").setValue("В");
        $("[data-test-id=agreement]").click();
        $(withText("Забронировать")).click();
        $("[data-test-id=phone].input_invalid .input__sub").shouldHave(exactText("Поле обязательно для заполнения"));
    }

    @Test
    void ObligatoryFieldTestCheckbox() {
        $("[data-test-id=city] input").setValue("Москва").pressEnter();
        $("[data-test-id=date] input").doubleClick().sendKeys(dayMeeting(3));
        $("[data-test-id=name] input").setValue("Фамилия Имя");
        $("[data-test-id=phone] input").setValue("+79222222224");
        $(withText("Забронировать")).click();
        $("[data-test-id='agreement'].input_invalid ").shouldBe(visible);
        $(".input_invalid .checkbox__text").shouldHave(exactText("Я соглашаюсь с условиями обработки и использования моих персональных данных"));
    }

}