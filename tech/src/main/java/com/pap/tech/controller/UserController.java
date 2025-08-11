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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class UserController {
    UserService userService;
    UserMapper  userMapper;

    @PostMapping
    public ApiResponse<UserResponse>createUser(@RequestBody @Valid UserCreationRequest request){
        ApiResponse<UserResponse> apiResponse = new ApiResponse<>();

        apiResponse.setResult(userMapper.toUserResponse(userService.createUser(request)));
        return apiResponse;
    }

    @GetMapping
    public List<User> getUsers(){
        return userService.getUsers();
    }

    @GetMapping("/{userId}")
    public UserResponse getUser(@PathVariable("userId") String userId){
        return userService.getUser(userId);
    }

    @PutMapping("/{userId}")
    public UserResponse updateUser(@PathVariable("userId") String userId, @RequestBody @Valid UserUpdateRequest request){
        return userService.updateRequest(userId, request);
    }

    @DeleteMapping("/{userId}")
    public String deleteUser(@PathVariable("userId") String userId){
        userService.deleteUser(userId);
        return "Xóa người dùng thành công";
    }
}
