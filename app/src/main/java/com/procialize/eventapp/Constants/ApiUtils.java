package com.procialize.eventapp.Constants;

public class ApiUtils {

    private ApiUtils() {
    }


    public static APIService getAPIService() {

        return RetrofitClient.getClient(Constant.BASE_URL).create(APIService.class);
    }
}
