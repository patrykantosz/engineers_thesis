package com.example.engieersthesis.utility;

public class Consts {
    public static final String API_LOGIN_ENDPOINT = "https://backendforthesis.herokuapp.com/api/auth/login";
    public static final String API_REGISTER_ENDPOINT = "https://backendforthesis.herokuapp.com/api/auth/register";
    public static final String API_LIST_ALL_FOOD_PRODUCTS_ENDPOINT = "https://backendforthesis.herokuapp.com/api/food/";
    public static final String API_ADD_NEW_FOOD_PRODUCT = "https://backendforthesis.herokuapp.com/api/food/add";
    public static final String API_LOG_OUT_ENDPOINT = "https://backendforthesis.herokuapp.com/api/auth/logout";
    public static final String API_USER_INFO_ENDPOINT = "https://backendforthesis.herokuapp.com/api/auth/user";
    public static final String API_QUERY_PARAM_TO_SEARCH_FOOD_BY_NAME_ENDPOINT = "?food_name=";
    public static final String POST_METHOD = "POST";
    public static final String GET_METHOD = "GET";
    public static final String TOKEN_KEY = "token";
    public static final String CONTENT_TYPE = "Content-Type";
    public static final String CONTENT_TYPE_APPLICATION_JSON_UTF8 = "application/json; UTF-8";
    public static final String TOKEN_FILE = "tokenFile.txt";

    public static final String FOOD_PRODUCT_NAME = "name";
    public static final String FOOD_PRODUCT_BRAND = "brand";
    public static final String FOOD_PRODUCT_ENERGY_VALUE = "energy_value";
    public static final String FOOD_PRODUCT_FATS = "fats";
    public static final String FOOD_PRODUCT_SATURATED_FATS = "saturated_fats";
    public static final String FOOD_PRODUCT_CARBOHYDRATES = "carbohydrates";
    public static final String FOOD_PRODUCT_SUGARS = "sugars";
    public static final String FOOD_PRODUCT_PROTEINS = "proteins";
    public static final String FOOD_PRODUCT_SALT = "salt";

    public static final String BREAKFAST_PL = "Śniadanie";
    public static final String BRUNCH_PL = "Drugie śniadanie";
    public static final String DINNER_PL = "Obiad";
    public static final String SUPPER_PL = "Kolacja";

    public static final String POST_CALL_REQUEST_TYPE = "POSTCALL";

    public static final int LOGIN_TIMEOUT_MS = 4000;
    public static final int LOGIN_MAX_RETRIES = 3;
    public static final float LOGIN_MAX_MULTIPLIER = 2f;

    public static final String ADD_NEW_FOOD_EMPTY_FIELDS_MSG_PL = "Należy wypełnić wszystkie pola";
    public static final String ADD_NEW_FOOD_SUCCES_MSG_PL = "Produkt został dodany";
    public static final String ADD_NEW_FOOD_FAILURE_MSG_PL = "Produkt nie został dodany, spróbuj ponownie";
    public static final String WRONG_CREDENTIALS_MSG_PL = "Nieprawidłowe dane logowania, spróbuj ponownie";
}
