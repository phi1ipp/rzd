package com.grigorio.rzd;

import javafx.beans.property.SimpleStringProperty;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Philipp on 9/10/2014.
 */
public class Contact {
    private String  firstName;
    private String  lastName;
    private String  middleName;
    private String  docNumber;
    private Integer docType;
    private Integer docCountry;
    private Integer gender;
    private String  birthPlace;
    private String  birthDate;

    public static String[] arDocTypes = {"Неизвестный тип", "Паспорт РФ", "Паспорт формы СССР", "Заграничный паспорт",
            "Иностранный документ", "Паспорт моряка", "Свидетельство о рождении", "Военный билет"};

    public Integer getGender() {
        return gender;
    }

    public void setGender(Integer gender) {
        this.gender = gender;
    }

    public Contact(String strFN, String strLN, String strMN, String strDocNumber, Integer iDocType,
                   String strBirthPlace, String strBirthDate, Integer iGender, Integer iCountry) {
        firstName = strFN;
        lastName = strLN;
        middleName = strMN;
        docNumber = strDocNumber;
        docCountry = iCountry;
        docType = iDocType;
        birthDate = strBirthDate;
        birthPlace = strBirthPlace;
        gender = iGender;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String a) {
        firstName = a;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String a) {
        lastName = a;
    }

    public String getMiddleName() {
        return middleName;
    }

    public void setMiddleName(String a) {
        middleName = a;
    }

    public String getDocumentNumber() {
        return docNumber;
    }

    public void setDocumentNumber(String a) {
        docNumber = a;
    }

    public Integer getDocType() {
        return docType;
    }

    public void setDocType(Integer docType) {
        this.docType = docType;
    }

    public String getBirthPlace() {
        return birthPlace;
    }

    public void setBirthPlace(String birthPlace) {
        this.birthPlace = birthPlace;
    }

    public String getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(String birthDate) {
        this.birthDate = birthDate;
    }

    public Integer getDocCountry() {
        return docCountry;
    }

    public void setDocCountry(Integer docCountry) {
        this.docCountry = docCountry;
    }

    @Override
    public String toString() {
        return String.format("%s %s, %s - %s - %s, %s : %s", firstName, lastName, middleName, birthDate,
                docNumber, arDocTypes[docType], docCountry);
    }
}
