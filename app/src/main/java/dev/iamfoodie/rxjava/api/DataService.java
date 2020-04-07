package dev.iamfoodie.rxjava.api;

import android.telecom.Call;

import java.util.List;

import dev.iamfoodie.rxjava.models.Comment;
import dev.iamfoodie.rxjava.models.Post;
import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface DataService {

    @GET("posts")
    public Observable<List<Post>> getPosts();

    @GET("posts/{id}/comments")
    public Observable<List<Comment>> getCommentsForPost(
            @Path("id") Integer id
    );

}
