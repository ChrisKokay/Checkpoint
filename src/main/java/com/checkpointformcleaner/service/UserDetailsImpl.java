package com.checkpointformcleaner.service;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import org.springframework.security.core.userdetails.UserDetails;

import com.checkpointformcleaner.entity.Role;
import com.checkpointformcleaner.entity.User;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

/**
 * A UserDetails-ben felülírjuk, h hogyan dolgozza fel 1 user adatait a Spring.
 * Ezt csak loginkor kell megtenni, hiszen a saját login rendszerét fogjuk felülírni
 *  
 * @author Kiki
 *
 */
public class UserDetailsImpl implements UserDetails {
	
	private static final long serialVersionUID = 3185970362329652822L;

	private User user;

	public UserDetailsImpl(User user) {
		this.user = user;
	}

	/**
	 * Itt megmondjuk, h hogyan vegye ki a jogköröket
	 */
	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		//GrantedAuthority - Spring Security kezeli, ebben tárolódnak a szerepkörök
		Collection<GrantedAuthority> authorities = new HashSet<GrantedAuthority>();
		//kikérem a bejelentkezésre váró user jogait egy Set-be
		Set<Role> roles = user.getRoles();
		//végigmegyünk 1 ciklussal az összes jogon
		for (Role role : roles) {
			//minden új jog nevét becsomagoljuk egy SimpleGrantedAuthority-be, amit ért és kezel a Spring Sec.
			//majd pedig hozzáadom az authorities csomaghoz, amiben a Spring Sec. gyűjti a  neveket
			authorities.add(new SimpleGrantedAuthority(role.getRole()));
		}
		return authorities;
	}

	//innen fogja tudni a spring security, h milyen jelszót adott meg a user, hiszen az már benne van a User-ben
	@Override
	public String getPassword() {
		return user.getPassword();
	}

	//a usernevet a spring security pedig email névként kapja meg
	@Override
	public String getUsername() {
		//System.out.println("email: ");
		//System.out.println(user.getEmail() );
		return user.getEmail();
	}

	//az account-ot soha nem engedjük lejárni
	@Override
	public boolean isAccountNonExpired() {
		return true;
	}

	//nincs lock-olva az account
	@Override
	public boolean isAccountNonLocked() {
		return true;
	}

	//nem járt-e le a user jelszava (true-t adunk vissza, nem foglalkozunk ezzel sem)
	@Override
	public boolean isCredentialsNonExpired() {
		return true;
	}

	//engedélyezve van-e a fiók
	@Override
	public boolean isEnabled() {
		//ha true-t ad vissza az obj-ból, akkor beléphet a rendszerbe a user, egyébként nem
		return user.getEnabled();
	}
	
}
