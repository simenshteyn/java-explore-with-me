package ru.practicum.explorewithme;

import org.modelmapper.ModelMapper;
import org.modelmapper.TypeMap;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import ru.practicum.explorewithme.participation.ParticipationRequest;
import ru.practicum.explorewithme.participation.dto.ParticipationRequestDto;

@SpringBootApplication
public class ExploreWithMeApplication {

	public static void main(String[] args) {
		SpringApplication.run(ExploreWithMeApplication.class, args);
	}

	@Bean
	public ModelMapper modelMapper() {
		ModelMapper modelMapper = new ModelMapper();
		TypeMap<ParticipationRequest, ParticipationRequestDto> propertyMapper = modelMapper.createTypeMap(
				ParticipationRequest.class, ParticipationRequestDto.class
		);
		propertyMapper.addMappings(
				mapper -> {
					mapper.map(src -> src.getEvent().getId(), ParticipationRequestDto::setEvent);
					mapper.map(src -> src.getRequester().getId(), ParticipationRequestDto::setRequester);
				}
		);
		return modelMapper;
	}

}
