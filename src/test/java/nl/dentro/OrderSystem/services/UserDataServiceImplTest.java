package nl.dentro.OrderSystem.services;

import nl.dentro.OrderSystem.dtos.UserDataDto;
import nl.dentro.OrderSystem.dtos.UserDataInputDto;
import nl.dentro.OrderSystem.exceptions.RecordNotFoundException;
import nl.dentro.OrderSystem.models.UserData;
import nl.dentro.OrderSystem.repositories.UserDataRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class UserDataServiceImplTest {

    @Mock
    UserDataRepository userDataRepository;

    @InjectMocks
    private UserDataServiceImpl userDataService;

    @Captor
    ArgumentCaptor<UserData> argumentCaptor;

    UserData userData1;
    UserDataDto userDataDto1;

    UserDataInputDto userDataInputDto1;

    @BeforeEach
    void setUp() {
        userData1 = new UserData(200L, "Charles", "Darwin", "charles@darwin.com", "06-12345678");
        userDataDto1 = new UserDataDto(200L, "Charles", "Darwin", "charles@darwin.com", "06-12345678");
        userDataInputDto1 = new UserDataInputDto("Charles", "Darwin", "charles@darwin.com", "06-12345678");
    }

    @Test
    void shouldReturnUserDataDtoWhenUserDataIdIsGiven() {
        Long id = 201L;
        when(userDataRepository.findById(id)).thenReturn(Optional.of(userData1));

        UserData userData = userDataRepository.findById(id).get();
        UserDataDto userDataDto = userDataService.getUserDataById(id);

        assertEquals(userData.getFirstName(), userDataDto.getFirstName());
        assertEquals(userData.getLastName(), userDataDto.getLastName());
        assertEquals(userData.getEmail(), userDataDto.getEmail());
        assertEquals(userData.getPhoneNumber(), userDataDto.getPhoneNumber());
    }


    @Test
    void shouldReturnRecordNotFoundExceptionNotExistingUserDataIdIsGiven() {
        Long id = null;

        assertThrows(RecordNotFoundException.class, () -> userDataService.getUserDataById(id));
    }

    @Test
    void shouldCreateUserDataWhenUserDataIsGiven() {
        when(userDataRepository.save(Mockito.any(UserData.class))).thenReturn(userData1);
        userDataService.createUserData(userDataInputDto1);
        verify(userDataRepository, times(1)).save(argumentCaptor.capture());
        UserData userData = argumentCaptor.getValue();

        assertEquals(userData.getFirstName(), userDataDto1.getFirstName());
        assertEquals(userData.getLastName(), userDataDto1.getLastName());
        assertEquals(userData.getEmail(), userDataDto1.getEmail());
        assertEquals(userData.getPhoneNumber(), userDataDto1.getPhoneNumber());
    }
}