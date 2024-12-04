package com.mayab.quality.unittest.service;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import com.mayab.quality.unittest.dao.IDAOUser;
import com.mayab.quality.unittest.model.User;
import com.mayab.quality.unittest.service.UserService;

class UserServiceTest {

	private UserService service;
	private IDAOUser dao;
	private User user;
	private HashMap<Integer, User>db;
	
	@BeforeEach
	public void setUp() throws Exception{
		dao = mock(IDAOUser.class);
		service = new UserService(dao);
		db = new HashMap<Integer, User>();
	}
// --------------------------------- 1.1 CREATE NEW USER -----------------------------------
	@Test
	public void whenAllValid_test(){
		//initialize
		int sizeBefore = db.size();
		System.out.println("sizeBefore = " + sizeBefore);
		
		//fake code for findUserByEmail & save methods
		when(dao.findUserByEmail(anyString())).thenReturn(null);
		when(dao.save(any(User.class))).thenAnswer(new Answer<Integer>() {
			//Method within the class
			public Integer answer(InvocationOnMock invocation) throws Throwable{
				//Set behavior in every location
				User arg = (User) invocation.getArguments()[0];
				db.put(db.size()+1, arg);
				System.out.println("SizeAfter= " + db.size()+ "\n");
				//Return the invoked value
				return db.size()+1;
				}
			}
		);
		
		//Exercise
		User user = service.createUser("hola", "Hola@email.com", "pass1234");
		
		//Verify
		assertThat(db.size(),is(sizeBefore+1));
		
	}

// --------------------------------- 1.2 Duplicated Email -----------------------------------
	@Test
	public void whenEmailTaken_test(){
		//initialize
		String Pass = "123";
		String name = "user1";
		String email = "user01@email.com";
		User user = null;
		//fake code for findUserByEmail & save methods
		when(dao.findUserByEmail(anyString())).thenReturn(user);
		when(dao.save(any(User.class))).thenReturn(0);
		
		//Exercise
		user = service.createUser(name, email, Pass);
		User expected = null;
		
		//Verify
		assertThat(user, is(expected));
	}

// --------------------------------- 2 Update Password -----------------------------------
	@Test
	public void whenUserUpdatePassword_test(){
		//initialize
		User oldUser = new User("oldUser","oldEmail","oldPassword");
		db.put(1,oldUser);
		oldUser.setId(1);
		
		User newUser = new User("oldUser","oldEmail","newPassword");
		newUser.setId(1);
		
		when(dao.findById(1)).thenReturn(oldUser);
		
		when(dao.updateUser(any(User.class))).thenAnswer(new Answer<User>(){
			//Method within the class
			public User answer(InvocationOnMock invocation) throws Throwable{
				//Set behavior in every invocation
				User arg = (User) invocation.getArguments()[0];
				db.replace(arg.getId(), arg);
				
				//Return the invoked value
				return db.get(arg.getId());
				}
			}
		);
		//Exercise
		User result = service.updateUser(newUser);
		
		//Verification
		assertThat(result.getName(),is("oldUser"));
		assertThat(result.getEmail(),is("oldEmail"));
		assertThat(result.getPassword(), is("newPassword"));
	}

// --------------------------------- 3 Delete User ---------------------------------------
	@Test
	public void WhenDeleteUser_test(){
		//Initialize
		User deleted = new User("deleted","deleted@gmail.com","delpassword");
		db.put(1, deleted);
		deleted.setId(1);
		
		when(dao.findById(1)).thenReturn(deleted);
		
		//Invocation sirve para respuestas complejas en mockito,
		//lo use para  borrar la base de datos
		when(dao.deleteUser(1)).thenAnswer(invocation->{
			db.remove(1);
			return true;
		});
		
		//exercise
		boolean delete = service.deleteUser(1);
		
		//Verify
		assertTrue(delete);
		assertFalse(db.containsKey(1));
	}
// --------------------------------- 4.1 Find by email -----------------------------------
	@Test
	public void WhenSearchByEmailFind_Test(){
		//initialize
		User searched = new User("searched","searched@gmail.com","seaPassword");
		db.put(1, searched);
		searched.setId(1);
		
		when(dao.findUserByEmail(anyString())).thenReturn(searched);
		
		//Exercise
		User Testsearch = service.findUserByEmail("searched@gmail.com");
				
		//Verify
		assertThat(Testsearch,is(searched));
		
	}
// ------------------------------- 4.2 Not find by email ---------------------------------
	@Test
	public void WhenSearchByEmailFails_Test(){
		//initialize
		
		when(dao.findUserByEmail(anyString())).thenReturn(null);
		
		//Exercise
		User Testsearch = service.findUserByEmail("Notsearched@gmail.com");
		User expected = null;
				
		//Verify
		assertThat(Testsearch,is(expected));
		
	}
// --------------------------------- 5 Find all users ------------------------------------

	@Test
	public void WhenFindAllUsers_Test() {
		//Initialize
		List<User> users = Arrays.asList(
		        new User("Juan", "JP@gmail.com", "password"),
		        new User("Ramon", "RD@gmail.com", "password"),
		        new User("Lorenzo", "LQ@gmail.com","password")
		    );
		
		when(dao.findAll()).thenReturn(users);
		
		//Exercise
		List<User> lista = service.findAllUsers();
		
		//Verify
		assertThat(lista, is(users));
		assertThat(lista.size(), is(users.size()));
	}
	
}
