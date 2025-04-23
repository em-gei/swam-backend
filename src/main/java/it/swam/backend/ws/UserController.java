package it.swam.backend.ws;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import it.swam.backend.dto.UserCreateDto;
import it.swam.backend.dto.UserCreateResponseDto;
import it.swam.backend.dto.UserModifyDto;
import it.swam.backend.dto.UserResponseDto;
import it.swam.backend.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
@Validated
public class UserController {

    private final UserService service;

    @Operation(summary = "Create a new user")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "400", description = "Invalid parameter"),
        @ApiResponse(responseCode = "500", description = "Internal error")
    })
    @PostMapping(headers = "Accept=application/json")
    @ResponseStatus(HttpStatus.CREATED)
    public UserCreateResponseDto createUser(@RequestBody @Valid UserCreateDto userCreateDto) {
        return service.createUser(userCreateDto);
    }

    @Operation(summary = "Get user")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "400", description = "Invalid parameter"),
        @ApiResponse(responseCode = "500", description = "Internal error")
    })
    @GetMapping(value = "/{id}", headers = "Accept=application/json")
    @ResponseStatus(HttpStatus.OK)
    public UserResponseDto getUser(@PathVariable String id) {
        return service.getUser(id);
    }

    @Operation(summary = "Delete user")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "400", description = "Invalid parameter"),
        @ApiResponse(responseCode = "500", description = "Internal error")
    })
    @DeleteMapping(value = "/{id}", headers = "Accept=application/json")
    @ResponseStatus(HttpStatus.OK)
    public void deleteUser(@PathVariable String id) {
        service.deleteUser(id);
    }

    @Operation(summary = "Modify user")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "400", description = "Invalid parameter"),
        @ApiResponse(responseCode = "500", description = "Internal error")
    })
    @PutMapping(value = "/{id}", headers = "Accept=application/json")
    @ResponseStatus(HttpStatus.OK)
    public void modifyUser(@PathVariable String id, @RequestBody @Valid UserModifyDto userModifyDto) {
        service.modifyUser(id, userModifyDto);
    }
}
