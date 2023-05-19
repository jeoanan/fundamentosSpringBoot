package com.fundamentos.springboot.fundamentos;

import com.fundamentos.springboot.fundamentos.bean.MyBean;
import com.fundamentos.springboot.fundamentos.bean.MyBeanWithDependency;
import com.fundamentos.springboot.fundamentos.bean.MyBeanWithPropierties;
import com.fundamentos.springboot.fundamentos.component.ComponentDependency;
import com.fundamentos.springboot.fundamentos.entity.User;
import com.fundamentos.springboot.fundamentos.pojo.UserPojo;
import com.fundamentos.springboot.fundamentos.repository.UserRepository;
import com.fundamentos.springboot.fundamentos.service.UserService;
import org.apache.juli.logging.Log;
import org.apache.juli.logging.LogFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.domain.Sort;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

@SpringBootApplication
public class FundamentosApplication implements CommandLineRunner {

	private final Log LOGGER = LogFactory.getLog(FundamentosApplication.class);

	private ComponentDependency componentDependency;
	private MyBean myBean;
	private MyBeanWithDependency myBeanWithDependency;
	private MyBeanWithPropierties myBeanWithPropierties;
	private UserPojo userPojo;
	private UserRepository userRepository;

	private UserService userService;

	public FundamentosApplication(@Qualifier("componentTwoImplement") ComponentDependency componentDependency, MyBean myBean, MyBeanWithDependency myBeanWithDependency, MyBeanWithPropierties myBeanWithPropierties, UserPojo userPojo, UserRepository userRepository, UserService userService) {
		this.componentDependency = componentDependency;
		this.myBean = myBean;
		this.myBeanWithDependency = myBeanWithDependency;
		this.myBeanWithPropierties = myBeanWithPropierties;
		this.userPojo = userPojo;
		this.userRepository = userRepository;
		this.userService = userService;
	}

	public static void main(String[] args) {
		SpringApplication.run(FundamentosApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		//ejemplosAnteriores();
		//saveUserinDB();
		//getInformationJpqlFromUser();
		saveWithErrorTransactional();

	}

	public void saveWithErrorTransactional(){
		User test1 = new User("JuanTest","juantest@domain.com",LocalDate.now());
		User test2 = new User("DianaTest","dianatest@domain.com",LocalDate.now());
		User test3 = new User("FelipeTest","felipetest@domain.com",LocalDate.now());
		User test4 = new User("MariaTest","mariatest@domain.com",LocalDate.now());

		List<User> users = Arrays.asList(test1,test2,test3,test4);

		try {
			userService.saveTransactional(users);

			userService.getAllusers().stream()
					.forEach(user -> LOGGER.info("Este es el usuario dentro del metodo transaccional " + user));
		}catch (Exception e){
			LOGGER.error("Esta es una excepcion del transactional: " + e);
		}

	}


	private void getInformationJpqlFromUser(){
		LOGGER.info("Usuario buscado por email " + userRepository.findByUserEmail("juantest@mail.com")
				.orElseThrow(()-> new RuntimeException("No se encontro el usuario")));

		userRepository.findAndSort("Diana", Sort.by("id").descending()).forEach(user -> LOGGER.info("usuario con metodo sort " + user));

		userRepository.findByName("Juan").stream().forEach(user -> LOGGER.info("Usuario con query method: " + user));

		LOGGER.info("usuario con Query Method findByEmailAndName: " + userRepository.findByEmailAndName("dianatest@mail.com","Diana")
				.orElseThrow(()-> new RuntimeException("Usuario no encontrado")));

		userRepository.findByNameLike("%Diana%").stream().forEach(user -> LOGGER.info("Usuario con findByNamelike" + user));

		userRepository.findByNameOrEmail(null, "juantest@mail.com" ).stream().forEach(user -> LOGGER.info("Usuario con findByNameOrEmail" + user));

		userRepository.findByBirthDateBetween(LocalDate.of(1991,01,01),LocalDate.of(1993,12,31))
				.stream().forEach(user -> LOGGER.info("Usuario con intervalo de fechas: " + user));

		userRepository.findByNameLikeOrderByIdDesc("Juan").stream()
				.forEach(user-> LOGGER.info("Usuario con findByNameLikeOrderByIdDesc: " + user));

		userRepository.findByNameContainingOrderByIdDesc("Diana").stream()
				.forEach(user-> LOGGER.info("Usuario con findByNameContainingOrderByIdDesc: " + user));

		LOGGER.info("EL usuario a partir del named Parameter es: " + userRepository.getAllByBirthDateAndEmail(LocalDate.of(1991,03,20),"juantest@mail.com"));
	}

	private void saveUserinDB(){
		User user1=  new User("Juan","juantest@mail.com", LocalDate.of(1991,03,20));
		User user2= new User("Diana","dianatest@mail.com", LocalDate.of(1993,12,20));
		User user3= new User("Diana Paez","dianapaeztest@mail.com", LocalDate.of(1993,12,20));
		List<User> list= Arrays.asList(user1,user2);
		list.forEach(userRepository::save);
	}

	public void ejemplosAnteriores(){
		componentDependency.saludar();
		myBean.print();
		myBeanWithDependency.printWithDependency();
		System.out.println(myBeanWithPropierties.function());
		System.out.println(userPojo.getEmail() + " " + userPojo.getPassword());

		try {
			//error
			int value = 10 / 0;
			LOGGER.debug("Mi valor es " + value);
		} catch (Exception e) {
			LOGGER.error("Esto es un error al dividir por cero:" + e);
		}
	}

}
