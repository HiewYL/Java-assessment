package com.profile.controller;

import com.profile.constants.ResourcePath;
import com.profile.model.dto.PostDTO;
import com.profile.model.dto.ProfileDTO;
import com.profile.service.ProfileService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@RestController
@Slf4j
@RequestMapping(ResourcePath.BASE_URL)
@AllArgsConstructor
public class ProfileController {

    @Autowired
    private ProfileService profileService;

    @GetMapping(ResourcePath.GET_ALL_ACTIVE_PROFILES_WITH_PAGINATION)
    @Operation(description = "Retrieve all active user profiles with pagination")
    public ResponseEntity<Page<ProfileDTO>> getAllActiveUserProfiles(@PageableDefault(page = 0, size = 10, sort = "id", direction = Sort.Direction.ASC) Pageable pageable) {
        return ResponseEntity.ok(profileService.retrieveActiveProfileList(pageable));
    }

    @PostMapping(ResourcePath.ADD_PROFILE)
    @Operation(description = "Add new user profile")
    public ResponseEntity<ProfileDTO> addUserProfile(@Valid @RequestBody ProfileDTO profileDTO) {
        return ResponseEntity.ok(profileService.insertProfile(profileDTO));
    }

    @PutMapping(ResourcePath.UPDATE_PROFILE)
    @Operation(description = "Update user profile info")
    public ResponseEntity<ProfileDTO> updateUserProfile(@PathVariable Long userId, @Valid @RequestBody ProfileDTO profileDTO) {
        return ResponseEntity.ok(profileService.updateProfile(userId, profileDTO));
    }

    @PutMapping(ResourcePath.DELETE_PROFILE)
    @Operation(description = "Soft delete user profile info")
    public ResponseEntity<ProfileDTO> deleteUserProfile(@PathVariable Long userId) {
        return ResponseEntity.ok(profileService.deleteProfile(userId));
    }

    //Calling to get posts(from third party) by user profile id
    @GetMapping(ResourcePath.RETRIEVE_POSTS_BY_PROFILE_USER_ID)
    @Operation(description = "Get the valid user profile by id and call to external API to get posts by user id ")
    public ResponseEntity<List<PostDTO>> getAllPostsByProfile(@PathVariable Long userId) throws IOException {
        return ResponseEntity.ok(profileService.retrievePostByProfileId(userId));
    }
}
