package com.profile.constants;

import lombok.experimental.UtilityClass;

@UtilityClass
public class ResourcePath {

    public static final String BASE_URL = "/profile";
    public static final String GET_ALL_ACTIVE_PROFILES_WITH_PAGINATION = "/get";
    public static final String ADD_PROFILE = "/add";
    public static final String UPDATE_PROFILE = "/update/{userId}";
    public static final String DELETE_PROFILE = "/delete/{userId}";
    public static final String RETRIEVE_POSTS_BY_PROFILE_USER_ID = "/posts/get/{userId}";
}
