package nl.dentro.OrderSystem.services;

import nl.dentro.OrderSystem.dtos.UserDataDto;
import nl.dentro.OrderSystem.dtos.UserDataInputDto;
import nl.dentro.OrderSystem.models.UserData;

public interface UserDataService {
    UserDataDto getUserDataById(Long id);

    UserData createUserData(UserDataInputDto userDataInputDto);

    UserData fromUserDataDto(UserDataInputDto userDataInputDto);

    UserDataDto toUserDataDto(UserData userData);

    boolean availableUserDataId(Long id);
}
