package org.EmployeeUI;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.SelenideElement;
import cucumber.api.java.ru.Если;
import cucumber.api.java.ru.И;
import cucumber.api.java.ru.Когда;
import cucumber.api.java.ru.То;
import org.junit.Assert;
import org.openqa.selenium.By;


import java.util.List;

import static com.codeborne.selenide.Selectors.byId;
import static com.codeborne.selenide.Selectors.byText;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.open;

public class StepsDefinition {


    @Если("^я прошел по ссылке '(.+)'")
    public void goToLink(String link) {
        open(link);
    }

    @То("^откроется форма с таблицей '(.+)'")
    public void checkOnLoad(String fieldName1){
        $(byText(fieldName1)).should(Condition.appear);
    }

    @И("кнопками:$")
    public void checkOnLoadWindowWithButtons(List<String> buttons){
        for (String button:buttons){
            $(byText(button)).should(Condition.appear);
        }
    }

    @Когда("^я нажму кнопку '(.+)'")
    public void clickBtn1(String btnCaption){
        vaadinButtonClick(btnCaption);
    }

    @То("^откроется окно с полями:$")
    public void checkSortFields(List<String> fields){
        for (String field:fields){
            $(byText(field)).should(Condition.appear);
        }
    }

    @Когда("^я введу в поле '(.+)' '(.+)'")
    public void inputUsers(String fieldName1, String fieldVal1){
        findFieldByUpperLabelAndSetValue(fieldName1, fieldVal1);
    }

    private void findFieldByUpperLabelAndSetValue(String fieldName1, String fieldVal1) {
        SelenideElement elem = $(By.className("v-window-wrap")).find(byText(fieldName1));
        String idFor1 =  elem.parent().attr("for");

        SelenideElement elem1 = $(byId(idFor1));
        System.out.println("elem1 = " + elem1.toString());
        if (!elem1.toString().contains("NoSuchElementException")){
            elem1.val(fieldVal1).pressEnter();
        }else{
            elem.parent().parent().find(By.tagName("input")).val(fieldVal1).pressEnter();
        }
    }

    @И("^нажму кнопку '(.+)'")
    public void clickBtn2(String btnCaption){
        vaadinButtonClick(btnCaption);
    }

    @То("^закроется текущее окно и отобразится страница '(.+)' с таблицей")
    public void checkOnLoadWindow(String windowLbl){
        $(byText(windowLbl)).should(Condition.appear);
    }

    @И("^таблица будет содержать в себе '(.+)'")
    public void checkTableOnParam1(String param1){
        $(byText(param1)).should(Condition.appear);
    }


    @Если("^выбрать:$")
    public void selectRows(List<String> fields){
        for (String field:fields){
            $(byText(field)).should(Condition.appear);
            $(byText(field)).click();
        }
    }

    @И("^нажать кнопку '(.+)'")
    public void andPressBtn(String btnCaption){
        vaadinButtonClick(btnCaption);

    }

    private void vaadinButtonClick(String btnCaption) {
        $(byText(btnCaption)).parent().parent().click();
    }

    @То("^увижу сообщение с заголовком '(.+)' и текстом '(.+)'")
    public void showErrorMessage(String title, String body){
        $(byText(title)).should(Condition.appear);
        $(byText(body)).should(Condition.appear);
    }

    @То("^таблица не будет содержать в себе '(.+)'")
    public void checkTableOnRemovedParam1(String param1){
        $(byText(param1)).shouldNot(Condition.appear);
    }

    @И("^'(.+)' будет содержать '(.+)'")
    public void checkFieldData(String fieldName1, String fieldVal1){
        System.out.println($(byText(fieldName1)).parent().attr("for"));
        findFieldByUpperLabelAndCheckValue(fieldName1, fieldVal1);
        System.out.println("Есс");

    }

    private void findFieldByUpperLabelAndCheckValue(String fieldName1, String fieldVal1) {
        SelenideElement labelElement = $(By.className("v-window-wrap")).find(byText(fieldName1));
        String idFor1 =  labelElement.parent().attr("for");

        SelenideElement textFieldElement = $(byId(idFor1));
        System.out.println("elem1 = " + textFieldElement.toString());
        if (!textFieldElement.toString().contains("NoSuchElementException")){

        }else{
            textFieldElement = labelElement.parent().parent().find(By.tagName("input"));
        }
        Assert.assertEquals(textFieldElement.getValue(), fieldVal1);
    }


}
