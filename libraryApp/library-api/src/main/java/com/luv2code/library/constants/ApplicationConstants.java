package com.luv2code.library.constants;

import java.text.SimpleDateFormat;

public class ApplicationConstants {

    public static final String USER_EMAIL_CLAIM = "\"sub\"";

    public static final String USER_TYPE_CLAIM = "\"userType\"";

    public static final String HTTP_ALLOWED_ORIGINS = "http://localhost:3000";

    public static final String HTTPS_ALLOWED_ORIGINS = "https://localhost:3000";

    public static final String STRIPE_AMOUNT_PARAM = "amount";

    public static final String STRIPE_CURRENCY_PARAM = "currency";

    public static final String STRIPE_PAYMENT_METHOD_TYPES = "payment_method_types";

    public static SimpleDateFormat YEAR_MONTH_DAY_FORMATTER = new SimpleDateFormat("yyyy-MM-dd");

}
