package com.example.engieersthesis.utility;

public class Consts {
    public static final String API_LOGIN_ENDPOINT = "https://backendforthesis.herokuapp.com/api/auth/login";
    public static final String API_REGISTER_ENDPOINT = "https://backendforthesis.herokuapp.com/api/auth/register";
    public static final String API_LIST_ALL_FOOD_PRODUCTS_ENDPOINT = "https://backendforthesis.herokuapp.com/api/food/";
    public static final String API_ADD_NEW_FOOD_PRODUCT = "https://backendforthesis.herokuapp.com/api/food/add";
    public static final String API_LOG_OUT_ENDPOINT = "https://backendforthesis.herokuapp.com/api/auth/logout";
    public static final String API_USER_INFO_ENDPOINT = "https://backendforthesis.herokuapp.com/api/auth/user";
    public static final String API_USER_FOOD_HISTORY_LIST_ENDPOINT = "https://backendforthesis.herokuapp.com/api/auth/food_history/list";
    public static final String API_DELETE_FOOD_FROM_USER_HISTORY_ENDPOINT = "https://backendforthesis.herokuapp.com/api/auth/meal/delete";
    public static final String API_ADD_NEW_PRODUCT_TO_MEAL_ENDPOINT = "https://backendforthesis.herokuapp.com/api/auth/meal";
    public static final String API_UPDATE_FOOD_WEIGHT_ENDPOINT = "https://backendforthesis.herokuapp.com/api/auth/food_details/update";
    public static final String API_USER_BODY_PARAMETERS_ENDPOINT = "https://backendforthesis.herokuapp.com/api/auth/parameters";
    public static final String API_USER_NUTRITIONS_TARGET_ENDPOINT = "https://backendforthesis.herokuapp.com/api/auth/targets";
    public static final String API_QUERY_PARAM_TO_SEARCH_FOOD_BY_NAME_ENDPOINT = "?food_name=";
    public static final String API_QUERY_PARAM_TO_DETETE_FOOD_FROM_USER_HISTORY = "?food_details_id=";
    public static final String POST_METHOD = "POST";
    public static final String GET_METHOD = "GET";
    public static final String DELETE_METHOD = "DELETE";
    public static final String PATCH_METHOD = "PATCH";
    public static final String PUT_METHOD = "PUT";
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
    public static final String FOOD_PRODUCT_WEIGHT = "food_weight";
    public static final String FOOD_DETAILS_ID = "food_details_id";
    public static final String MEAL_DATE = "meal_date";
    public static final String MEAL_TYPE = "meal_type";
    public static final String FOOD_PRODUCT_ID = "food_id";
    public static final String USER_WEIGHT = "user_weight";
    public static final String USER_HEIGHT = "user_height";
    public static final String USER_BMI = "user_bmi";
    public static final String USER_TARGET_CALORIES = "target_calories";
    public static final String USER_TARGET_CARBOHYDRATES = "target_carbohydrates";
    public static final String USER_TARGET_PROTEINS = "target_proteins";
    public static final String USER_TARGET_FATS = "target_fats";


    public static final String JSON_STRING_FOOD_ID = "foodStringJson";

    public static final String BREAKFAST_PL = "Śniadanie";
    public static final String BRUNCH_PL = "Drugie śniadanie";
    public static final String DINNER_PL = "Obiad";
    public static final String SUPPER_PL = "Kolacja";

    public static final String BREAKFAST_EN = "Breakfast";
    public static final String BRUNCH_EN = "Brunch";
    public static final String DINNER_EN = "Dinner";
    public static final String SUPPER_EN = "Supper";

    public static final String MEAL_TYPE_TO_JSON_REQUEST = "mealTypeToJsonRequest";
    public static final String MEAL_TYPE_INTENT_EXTRA = "mealName";
    public static final String MEAL_DATE_INTENT_EXTRA = "mealDate";
    public static final String FOOD_EDITED_INTENT_EXTRA = "isFoodEdited";

    public static final String POST_CALL_REQUEST_TYPE = "POSTCALL";

    public static final int LOGIN_TIMEOUT_MS = 4000;
    public static final int LOGIN_MAX_RETRIES = 3;
    public static final float LOGIN_MAX_MULTIPLIER = 2f;

    public static final String ADD_NEW_FOOD_EMPTY_FIELDS_MSG_PL = "Należy wypełnić wszystkie pola";
    public static final String ADD_NEW_FOOD_SUCCESS_MSG_PL = "Produkt został dodany";
    public static final String ADD_NEW_FOOD_FAILURE_MSG_PL = "Produkt nie został dodany, spróbuj ponownie";
    public static final String WRONG_CREDENTIALS_MSG_PL = "Nieprawidłowe dane logowania, spróbuj ponownie";
    public static final String WRONG_REGISTER_CREDENTIALS_MSG_PL = "Nieprawidłowe dane rejestracji, spróbuj ponownie";
    public static final String FOOD_PRODUCT_DELETE_SUCCESS_MSG_PL = "Produkt został usunięty";
    public static final String FOOD_PRODUCT_DELETE_FAILURE_MSG_PL = "Nie udało się usunąć produktu, spróbuj ponownie";
    public static final String FOOD_PRODUCT_UPDATE_SUCCESS_MSG_PL = "Produkt został edytowany";
    public static final String FOOD_PRODUCT_UPDATE_FAILURE_MSG_PL = "Nie udało się edytować produktu, spróbuj ponownie";
    public static final String PUT_BODY_PARAMETERS_SUCCESS_MSG_PL = "Parametry Twojego ciała zostały zapisane";
    public static final String PUT_BODY_PARAMETERS_FAILURE_MSG_PL = "Nie udało się zapisać parametrów Twojego ciała, spróbuj ponownie";
    public static final String PUT_NUTRITIONS_TARGET_SUCCESS_MSG_PL = "Twój cel żywieniowy został zapisany";
    public static final String PUT_NUTRITUONS_TARGET_SUCCESS_MSG_PL = "Nie udało się zapisać Twojego celu żywieniowego, spróbuj ponownie";

    public static final double DEFAULT_MASS_DIV = 100;

    public static final int PROTEIN_CALORIES = 4;
    public static final int CARBOHYDRATE_CALORIES = 4;
    public static final int FAT_CALORIES = 9;

}
