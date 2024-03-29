package com.example.backendfruitable.service;

import com.example.backendfruitable.DTO.BaseResponse;
import com.example.backendfruitable.DTO.PostDTO;
import com.example.backendfruitable.Repository.CategoryPostRepository;
import com.example.backendfruitable.Repository.PostRepository;
import com.example.backendfruitable.Repository.UserRepository;
import com.example.backendfruitable.entity.CategoryPost;
import com.example.backendfruitable.entity.Post;
import com.example.backendfruitable.entity.User;
import com.example.backendfruitable.utils.Constant;
import com.example.backendfruitable.utils.ConvertRelationship;
import io.minio.GetPresignedObjectUrlArgs;
import io.minio.MinioClient;
import io.minio.PutObjectArgs;
import io.minio.RemoveObjectArgs;
import io.minio.http.Method;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

@Service
public class PostService {
    @Autowired
    private PostRepository postRepository;

    @Autowired
    private ConvertRelationship convertRelationship;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CategoryPostRepository categoryPostRepository;

    @Autowired
    private MinioClient minioClient;

    @Value("${minio.bucket}")
    private String bucketName;


    public BaseResponse<List<PostDTO>> getListPost() {
        BaseResponse<List<PostDTO>> baseResponse = new BaseResponse<>();
        List<PostDTO> postDTOList = new ArrayList<>();
        try {
            List<Post> postList = postRepository.findAll();
            if (postList == null || postList.isEmpty()) {
                baseResponse.setMessage(Constant.EMPTY_ALL_POST);
                baseResponse.setCode(Constant.NOT_FOUND_CODE);
                return baseResponse;
            }
            for (Post post : postList) {
                PostDTO postDTO = new PostDTO();
                postDTO.setPostId(post.getPostId());
                postDTO.setPostTitle(post.getPostTitle());
                //lấy image
                String object = post.getPostImage();
                String imageUrl = minioClient.getPresignedObjectUrl(
                        GetPresignedObjectUrlArgs.builder().method(Method.GET).bucket(bucketName).object(object).build()
                );
                postDTO.setPostImage(object);
                postDTO.setImageUrl(imageUrl);

                //tiếp tục convert
                postDTO.setPostDetail(post.getPostDetail());
                postDTO.setCreatedAt(post.getCreatedAt());
                postDTO.setUser(convertRelationship.convertToUserDTO(post.getUser()));
                postDTOList.add(postDTO);
            }
            baseResponse.setData(postDTOList);
            baseResponse.setMessage(Constant.SUCCESS_MESSAGE);
            baseResponse.setCode(Constant.SUCCESS_CODE);
        } catch (Exception e) {
            baseResponse.setMessage(Constant.ERROR_TO_GET_POST + e.getMessage());
            baseResponse.setCode(Constant.INTERNAL_SERVER_ERROR_CODE);
        }
        return baseResponse;
    }

    public BaseResponse<PostDTO> getPostById(Long postId) {
        BaseResponse<PostDTO> baseResponse = new BaseResponse<>();
        try {
            Post post = postRepository.findPostByPostId(postId);
            if (post == null) {
                baseResponse.setMessage(Constant.EMPTY_POST_BY_ID + postId);
                baseResponse.setCode(Constant.NOT_FOUND_CODE);
                return baseResponse;
            }

            PostDTO postDTO = new PostDTO();
            postDTO.setPostId(post.getPostId());
            postDTO.setPostTitle(post.getPostTitle());
            //lấy image
            String object = post.getPostImage();
            String imageUrl = minioClient.getPresignedObjectUrl(
                    GetPresignedObjectUrlArgs.builder().method(Method.GET).bucket(bucketName).object(object).build()
            );
            postDTO.setPostImage(object);
            postDTO.setImageUrl(imageUrl);

            //tiếp tục convert
            postDTO.setPostDetail(post.getPostDetail());
            postDTO.setCreatedAt(post.getCreatedAt());
            postDTO.setUser(convertRelationship.convertToUserDTO(post.getUser()));

            baseResponse.setData(postDTO);
            baseResponse.setMessage(Constant.SUCCESS_MESSAGE);
            baseResponse.setCode(Constant.SUCCESS_CODE);
        } catch (Exception e) {
            baseResponse.setMessage(Constant.ERROR_TO_GET_POST + e.getMessage());
            baseResponse.setCode(Constant.INTERNAL_SERVER_ERROR_CODE);
        }
        return baseResponse;
    }

