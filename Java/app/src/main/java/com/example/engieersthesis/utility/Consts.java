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

    public static final String MULTIPLE_ADD = "multipleAdd";


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

    public static final String PHOTO_PATH = "photoPath";

    public static final String ADD_NEW_FOOD_EMPTY_FIELDS_MSG_PL = "Należy wypełnić wszystkie pola";
    public static final String ADD_NEW_FOOD_EMPTY_FIELDS_MSG_ENG = "Please fill all fields";
    public static final String ADD_NEW_FOOD_SUCCESS_MSG_PL = "Produkt został dodany";
    public static final String ADD_NEW_FOOD_SUCCESS_MSG_ENG = "Product added successfully";
    public static final String ADD_NEW_FOOD_FAILURE_MSG_PL = "Produkt nie został dodany, spróbuj ponownie";
    public static final String ADD_NEW_FOOD_FAILURE_MSG_ENG = "Product adding failed, please try again";
    public static final String WRONG_CREDENTIALS_MSG_PL = "Nieprawidłowe dane logowania, spróbuj ponownie";
    public static final String WRONG_CREDENTIALS_MSG_ENG = "Invalid login or password, please try again";
    public static final String WRONG_REGISTER_CREDENTIALS_MSG_PL = "Nieprawidłowe dane rejestracji, spróbuj ponownie";
    public static final String WRONG_REGISTER_CREDENTIALS_MSG_ENG = "Login, password or email are invalid, please try again";
    public static final String FOOD_PRODUCT_DELETE_SUCCESS_MSG_PL = "Produkt został usunięty";
    public static final String FOOD_PRODUCT_DELETE_SUCCESS_MSG_ENG = "Product has been successfully removed";
    public static final String FOOD_PRODUCT_DELETE_FAILURE_MSG_PL = "Nie udało się usunąć produktu, spróbuj ponownie";
    public static final String FOOD_PRODUCT_DELETE_FAILURE_MSG_ENG = "Product removing failed, please try again";
    public static final String FOOD_PRODUCT_UPDATE_SUCCESS_MSG_PL = "Produkt został edytowany";
    public static final String FOOD_PRODUCT_UPDATE_SUCCESS_MSG_ENG = "Product edited successfully";
    public static final String FOOD_PRODUCT_UPDATE_FAILURE_MSG_PL = "Nie udało się edytować produktu, spróbuj ponownie";
    public static final String FOOD_PRODUCT_UPDATE_FAILURE_MSG_ENG = "Product editing failed, please try again";
    public static final String PUT_BODY_PARAMETERS_SUCCESS_MSG_PL = "Parametry Twojego ciała zostały zapisane";
    public static final String PUT_BODY_PARAMETERS_SUCCESS_MSG_ENG = "Your body parameters has been successfully saved";
    public static final String PUT_BODY_PARAMETERS_FAILURE_MSG_PL = "Nie udało się zapisać parametrów Twojego ciała, spróbuj ponownie";
    public static final String PUT_BODY_PARAMETERS_FAILURE_MSG_ENG = "Body parameters saving failed, please try again";
    public static final String PUT_NUTRITIONS_TARGET_SUCCESS_MSG_PL = "Twój cel żywieniowy został zapisany";
    public static final String PUT_NUTRITIONS_TARGET_SUCCESS_MSG_ENG = "Your nutrition target has been successfully saved";
    public static final String PUT_NUTRITUONS_TARGET_SUCCESS_MSG_PL = "Nie udało się zapisać Twojego celu żywieniowego, spróbuj ponownie";
    public static final String PUT_NUTRITUONS_TARGET_SUCCESS_MSG_ENG = "Nutrition target saving failed, please try again";
    public static final String PHOTO_ERROR_MSG_PL = "Zdjęcie nie zostało zanalezione";
    public static final String PHOTO_ERROR_MSG_ENG = "Error while opening photo, please try again";
    public static final String NO_OBJECTS_RECOGNIZED_MS_PL = "Nie udało się rozpoznać żadnego produktu. Spróbuj ponownie lub wprowadź produkt ręcznie";
    public static final String NO_OBJECTS_RECOGNIZED_MS_ENG = "No product has been recognized. Try again or use manual search";

    public static final double DEFAULT_MASS_DIV = 100;

    public static final int PROTEIN_CALORIES = 4;
    public static final int CARBOHYDRATE_CALORIES = 4;
    public static final int FAT_CALORIES = 9;

    public static final String[] FRIUTS_AND_VEGIES_FIRST_UPPER_ENG = {"Achoccha", "Amaranth", "Angelica", "Anise", "Apple", "Arrowroot", "Arrugula", "Artichoke", "Artichoke", "Asparagus", "Atemoya", "Avocado", "Groundnut", "Bamboo", "Bambara Groundnut", "Bamboo", "Banana", "Bananas", "Plantain", "Plantains", "Barbados", "Cherry", "Barbados Cherry", "Beans", "Bean", "Beet", "Beets", "Blackberry", "Blackberries", "Blueberry", "Blueberries", "Broccoli", "Brussels Sprouts", "Bunch Grape", "Burdock", "Cabbage", "Calabaza", "Cantaloupes", "Muskmelon", "Muskmelons", "Caper", "Capers", "Carambola", "Star Fruit", "Cardoon", "Carrot", "Cassava", "Cauliflower", "Celeriac", "Celeru", "Celtuce", "Chard", "Chaya", "Chayote", "Chicory", "Jujube", "Chives", "Chrysanthemum", "Chufa", "Cilantro", "Citron", "Coconut Palm", "Coconut", "Collards", "Comfrey", "Corn Salad", "Corn", "Cuban Sweet Potato", "Cucumber", "Cushcush", "Daikon", "Dandelion", "Dasheen", "Dill", "Eggplant", "Endive", "Eugenia", "Fennel", "Fig", "Galia Muskmelon", "Garbanzo", "Garlic", "Gherkin", "Ginger", "Ginseng", "Gourds", "Grape", "Guar", "Guava", "Hanover Salad", "Horseradish", "Horseradish Tree", "Huckleberry", "Ice Plant", "Jaboticaba", "Jackfruit", "Jicama", "Jojoba", "Kale", "Kangkong", "Kohlrabi", "Leek", "Lentils", "Lettuce", "Longan", "Loquat", "Lovage", "Luffa gourd", "Lychee", "Macadamia", "Malanga", "Mamey Sapote", "Mango", "Martynia", "Melon", "Koneydew", "Casaba", "Momordica", "Muscadine Grape", "Mushroom", "Mustard", "Mustard Collard", "Mustard Potherb", "Naranjillo", "Nasturtium", "Nectarine", "Okra", "Onion", "Orach", "Ortiental Persimmon", "Papaya", "Paprika", "Parsley", "Parsnip", "Parsley Root", "Passion Fruit", "Peach", "Plum", "Peas", "Peanut", "Pear", "Pecan", "Pepper", "Persimmon", "Pimiento", "Pineapple", "Pitaya", "Pokeweed", "Pomegranate", "Potato", "Pumpkin", "Purslane", "Radicchio", "Radish", "Rakkyo", "Rampion", "Raspberry", "Rhubarb", "Romaine Lettuce", "Roselle", "Rutabaga", "Saffron", "Salsify", "Sapodilla", "Sarsaparilla", "Sassafrass", "Scorzonera", "Sea Kale", "Seagrape", "Shallot", "Skirret", "Smallage", "Sorrel", "Southern pea", "Soybeans", "Spinach", "Spondias", "Squash", "Strawberries", "Sugar Apple", "Swamp Cabbage", "Sweet Basil", "Sweet Potato", "Swiss Chard", "Tomatillo", "Tomato", "Truffles", "Turnip", "Upland Cress", "Water Celery", "Waterchestnut", "Watercress", "Watermelon", "Yams", "Naranjilla", "Diospyros", "Tamarillo", };
    public static final String[] FRIUTS_AND_VEGIES_ENG = {"achoccha", "amaranth", "angelica", "anise", "apple", "arrowroot", "arrugula", "artichoke", "artichoke", "asparagus", "atemoya", "avocado", "groundnut", "bamboo", "bambara groundnut", "bamboo", "banana", "bananas", "plantain", "plantains", "barbados", "cherry", "barbados cherry", "beans", "bean", "beet", "beets", "blackberry", "blackberries", "blueberry", "blueberries", "broccoli", "brussels sprouts", "bunch grape", "burdock", "cabbage", "ccalabaza", "cantaloupes", "muskmelon", "muskmelons", "caper", "capers", "carambola", "star fruit", "cardoon", "carrot", "cassava", "cauliflower", "celeriac", "celeru", "celtuce", "chard", "chaya", "chayote", "chicory", "jujube", "chives", "chrysanthemum", "chufa", "cilantro", "citron", "coconut palm", "coconut", "collards", "comfrey", "corn salad", "corn", "cuban sweet potato", "cucumber", "cushcush", "daikon", "dandelion", "dasheen", "dill", "eggplant", "endive", "eugenia", "fennel", "fig", "galia muskmelon", "garbanzo", "garlic", "gherkin", "ginger", "ginseng", "gourds", "grape", "guar", "guava", "hanover salad", "horseradish", "horseradish tree", "huckleberry", "ice plant", "jaboticaba", "jackfruit", "jicama", "jojoba", "kale", "kangkong", "kohlrabi", "leek", "lentils", "lettuce", "longan", "loquat", "lovage", "luffa gourd", "lychee", "macadamia", "malanga", "mamey sapote", "mango", "martynia", "melon", "koneydew", "casaba", "momordica", "muscadine grape", "mushroom", "mmustard", "mustard collard", "mustard potherb", "naranjillo", "nasturtium", "nectarine", "okra", "onion", "orach", "ortiental persimmon", "papaya", "paprika", "parsley", "parsnip", "parsley root", "passion fruit", "peach", "plum", "peas", "peanut", "pear", "pecan", "pepper", "persimmon", "pimiento", "pineapple", "pitaya", "pokeweed", "pomegranate", "potato", "pumpkin", "purslane", "radicchio", "radish", "rakkyo", "rampion", "raspberry", "rhubarb", "romaine lettuce", "roselle", "rutabaga", "saffron", "salsify", "sapodilla", "sarsaparilla", "sassafrass", "scorzonera", "sea kale", "seagrape", "shallot", "skirret", "smallage", "sorrel", "southern pea", "soybeans", "spinach", "spondias", "squash", "strawberries", "sugar apple", "swamp cabbage", "sweet basil", "sweet potato", "swiss chard", "tomatillo", "tomato", "truffles", "turnip", "upland cress", "water celery", "waterchestnut", "watercress", "watermelon", "yams"};


}
