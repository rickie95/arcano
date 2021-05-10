package com.riccardomalavolti.arcano.mappers;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

import java.lang.reflect.Constructor;
import java.util.List;
import java.util.UUID;

import org.junit.jupiter.api.Test;

import com.riccardomalavolti.arcano.dto.UserBrief;
import com.riccardomalavolti.arcano.dto.UserDetails;
import com.riccardomalavolti.arcano.dto.UserMapper;
import com.riccardomalavolti.arcano.model.User;


class UserMapperTest {

	@Test
	void callingPrivateConstructorThrowsUnsuppordedException() {
		final Constructor<?>[] constructors = UserMapper.class.getDeclaredConstructors();
		assertThat(constructors).hasSize(1);
		assertThrows(IllegalAccessException.class, () -> constructors[0].newInstance());
	}
	
	@Test
	void testToUserBrief() {
		User user = new User(UUID.randomUUID());
		user.setName("Mike");
		user.setUsername("mikimiki");
		user.setPassword("secret");
		
		UserBrief brief = UserMapper.toUserBrief(user);
		
		assertThat(brief.getId()).isEqualTo(user.getId());
		assertThat(brief.getUsername()).isEqualTo(user.getUsername());
	}
	
	@Test
	void testToUserDetails() {
		User user = new User(UUID.randomUUID());
		user.setName("Mike");
		user.setUsername("mikimiki");
		user.setPassword("secret");
		
		UserDetails details = UserMapper.toUserDetails(user);
		
		assertThat(details.getId()).isEqualTo(user.getId());
		assertThat(details.getUsername()).isEqualTo(user.getUsername());
		assertThat(details.getName()).isEqualTo(user.getName());
	}
	
	@Test
	void testToUserBriefList() {
		User user = new User(UUID.randomUUID());
		user.setName("Mike");
		user.setUsername("mikimiki");
		user.setPassword("secret");
		
		List<UserBrief> briefList = UserMapper.toUserBriefList(List.of(user));
		
		assertThat(briefList).hasSize(1);
		
		UserBrief brief = briefList.get(0);
		
		assertThat(brief.getId()).isEqualTo(user.getId());
		assertThat(brief.getUsername()).isEqualTo(user.getUsername());
	}
	
	@Test
	void testToUserDetailsList() {
		User user = new User(UUID.randomUUID());
		user.setName("Mike");
		user.setUsername("mikimiki");
		user.setPassword("secret");
		
		List<UserDetails> detailsList = UserMapper.toUserDetailsList(List.of(user));
		
		assertThat(detailsList).hasSize(1);
		
		UserDetails details = detailsList.get(0);
		
		assertThat(details.getId()).isEqualTo(user.getId());
		assertThat(details.getUsername()).isEqualTo(user.getUsername());
	}

}
