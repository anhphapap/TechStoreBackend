package com.pap.tech.controller;

import com.pap.tech.dto.request.UserCreationRequest;
import com.pap.tech.dto.request.UserUpdateRequest;
import com.pap.tech.dto.response.ApiResponse;
import com.pap.tech.dto.response.UserResponse;
import com.pap.tech.entity.User;
import com.pap.tech.mapper.UserMapper;
import com.pap.tech.repository.UserRepository;
import com.pap.tech.service.UserService;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class UserController {
    UserService userService;
    UserMapper  userMapper;

    @PostMapping
    ApiResponse<UserResponse>createUser(@RequestBody @Valid UserCreationRequest request){
        ApiResponse<UserResponse> apiResponse = new ApiResponse<>();

        apiResponse.setResult(userMapper.toUserResponse(userService.createUser(request)));
        return apiResponse;
    }

    @GetMapping
    ApiResponse<List<UserResponse>> getUsers(){
        var auth = SecurityContextHolder.getContext().getAuthentication();

        log.info("Username: {}", auth.getName());
        log.info("Role: {}", auth.getAuthorities());

        return ApiResponse.<List<UserResponse>>builder()
                .result(userService.getUsers())
                .build();
    }

    @GetMapping("/{userId}")
    ApiResponse<UserResponse> getUser(@PathVariable("userId") String userId){
        return ApiResponse.<UserResponse>builder()
                .result(userService.getUser(userId))
                .build();
    }

    @PutMapping("/{userId}")
    ApiResponse<UserResponse> updateUser(@PathVariable("userId") String userId, @RequestBody @Valid UserUpdateRequest request){
        return ApiResponse.<UserResponse>builder()
                .result(userService.updateRequest(userId, request))
                .build();
    }

    @DeleteMapping("/{userId}")
    ApiResponse deleteUser(@PathVariable("userId") String userId){
        userService.deleteUser(userId);
        return ApiResponse.builder()
                .message("Xóa người dùng thành công")
                .build();
    }

    @GetMapping("/profile")
    ApiResponse<UserResponse> getUserProfile(){
        return ApiResponse.<UserResponse>builder()
                .result(userService.getProfile())
                .build();
    }

    @GetMapping("/stats")
    ApiResponse<Map<String, Long>> getStats(){
        return ApiResponse.<Map<String, Long>>builder()
                .result(userService.getUserStats())
                .build();
    }
}
