package pw.api.test.helpers

import groovy.json.JsonSlurper
import pw.api.test.BaseApiTest
import spock.lang.*

class UserHelper {
	
	def loggedInUsers = []
	
	BaseApiTest baseTest
	def UserHelper(BaseApiTest baseTest) {
		this.baseTest = baseTest
	}
	
	def getRandomUser(String role) {
		def response = new JsonSlurper().parse(new URL('https://randomuser.me/api'))
		[
			username:response.results[0].login.username,
			password:response.results[0].login.password,
			confirmPassword:response.results[0].login.password,
			email:response.results[0].email,
			role:role
		]
	}
	
	def create(Map params) {
		def response = baseTest.post("user/create", "user/Create", params)
		addToLoggedInUsers(response, params.password)
		response
	}
	
	def login(String username, String password) {
		def params = [username:username, password:password]
		def response = baseTest.post('user/apiLogin', 'user/Login', params)
		addToLoggedInUsers(response, password)
		response
	}
	
	private addToLoggedInUsers(response, String password) {
		if (response?.user?.id) {
			response.with {
				if (this.loggedInUsers) {
					this.loggedInUsers << [id:user.id, username:user.userame, password:password, token:token]
				} else {
					this.loggedInUsers = [[id:user.id, username:user.userame, password:password, token:token]]
				}
			}
		}
	}
	
	def getUserToken(String username, String password) {
		def user = loggedInUsers.find { it.username == username && it.password == password }
		if (user) {
			user.token
		} else {
			def response = login(username, password)
			response?.token
		}
	}
	
	def getUserToken(userId) {
		def user = loggedInUsers.find { it.id == userId }
		if (user) {
			user.token
		} else {
			throw new Error("User with id $userId is not logged in")
		}
	}
	
	def getUserIdByToken(String token) {
		def user = loggedInUsers.find { it.token == token }
		user?.id
	}
}
