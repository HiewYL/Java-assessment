package com.profile.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.profile.exception.RecordNotFoundException;
import com.profile.model.dto.Post;
import com.profile.model.dto.PostDTO;
import com.profile.model.dto.ProfileDTO;
import com.profile.model.entity.Profile;
import com.profile.repository.ProfileRepository;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
@AllArgsConstructor
@NoArgsConstructor
public class ProfileService {

    @Autowired
    private ProfileRepository profileRepository;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private RestTemplate restTemplate;
    @Value("${com.profile.external.base-url}")
    private String externalBaseUrl;

    @Transactional(readOnly = true)
    public Page<ProfileDTO> retrieveActiveProfileList(Pageable pageable) {
        log.debug("[retrieveActiveProfileList] start retrieve active profile list with pageable config: {}", pageable);
        Page<Profile> profilePage = profileRepository.findByStatusCode("A", pageable);
        List<ProfileDTO> profileDTOList = profilePage.getContent().stream()
                .map(data -> objectMapper.convertValue(data, ProfileDTO.class))
                .toList();
        Pageable responsePageable = PageRequest.of(profilePage.getPageable().getPageNumber(), profilePage.getPageable().getPageSize());
        return new PageImpl<>(profileDTOList, responsePageable, profilePage.getTotalElements());
    }

    @Transactional
    public ProfileDTO insertProfile(ProfileDTO profileDTO) {
        Long maxId = profileRepository.findMaxId();
        log.debug("[insertProfile] maxId: {}", maxId);
        if (maxId != null) {
            profileDTO.setId(maxId + 1);
        } else {
            profileDTO.setId(1L);
        }
        profileDTO.setCreatedTime(OffsetDateTime.now());
        profileDTO.setStatusCode("A");
        Profile userProfile = objectMapper.convertValue(profileDTO, Profile.class);
        userProfile = profileRepository.saveAndFlush(userProfile);
        log.debug("[insertProfile] insert profile success!");
        return objectMapper.convertValue(userProfile, ProfileDTO.class);
    }

    @Transactional
    public ProfileDTO updateProfile(Long userId, ProfileDTO profileDTO) {
        Profile profileFound = profileRepository.findActiveProfileById(userId).orElseThrow(() -> new RecordNotFoundException("User profile not found!"));
        profileFound.setFirstName(profileDTO.getFirstName());
        profileFound.setLastName(profileDTO.getLastName());
        profileFound.setHomeAddress1(profileDTO.getHomeAddress1());
        profileFound.setHomeAddress2(profileDTO.getHomeAddress2());
        profileFound.setEmailAddress(profileDTO.getEmailAddress());
        profileFound.setPhoneNumber(profileDTO.getPhoneNumber());
        profileFound.setVersion(profileFound.getVersion() + 1);
        profileRepository.saveAndFlush(profileFound);
        log.debug("[updateProfile] update profile success!");
        return objectMapper.convertValue(profileFound, ProfileDTO.class);
    }

    @Transactional
    public ProfileDTO deleteProfile(Long userId) {
        Profile profileFound = profileRepository.findActiveProfileById(userId).orElseThrow(() -> new RecordNotFoundException("User profile not found!"));
        profileFound.setStatusCode("D");
        profileFound.setVersion(profileFound.getVersion() + 1);
        profileRepository.saveAndFlush(profileFound);
        log.debug("[deleteProfile] soft delete profile success!");
        return objectMapper.convertValue(profileFound, ProfileDTO.class);
    }

    //Calling third party API
    @Transactional
    public List<PostDTO> retrievePostByProfileId(Long userId) throws IOException {
        log.debug("[retrievePostByProfileId] userId : {}", userId);
        Profile profileFound = profileRepository.findActiveProfileById(userId).orElseThrow(() -> new RecordNotFoundException("User profile not found!"));
        log.debug("[retrievePostByProfileId] valid user profile: {}", profileFound);
        try {
            //sample url: https://jsonplaceholder.typicode.com/users/{userId}/posts
            ResponseEntity<String> response = restTemplate.exchange(externalBaseUrl + profileFound.getId() + "/posts", HttpMethod.GET, null, String.class);
            List<Post> posts = objectMapper.readValue(response.getBody(), objectMapper.getTypeFactory().constructCollectionType(List.class, Post.class));
            return posts.stream()
                    .map(post -> {
                        PostDTO postDTO = new PostDTO();
                        postDTO.setUserId(post.getUserId());
                        postDTO.setPostId(post.getId());
                        postDTO.setPostTitle(post.getTitle());
                        postDTO.setPostBody(post.getBody());
                        return postDTO;
                    })
                    .toList();
        } catch (IOException ex) {
            log.error("[retrievePostByProfileId] exception found: {}", ex.getMessage());
            throw new IOException("Error while retrieving posts by the userId", ex);
        }
    }
}
