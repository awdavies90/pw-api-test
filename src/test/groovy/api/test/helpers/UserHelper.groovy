package api.test.helpers

import groovy.json.JsonSlurper
import pw.api.test.BaseApiTest
import spock.lang.*

class UserHelper {
	
	def loggedInUsers = []
	
	BaseApiTest baseTest
	def UserHelper(BaseApiTest baseTest) {
		this.baseTest = baseTest
	}
	
	def create(Map params) {
		def response = baseTest.post("user/create", "user/Create", params)
		addToLoggedInUsers(response)
		response
	}
	
	def login(String username, String password) {
		def params = [username:username, password:password]
		def response = baseTest.post('user/apiLogin', 'user/Login', params)
		addToLoggedInUsers(response)
		response
	}
	
	private addToLoggedInUsers(response) {
		if (response?.user?.id) {
			response.user.with {
				if (loggedInUsers) {
					loggedInUsers << [id:id, username:userame, password:password, token:token]
				} else {
					loggedInUsers = [[id:id, username:userame, password:password, token:token]]
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
			throw new Error("User wih id $userId is not logged in")
		}
	}
}
