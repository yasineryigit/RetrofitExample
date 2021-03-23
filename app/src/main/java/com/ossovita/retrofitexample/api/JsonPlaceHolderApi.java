package com.ossovita.retrofitexample.api;


import com.ossovita.retrofitexample.model.Post;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

public interface JsonPlaceHolderApi {

    @GET("posts")
    Call<List<Post>> getPosts();

}
