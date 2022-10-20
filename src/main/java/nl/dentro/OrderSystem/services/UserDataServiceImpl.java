package nl.dentro.OrderSystem.services;

import nl.dentro.OrderSystem.dtos.UserDataDto;
import nl.dentro.OrderSystem.dtos.UserDataInputDto;
import nl.dentro.OrderSystem.exceptions.RecordNotFoundException;
import nl.dentro.OrderSystem.models.UserData;
import nl.dentro.OrderSystem.repositories.UserDataRepository;
import org.springframework.stereotype.Service;

@Service
public class UserDataServiceImpl implements UserDataService {
    private final UserDataRepository userDataRepository;

    public UserDataServiceImpl(UserDataRepository userDataRepository) {
        this.userDataRepository = userDataRepository;
    }

    @Override
    public UserDataDto getUserDataById(Long id) {
        if (availableUserDataId(id)) {
            UserData userData = userDataRepository.findById(id).get();
            UserDataDto userDataDto = toUserDataDto(userData);
            return userDataDto;
        } else {
            throw new RecordNotFoundException("Could not find user data with id: " + id + ".");
        }
    }

    @Override
    public UserData createUserData(UserDataInputDto userDataInputDto) {
        UserData userData = fromUserDataDto(userDataInputDto);
        UserData savedUserData = userDataRepository.save(userData);

        return savedUserData;
    }

    @Override
    public UserData fromUserDataDto(UserDataInputDto userDataInputDto) {
        var userData = new UserData();
        userData.setFirstName(userDataInputDto.getFirstName());
        userData.setLastName(userDataInputDto.getLastName());
        userData.setEmail(userDataInputDto.getEmail());
        userData.setPhoneNumber(userDataInputDto.getPhoneNumber());
        return userData;
    }

    @Override
    public UserDataDto toUserDataDto(UserData userData) {
        var dto = new UserDataDto();
        dto.setId(userData.getId());
        dto.setFirstName(userData.getFirstName());
        dto.setLastName(userData.getLastName());
        dto.setEmail(userData.getEmail());
        dto.setPhoneNumber(userData.getPhoneNumber());
        return dto;
    }

    @Override
    public boolean availableUserDataId(Long id) {
        return userDataRepository.findById(id).isPresent();
    }
}
