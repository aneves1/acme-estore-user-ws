package com.acme.estore.user.ws.io.entity;

import java.io.Serializable;
import java.util.Collection;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;

@Entity
@Table(name="roles")
public class RoleEntity implements Serializable {
	
	private static final long serialVersionUID = 356736627533301189L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long id;
	
	@Column(name="user_role_name", nullable=false, unique=true)
	private String roleName;
	
	@ManyToMany(mappedBy="roles")
	private Collection<UserEntity> users;
	
	@ManyToMany(cascade= { CascadeType.PERSIST}, fetch= FetchType.EAGER)
	@JoinTable(name="role_authority_reference",
			joinColumns=@JoinColumn(name="role_id", referencedColumnName="id"),
			inverseJoinColumns=@JoinColumn(name="authority_id", referencedColumnName="id"))
	private Collection<AuthorityEntity> authorities;
	
	public RoleEntity(String name) {
		this.roleName = name;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getRoleName() {
		return roleName;
	}

	public void setRoleName(String roleName) {
		this.roleName = roleName;
	}

	public Collection<UserEntity> getUsers() {
		return users;
	}

	public void setUsers(Collection<UserEntity> users) {
		this.users = users;
	}

	public Collection<AuthorityEntity> getAuthorities() {
		return authorities;
	}

	public void setAuthorities(Collection<AuthorityEntity> authorities) {
		this.authorities = authorities;
	}


}
