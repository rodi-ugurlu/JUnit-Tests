package com.rodiugurlu.junittests.service;

import com.rodiugurlu.junittests.entity.User;
import com.rodiugurlu.junittests.repository.UserRepository;
import com.rodiugurlu.junittests.service.impl.UserServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {
    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserServiceImpl userService;
    private User user1;
    int existId;
    int nonExistId;

    @BeforeEach
    void setUp() {
        user1 = new User("mail@example.com", "RODI UGURLU", "Rodibaba@1", "BABA");
        user1.setId(1);
        existId=1;
        nonExistId=999;
    }

    @Test
    void createUser_success() {
        //Given
        when(userRepository.findByEmail("mail@example.com")).thenReturn(Optional.empty());
        when(userRepository.save(any(User.class))).thenReturn(user1);

        // When
        User result = userService.createUser(
                user1.getEmail(),
                user1.getName(),
                user1.getPassword(),
                user1.getRole()
        );

        //Then
        assertNotNull(result);
        assertEquals("mail@example.com", result.getEmail());
        assertEquals("RODI UGURLU", result.getName());
        verify(userRepository, times(1)).findByEmail("mail@example.com");
        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    void createUser_whenEmailExists_throwsException() {
        //Given
        when(userRepository.findByEmail("mail@example.com")).thenReturn(Optional.of(user1));

        //When Then
        assertThrows(RuntimeException.class, () ->
                userService.createUser(user1.getEmail(),
                        user1.getName(),
                        user1.getPassword(),
                        user1.getRole()));
        verify(userRepository, times(1)).findByEmail("mail@example.com");
        verify(userRepository, never()).save(any(User.class));


    }

    @Test
    void getUserById_Success() {
        //Given
        when(userRepository.findById(1)).thenReturn(Optional.of(user1));

        //When
        User result=userService.getUserById(1);

        //Then
        assertNotNull(result);
        assertEquals(1,result.getId());
        assertEquals("mail@example.com",result.getEmail());
        verify(userRepository,times(1)).findById(1);

    }
    @Test
    @DisplayName("Var olmayan ID ile kullanıcı getirilmeye çalışıldığında hata fırlatılmalı")
    void getUserById_WhenUserNotExists_ShouldThrowRuntimeException() {
        int nonExistingUserId = 999;


        when(userRepository.findById(nonExistingUserId)).thenReturn(Optional.empty());


        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            userService.getUserById(nonExistingUserId);
        });

        verify(userRepository, times(1)).findById(nonExistingUserId);
    }

    @Test
    void getUserByEmail_Success() {
        //Given
        String existEmail="mail@example.com";
        when(userRepository.findByEmail(existEmail)).thenReturn(Optional.of(user1));
         //When
        User result=userService.getUserByEmail(existEmail);

        //Then
        assertNotNull(result);
        assertEquals(existEmail,result.getEmail());
        assertEquals("RODI UGURLU",result.getName());

        verify(userRepository,times(1)).findByEmail(existEmail);

    }
    @Test
    void getUserByEmail_ShouldThrowRuntimeException() {
        //Given
        String nonExistEmail="existmail@mail.com";
        when(userRepository.findByEmail(nonExistEmail)).thenReturn(Optional.empty());
        //When Then
        RuntimeException exception=assertThrows(RuntimeException.class,()->
                userService.getUserByEmail(nonExistEmail));
        verify(userRepository,times(1)).findByEmail(nonExistEmail);


    }

    @Test
    void deleteUser_Success() {
        int existId=1;
        when(userRepository.findById(existId)).thenReturn(Optional.of(user1));
        //When Then
        doNothing().when(userRepository).delete(user1);
        userService.deleteUser(existId);
        verify(userRepository,times(1)).delete(user1);
    }
    @Test
    void deleteUser_ShouldThrowRuntimeException() {
        int nonExistId=999;
        when(userRepository.findById(nonExistId)).thenReturn(Optional.empty());
        assertThrows(RuntimeException.class,()->
                userService.deleteUser(nonExistId));
        verify(userRepository,times(1)).findById(nonExistId);
    }
    @Test
    void changePassword_Success(){
        //Given
        int existId=1;
        String oldPassword="Rodibaba@1";
        when(userRepository.findById(existId)).thenReturn(Optional.of(user1));
        when(userRepository.save(any(User.class))).thenReturn(user1);
                       //When
        User result=userService.changePassword(existId,oldPassword,"737321rodi");

        //Then
        assertNotNull(result);
        assertEquals("737321rodi",result.getPassword());
        verify(userRepository,times(1)).findById(existId);
        verify(userRepository,times(1)).save(user1);
    }
    @Test
    void changePassword_UserNotExist(){
        int nonExistId=999;
        when(userRepository.findById(nonExistId)).thenReturn(Optional.empty());
        assertThrows(RuntimeException.class,()->
                userService.changePassword(nonExistId,"oldPassword","newPassword"));
        verify(userRepository,times(1)).findById(nonExistId);
    }
    @Test
    void changePassword_NewPasswordCantBeSameWithOldPassword(){

        //Given
        when(userRepository.findById(existId)).thenReturn(Optional.of(user1));
        assertThrows(RuntimeException.class,()->userService.changePassword(existId,"password","password")
                );
        verify(userRepository,times(1)).findById(existId);
    }
}