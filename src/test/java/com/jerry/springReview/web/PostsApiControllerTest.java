package com.jerry.springReview.web;

import com.jerry.springReview.domain.posts.Posts;
import com.jerry.springReview.domain.posts.PostsRepository;
import com.jerry.springReview.web.dto.PostsServiceRequestDto;
import com.jerry.springReview.web.dto.PostsUpdateRequestDto;
import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class PostsApiControllerTest {
    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private PostsRepository postsRepository;

    @After
    public void cleanup() {
        postsRepository.deleteAll();
    }

    @Test
    public void write_posts() throws Exception{
        String title = "title";
        String content = "content";
        PostsServiceRequestDto requestDto = PostsServiceRequestDto.builder()
                .title(title)
                .content(content)
                .author("jerry")
                .build();

        String url =  "http://localhost:"+port+"/api/v1/posts";

        ResponseEntity<Long> responseEntity =  restTemplate.postForEntity(url, requestDto, Long.class);


        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(responseEntity.getBody()).isGreaterThan(0L);

        List<Posts> all = postsRepository.findAll();
        assertThat(all.get(0).getTitle()).isEqualTo(title);
        assertThat(all.get(0).getContent()).isEqualTo(content);
    }

    @Test
    public void update_posts() throws Exception {
        Posts savedPost = postsRepository.save(Posts.builder()
                .title("title")
                .content("content")
                .author("jerry")
                .build());

        Long updateId = savedPost.getId();
        String expectedTitle ="title2";
        String expectedContent = "content2";

        PostsUpdateRequestDto requestDto = PostsUpdateRequestDto.builder()
                .title(expectedTitle)
                .content(expectedContent)
                .build();

        String url =  "http://localhost:"+port+"/api/v1/posts/"+updateId;

        HttpEntity<PostsUpdateRequestDto> requestEntity = new HttpEntity<>(requestDto);
        ResponseEntity<Long> responseEntity = restTemplate.exchange(url, HttpMethod.PUT, requestEntity, Long.class);

        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(responseEntity.getBody()).isGreaterThan(0L);

        List<Posts> all = postsRepository.findAll();
        assertThat(all.get(0).getTitle()).isEqualTo(expectedTitle);
        assertThat(all.get(0).getContent()).isEqualTo(expectedContent);
    }

    @Test
    public void delete_posts() throws Exception {
        Posts savedPost = postsRepository.save(Posts.builder()
                .title("title")
                .content("content")
                .author("jerry")
                .build());

        Long deleteId = savedPost.getId();
        String url =  "http://localhost:"+port+"/api/v1/posts/"+deleteId;

        ResponseEntity<Long> responseEntity = restTemplate.exchange(url, HttpMethod.DELETE, null, Long.class);

        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(responseEntity.getBody()).isEqualTo(deleteId);
    }
}