    public BaseResponse<PostDTO> addPost(Long categoryPostId, Long userId, PostDTO postDTO) {
        BaseResponse<PostDTO> baseResponse = new BaseResponse<>();
        try {
            User user = userRepository.getUserById(userId);
            CategoryPost categoryPost = categoryPostRepository.findCategoryPostByCategoryId(categoryPostId);
            if (user == null) {
                baseResponse.setMessage(Constant.EMPTY_USER_BY_ID + userId);
                baseResponse.setCode(Constant.NOT_FOUND_CODE);
                return baseResponse;
            }
            if (categoryPost == null) {
                baseResponse.setMessage(Constant.EMPTY_CATEGORY_POST_BY_ID + categoryPostId);
                baseResponse.setCode(Constant.NOT_FOUND_CODE);
                return baseResponse;
            }

            Post post = new Post();
            post.setPostTitle(postDTO.getPostTitle());
            // lưu image
            byte[] imageByte = Base64.getDecoder().decode(Base64.getEncoder().encodeToString(postDTO.getDataImage()));
            InputStream inputStream = new ByteArrayInputStream(imageByte);
            String objectName = "post_" + System.currentTimeMillis() + ".jpg";
            minioClient.putObject(PutObjectArgs.builder()
                    .bucket(bucketName)
                    .stream(inputStream, imageByte.length, -1).object(objectName)
                    .contentType("image/jpeg")
                    .build());
            post.setPostImage(objectName);
            post.setPostDetail(postDTO.getPostDetail());
            post.setCreatedAt(LocalDate.now());
            post.setCategoryPost(categoryPost);
            post.setUser(user);

            postRepository.save(post);
            baseResponse.setData(postDTO);
            baseResponse.setMessage(Constant.SUCCESS_ADD_MESSAGE);
            baseResponse.setCode(Constant.SUCCESS_CODE);

        } catch (Exception e) {
            baseResponse.setMessage(Constant.ERROR_TO_ADD_POST + e.getMessage());
            baseResponse.setCode(Constant.INTERNAL_SERVER_ERROR_CODE);
        }
        return baseResponse;
    }

    public BaseResponse<PostDTO> updatePost(Long postId, Long categoryPostId, Long userId, PostDTO postDTO) {
        BaseResponse<PostDTO> baseResponse = new BaseResponse<>();
        try {
            Post post = postRepository.findPostByPostId(postId);
            if (post == null) {
                baseResponse.setMessage(Constant.EMPTY_POST_BY_ID + postId);
                baseResponse.setCode(Constant.NOT_FOUND_CODE);
                return baseResponse;
            }
            User user = userRepository.getUserById(userId);
            CategoryPost categoryPost = categoryPostRepository.findCategoryPostByCategoryId(categoryPostId);
            if (user == null) {
                baseResponse.setMessage(Constant.EMPTY_USER_BY_ID + userId);
                baseResponse.setCode(Constant.NOT_FOUND_CODE);
                return baseResponse;
            }
            if (categoryPost == null) {
                baseResponse.setMessage(Constant.EMPTY_CATEGORY_POST_BY_ID + categoryPostId);
                baseResponse.setCode(Constant.NOT_FOUND_CODE);
                return baseResponse;
            }
            if(postDTO.getPostImage() == null || postDTO.getPostImage().isEmpty()){
                baseResponse.setMessage(Constant.EMPTY_POST_IMAGE_NAME);
                baseResponse.setCode(Constant.BAD_REQUEST_CODE);
                return baseResponse;
            }

            post.setPostTitle(postDTO.getPostTitle());
            // update Image
            if (postDTO.getPostImage().equals(post.getPostImage())) {
                byte[] dataImage = Base64.getDecoder().decode(Base64.getEncoder().encode(postDTO.getDataImage()));
                InputStream inputStream = new ByteArrayInputStream(dataImage);
                String object = postDTO.getPostImage();
                // xoá ảnh trong minio
                minioClient.removeObject(RemoveObjectArgs.builder().bucket(bucketName).object(object).build());

                // thêm lại ảnh mới trong minio (cập nhật)
                minioClient.putObject(PutObjectArgs.builder()
                        .bucket(bucketName)
                        .object(object)
                        .stream(inputStream, dataImage.length, -1)
                        .contentType("image/jpeg")
                        .build());

                post.setPostImage(object);
            }

            post.setPostDetail(postDTO.getPostDetail());
            post.setUser(user);
            post.setCategoryPost(categoryPost);

            postRepository.save(post);

            baseResponse.setData(postDTO);
            baseResponse.setMessage(Constant.SUCCESS_UPDATE_MESSAGE);
            baseResponse.setCode(Constant.SUCCESS_CODE);

        } catch (Exception e) {
            baseResponse.setMessage(Constant.ERROR_TO_UPDATE_POST + e.getMessage());
            baseResponse.setCode(Constant.INTERNAL_SERVER_ERROR_CODE);
        }
        return baseResponse;
    }

    public BaseResponse<PostDTO> deletePost(Long postId) {
        BaseResponse<PostDTO> baseResponse = new BaseResponse<>();
        try {
            Post post = postRepository.findPostByPostId(postId);
            if (post == null) {
                baseResponse.setMessage(Constant.EMPTY_POST_BY_ID + postId);
                baseResponse.setCode(Constant.NOT_FOUND_CODE);
                return baseResponse;
            }
            // xoá ảnh trong minio
            String object = post.getPostImage();
            minioClient.removeObject(
                    RemoveObjectArgs.builder().bucket(bucketName).object(object).build()
            );

            // xoá bài viết khỏi csdl
            postRepository.delete(post);

            baseResponse.setMessage(Constant.DELETE_SUCCESS_POST_ID + postId);
            baseResponse.setCode(Constant.SUCCESS_CODE);
        } catch (Exception e) {
            baseResponse.setMessage(Constant.ERROR_TO_DELETE_POST + e.getMessage());
            baseResponse.setCode(Constant.INTERNAL_SERVER_ERROR_CODE);
        }
        return baseResponse;
    }
}
