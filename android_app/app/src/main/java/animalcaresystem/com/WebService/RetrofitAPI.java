package animalcaresystem.com.WebService;


import java.util.List;

import animalcaresystem.com.Model.ForumCommentModel;
import animalcaresystem.com.Model.ForumModel;
import animalcaresystem.com.Model.ImageModel;
import animalcaresystem.com.Model.ResponseModel;
import animalcaresystem.com.Model.TokenModel;
import animalcaresystem.com.Model.UserModel;
import okhttp3.MultipartBody;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Url;

/**
 * Created by Krevilraj on 4/8/2018.
 */

public class RetrofitAPI {
    public static final String base_url = "http://10.0.2.2:3000/";
    public static final String url = base_url + "api/";
    public static final String cache_control = "Cache-Control:no-cache";

    public static PostService postService = null;

    public static PostService getService() {

        if (postService == null) {
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(url)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
            postService = retrofit.create(PostService.class);

        }
        return postService;


    }

    public interface PostService {

        @FormUrlEncoded
        @POST
        Call<TokenModel> getToken(@Url String url,
                                  @Field("email") String email,
                                  @Field("password") String password);

        @FormUrlEncoded
        @POST
        Call<ResponseModel> register_user(@Url String url,
                                          @Field("username") String username,
                                          @Field("email") String email,
                                          @Field("image") String image,
                                          @Field("passwordConf") String passwordConf,
                                          @Field("password") String password);


        @FormUrlEncoded
        @POST
        Call<ResponseModel> update_user(@Url String url,
                                        @Field("name") String name,
                                        @Field("username") String username,
                                        @Field("email") String email,
                                        @Field("phone") String phone,
                                        @Field("image") String image,
                                        @Field("passwordConf") String passwordConf,
                                        @Field("password") String password,
                                        @Field("_id") String _id,
                                        @Field("token") String token);


        @FormUrlEncoded
        @POST
        Call<UserModel> get_user_detail(@Url String url,
                                        @Field("token") String token,
                                        @Field("username") String username,
                                        @Field("_id") String _id);

        @FormUrlEncoded
        @POST
        Call<ResponseModel> takeAppointment(@Url String url,
                                            @Field("name") String name,
                                            @Field("petname") String petname,
                                            @Field("email") String email,
                                            @Field("phone") String phone,
                                            @Field("date") String date,
                                            @Field("time") String time);

        @FormUrlEncoded
        @POST
        Call<ResponseModel> comment_post(@Url String url,
                                         @Field("username") String username,
                                         @Field("description") String description,
                                         @Field("_id") String id,
                                         @Field("forum_id") String forum_id,
                                         @Field("token") String token);

        @FormUrlEncoded
        @POST
        Call<ResponseModel> send_contact(@Url String url,
                                         @Field("name") String name,
                                         @Field("email") String email,
                                         @Field("phone") String phone,
                                         @Field("message") String message);

        @FormUrlEncoded
        @POST
        Call<ResponseModel> forum_post(@Url String url,
                                       @Field("title") String title,
                                       @Field("description") String description,
                                       @Field("username") String username,
                                       @Field("token") String token,
                                       @Field("_id") String user_id);

        @Multipart
        @POST("/api/upload")
        Call<ImageModel> uploadImage(@Part MultipartBody.Part image);


        @GET("/api/forum")
        Call<List<ForumModel>> getForum();

        @GET
        Call<List<ForumCommentModel>> getComment(@Url String url);

    }
}
