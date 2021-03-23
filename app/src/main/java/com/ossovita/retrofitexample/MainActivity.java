package com.ossovita.retrofitexample;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.ossovita.retrofitexample.api.JsonPlaceHolderApi;
import com.ossovita.retrofitexample.model.Comment;
import com.ossovita.retrofitexample.model.Post;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {

    private TextView textViewResult;
    private JsonPlaceHolderApi jsonPlaceHolderApi;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        textViewResult = findViewById(R.id.text_view_result);

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://jsonplaceholder.typicode.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        jsonPlaceHolderApi = retrofit.create(JsonPlaceHolderApi.class);
        
        getPosts();
        //getComments();

    }

    private void getComments() {
        Call<List<Comment>> call = jsonPlaceHolderApi
                .getComments(2);

        call.enqueue(new Callback<List<Comment>>() {
            @Override
            public void onResponse(Call<List<Comment>> call, Response<List<Comment>> response) {
                if(!response.isSuccessful()){
                    Toast.makeText(MainActivity.this, "Error: " + response.code(), Toast.LENGTH_SHORT).show();
                    return; //hata varsa daha fazla reponse metodunda bekleme
                }
                List<Comment> comments = response.body();
                for(Comment comment: comments){

                    String content="";
                    content += "ID: " + comment.getId() + "\n";
                    content += "Post ID: " + comment.getPostId() + "\n";
                    content += "Name: " + comment.getName();
                    content += "Email: " + comment.getEmail() + "\n";
                    content += "Text: " + comment.getText() + "\n\n";
                    textViewResult.append(content);

                }



            }

            @Override
            public void onFailure(Call<List<Comment>> call, Throwable t) {
                Toast.makeText(MainActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void getPosts() {
        Map<String,String> parameters = new HashMap<>();
        parameters.put("userId","1");
        parameters.put("_sort","id");
        parameters.put("_order","desc");
        ArrayList<Integer> userIdList = new ArrayList<>();
        userIdList.add(2);
        userIdList.add(5);
        Call<List<Post>> call = jsonPlaceHolderApi.getPosts(userIdList);//bu parametrelere göre veri getir

        call.enqueue(new Callback<List<Post>>() {
            @Override
            public void onResponse(Call<List<Post>> call, Response<List<Post>> response) {
                if(!response.isSuccessful()) {//Eğer gelen veride hata varsa gelen hata kodunu yazdırıyoruz
                    textViewResult.setText("Code: " + response.code());
                    return;//hata varsa daha fazla devam etmeden metottan çık
                }

                List<Post> posts = response.body();//Gelen body verisini Post class'ının listesine attık
                for(Post post : posts){//posts listesindeki her bir Post objesi için
                    String content="";
                    content += "ID: " + post.getId() + "\n";
                    content += "User ID: " + post.getUserId() + "\n";
                    content += "Title: " + post.getTitle();
                    content += "Text: " + post.getText() + "\n\n";

                    textViewResult.append(content);


                }

            }

            @Override
            public void onFailure(Call<List<Post>> call, Throwable t) {
                textViewResult.setText(t.getMessage());
            }
        });

    }
}