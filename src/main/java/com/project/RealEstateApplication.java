package com.project;

import com.project.entity.concretes.user.UserRole;
import com.project.entity.enums.Role;
import com.project.payload.request.user.UserRequest;
import com.project.repository.user.UserRoleRepository;
import com.project.service.user.UserRoleService;
import com.project.service.user.UserService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.time.LocalDate;

@SpringBootApplication
public class RealEstateApplication implements CommandLineRunner {

	private final UserRoleService userRoleService;
	private final UserRoleRepository userRoleRepository;
	private final UserService userService;

    public RealEstateApplication(UserRoleService userRoleService, UserRoleRepository userRoleRepository, UserService userService) {
        this.userRoleService = userRoleService;
        this.userRoleRepository = userRoleRepository;
        this.userService = userService;
    }

    public static void main(String[] args) {
		SpringApplication.run(RealEstateApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		//!!! Role tablomu doldurcam
		if(userRoleService.getAllUserRole().isEmpty()) {

			UserRole admin = new UserRole();
			admin.setRoleName("Admin");
			admin.setRole(Role.ADMIN);
			userRoleRepository.save(admin);

			UserRole manager = new UserRole();
			manager.setRole(Role.MANAGER);
			manager.setRoleName("Dean");
			userRoleRepository.save(manager);

			UserRole customer = new UserRole();
			customer.setRoleName("Customer");
			customer.setRole(Role.CUSTOMER);


		}

		//!!! Built_IN ADMIN olusturacagiz
		if(userService.countAllAdmins() == 0) {
			UserRequest adminRequest = new UserRequest();
			adminRequest.setUsername("wolverine");
			adminRequest.setFirstName("Hugh");
			adminRequest.setLastName("Jackman");
			adminRequest.setPhone("123-456-7890");
			adminRequest.setEmail("123@gmail.com");
			adminRequest.setPasswordHash("12345678");
			adminRequest.setBuiltIn(true);


			userService.saveUser(adminRequest, "Admin");
		}



	}
}
