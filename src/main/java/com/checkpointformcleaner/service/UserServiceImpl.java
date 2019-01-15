package com.checkpointformcleaner.service;

import java.util.Random;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.checkpointformcleaner.entity.Role;
import com.checkpointformcleaner.entity.User;
import com.checkpointformcleaner.repository.RoleRepository;
import com.checkpointformcleaner.repository.UserRepository;

@Service
public class UserServiceImpl implements UserService, UserDetailsService {
	
	private final Logger log = LoggerFactory.getLogger(this.getClass());
	
	private UserRepository userRepository;
	private RoleRepository roleRepository;
	
	private final String USER_ROLE = "USER";
	
	@Autowired
	public UserServiceImpl(UserRepository userRepo, RoleRepository roleRepo) {
		this.userRepository = userRepo;
		this.roleRepository = roleRepo;
	}
	
	@Override
	public User findByEmail(String email) {
		return userRepository.findByEmail(email);
	}

	//felülírjuk a Security működését, s mi mondjuk meg, h honnan töltse be a username-t
	//argumentumként a username a formról fog visszajönni
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		//kikeresem a saját adatbázisomból a usernevet, 1 ha van találat, 1 rekord fog visszajönni
		System.out.println("User keresés");
		User user = findByEmail(username);
		if ( user == null ) {
			System.out.println("A "+username+" felhasználó nem található!!!");
			throw new UsernameNotFoundException(username);
		}
		//visszaadjuk azt az implementációt, ami tudja, h mit kezdjen ezzel a userrel
		return new UserDetailsImpl(user);		
	}
	
	//felülírom a UserService interface-ben látható ugyanilyen nevű metódust
	@Override
	public String registerUser(User userToRegister) {
		
		//megvizsgálom a formról jövő email cím alapján, h létezik-e már a db-ben vagy sem
		User userCheck = userRepository.findByEmail(userToRegister.getEmail());
		//ha igen, akkor visszaküldünk egy alreadyExists hibajelzést
		if ( userCheck != null ) return "alreadyExists";
		
		//megvizsálom, h a db-ben létezik-e már a USER szerepkör vagy sem
		Role userRole = roleRepository.findByRole(USER_ROLE);
		//ha létezik, akkor lekérem az összes Role-t, majd hozzájuk adom az újat a HashSet-hez
		//mivel HashSet-hez adom hozzá, s mivel abban nem lehet duplikált adat, valójában nem történik hozzáadás
		if ( userRole != null ) userToRegister.getRoles().add(userRole);
		//ha nem létezik, akkor újként hozzáadom az új szerepkört az osztályhoz
		else userToRegister.addRoles(USER_ROLE);
		
		userToRegister.setEnabled(false);
		userToRegister.setActivation(generateKey());
		userRepository.save(userToRegister);
		
		//ide tehetem be az email küldést: emailService.sendMessage(userToRegister.getEmail());
		
		return "ok";
		
	}
	
	//felülírom a UserService interface-ben látható ugyanilyen nevű metódust
	@Override
	public String userActivation(String code) {
		
		User user = userRepository.findByActivation(code);
		if ( user == null ) return "noresult";
		
		user.setEnabled(true);
		user.setActivation("");
		userRepository.save(user);
		
		return "ok";
		
	}

	//generálunk egy 16 karakterből álló véletlen szöveget
	private String generateKey() {
				
		String key = "";
		Random random = new Random();
		char[] word = new char[16];
		for (int j = 0; j < word.length; j++) {
			word[j] = (char) ('a' + random.nextInt(26));
		}
		
		String toReturn = new String(word);
		log.debug("random code: " + toReturn);
		
		return toReturn;
		
	}

}
