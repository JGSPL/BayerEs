package com.procialize.bayer2020.Constants;

public class ApiUtils {

    private ApiUtils() {
    }


    public static APIService getAPIService() {

        return RetrofitClient.getClient(Constant.BASE_URL).create(APIService.class);
    }


    public static TenorApiService getTenorAPIService() {
        return TenorClient.getClient(Constant.TENOR_URL).create(TenorApiService.class);
    }

    public static String storeHeaderToken(String Token) {
        return Token;
    }
}
