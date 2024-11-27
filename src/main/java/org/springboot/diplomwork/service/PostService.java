package org.springboot.diplomwork.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.ssl.SslProperties;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springboot.diplomwork.entity.Post;
import org.springboot.diplomwork.repository.PostRepository;
import org.springframework.util.ObjectUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;

@Service
public class PostService implements IPostService {

    @Autowired
    private PostRepository postRepository;

    @Override
    public Post savePost(Post post) {
        return postRepository.save(post);
    }

    @Override
    public List<Post> getAllPosts() {
        return postRepository.findAll();
    }

    @Override
    public Boolean deletePost(int id) {
        Post post = postRepository.findById(id).orElse(null);
        if (post != null) {
            postRepository.delete(post);
            return true;
        }
        return false;
    }

    @Override
    public Post getPostById(int id) {
        Post post = postRepository.findById(id).orElse(null);
        return post;
    }

    @Override
    public List<Post> getAllSelectPosts(String category) {
        List<Post> posts = null;


       if (ObjectUtils.isEmpty(category)) {
            posts = postRepository.findAll();
        } else{
            posts = postRepository.findByCategory(category);
        }
        return posts;
    }


    @Override
    public List<Post> searchPost(String ch) {
        return postRepository.findByTitleContainingIgnoreCaseOrCategoryContainingIgnoreCase(ch, ch);
    }

    @Override
    public Page<Post> getAllPostPagination(Integer pageNo, Integer pageSize, String category) {
        Pageable pageable = PageRequest.of(pageNo, pageSize);
        Page<Post> pagePosts = null;

        if(ObjectUtils.isEmpty(category)){
            pagePosts = postRepository.findAllBy(pageable);
        } else {
            pagePosts = postRepository.findByCategory(pageable, category);
        }
        return pagePosts;
    }

    @Override
    public Post updatePost(Post post, MultipartFile image) {
        Post dbPost = getPostById(post.getId());
        String imageName = image.isEmpty() ? dbPost.getImage() : image.getOriginalFilename();

        dbPost.setTitle(post.getTitle());
        dbPost.setContent(post.getContent());
        dbPost.setCategory(post.getCategory());
        dbPost.setImage(imageName);

        Post updatePost = postRepository.save(dbPost);
        if (updatePost != null) {
            if(!image.isEmpty()){
                try{
                    String saveFile = new File("src/main/resources/static/img").getAbsolutePath();
                    Path path = Paths.get(saveFile + File.separator + "post_img" + File.separator + image.getOriginalFilename());
                    Files.copy(image.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);
                } catch (Exception e){
                    e.printStackTrace();
                }
            }
            return post;
        }
        return null;
    }

}