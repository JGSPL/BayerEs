package com.procialize.eventapp.Constants;

public class ApiUtils {

    private ApiUtils() {
    }


    public static APIService getAPIService() {

        return RetrofitClient.getClient(Constant.BASE_URL).create(APIService.class);
    }


    public static TenorApiService getTenorAPIService() {
        return RetrofitClient.getClient(Constant.TENOR_URL).create(TenorApiService.class);
    }
}
